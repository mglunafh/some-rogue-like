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
    private val heroParty: Party<HeroCharacter>,
    private val enemyParty: Party<EnemyCharacter>
) {

    var round: Int = 1
        private set

    var sequenceOfTurns: Queue<Triple<Int, DungeonCharacter, ControlType>>
        private set
    lateinit var curCharTuple: Triple<Int, DungeonCharacter, ControlType>
        private set

    val deleteCorpses = true

    init {
        require(heroParty.isAlive) { "At least one character in the party must be alive!" }
        sequenceOfTurns = decideTurnSequence(heroParty, enemyParty)
    }

    fun areTeammates(person1: DungeonCharacter, person2: DungeonCharacter): Boolean {
        return (person1 is HeroCharacter && person2 is HeroCharacter)
                || (person1 is EnemyCharacter && person2 is EnemyCharacter)
    }

    fun getAllyTeam(person: DungeonCharacter): Party<DungeonCharacter> {
        return when(person.team) {
            Party.Team.HEROES -> heroParty
            Party.Team.FIENDS -> enemyParty
        }
    }

    fun getOpposingTeam(person: DungeonCharacter): Party<DungeonCharacter> {
        return when(person.team) {
            Party.Team.HEROES -> enemyParty
            Party.Team.FIENDS -> heroParty
        }
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

    private fun atTurnEnd(): SkirmishStatus {
        removeCorpses()
        return when {
            heroParty.gotWiped -> SkirmishStatus.DEFEAT
            enemyParty.gotWiped -> SkirmishStatus.VICTORY
            else -> SkirmishStatus.CONTINUE_TURN
        }
    }

    private fun removeCorpses() {
        if (!deleteCorpses) {
            return
        }
        heroParty.removeDeadCharacters()
        enemyParty.removeDeadCharacters()
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

                when (atTurnEnd()) {
                    SkirmishStatus.VICTORY -> {
                        println("Congratulations, your adventurers vanquished the enemy!")
                        println("VICTORY")
                        return
                    }
                    SkirmishStatus.DEFEAT -> {
                        println("Unfortunately, your adventurers failed")
                        println("DEFEAT")
                        return
                    }
                    else -> {
                    }
                }
            } while (sequenceOfTurns.isNotEmpty())
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
                .filter { it.isAlive }
                .forEach {
                    tempCollection.add(Triple(it.speed, it, PlayerControl))
                }
            enemyParty.getCharacters()
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

    enum class SkirmishStatus {
        CONTINUE_TURN, VICTORY, DEFEAT
    }
}
