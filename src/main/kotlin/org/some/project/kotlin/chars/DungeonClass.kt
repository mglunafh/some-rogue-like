package org.some.project.kotlin.chars

import org.some.project.kotlin.abilities.Ability
import kotlin.reflect.KProperty

interface DungeonClass {
    operator fun getValue(dungeonCharacter: DungeonCharacter, property: KProperty<*>): List<Ability> = abilities

    val name: String
    val baseHp: Int
    val minDamage: Int
    val maxDamage: Int
    val turns: Int
    val speed: Int
    val abilities: List<Ability>

    val listOfAbilities: String
        get() = "Abilities: ${abilities.joinToString { it.name }}"

}