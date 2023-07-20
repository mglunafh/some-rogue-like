package org.some.project.kotlin.chars

import org.some.project.kotlin.Experience
import org.some.project.kotlin.abilities.Position

class HeroCharacter(
    charClass: HeroClass,
    val name: String,
    exp: Experience,
    hp: Int,
    override val turns: Int,
    override val speed: Int,
    isAlive: Boolean,
    pos: Position
): DungeonCharacter(charClass, hp, isAlive, pos), HasTurns {

    constructor(charClass: HeroClass, name: String, pos: Position, isAlive: Boolean = true) :
            this(
                charClass = charClass,
                name = name,
                exp = Experience.create(),
                hp = charClass.baseHp,
                turns = charClass.turns,
                speed = charClass.speed,
                isAlive = isAlive,
                pos = pos
            )

    var experience: Experience = exp
        private set

    override val fancyName: String
        get() = "\u001b[4m$name\u001b[0m"

    override val description: String
        get() = "${charClass.fancyName} $fancyName, ${experience.description}, ${currentHp}hp, ${if (isAlive) "alive" else "dead"}"

    fun gainExp(xp: Int) {
        if (!isAlive) {
            return
        }
        experience = experience.gainExp(xp)
    }
}
