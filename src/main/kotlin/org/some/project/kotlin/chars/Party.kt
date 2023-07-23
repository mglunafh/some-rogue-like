package org.some.project.kotlin.chars

import org.some.project.kotlin.GLOBAL_PARTY_SIZE
import org.some.project.kotlin.abilities.Position
import java.lang.Integer.max
import java.lang.Integer.min

class Party<out T: DungeonCharacter> private constructor(characterList: List<T>) {

    companion object {
        const val PARTY_SIZE = GLOBAL_PARTY_SIZE
    }

    private val characters: MutableList<T>

    private val deadCharacters: MutableList<T> = mutableListOf()

    init {
        require(characterList.size <= PARTY_SIZE) { "Too many characters in one party: ${characterList.size}" }
        characters = MutableList(characterList.size) { pos ->
            val chr = characterList[pos]
            chr.pos = Position.fromInt(pos)
            chr
        }
    }

    constructor(char1: T): this(listOf(char1))

    constructor(char1: T, char2: T): this(listOf(char1, char2))

    constructor(char1: T, char2: T, char3: T): this(listOf(char1, char2, char3))

    constructor(char1: T, char2: T, char3: T, char4: T): this(listOf(char1, char2, char3, char4))

    operator fun get(pos: Position): T? {
        return characters.getOrNull(pos.pos)
    }

    val isAlive: Boolean
        get() = characters.any { it.isAlive }

    val gotWiped: Boolean
        get() = characters.all { it.isDead }

    fun getCharacters(): List<T> {
        return characters.toList()
    }

    private fun move(position: Position, amount: Int, back: Boolean) {
        require(amount > 0) {
            "Value of how far a character is to be pushed should be positive, got '$amount' instead"
        }
        val char = characters.getOrNull(position.pos)
        requireNotNull (char) {
            "This party does not have $position"
        }
        val newPos = if (back) {
            min(position.pos + amount, characters.size)
        } else {
            max(position.pos - amount, 0)
        }
        characters.removeAt(position.pos)
        characters.add(newPos, char)
        updatePositions()
    }

    fun moveBack(pos: Position, amount: Int) {
        move(position = pos, amount = amount, back = true)
    }

    fun moveForward(pos: Position, amount: Int) {
        move(position = pos, amount = amount, back = false)
    }

    fun descriptionBackLineFirst(): String {
        return characters.reversed()
            .mapIndexed { i, it -> "${it.fancyName}[${it.currentHp}/${it.charClass.baseHp}] (${characters.size - i})" }
            .joinToString()
    }

    fun descriptionFrontLineFirst(): String {
        return characters
            .mapIndexed { i, it -> "(${i + 1}) ${it.fancyName}[${it.currentHp}/${it.charClass.baseHp}]" }.joinToString()
    }

    fun removeDeadCharacters() {
        characters.forEach {
            if (it.isDead) {
                deadCharacters.add(it)
            }
        }

        characters.removeIf { it.isDead }
        updatePositions()

        for (i in 0 until characters.size) {
            val chr = characters[i]
            require(Position.fromInt(i) == chr.pos)
        }
    }

    private fun updatePositions() {
        characters.forEachIndexed { index, chr ->
            chr.pos = Position.fromInt(index)
        }
    }

    enum class Team {
        HEROES, FIENDS
    }
}
