package org.some.project.kotlin.control

import org.some.project.kotlin.abilities.AbilityCast
import org.some.project.kotlin.abilities.BasicEffect
import org.some.project.kotlin.abilities.PassTurn
import org.some.project.kotlin.chars.DungeonCharacter
import org.some.project.kotlin.scenes.Skirmish

object ComputerControl: ControlType() {

    override fun getAbility(skirmish: Skirmish, character: DungeonCharacter): AbilityCast<BasicEffect> {
        val applicableAbilities = character.abilities.filter { it.canBeUsedFrom(character.pos) }
        return if (applicableAbilities.isNotEmpty()) {
            print("can use any of ${applicableAbilities.joinToString { it.fancyName }}. ")

            val ability = applicableAbilities.random()
            if (ability.numberOfArgs == 0) {
                AbilityCast(ability, character.pos)
            } else {
                val randomPosition = ability.mainEffect.appliedTo.positions.random()
                AbilityCast(ability, randomPosition)
            }
        } else {
            print("cannot use anything. ")
            AbilityCast(PassTurn, character.pos)
        }
    }
}
