package org.some.project.kotlin.chars

class EnemyCharacter(
    enemyClass: EnemyClass,
    hp: Int,
    override val turns: Int,
    override var speed: Int,
    isAlive: Boolean
) : DungeonCharacter(enemyClass, hp, isAlive), HasTurns {

    constructor(enemyClass: EnemyClass, isAlive: Boolean = true) :
            this(
                enemyClass = enemyClass,
                hp = enemyClass.baseHp,
                turns = enemyClass.turns,
                speed = enemyClass.speed,
                isAlive = isAlive
            )
}
