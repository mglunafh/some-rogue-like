package org.some.project.kotlin.chars

import org.some.project.kotlin.abilities.Ability
import org.some.project.kotlin.abilities.AbilityEffect
import org.some.project.kotlin.abilities.AllOf
import org.some.project.kotlin.abilities.AnyOf
import org.some.project.kotlin.abilities.BasicEffect
import org.some.project.kotlin.abilities.Damage
import org.some.project.kotlin.abilities.Position
import org.some.project.kotlin.scenes.Skirmish

open class EnemyClass(
    final override val name: String,
    final override val baseHp: Int,
    final override val minDamage: Int,
    final override val maxDamage: Int,
    final override val turns: Int = 1,
    final override val speed: Int,
    final override val abilities: List<Ability<BasicEffect>>
): DungeonClass {

    override val abilitiesLookUp: Map<String, Ability<BasicEffect>> by lazy {
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

    object Slice: Ability<Damage>(
        name = "Slice",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(effect = Damage(4), appliedFrom = AnyOf.FRONT_THREE, appliedTo = AllOf.FRONT_TWO)
    ) {

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

    object Shank: Ability<Damage>(
        name = "Shank",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(effect = Damage(6), appliedFrom = AnyOf.FRONT_THREE, appliedTo = AnyOf.FRONT_TWO)
    ) {


        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val target = skirmish.heroParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            val damage = mainEffect.effect.dmg
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

    object GraveyardSlash: Ability<Damage>(
        name = "Graveyard Slash",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(effect = Damage(5), appliedTo = AnyOf.FRONT_THREE, appliedFrom = AnyOf.FRONT_TWO)
    ) {


        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val target = skirmish.heroParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            val damage = mainEffect.effect.dmg
            target.takeDamage(damage)
            println("${dealer.fancyName} hit ${target.fancyName} for $damage")
        }
    }

    object GraveyardStumble: Ability<Damage>(
        name = "Graveyard Stumble",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(
            effect = Damage(4),
            appliedTo = AnyOf.FRONT_THREE,
            appliedFrom = AnyOf.BACKLINE_THREE
        )
    ) {


        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val target = skirmish.heroParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            val damage = GraveyardSlash.mainEffect.effect.dmg
            target.takeDamage(mainEffect.effect.dmg)
            println("${dealer.fancyName} hit ${target.fancyName} for $damage")
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

    object Spit: Ability<Damage>(
        name = "Spit",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(Damage(4), appliedTo = AnyOf.ALL_FOUR, appliedFrom = AnyOf.ALL_FOUR)
    ) {


        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val target = skirmish.heroParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            val damage = mainEffect.effect.dmg
            target.takeDamage(damage)
            println("${dealer.fancyName} hit ${target.fancyName} for $damage")
        }
    }

    object Bite: Ability<Damage>(
        name = "Bite",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(Damage(4), appliedTo = AnyOf.ALL_FOUR, appliedFrom = AnyOf.ALL_FOUR)
    ) {

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val target = skirmish.heroParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            val damage = mainEffect.effect.dmg
            target.takeDamage(damage)
            println("${dealer.fancyName} hit ${target.fancyName} for $damage")
        }
    }
}
