package org.some.project.kotlin.chars

import org.some.project.kotlin.FancyName
import org.some.project.kotlin.abilities.Ability
import kotlin.reflect.KProperty

interface DungeonClass : FancyName {
    operator fun getValue(dungeonCharacter: DungeonCharacter, property: KProperty<*>): List<Ability> = abilities

    val name: String
    val baseHp: Int
    val minDamage: Int
    val maxDamage: Int
    val turns: Int
    val speed: Int
    val abilities: List<Ability>
    val abilitiesLookUp: Map<String, Ability>

    override val fancyName: String
        get() = "\u001b[34m$name\u001b[0m"

    val listOfAbilities: String
        get() = "Abilities: ${abilities.joinToString { it.fancyName }}"

}