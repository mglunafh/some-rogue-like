package org.some.project.kotlin.chars

import org.some.project.kotlin.abilities.Ability
import org.some.project.kotlin.abilities.AbilityEffect
import org.some.project.kotlin.abilities.AllOf
import org.some.project.kotlin.abilities.AnyOf
import org.some.project.kotlin.abilities.Damage

open class HeroClass(
    final override val name: String,
    final override val baseHp: Int,
    final override val minDamage: Int,
    final override val maxDamage: Int,
    final override val turns: Int = 1,
    final override val speed: Int,
    final override val abilities: Array<Ability>
) : DungeonClass {

    init {
        require(name.isNotBlank()) { "Class name must not be empty" }
        require(baseHp > 0) { "Amount of health points must be positive, got $baseHp instead" }
        require(minDamage < maxDamage) { "Min damage $minDamage must be less than max damage value $maxDamage" }
        require(turns > 0) { "That's a playable character, it must have an ability to act at least once" }
        require(abilities.isNotEmpty()) { "Please give some abilities to the class" }
    }
}

object Crusader : HeroClass(
    name = "Crusader",
    baseHp = 40,
    minDamage = 5,
    maxDamage = 10,
    speed = 3,
    abilities = arrayOf(Smite, CrushingBlow, ShowThemTheBills)
) {

    object Smite : Ability("Smite") {
        val effect = AbilityEffect(effect = Damage(8), appliedTo =  AnyOf.FIRST_TWO, appliedFrom = AnyOf.FIRST_TWO)
    }

    object CrushingBlow : Ability("Crushing Blow") {
        val effect = AbilityEffect(effect = Damage(4), appliedTo = AnyOf.FIRST_TWO, appliedFrom = AnyOf.FIRST_TWO)
    }

    object ShowThemTheBills: Ability("Show Them the Bills") {
        val effect = AbilityEffect(effect = Damage(3), appliedTo = AllOf.FIRST_TWO, appliedFrom = AnyOf.FIRST_TWO)
    }
}

object Highwayman : HeroClass(
    name = "Highwayman",
    baseHp = 28,
    minDamage = 8,
    maxDamage = 15,
    speed = 5,
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
    speed = 4,
    abilities = arrayOf(MaceBash, DivineGrace)
) {

    object DivineComfort: Ability("Divine Comfort")

    object DivineGrace: Ability("Divine Grace")

    object MaceBash: Ability("Mace Bash")

}
