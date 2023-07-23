package org.some.project.kotlin.chars

import org.some.project.kotlin.abilities.Ability
import org.some.project.kotlin.abilities.AbilityEffect
import org.some.project.kotlin.abilities.AllOnPositions
import org.some.project.kotlin.abilities.AnyOf
import org.some.project.kotlin.abilities.BasicEffect
import org.some.project.kotlin.abilities.Damage
import org.some.project.kotlin.abilities.Position
import org.some.project.kotlin.abilities.TargetCriteria.Companion.allEnemies
import org.some.project.kotlin.abilities.TargetCriteria.Companion.anyEnemy
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
        needsTarget = true,
        mainEffect = AbilityEffect(
            effect = Damage(4),
            appliedFrom = AnyOf.FRONT_THREE,
            appliedTo = allEnemies(Position.FRONTLINE_TWO)
        )
    ) {

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val team = skirmish.getOpposingTeam(dealer)
            val criteria = mainEffect.appliedTo.forPosition
            val targetPositions = (criteria as AllOnPositions).positions
            val damage = mainEffect.effect.dmg
            require(targetPositions.any { team[it] != null }) {
                "Fix the target validation please:" +
                        " ${dealer.fancyName} tries to hit empty positions $targetPositions" +
                        " with ${this.fancyName}"
            }
            targetPositions.forEach { pos ->
                val target = team[pos]
                target?.also {
                    println("${dealer.fancyName} hit ${it.fancyName} for $damage")
                }?.takeDamage(damage)
            }
        }
    }

    object Shank: Ability<Damage>(
        name = "Shank",
        needsTarget = true,
        mainEffect = AbilityEffect(
            effect = Damage(6),
            appliedFrom = AnyOf.FRONT_THREE,
            appliedTo = anyEnemy(Position.FRONTLINE_TWO))
    ) {

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val team = skirmish.getOpposingTeam(dealer)
            val target = team[targetPosition]
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
        needsTarget = true,
        mainEffect = AbilityEffect(
            effect = Damage(5),
            appliedFrom = AnyOf.FRONT_TWO,
            appliedTo = anyEnemy(Position.FRONTLINE_THREE))
    ) {

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val team = skirmish.getOpposingTeam(dealer)
            val target = team[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            val damage = mainEffect.effect.dmg
            target.takeDamage(damage)
            println("${dealer.fancyName} hit ${target.fancyName} for $damage")
        }
    }

    object GraveyardStumble: Ability<Damage>(
        name = "Graveyard Stumble",
        needsTarget = true,
        mainEffect = AbilityEffect(
            effect = Damage(4),
            appliedFrom = AnyOf.BACKLINE_THREE,
            appliedTo = anyEnemy(Position.FRONTLINE_THREE)
        )
    ) {

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val team = skirmish.getOpposingTeam(dealer)
            val target = team[targetPosition]
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
        needsTarget = true,
        mainEffect = AbilityEffect(
            effect = Damage(4),
            appliedFrom = AnyOf.ALL_FOUR,
            appliedTo = anyEnemy(Position.ALL_FOUR))
    ) {

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val team = skirmish.getOpposingTeam(dealer)
            val target = team[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            val damage = mainEffect.effect.dmg
            target.takeDamage(damage)
            println("${dealer.fancyName} hit ${target.fancyName} for $damage")
        }
    }

    object Bite: Ability<Damage>(
        name = "Bite",
        needsTarget = true,
        mainEffect = AbilityEffect(
            effect = Damage(4),
            appliedFrom = AnyOf.ALL_FOUR,
            appliedTo = anyEnemy(Position.ALL_FOUR))
    ) {

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val team = skirmish.getOpposingTeam(dealer)
            val target = team[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            val damage = mainEffect.effect.dmg
            target.takeDamage(damage)
            println("${dealer.fancyName} hit ${target.fancyName} for $damage")
        }
    }
}
