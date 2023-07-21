package org.some.project.kotlin.abilities

data class AbilityCast<out E: BasicEffect>(val ability: Ability<E>, val pos: Position)
