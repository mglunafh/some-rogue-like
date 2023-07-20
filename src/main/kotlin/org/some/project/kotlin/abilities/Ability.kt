package org.some.project.kotlin.abilities

import org.some.project.kotlin.FancyName
import org.some.project.kotlin.GLOBAL_PARTY_SIZE
import org.some.project.kotlin.MyResult
import org.some.project.kotlin.abilities.Position.Companion.ONE
import org.some.project.kotlin.abilities.Position.Companion.THREE
import org.some.project.kotlin.abilities.Position.Companion.TWO
import org.some.project.kotlin.abilities.Position.Companion.ZERO
import org.some.project.kotlin.chars.DungeonCharacter
import org.some.project.kotlin.chars.Party
import org.some.project.kotlin.control.CommandName
import org.some.project.kotlin.scenes.Skirmish
import java.lang.IllegalStateException

abstract class Ability(
    val name: String,
    override val numberOfArgs: Int,
    val mainEffect: AbilityEffect
): FancyName, CommandName {

    // probably should be deprecated
    abstract fun isApplicable(skirmish: Skirmish): Boolean

    abstract fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position)

    open fun canBeUsedFrom(pos: Position): Boolean {
        return pos in mainEffect.appliedFrom.positions
    }

    /**
     * Basic implementation:
     *   1) caster cannot cast an ability on themselves
     *   2) the main target should be at any of the positions from the main effect properties
     */
    open fun canBeUsedUpon(caster: DungeonCharacter, mainTarget: DungeonCharacter): Boolean {
        return caster != mainTarget && mainTarget.pos in mainEffect.appliedTo.positions
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
        val errorLambda = { dealer: DungeonCharacter, pos: Position, ability: Ability ->
            "Fix the target validation please:" +
                    " ${dealer.fancyName} tries to hit the empty position $pos with ${ability.fancyName}"
        }
    }
}

object PassTurn: Ability(
    name = "Pass",
    numberOfArgs = 0,
    mainEffect = AbilityEffect(Pass, appliedTo = AnyOf.ALL_FOUR, appliedFrom = AnyOf.ALL_FOUR)
) {

    override fun isApplicable(skirmish: Skirmish): Boolean = true

    override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
        println("${dealer.fancyName} passes a turn.")
    }

    override fun canBeUsedUpon(caster: DungeonCharacter, mainTarget: DungeonCharacter): Boolean {
        return caster == mainTarget
    }
}

class AbilityEffect(val effect: BasicEffect, val appliedTo: AppliedTo, val appliedFrom: AppliedTo)

sealed class BasicEffect

data class Damage(val dmg: Int): BasicEffect()

data class Healing(val heal: Int): BasicEffect()

object Stun: BasicEffect()

object Pass: BasicEffect()

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
        val MAX_POSITION = GLOBAL_PARTY_SIZE

        val  ZERO = Position(0)
        val   ONE = Position(1)
        val   TWO = Position(2)
        val THREE = Position(3)

        val ALL_FOUR = listOf(ZERO, ONE, TWO, THREE)

        fun convert(arg: String): MyResult<Position> {
            return when (val t= arg.toIntOrNull()) {
                null -> MyResult.failure("Could not parse position")
                in 0 until GLOBAL_PARTY_SIZE -> MyResult.success(Position(t))
                else -> MyResult.failure("Position exceeds boundaries")
            }
        }

        fun fromInt(number: Int): Position {
            return when (number) {
                0 -> ZERO
                1 -> ONE
                2 -> TWO
                3 -> THREE
                else -> throw IllegalStateException("Number $number cannot represent a valid position from 0 to $MAX_POSITION")
            }
        }
    }
}
