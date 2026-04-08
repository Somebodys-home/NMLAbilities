package io.github.NoOne.nMLAbilities.expertiseSystem.ongoingAbilityEffects;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLPlayerStats.statSystem.StatChangeEvent;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class OngoingAbilityEffectsTracker {
    private static HashMap<UUID, ArrayList<OngoingAbilityEffect>> ongoingAbilityEffects = new HashMap<>();

    public static void addAbilityEffect(NMLAbilities nmlAbilities, Player player, OngoingAbilityEffect ongoingAbilityEffect) {
        ArrayList<OngoingAbilityEffect> arrayList = ongoingAbilityEffects.getOrDefault(player.getUniqueId(), new ArrayList<>());

        for (Map.Entry<String, Double> entry : ongoingAbilityEffect.statChanges().entrySet()) {
            Bukkit.getServer().getPluginManager().callEvent(new StatChangeEvent(player, entry.getKey(), entry.getValue()));
        }

        ongoingAbilityEffect.abilityEffect().runTaskTimer(nmlAbilities, ongoingAbilityEffect.delay(), ongoingAbilityEffect.period());
        arrayList.add(ongoingAbilityEffect);
        ongoingAbilityEffects.put(player.getUniqueId(), arrayList);
    }

    public static void removeAbilityEffect(Player player, String abilityEffectName) {
        if (!ongoingAbilityEffects.containsKey(player.getUniqueId())) return;

        ArrayList<OngoingAbilityEffect> ongoingAbilityEffectArrayList = ongoingAbilityEffects.get(player.getUniqueId());
        Iterator<OngoingAbilityEffect> iterator = ongoingAbilityEffectArrayList.iterator();

        while (iterator.hasNext()) {
            OngoingAbilityEffect ongoingAbilityEffect = iterator.next();

            if (ongoingAbilityEffect.abilityEffectName().equals(abilityEffectName)) {
                ongoingAbilityEffect.abilityEffect().cancel();

                for (Map.Entry<String, Double> entry : ongoingAbilityEffect.statChanges().entrySet()) {
                    Bukkit.getServer().getPluginManager().callEvent(new StatChangeEvent(player, entry.getKey(), -entry.getValue()));
                }

                iterator.remove();
            }
        }
    }

    public static void removeAllAbilityEffects(Player player, Stats stats) {
        if (!ongoingAbilityEffects.containsKey(player.getUniqueId())) return;

        ArrayList<OngoingAbilityEffect> ongoingAbilityEffectArrayList = ongoingAbilityEffects.get(player.getUniqueId());
        Iterator<OngoingAbilityEffect> iterator = ongoingAbilityEffectArrayList.iterator();

        while (iterator.hasNext()) {
            OngoingAbilityEffect ongoingAbilityEffect = iterator.next();
            ongoingAbilityEffect.abilityEffect().cancel();

            for (Map.Entry<String, Double> entry : ongoingAbilityEffect.statChanges().entrySet()) {
                stats.removeFromStat(entry.getKey(), entry.getValue());
            }

            iterator.remove();
        }
    }
}
