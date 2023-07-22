package org.some.project.kotlin.chars

import org.some.project.kotlin.FancyName
import org.some.project.kotlin.abilities.Ability
import org.some.project.kotlin.abilities.BasicEffect
import org.some.project.kotlin.abilities.PassTurn
import org.some.project.kotlin.abilities.Position
import org.some.project.kotlin.control.ControlType
import org.some.project.kotlin.scenes.Skirmish
import java.lang.Integer.min

open class DungeonCharacter(
    val charClass: DungeonClass,
    hp: Int,
    isAlive: Boolean,
    pos: Position,
    team: Party.Team
): FancyName {

    var currentHp: Int = hp
        private set

    var isAlive: Boolean = isAlive
        private set

    val isDead: Boolean
        get() = !isAlive

    var pos: Position = pos
        internal set

    var team = team
        internal set

    val abilities: List<Ability<BasicEffect>> by charClass

    open val description: String
        get() = "${charClass.fancyName}, ${currentHp}hp, ${if (isAlive) "alive" else "dead"}"

    open fun performAbility(skirmish: Skirmish, control: ControlType) {
        val (ability, pos) = control.getAbility(skirmish, this)
        if (ability == PassTurn) {
            println("$fancyName passes.")
            return
        }
        println("${ability.fancyName} at the position $pos")
        ability.apply(skirmish, this, pos)
    }

    fun takeDamage(damagePoints: Int) {
        require(damagePoints >= 0) {
            "Amount of damage must be non-negative, got $damagePoints instead"
        }
        if (damagePoints >= currentHp) {
            isAlive = false
            currentHp = 0
        } else {
            currentHp -= damagePoints
        }
    }

    fun healUp(healthPoints: Int) {
        require(healthPoints >= 0) {
            "Amount of healing must be non-negative, got $healthPoints instead"
        }
        if (isAlive) {
            val temp = min(charClass.baseHp, currentHp + healthPoints)
            currentHp = temp
        }
        else {
            error("Someone tried to heal a dead entity: ${this.fancyName}")
        }
    }

    override val fancyName: String
        get() = this.charClass.fancyName

}
