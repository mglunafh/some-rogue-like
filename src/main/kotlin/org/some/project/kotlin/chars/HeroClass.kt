package org.some.project.kotlin.chars

import org.some.project.kotlin.abilities.Ability

open class HeroClass(
    final override val name: String,
    final override val baseHp: Int,
    final override val minDamage: Int,
    final override val maxDamage: Int,
    final override val abilities: Array<Ability>
) : DungeonClass {

    init {
        require(name.isNotBlank()) { "Class name must not be empty" }
        require(baseHp > 0) { "Amount of health points must be positive, got $baseHp instead" }
        require(minDamage < maxDamage) { "Min damage $minDamage must be less than max damage value $maxDamage" }
        require(abilities.isNotEmpty()) { "Please give some abilities to the class" }
    }
}

object Crusader : HeroClass(
    name = "Crusader",
    baseHp = 40,
    minDamage = 5,
    maxDamage = 10,
    abilities = arrayOf(Smite, CrushingBlow)
) {

    object Smite : Ability("Smite")

    object CrushingBlow : Ability("Crushing Blow")

}

object Highwayman : HeroClass(
    name = "Highwayman",
    baseHp = 28,
    minDamage = 8,
    maxDamage = 15,
    abilities = arrayOf(DuelistAdvance, PointBlackShot)
) {

    object DuelistAdvance: Ability("Duelist Advance")

    object PointBlackShot: Ability("Point-Blank Shot")

}

object Vestal : HeroClass(
    name = "Vestal",
    baseHp = 25,
    minDamage = 3,
    maxDamage = 8,
    abilities = arrayOf(MaceBash, DivineGrace)
) {

    object DivineGrace: Ability("Divine Grace")

    object MaceBash: Ability("Mace Bash")

}
