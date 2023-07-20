package org.some.project.kotlin.chars

import org.some.project.kotlin.abilities.Ability
import org.some.project.kotlin.abilities.AbilityEffect
import org.some.project.kotlin.abilities.AllOf
import org.some.project.kotlin.abilities.AnyOf
import org.some.project.kotlin.abilities.Damage
import org.some.project.kotlin.abilities.Position
import org.some.project.kotlin.abilities.Position.Companion.ONE
import org.some.project.kotlin.abilities.Position.Companion.ZERO
import org.some.project.kotlin.scenes.Skirmish

open class EnemyClass(
    final override val name: String,
    final override val baseHp: Int,
    final override val minDamage: Int,
    final override val maxDamage: Int,
    final override val turns: Int = 1,
    final override val speed: Int,
    final override val abilities: List<Ability>
): DungeonClass {

    override val abilitiesLookUp: Map<String, Ability> by lazy {
        abilities.associateBy { it.commandName }
    }
}

object Brigand: EnemyClass(
    name = "Brigand",
    baseHp = 20,
    minDamage = 5,
    maxDamage = 10,
    speed = 4,
    abilities = listOf(Slice, Shank)
) {

    object Slice: Ability(
        name = "Slice",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(effect = Damage(4), appliedFrom = AnyOf.FRONT_THREE, appliedTo = AllOf.FRONT_TWO)
    ) {

        override fun isApplicable(skirmish: Skirmish): Boolean {
            return skirmish.heroParty.isPresentOnAny(listOf(ZERO, ONE))
        }

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val targetPositions = mainEffect.appliedTo.positions
            require(targetPositions.any { skirmish.heroParty[it] != null }) {
                "Fix the target validation please:" +
                        " ${dealer.fancyName} tries to hit empty positions ${mainEffect.appliedTo.positions}" +
                        " with ${this.fancyName}"
            }
            targetPositions.forEach {
                val target = skirmish.heroParty[it]
                val damage = (mainEffect.effect as Damage).dmg
                target?.also {
                    println("${dealer.fancyName} hit ${it.fancyName} for $damage")
                }?.takeDamage(damage)
            }
        }
    }

    object Shank: Ability(
        name = "Shank",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(effect = Damage(6), appliedFrom = AnyOf.FRONT_THREE, appliedTo = AnyOf.FRONT_TWO)) {

        override fun isApplicable(skirmish: Skirmish): Boolean {
            return skirmish.heroParty.isPresentOnAny(listOf(ZERO, ONE))
        }

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val target = skirmish.heroParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            val damage = (mainEffect.effect as Damage).dmg
            target.takeDamage(damage)
            println("${dealer.fancyName} hit ${target.fancyName} for $damage")
        }
    }
}

object BoneSoldier: EnemyClass(
    name = "Bone Soldier",
    baseHp = 10,
    minDamage = 3,
    maxDamage = 8,
    speed = 4,
    abilities = listOf(GraveyardSlash, GraveyardStumble)
) {

    object GraveyardSlash: Ability(
        name = "Graveyard Slash",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(effect = Damage(5), appliedTo = AnyOf.FRONT_THREE, appliedFrom = AnyOf.FRONT_TWO)) {

        override fun isApplicable(skirmish: Skirmish): Boolean {
            return isPresentOnPositions(skirmish.heroParty, mainEffect.appliedTo.positions)
        }

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val target = skirmish.heroParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            val damage = (mainEffect.effect as Damage).dmg
            target.takeDamage(damage)
            println("${dealer.fancyName} hit $target for $damage")
        }
    }

    object GraveyardStumble: Ability(
        name = "Graveyard Stumble",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(effect = Damage(4), appliedTo = AnyOf.FRONT_THREE, appliedFrom = AnyOf.BACKLINE_THREE)) {

        override fun isApplicable(skirmish: Skirmish): Boolean {
            return isPresentOnPositions(skirmish.heroParty, mainEffect.appliedTo.positions)
        }

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val target = skirmish.heroParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            target.takeDamage((mainEffect.effect as Damage).dmg)
        }
    }
}

object Spider: EnemyClass(
    name = "Spider",
    baseHp = 8,
    minDamage = 3,
    maxDamage = 6,
    speed = 5,
    abilities = listOf(Spit, Bite)
) {

    object Spit: Ability(
        name = "Spit",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(Damage(4), appliedTo = AnyOf.ALL_FOUR, appliedFrom = AnyOf.ALL_FOUR)) {

        override fun isApplicable(skirmish: Skirmish): Boolean = isPresentOnPositions(skirmish.heroParty, mainEffect.appliedTo.positions)
        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val target = skirmish.heroParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            target.takeDamage((mainEffect.effect as Damage).dmg)
        }
    }

    object Bite: Ability(
        name = "Bite",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(Damage(4), appliedTo = AnyOf.ALL_FOUR, appliedFrom = AnyOf.ALL_FOUR)) {

        override fun isApplicable(skirmish: Skirmish): Boolean = isPresentOnPositions(skirmish.heroParty, mainEffect.appliedTo.positions)
        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val target = skirmish.heroParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            target.takeDamage((mainEffect.effect as Damage).dmg)
        }
    }
}
