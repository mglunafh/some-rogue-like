package org.some.project.kotlin.chars

class EnemyCharacter(
    enemyClass: EnemyClass,
    hp: Int,
    isAlive: Boolean
) : DungeonCharacter(enemyClass, hp, isAlive) {

    constructor(enemyClass: EnemyClass, isAlive: Boolean = true) :
            this(
                enemyClass = enemyClass,
                hp = enemyClass.baseHp,
                isAlive = isAlive
            )
}
