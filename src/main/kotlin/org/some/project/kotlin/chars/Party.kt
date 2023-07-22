package org.some.project.kotlin.chars

import org.some.project.kotlin.GLOBAL_PARTY_SIZE
import org.some.project.kotlin.abilities.Position

class Party<out T : DungeonCharacter> private constructor(characterList: List<T>) {

    companion object {
        const val PARTY_SIZE = GLOBAL_PARTY_SIZE
    }

    private val characters: MutableList<T?>

    private val deadCharacters: MutableList<T> = mutableListOf()

    init {
        require(characterList.size <= PARTY_SIZE) { "Too many characters in one party: ${characterList.size}" }
        characters = MutableList(PARTY_SIZE) { pos ->
            val chr = characterList.getOrNull(pos)
            chr?.let {
                it.pos = Position.fromInt(pos)
            }
            chr
        }
    }

    constructor(char1: T) : this(listOf(char1))

    constructor(char1: T, char2: T) : this(listOf(char1, char2))

    constructor(char1: T, char2: T, char3: T) : this(listOf(char1, char2, char3))

    constructor(char1: T, char2: T, char3: T, char4: T) : this(listOf(char1, char2, char3, char4))

    operator fun get(pos: Position): T? {
        return characters[pos.pos]
    }

    fun isPresentOn(pos: Position): Boolean {
        return characters[pos.pos] != null
    }

    fun isPresentOnAny(positions: List<Position>): Boolean {
        return positions.any { characters[it.pos] != null }
    }

    fun isPresentOnAll(positions: List<Position>): Boolean {
        return positions.all { characters[it.pos] != null }
    }

    fun getCharacters(): List<T?> {
        return characters.toList()
    }
    val isAlive: Boolean
        get() = characters.any { it != null && it.isAlive }

    val gotWiped: Boolean
        get() = characters.all { it == null || it.isDead }

    fun descriptionBackLineFirst(): String {
        return characters.reversed()
            .mapIndexed { i, it -> it?.let { "${it.fancyName}[${it.currentHp}/${it.charClass.baseHp}] (${PARTY_SIZE - i})" } ?: "_" }
            .joinToString()
    }

    fun descriptionFrontLineFirst(): String {
        return characters
            .filterNotNull()
            .mapIndexed { i, it ->  "(${i + 1}) ${it.fancyName}[${it.currentHp}/${it.charClass.baseHp}]" }. joinToString()
    }

    fun removeDeadCharacters() {
        var indAlive = 0
        for (i in 0 until  characters.size) {
            val t = characters[i]
            if (t != null) {
                if (t.isDead) {
                    deadCharacters.add(t)
                    characters[i] = null
                }
            }
        }

        for (i in 0 until characters.size) {
            val t = characters[i]
            if (t != null) {
                if (indAlive < i) {
                    characters[indAlive] = t
                    characters[i] = null
                }
                indAlive++
            }
        }
    }

    enum class Team {
        HEROES, FIENDS
    }
}
