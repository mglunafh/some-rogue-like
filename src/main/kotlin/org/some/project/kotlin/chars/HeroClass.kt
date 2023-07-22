package org.some.project.kotlin.chars

import org.some.project.kotlin.abilities.Ability
import org.some.project.kotlin.abilities.AbilityEffect
import org.some.project.kotlin.abilities.AllOnPositions
import org.some.project.kotlin.abilities.AnyOf
import org.some.project.kotlin.abilities.BasicEffect
import org.some.project.kotlin.abilities.Damage
import org.some.project.kotlin.abilities.Healing
import org.some.project.kotlin.abilities.Position
import org.some.project.kotlin.abilities.Position.Companion.FRONTLINE_THREE
import org.some.project.kotlin.abilities.Position.Companion.FRONTLINE_TWO
import org.some.project.kotlin.abilities.Position.Companion.ZERO
import org.some.project.kotlin.abilities.Stun
import org.some.project.kotlin.abilities.TargetCriteria
import org.some.project.kotlin.abilities.TargetCriteria.Companion.allAllies
import org.some.project.kotlin.abilities.TargetCriteria.Companion.anyAlly
import org.some.project.kotlin.abilities.TargetCriteria.Companion.anyEnemy
import org.some.project.kotlin.scenes.Skirmish

open class HeroClass(
    final override val name: String,
    final override val baseHp: Int,
    final override val minDamage: Int,
    final override val maxDamage: Int,
    final override val turns: Int = 1,
    final override val speed: Int,
    final override val abilities: List<Ability<BasicEffect>>
): DungeonClass {

    init {
        require(name.isNotBlank()) { "Class name must not be empty" }
        require(baseHp > 0) { "Amount of health points must be positive, got $baseHp instead" }
        require(minDamage < maxDamage) { "Min damage $minDamage must be less than max damage value $maxDamage" }
        require(turns > 0) { "That's a playable character, it must have an ability to act at least once" }
        require(abilities.isNotEmpty()) { "Please give some abilities to the class" }
    }

    override val abilitiesLookUp: Map<String, Ability<BasicEffect>> by lazy {
        abilities.associateBy { it.commandName }
    }
}

object Crusader: HeroClass(
    name = "Crusader",
    baseHp = 40,
    minDamage = 5,
    maxDamage = 10,
    speed = 3,
    abilities = listOf(Smite, CrushingBlow, ShowThemTheBills)
) {

    object Smite: Ability<Damage>(
        name = "Smite",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(
            effect = Damage(8),
            appliedTo = anyEnemy(FRONTLINE_TWO),
            appliedFrom = AnyOf.FRONT_TWO)
    ) {

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val target = skirmish.enemyParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            val damage = mainEffect.effect.dmg
            target.takeDamage(damage)
            println("${dealer.fancyName} hit ${target.fancyName} for $damage")
        }
    }

    object CrushingBlow: Ability<Damage>(
        name = "Crushing Blow",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(
            effect = Damage(4),
            appliedTo = anyEnemy(FRONTLINE_TWO),
            appliedFrom = AnyOf.FRONT_TWO)
    ) {

        val stun = AbilityEffect(
            effect = Stun,
            appliedFrom = mainEffect.appliedFrom,
            appliedTo = mainEffect.appliedTo)


        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val target = skirmish.enemyParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            val damage = Smite.mainEffect.effect.dmg
            target.takeDamage(damage)
            println("${dealer.fancyName} hit ${target.fancyName} for $damage")
        }
    }

    object ShowThemTheBills: Ability<Damage>(
        name = "Show Them the Bills",
        numberOfArgs = 0,
        mainEffect = AbilityEffect(
            effect = Damage(3),
            appliedTo = TargetCriteria.allEnemies(FRONTLINE_TWO),
            appliedFrom = AnyOf.FRONT_TWO)
    ) {

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val criteria = mainEffect.appliedTo.forPosition
            val targetPositions = (criteria as AllOnPositions).positions
            require(targetPositions.any { skirmish.enemyParty[it] != null }) {
                "Fix the target validation please:" +
                        " ${dealer.fancyName} tries to hit empty positions $targetPositions" +
                        " with ${this.fancyName}"
            }
            targetPositions.forEach {
                val target = skirmish.enemyParty[it]
                val damage = mainEffect.effect.dmg
                target?.also {
                    println("${dealer.fancyName} hit ${it.fancyName} for $damage")
                }?.takeDamage(damage)
            }
        }
    }
}

