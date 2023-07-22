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
import org.some.project.kotlin.chars.Party
import org.some.project.kotlin.control.CommandName
import org.some.project.kotlin.scenes.Skirmish
import java.lang.IllegalStateException

abstract class Ability<out E: BasicEffect>(
    val name: String,
    override val numberOfArgs: Int,
    val mainEffect: AbilityEffect<E>
): FancyName, CommandName {

    abstract fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position)

    open fun canBeUsedFrom(pos: Position): Boolean {
        return pos in mainEffect.appliedFrom.positions
    }

    open fun canBeUsedUpon(caster: DungeonCharacter, mainTarget: DungeonCharacter): Boolean {
        return PassTurn.mainEffect.appliedTo.isApplicable(caster, mainTarget)
    }

    override val fancyName = "\u001b[34;1m${name}\u001b[0m"

    override val commandName: String
        get() = name.lowercase().replace(" ", "-")

    fun <T: DungeonCharacter> isPresentOnPositions(party: Party<T>, positions: List<Position>): Boolean {
        return party.isPresentOnAny(positions)
    }

    fun <T: DungeonCharacter> isPresentOnPositions(party: Party<T>, appliedTo: AppliedTo): Boolean {
        return when (appliedTo) {
            is AllOf -> party.isPresentOnAny(appliedTo.positions)
            is AnyOf -> party.isPresentOnAny(appliedTo.positions)
        }
    }

    companion object {
        fun <E: BasicEffect> errorLambda(dealer: DungeonCharacter, pos: Position, ability: Ability<E>) : String =
            "Fix the target validation please:" +
                    " ${dealer.fancyName} tries to hit the empty position $pos with ${ability.fancyName}"

    }
}

object PassTurn: Ability<PassEffect>(
    name = "Pass",
    numberOfArgs = 0,
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

        fun convert(arg: String): MyResult<Position> {
            return when (val t = arg.toIntOrNull()) {
                null -> MyResult.failure("Could not parse position")
                in 1..MAX_POSITION -> MyResult.success(Position(t - 1))
                else -> MyResult.failure("Position exceeds boundaries")
            }
        }

        fun fromInt(number: Int): Position {
            return when {
                number == 0 -> ZERO
                number == 1 -> ONE
                number == 2 -> TWO
                number == 3 -> THREE
                number < MAX_POSITION -> throw IllegalStateException("Somehow position for '$number' hasn't been defined")
                else -> throw IllegalStateException("Number $number cannot represent a valid position from 0 to $MAX_POSITION")
            }
        }
    }
}
