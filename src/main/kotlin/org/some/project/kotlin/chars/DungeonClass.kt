package org.some.project.kotlin.chars

import org.some.project.kotlin.abilities.Ability

interface DungeonClass {

    val name: String
    val baseHp: Int
    val minDamage: Int
    val maxDamage: Int
    val abilities: Array<Ability>

    val listOfAbilities: String
        get() = "Abilities: ${abilities.joinToString { it.name }}"

}