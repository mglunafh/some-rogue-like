package org.some.project.kotlin

import org.some.project.kotlin.chars.Crusader
import org.some.project.kotlin.chars.DungeonCharacter
import org.some.project.kotlin.chars.Highwayman
import org.some.project.kotlin.chars.Vestal

fun main() {
    println("Hello World!")

    val crusader = DungeonCharacter(Crusader, "Reynauld")
    val hwm = DungeonCharacter(Highwayman, "Dismas")
    val vestal = DungeonCharacter(Vestal, "Junia")
    listOf(crusader, hwm, vestal).forEach {
        print("${it.description}. ")
        println(it.charClass.listOfAbilities)
        it.takeDamage(20)
        println(it.description)
        it.gainExp(5)
        println(it.description)
        it.takeDamage(10)
        println(it.description)
        it.gainExp(3)
        println(it.description)
    }
}
