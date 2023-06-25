package org.some.project.kotlin.chars

import org.some.project.kotlin.Experience

class DungeonCharacter(
    val charClass: CharacterClass,
    val name: String,
    exp: Experience,
    hp: Int,
    isAlive: Boolean
) {

    constructor(charClass: CharacterClass, name: String, isAlive: Boolean = true) :
            this(
                charClass = charClass,
                name = name,
                exp = Experience.create(),
                hp = charClass.baseHp,
                isAlive = isAlive
            )

    var experience: Experience = exp
        private set

    var currentHp = hp
        private set

    var isAlive: Boolean = isAlive
        private set

    val description: String
        get() = "${charClass.name} $name, ${experience.description}, ${currentHp}hp, ${if (isAlive) "alive" else "dead"}"

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

    fun gainExp(xp: Int) {
        if (!isAlive) {
            return
        }
        experience = experience.gainExp(xp)
    }
}
