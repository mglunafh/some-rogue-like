package org.some.project.kotlin.chars

import org.some.project.kotlin.abilities.Ability
import org.some.project.kotlin.abilities.Position.Companion.ALL_FOUR
import org.some.project.kotlin.abilities.Position.Companion.ONE
import org.some.project.kotlin.abilities.Position.Companion.THREE
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
) : DungeonClass

object Brigand : EnemyClass(
    name = "Brigand",
    baseHp = 20,
    minDamage = 5,
    maxDamage = 10,
    speed = 4,
    abilities = listOf(Slice, Shank)
) {

    object Slice : Ability("Slice") {
        override fun isApplicable(skirmish: Skirmish): Boolean {
            return skirmish.heroParty.isPresentOnAny(listOf(ZERO, ONE))
        }
    }

    object Shank : Ability("Shank") {
        override fun isApplicable(skirmish: Skirmish): Boolean {
            return skirmish.heroParty.isPresentOnAny(listOf(ZERO, ONE))
        }
    }
}

object BoneSoldier : EnemyClass(
    name = "Bone Soldier",
    baseHp = 10,
    minDamage = 3,
    maxDamage = 8,
    speed = 4,
    abilities = listOf(GraveyardSlash, GraveyardStumble)
) {

    object GraveyardSlash : Ability("Graveyard Slash") {
        override fun isApplicable(skirmish: Skirmish): Boolean {
            return isPresentOnPositions(skirmish.heroParty, listOf(ZERO, ONE, THREE))
        }
    }

    object GraveyardStumble : Ability("Graveyard Stumble") {
        override fun isApplicable(skirmish: Skirmish): Boolean {
            return isPresentOnPositions(skirmish.heroParty, listOf(ZERO, ONE, THREE))
        }
    }
}

object Spider : EnemyClass(
    name = "Spider",
    baseHp = 8,
    minDamage = 3,
    maxDamage = 6,
    speed = 5,
    abilities = listOf(Spit, Bite)
) {

    object Spit : Ability("Spit") {
        override fun isApplicable(skirmish: Skirmish): Boolean = isPresentOnPositions(skirmish.heroParty, ALL_FOUR)

    }

    object Bite : Ability("Bite") {
        override fun isApplicable(skirmish: Skirmish): Boolean = isPresentOnPositions(skirmish.heroParty, ALL_FOUR)
    }
}
