package org.some.project.kotlin.abilities

import org.some.project.kotlin.FancyName
import org.some.project.kotlin.GLOBAL_PARTY_SIZE
import org.some.project.kotlin.MyResult
import org.some.project.kotlin.abilities.Position.Companion.ALL_FOUR
import org.some.project.kotlin.abilities.Position.Companion.ONE
import org.some.project.kotlin.abilities.Position.Companion.THREE
import org.some.project.kotlin.abilities.Position.Companion.TWO
import org.some.project.kotlin.abilities.Position.Companion.ZERO
import org.some.project.kotlin.chars.DungeonCharacter
import org.some.project.kotlin.control.CommandName
import org.some.project.kotlin.scenes.Skirmish

abstract class Ability<out E: BasicEffect>(
    val name: String,
    override val needsTarget: Boolean,
    val mainEffect: AbilityEffect<E>
): FancyName, CommandName {

    abstract fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position)

    open fun canBeUsedFrom(pos: Position): Boolean {
        return pos in mainEffect.appliedFrom.positions
    }

    open fun canBeUsedUpon(caster: DungeonCharacter, mainTarget: DungeonCharacter): Boolean {
        return mainEffect.appliedTo.isApplicable(caster, mainTarget)
    }

    override val fancyName = "\u001b[34;1m${name}\u001b[0m"

    override val commandName: String
        get() = name.lowercase().replace(" ", "-")

    companion object {
        fun <E: BasicEffect> errorLambda(dealer: DungeonCharacter, pos: Position, ability: Ability<E>) : String =
            "Fix the target validation please:" +
                    " ${dealer.fancyName} tries to hit the empty position $pos with ${ability.fancyName}"
    }
}

object PassTurn: Ability<PassEffect>(
    name = "Pass",
    needsTarget = false,
    mainEffect = AbilityEffect(PassEffect, appliedTo = TargetCriteria(AnyOnPositions(ALL_FOUR), setOf(Self)) , appliedFrom = AnyOf.ALL_FOUR)
) {

    override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
        println("${dealer.fancyName} passes a turn.")
    }
}

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

    override fun toString(): String {
        return (pos + 1).toString()
    }
    companion object {
        const val MAX_POSITION = GLOBAL_PARTY_SIZE

        val  ZERO = Position(0)
        val   ONE = Position(1)
        val   TWO = Position(2)
        val THREE = Position(3)

        val FRONTLINE_TWO = listOf(ZERO, ONE)
        val FRONTLINE_THREE = listOf(ZERO, ONE, TWO)
        val ALL_FOUR = listOf(ZERO, ONE, TWO, THREE)

        /**
         * Positions from the user's perspective start from 1
         */
        fun convert(arg: String): MyResult<Position> {
            return when (val t = arg.toIntOrNull()) {
                null -> MyResult.failure("Could not parse position")
                in 1..MAX_POSITION -> MyResult.success(Position(t - 1))
                else -> MyResult.failure("Position exceeds boundaries")
            }
        }

        /**
         * Positions internally are 0-based numbers.
         */
        internal fun fromInt(number: Int): Position {
            return when {
                number < 0 -> throw IllegalStateException("Position must not be negative, got '$number' instead")
                number in 0 until MAX_POSITION -> Position(number)
                else -> throw IllegalStateException("Number $number cannot represent a valid position from 0 to $MAX_POSITION")
            }
        }
    }
}
