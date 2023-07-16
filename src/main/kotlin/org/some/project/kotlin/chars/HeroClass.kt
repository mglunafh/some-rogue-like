package org.some.project.kotlin.chars

import org.some.project.kotlin.abilities.Ability
import org.some.project.kotlin.abilities.AbilityEffect
import org.some.project.kotlin.abilities.AllOf
import org.some.project.kotlin.abilities.AnyOf
import org.some.project.kotlin.abilities.Damage
import org.some.project.kotlin.abilities.Healing
import org.some.project.kotlin.abilities.Position.Companion.ZERO
import org.some.project.kotlin.scenes.Skirmish

open class HeroClass(
    final override val name: String,
    final override val baseHp: Int,
    final override val minDamage: Int,
    final override val maxDamage: Int,
    final override val turns: Int = 1,
    final override val speed: Int,
    final override val abilities: List<Ability>
) : DungeonClass {

    init {
        require(name.isNotBlank()) { "Class name must not be empty" }
        require(baseHp > 0) { "Amount of health points must be positive, got $baseHp instead" }
        require(minDamage < maxDamage) { "Min damage $minDamage must be less than max damage value $maxDamage" }
        require(turns > 0) { "That's a playable character, it must have an ability to act at least once" }
        require(abilities.isNotEmpty()) { "Please give some abilities to the class" }
    }

    override val abilitiesLookUp: Map<String, Ability> by lazy {
        abilities.associateBy { it.commandName }
    }
}

object Crusader : HeroClass(
    name = "Crusader",
    baseHp = 40,
    minDamage = 5,
    maxDamage = 10,
    speed = 3,
    abilities = listOf(Smite, CrushingBlow, ShowThemTheBills)
) {

    object Smite : Ability("Smite", 1) {
        val effect = AbilityEffect(effect = Damage(8), appliedTo = AnyOf.FRONT_TWO, appliedFrom = AnyOf.FRONT_TWO)

        override fun isApplicable(skirmish: Skirmish): Boolean {
            return isPresentOnPositions(skirmish.enemyParty, effect.appliedTo.positions)
        }
    }

    object CrushingBlow : Ability("Crushing Blow", 1) {
        val effect = AbilityEffect(effect = Damage(4), appliedTo = AnyOf.FRONT_TWO, appliedFrom = AnyOf.FRONT_TWO)

        override fun isApplicable(skirmish: Skirmish): Boolean {
            return isPresentOnPositions(skirmish.enemyParty, effect.appliedTo)
        }
    }

    object ShowThemTheBills : Ability("Show Them the Bills", 0) {
        val effect = AbilityEffect(effect = Damage(3), appliedTo = AllOf.FRONT_TWO, appliedFrom = AnyOf.FRONT_TWO)

        override fun isApplicable(skirmish: Skirmish): Boolean {
            return isPresentOnPositions(skirmish.enemyParty, effect.appliedTo)
        }
    }
}

object Highwayman : HeroClass(
    name = "Highwayman",
    baseHp = 28,
    minDamage = 8,
    maxDamage = 15,
    speed = 5,
    abilities = listOf(DuelistAdvance, PointBlackShot)
) {

    object DuelistAdvance : Ability("Duelist Advance", 1) {
        // TODO: move forward
        // TODO: riposte
        val effect = AbilityEffect(effect = Damage(3), appliedTo = AnyOf.FRONT_THREE, appliedFrom = AnyOf.FRONT_THREE)

        override fun isApplicable(skirmish: Skirmish): Boolean {
            return isPresentOnPositions(skirmish.enemyParty, effect.appliedTo)
        }
    }

    object PointBlackShot : Ability("Point-Blank Shot", 0) {

        // TODO: move back
        val effect = AbilityEffect(effect = Damage(10), appliedTo = AnyOf(ZERO), appliedFrom = AnyOf(ZERO))

        override fun isApplicable(skirmish: Skirmish): Boolean {
            return isPresentOnPositions(skirmish.enemyParty, effect.appliedTo)
        }
    }
}

object Vestal : HeroClass(
    name = "Vestal",
    baseHp = 25,
    minDamage = 3,
    maxDamage = 8,
    speed = 4,
    abilities = listOf(MaceBash, DivineGrace, DivineComfort)
) {

    object DivineComfort : Ability("Divine Comfort", 1) {
        val effect = AbilityEffect(effect = Healing(10), appliedTo = AnyOf.ALL_FOUR, appliedFrom = AnyOf.BACKLINE_TWO)
        override fun isApplicable(skirmish: Skirmish): Boolean = true
    }

    object DivineGrace : Ability("Divine Grace", 0) {
        val effect = AbilityEffect(effect = Healing(4), appliedTo = AllOf.ALL_FOUR, appliedFrom = AnyOf.BACKLINE_THREE)
        override fun isApplicable(skirmish: Skirmish): Boolean = true
    }

    object MaceBash : Ability("Mace Bash", 1) {
        val effect = AbilityEffect(effect = Damage(4), appliedTo = AnyOf.FRONT_TWO, appliedFrom = AnyOf.FRONT_TWO)

        override fun isApplicable(skirmish: Skirmish): Boolean {
            return isPresentOnPositions(skirmish.enemyParty, effect.appliedTo)
        }
    }
}
