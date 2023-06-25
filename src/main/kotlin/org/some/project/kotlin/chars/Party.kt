package org.some.project.kotlin.chars

class Party<T : DungeonCharacter> private constructor(private val characters: List<T>) {

    init {
        require(characters.size <= 4) { "Too many characters in one party: ${characters.size}" }
    }

    constructor(char1: T) : this(listOf(char1))

    constructor(char1: T, char2: T) : this(listOf(char1, char2))

    constructor(char1: T, char2: T, char3: T) : this(listOf(char1, char2, char3))

    constructor(char1: T, char2: T, char3: T, char4: T) : this(listOf(char1, char2, char3, char4))


    // is it possible to implement destructuring here ?

    fun descriptionRightFirst(): String {
        return characters.reversed().mapIndexed { i, it -> "${it.charClass.name} (${characters.size - i})"}.joinToString()
    }

    fun descriptionLeftFirst(): String {
        return characters.mapIndexed { i, it -> "${it.charClass.name} (${i + 1})"}. joinToString()
    }
}
