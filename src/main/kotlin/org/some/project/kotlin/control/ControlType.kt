package org.some.project.kotlin.control

import org.some.project.kotlin.abilities.AbilityCast
import org.some.project.kotlin.chars.DungeonCharacter
import org.some.project.kotlin.scenes.Skirmish

sealed class ControlType {

    abstract fun getAbility(skirmish: Skirmish, character: DungeonCharacter): AbilityCast

}
