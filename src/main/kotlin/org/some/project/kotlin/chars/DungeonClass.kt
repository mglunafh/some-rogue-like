package org.some.project.kotlin.chars

import org.some.project.kotlin.FancyName
import org.some.project.kotlin.abilities.Ability
import org.some.project.kotlin.abilities.BasicEffect
import kotlin.reflect.KProperty

interface DungeonClass : FancyName {
    operator fun getValue(dungeonCharacter: DungeonCharacter, property: KProperty<*>): List<Ability<BasicEffect>> = abilities

    val name: String
    val baseHp: Int
    val minDamage: Int
    val maxDamage: Int
    val turns: Int
    val speed: Int
    val abilities: List<Ability<BasicEffect>>
    val abilitiesLookUp: Map<String, Ability<BasicEffect>>

    override val fancyName: String
        get() = "\u001b[34m$name\u001b[0m"

    val listOfAbilities: String
        get() = "Abilities: ${abilities.joinToString { it.fancyName }} / ${abilities.joinToString { it.commandName }}"

}
