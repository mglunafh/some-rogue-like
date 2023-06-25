package org.some.project.kotlin.chars

import org.some.project.kotlin.abilities.Ability
import org.some.project.kotlin.abilities.CrushingBlow
import org.some.project.kotlin.abilities.DivineGrace
import org.some.project.kotlin.abilities.DuelistAdvance
import org.some.project.kotlin.abilities.MaceBash
import org.some.project.kotlin.abilities.PointBlackShot
import org.some.project.kotlin.abilities.Smite

open class CharacterClass(
    val name: String,
    val baseHp: Int,
    val minDamage: Int,
    val maxDamage: Int,
    val abilities: Array<Ability>) {

    init {
        require(name.isNotBlank()) { "Class name must not be empty" }
        require(baseHp > 0) { "Amount of health points must be positive, got $baseHp instead" }
        require(minDamage < maxDamage) { "Min damage $minDamage must be less than max damage value $maxDamage" }
        require(abilities.isNotEmpty()) { "Please give some abilities to the class" }
    }

    val listOfAbilities = "Abilities: ${abilities.joinToString { it.name }}"
}

object Crusader: CharacterClass(
    name = "Crusader",
    baseHp = 40,
    minDamage = 5,
    maxDamage = 10,
    abilities = arrayOf(Smite, CrushingBlow)
)

object Highwayman: CharacterClass(
    name = "Highwayman",
    baseHp = 28,
    minDamage = 8,
    maxDamage = 15,
    abilities = arrayOf(DuelistAdvance, PointBlackShot)
)

object Vestal: CharacterClass(
    name = "Vestal",
    baseHp = 25,
    minDamage = 3,
    maxDamage = 8,
    abilities = arrayOf(MaceBash, DivineGrace)
)
