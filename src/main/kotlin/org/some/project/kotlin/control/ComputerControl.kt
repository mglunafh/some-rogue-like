package org.some.project.kotlin.control

import org.some.project.kotlin.abilities.AbilityCast
import org.some.project.kotlin.abilities.BasicEffect
import org.some.project.kotlin.abilities.Enemy
import org.some.project.kotlin.abilities.PassTurn
import org.some.project.kotlin.chars.DungeonCharacter
import org.some.project.kotlin.scenes.Skirmish

object ComputerControl: ControlType() {

    override fun getAbility(skirmish: Skirmish, character: DungeonCharacter): AbilityCast<BasicEffect> {
        val applicableAbilities = character.abilities.filter { it.canBeUsedFrom(character.pos) }.shuffled()
        if (applicableAbilities.isEmpty()) {
            print("cannot use anything. ")
            return AbilityCast(PassTurn, character.pos)
        }

        print("can use any of ${applicableAbilities.joinToString { it.fancyName }}. ")

        applicableAbilities.forEach { ability ->
            // TODO RGL-15: Add help
            if (!ability.needsTarget) {
                return AbilityCast(ability, character.pos)
            }
            val criteria = ability.mainEffect.appliedTo
            val party = if (criteria.forCharacher.any { it == Enemy }) {
                skirmish.getOpposingTeam(character)
            } else {
                skirmish.getAllyTeam(character)
            }

            val targets = party.getCharacters().filterNotNull().shuffled()
            targets.forEach { target ->
                if (criteria.isApplicable(character, target)) {
                    return AbilityCast(ability, target.pos)
                }
            }

        }
        print("cannot use anything. ")
        return AbilityCast(PassTurn, character.pos)
    }
}
