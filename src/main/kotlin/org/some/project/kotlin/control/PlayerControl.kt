package org.some.project.kotlin.control

import org.some.project.kotlin.abilities.AbilityCast
import org.some.project.kotlin.abilities.BasicEffect
import org.some.project.kotlin.abilities.Enemy
import org.some.project.kotlin.abilities.PassTurn
import org.some.project.kotlin.abilities.Position
import org.some.project.kotlin.chars.DungeonCharacter
import org.some.project.kotlin.scenes.Skirmish
import kotlin.system.exitProcess

object PlayerControl: ControlType() {

    override fun getAbility(skirmish: Skirmish, character: DungeonCharacter): AbilityCast<BasicEffect> {
        val applicableAbilities = character.abilities.filter { it.canBeUsedFrom(character.pos) }
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
                    in listOf("p", "pass") -> {
                        return AbilityCast(PassTurn, character.pos)
                    }
                    // TODO RGL-15
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
                        if (!potentialAbility.needsTarget) {
                            return AbilityCast(potentialAbility, character.pos)
                        }
                        // TODO RGL-15: add help for the ability
                        if (args.size <= 1) {
                            print("Ability ${potentialAbility.fancyName} needs a target, please specify with target: > ")
                            continue
                        }
                        val result = Position.convert(args[1])
                        if (result.isFailure) {
                            print("Could not understand argument: ${result.errorMessageOrNull()}. > ")
                            continue
                        }
                        else {
                            val criteria = potentialAbility.mainEffect.appliedTo
                            val position = result.getOrNull()!!
                            val party = if (criteria.forCharacher.any { it == Enemy }) {
                                skirmish.getOpposingTeam(character)
                            } else {
                                skirmish.getAllyTeam(character)
                            }
                            val t = party[position]
                            if (t != null) {
                                // TODO RGL-16: ability may have various reasons why it couldn't be applied (like healing on full hp or out of reach)
                                if (potentialAbility.canBeUsedUpon(character, t)) {
                                    return AbilityCast(potentialAbility, position)
                                }
                                else {
                                    print("Ability ${potentialAbility.fancyName} doesn't have a reach to position $position. > ")
                                }
                            } else {
                                print("There is nobody on position $position. > ")
                            }
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
