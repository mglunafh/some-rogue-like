package org.some.project.kotlin.chars

import org.some.project.kotlin.abilities.Ability

open class EnemyClass(
    final override val name: String,
    final override val baseHp: Int,
    final override val minDamage: Int,
    final override val maxDamage: Int,
    final override val abilities: Array<Ability>
) : DungeonClass

object Brigand : EnemyClass(
    name = "Brigand",
    baseHp = 20,
    minDamage = 5,
    maxDamage = 10,
    abilities = arrayOf(Slice, Shank)
) {

    object Slice : Ability("Slice")

    object Shank : Ability("Shank")

}

object BoneSoldier : EnemyClass(
    name = "Bone Soldier",
    baseHp = 10,
    minDamage = 3,
    maxDamage = 8,
    abilities = arrayOf(GraveyardSlash, GraveyardStumble)
) {

    object GraveyardSlash : Ability("Graveyard Slash")

    object GraveyardStumble : Ability("Graveyard Stumble")

}

object Spider : EnemyClass(
    name = "Spider",
    baseHp = 8,
    minDamage = 3,
    maxDamage = 6,
    abilities = arrayOf(Spit, Bite)
) {

    object Spit : Ability("Spit")

    object Bite : Ability("Bite")

}