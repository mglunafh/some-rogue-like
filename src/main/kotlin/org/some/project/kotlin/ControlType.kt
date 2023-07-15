package org.some.project.kotlin

import org.some.project.kotlin.abilities.Ability
import org.some.project.kotlin.abilities.PassTurn
import org.some.project.kotlin.chars.DungeonCharacter
import org.some.project.kotlin.scenes.Skirmish

sealed class ControlType {

    abstract fun getAbility(skirmish: Skirmish, character: DungeonCharacter): Ability

    object PlayerControl: ControlType() {
        override fun getAbility(skirmish: Skirmish, character: DungeonCharacter): Ability {
            val applicableAbilities = character.abilities.filter { it.isApplicable(skirmish) }
            return if (applicableAbilities.isNotEmpty()) {
                print("can use any of ${applicableAbilities.joinToString { it.name }}. ")
                applicableAbilities.random()
            } else {
                print("cannot use anything. ")
                PassTurn
            }
        }
    }

    object ComputerControl: ControlType() {
        override fun getAbility(skirmish: Skirmish, character: DungeonCharacter): Ability {
            val applicableAbilities = character.abilities.filter { it.isApplicable(skirmish) }
            return if (applicableAbilities.isNotEmpty()) {
                print("can use any of ${applicableAbilities.joinToString { it.name }}. ")
                applicableAbilities.random()
            } else {
                print("cannot use anything. ")
                PassTurn
            }
        }
    }
}