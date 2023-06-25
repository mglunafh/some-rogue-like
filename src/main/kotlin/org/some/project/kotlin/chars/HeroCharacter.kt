package org.some.project.kotlin.chars

import org.some.project.kotlin.Experience

class HeroCharacter(
    charClass: HeroClass,
    val name: String,
    exp: Experience,
    hp: Int,
    isAlive: Boolean
): DungeonCharacter(charClass, hp, isAlive) {

    constructor(charClass: HeroClass, name: String, isAlive: Boolean = true) :
            this(
                charClass = charClass,
                name = name,
                exp = Experience.create(),
                hp = charClass.baseHp,
                isAlive = isAlive
            )

    var experience: Experience = exp
        private set

    override val description: String
        get() = "${charClass.name} $name, ${experience.description}, ${currentHp}hp, ${if (isAlive) "alive" else "dead"}"

    fun gainExp(xp: Int) {
        if (!isAlive) {
            return
        }
        experience = experience.gainExp(xp)
    }
}