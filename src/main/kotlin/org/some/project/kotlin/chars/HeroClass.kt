package org.some.project.kotlin.chars

import org.some.project.kotlin.abilities.Ability
import org.some.project.kotlin.abilities.AbilityEffect
import org.some.project.kotlin.abilities.AllOf
import org.some.project.kotlin.abilities.AnyOf
import org.some.project.kotlin.abilities.Damage
import org.some.project.kotlin.abilities.Healing
import org.some.project.kotlin.abilities.Position
import org.some.project.kotlin.abilities.Position.Companion.ZERO
import org.some.project.kotlin.abilities.Stun
import org.some.project.kotlin.scenes.Skirmish

open class HeroClass(
    final override val name: String,
    final override val baseHp: Int,
    final override val minDamage: Int,
    final override val maxDamage: Int,
    final override val turns: Int = 1,
    final override val speed: Int,
    final override val abilities: List<Ability>
): DungeonClass {

    init {
        require(name.isNotBlank()) { "Class name must not be empty" }
        require(baseHp > 0) { "Amount of health points must be positive, got $baseHp instead" }
        require(minDamage < maxDamage) { "Min damage $minDamage must be less than max damage value $maxDamage" }
        require(turns > 0) { "That's a playable character, it must have an ability to act at least once" }
        require(abilities.isNotEmpty()) { "Please give some abilities to the class" }
    }

    override val abilitiesLookUp: Map<String, Ability> by lazy {
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

    object Smite: Ability(
        name = "Smite",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(effect = Damage(8), appliedTo = AnyOf.FRONT_TWO, appliedFrom = AnyOf.FRONT_TWO)
    ) {

        override fun isApplicable(skirmish: Skirmish): Boolean {
            return isPresentOnPositions(skirmish.enemyParty, mainEffect.appliedTo.positions)
        }

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val target = skirmish.enemyParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            target.takeDamage((mainEffect.effect as Damage).dmg)
        }
    }

    object CrushingBlow: Ability(
        name = "Crushing Blow",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(effect = Damage(4), appliedTo = AnyOf.FRONT_TWO, appliedFrom = AnyOf.FRONT_TWO)
    ) {

        val stun = AbilityEffect(effect = Stun, appliedTo = mainEffect.appliedTo, appliedFrom = mainEffect.appliedFrom)

        override fun isApplicable(skirmish: Skirmish): Boolean {
            return isPresentOnPositions(skirmish.enemyParty, mainEffect.appliedTo)
        }

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val target = skirmish.enemyParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            target.takeDamage((mainEffect.effect as Damage).dmg)
        }
    }

    object ShowThemTheBills: Ability(
        name = "Show Them the Bills",
        numberOfArgs = 0,
        mainEffect = AbilityEffect(effect = Damage(3), appliedTo = AllOf.FRONT_TWO, appliedFrom = AnyOf.FRONT_TWO)
    ) {

        override fun isApplicable(skirmish: Skirmish): Boolean {
            return isPresentOnPositions(skirmish.enemyParty, mainEffect.appliedTo)
        }

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val targetPositions = mainEffect.appliedTo.positions
            require(targetPositions.any { skirmish.heroParty[it] != null }) {
                "Fix the target validation please:" +
                        " ${dealer.fancyName} tries to hit empty positions ${mainEffect.appliedTo.positions}" +
                        " with ${this.fancyName}"
            }
            targetPositions.forEach {
                val target = skirmish.heroParty[it]
                val damage = (mainEffect.effect as Damage).dmg
                target?.also {
                    println("${dealer.fancyName} hit ${it.fancyName} for $damage")
                }?. takeDamage(damage)
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

    object DuelistAdvance: Ability(
        name = "Duelist Advance",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(effect = Damage(3), appliedTo = AnyOf.FRONT_THREE, appliedFrom = AnyOf.BACKLINE_THREE)) {
        // TODO: move forward
        // TODO: riposte

        override fun isApplicable(skirmish: Skirmish): Boolean {
            return isPresentOnPositions(skirmish.enemyParty, mainEffect.appliedTo)
        }

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val target = skirmish.enemyParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            target.takeDamage((mainEffect.effect as Damage).dmg)
            // TODO: move forward
        }
    }

    object PointBlackShot: Ability(
        name = "Point-Blank Shot",
        numberOfArgs = 0,
        mainEffect = AbilityEffect(effect = Damage(10), appliedTo = AnyOf(ZERO), appliedFrom = AnyOf(ZERO))
    ) {

        override fun isApplicable(skirmish: Skirmish): Boolean {
            return isPresentOnPositions(skirmish.enemyParty, mainEffect.appliedTo)
        }

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            require(targetPosition == ZERO) {
                "$fancyName must be applied to the first position, got '$targetPosition' instead"
            }
            require(dealer.pos == ZERO) {
                "$fancyName must be applied from the first position, got ${dealer.pos} instead"
            }
            val target = skirmish.enemyParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            target.takeDamage((mainEffect.effect as Damage).dmg).apply {

            }
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

    object DivineComfort: Ability(
        name = "Divine Comfort",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(effect = Healing(10), appliedTo = AnyOf.ALL_FOUR, appliedFrom = AnyOf.BACKLINE_TWO)
    ) {

        override fun isApplicable(skirmish: Skirmish): Boolean = true

        override fun canBeUsedUpon(caster: DungeonCharacter, mainTarget: DungeonCharacter): Boolean {
            return mainTarget.currentHp < mainTarget.charClass.baseHp
        }

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val target = skirmish.heroParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            if (!target.isAlive) {
                println("Sorry, $fancyName cannot heal a dead person.")
            }
            val hpToRestore = (mainEffect.effect as Healing).heal
            target.healUp(hpToRestore).also {
                println("$dealer heal up to $hpToRestore to ${target.fancyName}'s health")
            }
        }
    }

    object DivineGrace: Ability(
        name = "Divine Grace",
        numberOfArgs = 0,
        mainEffect = AbilityEffect(effect = Healing(4), appliedTo = AllOf.ALL_FOUR, appliedFrom = AnyOf.BACKLINE_THREE)
    ) {

        override fun isApplicable(skirmish: Skirmish): Boolean = true
        override fun canBeUsedUpon(caster: DungeonCharacter, mainTarget: DungeonCharacter): Boolean = true

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val hpToRestore = (mainEffect.effect as Healing).heal
            skirmish.heroParty.getCharacters().filterNotNull().filter { it.isAlive }.forEach { hero ->
                hero.healUp(hpToRestore).also {
                    println("${dealer.fancyName} healed ${hero.fancyName} to ${hero.currentHp}")
                }
            }
        }
    }

    object MaceBash: Ability(
        name = "Mace Bash",
        numberOfArgs = 1,
        mainEffect = AbilityEffect(effect = Damage(4), appliedTo = AnyOf.FRONT_TWO, appliedFrom = AnyOf.FRONT_TWO)
    ) {

        override fun isApplicable(skirmish: Skirmish): Boolean {
            return isPresentOnPositions(skirmish.enemyParty, mainEffect.appliedTo)
        }

        override fun apply(skirmish: Skirmish, dealer: DungeonCharacter, targetPosition: Position) {
            val target = skirmish.enemyParty[targetPosition]
            requireNotNull(target) { errorLambda(dealer, targetPosition, this) }
            target.takeDamage((mainEffect.effect as Damage).dmg)
        }
    }
}
