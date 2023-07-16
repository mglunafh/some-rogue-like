package org.some.project.kotlin.control

import org.some.project.kotlin.abilities.Ability
import org.some.project.kotlin.abilities.PassTurn
import org.some.project.kotlin.chars.DungeonCharacter
import org.some.project.kotlin.scenes.Skirmish
import kotlin.system.exitProcess

object PlayerControl: ControlType() {

    override fun getAbility(skirmish: Skirmish, character: DungeonCharacter): Ability {
        val applicableAbilities = character.abilities.filter { it.isApplicable(skirmish) }
        if (applicableAbilities.isNotEmpty()) {
            print("can use any of ${applicableAbilities.joinToString { it.fancyName }}. > ")

            val lookUpAbilities = character.charClass.abilitiesLookUp

            var input: String?

            do {
                input = readlnOrNull()
                if (input == null) continue
                val args = input.split(" ")
                val abilityInput = args[0]

                when (abilityInput.lowercase()) {
                    in listOf("q", "quit", "exit") -> {
                        println("Have a nice day!")
                        exitProcess(0)                          // TODO: Save system
                    }
                    in listOf("h", "help") -> {
                        println("Help is not implemented yet, sorry")
                        return applicableAbilities.random()
                    }
                    in lookUpAbilities -> {
                        val potentialAbility = lookUpAbilities.getValue(abilityInput)
                        if (potentialAbility !in applicableAbilities) {
                            println("Ability ${potentialAbility.fancyName} cannot be used, use another one: > ")
                            continue
                        }
                        if (args.size <= potentialAbility.numberOfArgs) {
                            println("Ability ${potentialAbility.fancyName} needs a target, please specify with target: > ")
                            continue
                        }
                        // TODO: argument input
                        return potentialAbility
                    }
                    else -> {
                        print("Could not understand the input, sorry, try again: > ")
                    }
                }
            } while (true)
        } else {
            print("cannot use anything. ")
            return PassTurn
        }
    }
}
