package io.github.NoOne.nMLAbilities.expertiseSystem.ongoingAbilityEffects;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public record OngoingAbilityEffect(String abilityEffectName, HashMap<String, Double> statChanges, BukkitRunnable abilityEffect, int delay, int period) {
}
