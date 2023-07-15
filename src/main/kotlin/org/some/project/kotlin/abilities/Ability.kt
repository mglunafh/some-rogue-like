package org.some.project.kotlin.abilities

import org.some.project.kotlin.abilities.Position.Companion.ONE
import org.some.project.kotlin.abilities.Position.Companion.THREE
import org.some.project.kotlin.abilities.Position.Companion.TWO
import org.some.project.kotlin.abilities.Position.Companion.ZERO
import org.some.project.kotlin.chars.DungeonCharacter
import org.some.project.kotlin.chars.Party
import org.some.project.kotlin.scenes.Skirmish

abstract class Ability(val name: String) {

    abstract fun isApplicable(skirmish: Skirmish): Boolean

    fun <T: DungeonCharacter> isPresentOnPositions(party: Party<T>, positions: List<Position>): Boolean {
        return party.isPresentOnAny(positions)
    }

    fun <T: DungeonCharacter> isPresentOnPositions(party: Party<T>, appliedTo: AppliedTo): Boolean {
        return when (appliedTo) {
            is AllOf -> party.isPresentOnAny(appliedTo.positions)
            is AnyOf -> party.isPresentOnAny(appliedTo.positions)
        }
    }
}

object PassTurn : Ability("Pass") {

    override fun isApplicable(skirmish: Skirmish): Boolean = true

}

class AbilityEffect(val effect: BasicEffect, val appliedTo: AppliedTo, val appliedFrom: AppliedTo)

sealed class BasicEffect

data class Damage(val dmg: Int): BasicEffect()

data class Healing(val heal: Int): BasicEffect()

sealed class AppliedTo(positions: Set<Position>) {

    val positions = positions.toList().sortedBy { it.pos }

}

class AnyOf(positions: Set<Position>): AppliedTo(positions) {
    constructor(pos1: Position): this(setOf(pos1))

    constructor(pos1: Position, pos2: Position): this(setOf(pos1, pos2))
    constructor(pos1: Position, pos2: Position, pos3: Position): this(setOf(pos1, pos2, pos3))
    constructor(pos1: Position, pos2: Position, pos3: Position, pos4: Position): this(setOf(pos1, pos2, pos3, pos4))

    companion object {
        val FRONT_TWO = AnyOf(ZERO, ONE)
        val FRONT_THREE = AnyOf(ZERO, ONE, TWO)
        val BACKLINE_TWO = AnyOf(TWO, THREE)
        val BACKLINE_THREE = AnyOf(ONE, TWO, THREE)
        val ALL_FOUR = AnyOf(ZERO, ONE, TWO, THREE)
    }
}

class AllOf(positions: Set<Position>): AppliedTo(positions) {

    constructor(pos1: Position): this(setOf(pos1))
    constructor(pos1: Position, pos2: Position): this(setOf(pos1, pos2))
    constructor(pos1: Position, pos2: Position, pos3: Position): this(setOf(pos1, pos2, pos3))
    constructor(pos1: Position, pos2: Position, pos3: Position, pos4: Position): this(setOf(pos1, pos2, pos3, pos4))

    companion object {
        val FRONT_TWO = AllOf(ZERO, ONE)
        val BACKLINE_TWO = AllOf(TWO, THREE)
        val ALL_FOUR = AllOf(ZERO, ONE, TWO, THREE)
    }
}

@JvmInline
value class Position private constructor(val pos: Int) {

    companion object {
        val ZERO = Position(0)
        val ONE = Position(1)
        val TWO = Position(2)
        val THREE = Position(3)

        val ALL_FOUR = listOf(ZERO, ONE, TWO, THREE)
    }
}
