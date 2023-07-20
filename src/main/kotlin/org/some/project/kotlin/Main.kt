package org.some.project.kotlin

import org.some.project.kotlin.abilities.Position.Companion.ZERO
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

    val crusader = HeroCharacter(Crusader, "Reynauld", ZERO)
    val hwm = HeroCharacter(Highwayman, "Dismas", ZERO)
    val vestal = HeroCharacter(Vestal, "Junia", ZERO)
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

    val spider = EnemyCharacter(Spider, ZERO)
    val brigand = EnemyCharacter(Brigand, ZERO)
    val boneSoldier = EnemyCharacter(BoneSoldier, ZERO)

    listOf(spider, brigand, boneSoldier).forEach {
        print("${it.description}. ")
        println(it.charClass.listOfAbilities)
        it.takeDamage(10)
        println(it.description)
        it.takeDamage(10)
        println(it.description)
    }

    val heroParty = Party(hwm, crusader, vestal)
    val enemyParty = Party(brigand, boneSoldier, spider)

    val skirmish = Skirmish(heroParty, enemyParty)
    skirmish.run()
}
