package org.some.project.kotlin.abilities

import org.some.project.kotlin.FancyName
import org.some.project.kotlin.abilities.Position.Companion.ALL_FOUR
import org.some.project.kotlin.chars.DungeonCharacter
import java.lang.IllegalArgumentException

class TargetCriteria(val forPosition: PositionCriterion, val forCharacher: Set<CharacterCriterion> = setOf()) {

    fun isApplicable(dealer: DungeonCharacter, mainTarget: DungeonCharacter): Boolean {
        val targetOnPosition = when (forPosition) {
            is AllOnPositions -> mainTarget.pos in forPosition.positions
            is AnyOnPositions -> mainTarget.pos in forPosition.positions
            is OnPosition -> mainTarget.pos == forPosition.position
        }
        if (!targetOnPosition)
            return false
        var acc = true
        // fold this
        for (criteria in forCharacher) {
            val currentCheck = when (criteria) {
                Ally -> dealer.team == mainTarget.team
                Enemy -> dealer.team != mainTarget.team
                is HasHpLessThan -> mainTarget.currentHp < criteria.threshold
                Self -> dealer == mainTarget
            }
            acc = acc.and(currentCheck)
        }
        return acc
    }

    companion object {

        fun anyEnemy(position: Position): TargetCriteria = TargetCriteria(OnPosition(position), setOf(Enemy))

        fun anyEnemy(positions: List<Position>): TargetCriteria {
            return when (positions.size) {
                0 -> throw IllegalArgumentException("Empty list of positions!")
                1 -> TargetCriteria(OnPosition(positions[0]), setOf(Enemy))
                else -> TargetCriteria(AnyOnPositions(positions), setOf(Enemy))
            }
        }

        fun allEnemies(positions: List<Position>): TargetCriteria {
            return when (positions.size) {
                0 -> throw IllegalArgumentException("Empty list of positions!")
                1 -> TargetCriteria(OnPosition(positions[0]), setOf(Enemy))
                else -> TargetCriteria(AllOnPositions(positions), setOf(Enemy))
            }
        }

        fun anyAlly(): TargetCriteria = TargetCriteria(AnyOnPositions(ALL_FOUR), setOf(Ally))

        fun allAllies(): TargetCriteria = TargetCriteria(AllOnPositions(ALL_FOUR), setOf(Ally))

        fun self(): TargetCriteria = TargetCriteria(AllOnPositions(ALL_FOUR), setOf(Self))
    }
}

sealed interface Criterion

sealed interface CharacterCriterion: Criterion

object Self: CharacterCriterion

object Ally: CharacterCriterion

object Enemy: CharacterCriterion

data class HasHpLessThan(val threshold: Int): CharacterCriterion

sealed interface PositionCriterion: Criterion, FancyName

data class OnPosition(val position: Position): PositionCriterion {
    override val fancyName: String = "position $position"
}

data class AllOnPositions(val positions: List<Position>): PositionCriterion {
    override val fancyName: String = "all on positions ${positions.joinToString()}"
}

data class AnyOnPositions(val positions: List<Position>): PositionCriterion {
    override val fancyName: String = "any on positions ${positions.joinToString()}"
}
