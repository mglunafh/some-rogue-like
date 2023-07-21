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
        require(heroParty.isAlive) { "At least one character in the party must be alive!" }
        sequenceOfTurns = decideTurnSequence(heroParty, enemyParty)
    }

    fun areTeammates(person1: DungeonCharacter, person2: DungeonCharacter): Boolean {
        return (person1 is HeroCharacter && person2 is HeroCharacter)
                || (person1 is EnemyCharacter && person2 is EnemyCharacter)
    }

    val battlefield: String
        get() {
            return "${heroParty.descriptionBackLineFirst()} --x-- ${enemyParty.descriptionFrontLineFirst()}"
        }

    private fun atStart() {
    }

    private fun atRoundStart() {
    }

    private fun atRoundEnd() {
    }

    fun run() {
        while (true) {
            println("\n      ----------Round $round ---------- ")
            println(battlefield)
            do {
                curCharTuple = sequenceOfTurns.poll()
                val curChar = curCharTuple.second
                if (curChar.isDead) {
                    continue
                }
                val controlType = curCharTuple.third
                val control = when (controlType) {
                    ComputerControl -> "(by computer)"
                    PlayerControl   -> "(by player)"
                }
                print("(SPD ${curCharTuple.first}) ${curCharTuple.second.fancyName} turn $control: ")
                curChar.performAbility(this, controlType)
            } while (sequenceOfTurns.isNotEmpty())
            if (heroParty.gotWiped) {
                println("Unfortunately, your adventurers failed")
                println("DEFEAT")
                return
            }
            if (enemyParty.gotWiped) {
                println("Congratulations, your adventurers vanquished the enemy!")
                println("VICTORY")
                return
            }
            sequenceOfTurns = decideTurnSequence(heroParty, enemyParty)
            round++
        }
    }

    companion object {
        private fun decideTurnSequence(
            heroParty: Party<HeroCharacter>,
            enemyParty: Party<EnemyCharacter>
        ): LinkedList<Triple<Int, DungeonCharacter, ControlType>> {

            var tempCollection = mutableListOf<Triple<Int, DungeonCharacter, ControlType>>()
            heroParty.getCharacters()
                .filterNotNull()
                .filter { it.isAlive }
                .forEach {
                    tempCollection.add(Triple(it.speed, it, ComputerControl))       // <- switch to PlayerControl
                }
            enemyParty.getCharacters()
                .filterNotNull()
                .filter { it.isAlive }
                .forEach {
                    tempCollection.add(Triple(it.speed, it, ComputerControl))
                }

            tempCollection = tempCollection
                .filter { it.second is HasTurns }
                .sortedBy { (it.second as HasTurns).speed }
                .reversed()
                .toMutableList()

            return LinkedList(tempCollection)
        }
    }
}
