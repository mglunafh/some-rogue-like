package org.some.project.kotlin.abilities

class AbilityEffect<out E: BasicEffect>(val effect: E, val appliedTo: TargetCriteria, val appliedFrom: AppliedTo)

sealed class BasicEffect

data class Damage(val dmg: Int): BasicEffect()

data class Healing(val heal: Int): BasicEffect()

object Stun: BasicEffect()

data class Back(val amount: Int = 1): BasicEffect()

data class Forward(val amount: Int = 1): BasicEffect()

object Buff: BasicEffect()

object Debuff: BasicEffect()

object PassEffect: BasicEffect()
