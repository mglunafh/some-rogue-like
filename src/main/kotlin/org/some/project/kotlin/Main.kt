package org.some.project.kotlin

import kotlin.random.Random

fun main() {
    println("Hello World!")

    val random = Random.Default
    repeat(20) {
        val t = random.nextInt(1,50)
        val exp = Experience.create()
        val gainedExp = exp.gainExp(t)
        println("Earned $t, got $gainedExp")
    }
}
