package org.some.project.kotlin.chars

import org.some.project.kotlin.abilities.Position

class Party<T : DungeonCharacter> private constructor(characterList: List<T>) {

    companion object {
        const val PARTY_SIZE = 4
    }

    private val characters: MutableList<T?>

    init {
        require(characterList.size <= PARTY_SIZE) { "Too many characters in one party: ${characterList.size}" }
        characters = MutableList(PARTY_SIZE) {
            characterList.getOrNull(it)
        }
    }

    constructor(char1: T) : this(listOf(char1))

    constructor(char1: T, char2: T) : this(listOf(char1, char2))

    constructor(char1: T, char2: T, char3: T) : this(listOf(char1, char2, char3))

    constructor(char1: T, char2: T, char3: T, char4: T) : this(listOf(char1, char2, char3, char4))

    operator fun get(pos: Position): T? {
        return characters[pos.pos]
    }

    fun getCharacters(): List<T?> {
        return characters.toList()
    }
    fun isAlive(): Boolean {
        return characters.any { it != null && it.isAlive }
    }

    // is it possible to implement destructuring here ?

    fun descriptionRightFirst(): String {
        return characters.reversed().mapIndexed { i, it -> it?.let {"${it.charClass.name} (${characters.size - i})"} ?: "" }.joinToString()
    }

    fun descriptionLeftFirst(): String {
        return characters.mapIndexed { i, it -> it?.let { "${it.charClass.name} (${i + 1})" } ?: "" }. joinToString()
    }
}
