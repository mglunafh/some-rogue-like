package org.some.project.kotlin.scenes

import org.some.project.kotlin.ControlType
import org.some.project.kotlin.chars.DungeonCharacter
import org.some.project.kotlin.chars.EnemyCharacter
import org.some.project.kotlin.chars.HasTurns
import org.some.project.kotlin.chars.HeroCharacter
import org.some.project.kotlin.chars.Party
import java.util.LinkedList
import java.util.Queue

class Skirmish(
    private val heroParty: Party<HeroCharacter>,
    private val enemyParty: Party<EnemyCharacter>
) {

    var round: Int = 1
        private set

    var sequenceOfTurns: Queue<Triple<Int, DungeonCharacter, ControlType>>
        private set
    lateinit var currentCharacter: Triple<Int, DungeonCharacter, ControlType>
        private set

    init {
        require(heroParty.isAlive() ) { "At least one character in the party must be alive!" }

        var tempCollection = mutableListOf<Triple<Int, DungeonCharacter, ControlType>>()
        heroParty.getCharacters().forEach {
            it?.let {
                tempCollection.add(Triple(it.speed, it, ControlType.PlayerControl))
            }
        }
        enemyParty.getCharacters().forEach {
            it?.let {
                tempCollection.add(Triple(it.speed, it, ControlType.ComputerControl))
            }
        }
        tempCollection = tempCollection
            .filter { it.second is HasTurns }
            .sortedBy { (it.second as HasTurns).speed }
            .reversed()
            .toMutableList()

        sequenceOfTurns = LinkedList(tempCollection)
    }

    val battlefield: String
        get() {
            return "${heroParty.descriptionRightFirst()} --x-- ${enemyParty.descriptionLeftFirst()}"
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
            currentCharacter = sequenceOfTurns.poll()
            val control = when (currentCharacter.third) {
                ControlType.ComputerControl -> "(by computer)"
                ControlType.PlayerControl -> "(by player)"
            }
            println("${currentCharacter.second.charClass.name} turn (SPD ${currentCharacter.first}) $control:")

        } while (sequenceOfTurns.isNotEmpty())
    }
}