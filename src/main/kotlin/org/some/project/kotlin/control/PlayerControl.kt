package org.some.project.kotlin.control

import org.some.project.kotlin.abilities.AbilityCast
import org.some.project.kotlin.abilities.PassTurn
import org.some.project.kotlin.abilities.Position
import org.some.project.kotlin.chars.DungeonCharacter
import org.some.project.kotlin.scenes.Skirmish
import kotlin.system.exitProcess

object PlayerControl: ControlType() {

    override fun getAbility(skirmish: Skirmish, character: DungeonCharacter): AbilityCast {
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
                        print("Sorry, help is not implemented yet, go figure out yourself:> ")
                        continue
                    }
                    in lookUpAbilities -> {
                        val potentialAbility = lookUpAbilities.getValue(abilityInput)
                        if (potentialAbility !in applicableAbilities) {
                            print("Ability ${potentialAbility.fancyName} cannot be used, use another one: > ")
                            continue
                        }
                        if (potentialAbility.numberOfArgs == 0) {
                            return AbilityCast(potentialAbility, character.pos)
                        }
                        if (args.size - 1 < potentialAbility.numberOfArgs) {
                            print("Ability ${potentialAbility.fancyName} needs a target, please specify with target: > ")
                            continue
                        }
                        val result = Position.convert(args[1])
                        if (result.isFailure) {
                            print("Could not understand argument: ${result.errorMessageOrNull()}")
                            continue
                        }
                        else {
                            return AbilityCast(potentialAbility, result.getOrNull()!!)
                        }
                    }
                    else -> {
                        print("Could not understand the input, sorry, try again: > ")
                    }
                }
            } while (true)
        } else {
            print("cannot use anything. ")
            return AbilityCast(PassTurn, character.pos)
        }
    }
}
