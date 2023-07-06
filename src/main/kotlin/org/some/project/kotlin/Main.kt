package org.some.project.kotlin

import org.some.project.kotlin.chars.Crusader
import org.some.project.kotlin.chars.EnemyCharacter
import org.some.project.kotlin.chars.HeroCharacter
import org.some.project.kotlin.chars.Highwayman
import org.some.project.kotlin.chars.Vestal
import org.some.project.kotlin.chars.BoneSoldier
import org.some.project.kotlin.chars.Brigand
import org.some.project.kotlin.chars.Party
import org.some.project.kotlin.chars.Spider
import org.some.project.kotlin.scenes.Skirmish

fun main() {
    println("Hello World!")

    val crusader = HeroCharacter(Crusader, "Reynauld")
    val hwm = HeroCharacter(Highwayman, "Dismas")
    val vestal = HeroCharacter(Vestal, "Junia")
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

    val spider = EnemyCharacter(Spider)
    val brigand = EnemyCharacter(Brigand)
    val boneSoldier = EnemyCharacter(BoneSoldier)

    listOf(spider, brigand, boneSoldier).forEach {
        print("${it.description}. ")
        println(it.charClass.listOfAbilities)
        it.takeDamage(10)
        println(it.description)
        it.takeDamage(10)
        println(it.description)
    }

    val heroParty = Party(crusader, hwm, vestal)
    val enemyParty = Party(brigand, boneSoldier, spider)

    val skirmish = Skirmish(heroParty, enemyParty)
    skirmish.run()
}
