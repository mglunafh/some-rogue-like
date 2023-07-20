package org.some.project.kotlin.scenes

import org.some.project.kotlin.control.ControlType
import org.some.project.kotlin.chars.DungeonCharacter
import org.some.project.kotlin.chars.EnemyCharacter
import org.some.project.kotlin.chars.HasTurns
import org.some.project.kotlin.chars.HeroCharacter
import org.some.project.kotlin.chars.Party
import org.some.project.kotlin.control.ComputerControl
import org.some.project.kotlin.control.PlayerControl
import java.util.LinkedList
import java.util.Queue

class Skirmish(
    val heroParty: Party<HeroCharacter>,
    val enemyParty: Party<EnemyCharacter>
) {

    var round: Int = 1
        private set

    var sequenceOfTurns: Queue<Triple<Int, DungeonCharacter, ControlType>>
        private set
    lateinit var curCharTuple: Triple<Int, DungeonCharacter, ControlType>
        private set

    init {
        require(heroParty.isAlive() ) { "At least one character in the party must be alive!" }

        var tempCollection = mutableListOf<Triple<Int, DungeonCharacter, ControlType>>()
        heroParty.getCharacters().forEach {
            it?.let {
                tempCollection.add(Triple(it.speed, it, ComputerControl))       // <- switch to PlayerControl
            }
        }
        enemyParty.getCharacters().forEach {
            it?.let {
                tempCollection.add(Triple(it.speed, it, ComputerControl))
            }
        }
        tempCollection = tempCollection
            .filter { it.second is HasTurns }
            .sortedBy { (it.second as HasTurns).speed }
            .reversed()
            .toMutableList()

        sequenceOfTurns = LinkedList(tempCollection)
    }

    fun areTeammates(person1: DungeonCharacter, person2: DungeonCharacter): Boolean {
        return (person1 is HeroCharacter && person2 is HeroCharacter)
                || (person1 is EnemyCharacter && person2 is EnemyCharacter)
    }

    val battlefield: String
        get() {
            return "${heroParty.descriptionBacklineFirst()} --x-- ${enemyParty.descriptionFrontlineFirst()}"
        }

    private fun atStart() {
    }

    private fun atRoundStart() {
    }

    private fun atRoundEnd() {
    }

    fun run() {
        println(battlefield)
        println("Round $round")
        do {
            curCharTuple = sequenceOfTurns.poll()
            val curChar = curCharTuple.second
            val controlType = curCharTuple.third
            val control = when (controlType) {
                ComputerControl -> "(by computer)"
                PlayerControl   -> "(by player)"
            }
            print("(SPD ${curCharTuple.first}) ${curCharTuple.second.fancyName} turn $control: ")
            curChar.performAbility(this, controlType)
        } while (sequenceOfTurns.isNotEmpty())
        println(battlefield)
    }
}
