package org.some.project.kotlin.abilities

class AbilityEffect<out E: BasicEffect>(val effect: E, val appliedTo: TargetCriteria, val appliedFrom: AppliedTo)

sealed class BasicEffect

data class Damage(val dmg: Int): BasicEffect()

data class Healing(val heal: Int): BasicEffect()

object Stun: BasicEffect()

object Buff: BasicEffect()

object Debuff: BasicEffect()

object PassEffect: BasicEffect()