object Highwayman: HeroClass(
    name = "Highwayman",
    baseHp = 28,
    minDamage = 8,
    maxDamage = 15,
    speed = 5,
    abilities = listOf(DuelistAdvance, PointBlackShot)
) {

    object DuelistAdvance: Ability<Damage>(
        name = "Duelist Advance",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(
            effect = Damage(3),
            appliedTo = anyEnemy(FRONTLINE_THREE),
            appliedFrom = AnyOf.BACKLINE_THREE
        )
    ) {
        // TODO: move forward
        // TODO: riposte


        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val target = skirmish.enemyParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            val damage = mainEffect.effect.dmg
            target.takeDamage(damage)
            println("${dealer.fancyName} hit ${target.fancyName} for $damage")
            // TODO: move forward
        }
    }

    object PointBlackShot: Ability<Damage>(
        name = "Point-Blank Shot",
        numberOfArgs = 0,
        mainEffect = AbilityEffect(effect = Damage(10), appliedFrom = AnyOf(ZERO), appliedTo = anyEnemy(ZERO))
    ) {

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            require(targetPosition == ZERO) {
                "$fancyName must be applied to the first position, got '$targetPosition' instead"
            }
            require(dealer.pos == ZERO) {
                "$fancyName must be applied from the first position, got ${dealer.pos} instead"
            }
            val target = skirmish.enemyParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            val damage = mainEffect.effect.dmg
            target.takeDamage(damage)
            println("${dealer.fancyName} hit ${target.fancyName} for $damage")
            // TODO move back
        }
    }
}

object Vestal: HeroClass(
    name = "Vestal",
    baseHp = 25,
    minDamage = 3,
    maxDamage = 8,
    speed = 4,
    abilities = listOf(MaceBash, DivineGrace, DivineComfort)
) {

    object DivineComfort: Ability<Healing>(
        name = "Divine Comfort",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(effect = Healing(10), appliedFrom = AnyOf.BACKLINE_TWO, appliedTo = anyAlly())
    ) {


        override fun canBeUsedUpon(caster: DungeonCharacter, mainTarget: DungeonCharacter): Boolean {
            return mainTarget.currentHp < mainTarget.charClass.baseHp
        }

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val target = skirmish.heroParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            if (!target.isAlive) {
                println("Sorry, ${dealer.fancyName} cannot heal a dead person.")
                return
            }
            val hpToRestore = mainEffect.effect.heal
            target.healUp(hpToRestore)
            println("${dealer.fancyName} heal up to $hpToRestore to ${target.fancyName}'s health")
        }
    }

    object DivineGrace: Ability<Healing>(
        name = "Divine Grace",
        numberOfArgs = 0,
        mainEffect = AbilityEffect(effect = Healing(4), appliedFrom = AnyOf.BACKLINE_THREE, appliedTo = allAllies())
    ) {

        override fun canBeUsedUpon(caster: DungeonCharacter, mainTarget: DungeonCharacter): Boolean = true

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val hpToRestore = mainEffect.effect.heal
            skirmish.heroParty.getCharacters()
                .filterNotNull().filter { it.isAlive }.forEach { hero ->
                hero.healUp(hpToRestore)
                println("${dealer.fancyName} healed ${hero.fancyName} to ${hero.currentHp}")
            }
        }
    }

    object MaceBash: Ability<Damage>(
        name = "Mace Bash",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(effect = Damage(4), appliedFrom = AnyOf.FRONT_TWO, appliedTo = anyEnemy(FRONTLINE_TWO))
    ) {

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val target = skirmish.enemyParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            target.takeDamage(mainEffect.effect.dmg)
        }
    }
}
