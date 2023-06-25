package org.some.project.kotlin.chars

open class CharacterClass(
    val name: String,
    val baseHp: Int,
    val minDamage: Int,
    val maxDamage: Int) {

    init {
        require(name.isNotBlank()) { "Class name must not be empty" }
        require(baseHp > 0) { "Amount of health points must be positive, got $baseHp instead" }
        require(minDamage < maxDamage) { "Min damage $minDamage must be less than max damage value $maxDamage" }
    }
}

object Crusader: CharacterClass(
    name = "Crusader",
    baseHp = 40,
    minDamage = 5,
    maxDamage = 10
)

object Highwayman: CharacterClass(
    name = "Highwayman",
    baseHp = 28,
    minDamage = 8,
    maxDamage = 15
)

object Vestal: CharacterClass(
    name = "Vestal",
    baseHp = 25,
    minDamage = 3,
    maxDamage = 8
)
