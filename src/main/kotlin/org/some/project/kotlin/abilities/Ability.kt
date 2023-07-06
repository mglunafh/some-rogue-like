package org.some.project.kotlin.abilities

import org.some.project.kotlin.abilities.Position.Companion.ONE
import org.some.project.kotlin.abilities.Position.Companion.THREE
import org.some.project.kotlin.abilities.Position.Companion.TWO
import org.some.project.kotlin.abilities.Position.Companion.ZERO

open class Ability(val name: String) {

    open fun isApplicable(): Boolean = false
}

class AbilityEffect(effect: BasicEffect, appliedTo: AppliedTo, appliedFrom: AppliedTo)

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
        val FIRST_TWO = AnyOf(ZERO, ONE)
        val LAST_TWO = AnyOf(TWO, THREE)
        val ALL_FOUR = AnyOf(ZERO, ONE, TWO, THREE)
    }
}

class AllOf(positions: Set<Position>): AppliedTo(positions) {

    constructor(pos1: Position): this(setOf(pos1))
    constructor(pos1: Position, pos2: Position): this(setOf(pos1, pos2))
    constructor(pos1: Position, pos2: Position, pos3: Position): this(setOf(pos1, pos2, pos3))
    constructor(pos1: Position, pos2: Position, pos3: Position, pos4: Position): this(setOf(pos1, pos2, pos3, pos4))

    companion object {
        val FIRST_TWO = AllOf(ZERO, ONE)
        val LAST_TWO = AllOf(TWO, THREE)
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
    }
}
