package org.some.project.kotlin.chars

import org.some.project.kotlin.abilities.Position
import org.some.project.kotlin.chars.Party.Team.FIENDS

class EnemyCharacter(
    enemyClass: EnemyClass,
    hp: Int,
    override val turns: Int,
    override var speed: Int,
    isAlive: Boolean,
    pos: Position
) : DungeonCharacter(enemyClass, hp, isAlive, pos, FIENDS), HasTurns {

    constructor(enemyClass: EnemyClass, pos: Position, isAlive: Boolean = true) :
            this(
                enemyClass = enemyClass,
                hp = enemyClass.baseHp,
                turns = enemyClass.turns,
                speed = enemyClass.speed,
                isAlive = isAlive,
                pos = pos
            )
}
