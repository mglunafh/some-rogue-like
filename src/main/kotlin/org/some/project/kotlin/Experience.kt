package org.some.project.kotlin

data class Experience(val level: Int, val exp: Int = 0) {

    init {
        require(level in 0..MAX_LEVEL) { "Level $level must be in range from 0 to $MAX_LEVEL" }
        require(level == MAX_LEVEL || level in EXP_TO_LEVEL_UP) { "Somehow level $level is not mentioned in the XP map" }
        require(level == MAX_LEVEL || exp < EXP_TO_LEVEL_UP.getValue(level)) {
            "Amount of xp $exp must be less than max amount of exp for level $level"
        }
    }

    companion object {
        private const val MAX_LEVEL = 6
        private val EXP_TO_LEVEL_UP = List(MAX_LEVEL) { ind -> ind to 2 * (ind + 1) }.toMap()
        fun create() = Experience(0, 0)
    }

    val description = if (level == MAX_LEVEL) "Lvl.$MAX_LEVEL" else "Lvl.$level(${exp}/${EXP_TO_LEVEL_UP.getValue(level)})"

    fun gainExp(receivedExp: Int): Experience {
        require(receivedExp > 0) { "$receivedExp must be positive" }
        if (level == MAX_LEVEL) {
            return this
        }
        var curLevel = level
        var curExp = exp + receivedExp

        var expToLevelUp = EXP_TO_LEVEL_UP.getValue(curLevel)
        while (curExp >= expToLevelUp) {
            curLevel++
            curExp -= expToLevelUp
            if (curLevel == MAX_LEVEL) {
                return Experience(MAX_LEVEL)
            }
            expToLevelUp = EXP_TO_LEVEL_UP.getValue(curLevel)
        }
        require(curLevel <= MAX_LEVEL) { "Somehow current level $curLevel got bigger than max level of $MAX_LEVEL" }
        require(curLevel in EXP_TO_LEVEL_UP && curExp < EXP_TO_LEVEL_UP.getValue(curLevel)) {
            "Somehow current level $curLevel is missing from the map or" +
                    " current exp $curExp is bigger than the requirement for the level $curLevel"
        }
        return Experience(curLevel, curExp)
    }
}
