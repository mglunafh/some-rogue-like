package org.some.project.kotlin.chars

import org.some.project.kotlin.GLOBAL_PARTY_SIZE
import org.some.project.kotlin.abilities.Position

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

    fun getCharacters(): List<T> {
        return characters.toList()
    }

    val isAlive: Boolean
        get() = characters.any { it.isAlive }

    val gotWiped: Boolean
        get() = characters.all { it.isDead }

    fun descriptionBackLineFirst(): String {
        return characters.reversed()
            .mapIndexed { i, it -> "${it.fancyName}[${it.currentHp}/${it.charClass.baseHp}] (${PARTY_SIZE - i})" }
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
        characters.forEachIndexed { index, chr ->
            chr.pos = Position.fromInt(index)
        }

        for (i in 0 until characters.size) {
            val chr = characters[i]
            require(Position.fromInt(i) == chr.pos)
        }
    }

    enum class Team {
        HEROES, FIENDS
    }
}
