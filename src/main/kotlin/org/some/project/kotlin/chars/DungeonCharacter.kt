package org.some.project.kotlin.chars

import org.some.project.kotlin.abilities.Ability

open class DungeonCharacter(
    val charClass: DungeonClass,
    hp: Int,
    isAlive: Boolean
) {

    var currentHp: Int = hp
        private set

    var isAlive: Boolean = isAlive
        private set

    val abilities: List<Ability> by charClass

    open val description: String
        get() = "${charClass.name}, ${currentHp}hp, ${if (isAlive) "alive" else "dead"}"

    fun takeDamage(damagePoints: Int) {
        require(damagePoints >= 0) {
            "Amount of damage must be non-negative, got $damagePoints instead"
        }
        if (damagePoints >= currentHp) {
            isAlive = false
            currentHp = 0
        } else {
            currentHp -= damagePoints
        }
    }
}
