package io.github.NoOne.nMLAbilities;

import io.github.NoOne.damagePlugin.customDamage.DamageHelper;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldownSystem.CooldownManager;
import io.github.NoOne.nMLEnergySystem.EnergyManager;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import io.github.NoOne.nMLWeapons.AttackCooldownSystem;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// parent class for the classes that make expertise ability effects
// generally just for qol methods and variables
public class ExpertiseEffectsHelper {
    protected static NMLAbilities nmlAbilities = NMLAbilities.getInstance();
    protected static ProfileManager profileManager = nmlAbilities.getProfileManager();

    public static void useEnergyAndCooldown(Player player, int energyUse, double cooldown) {
        EnergyManager.useEnergy(player, energyUse);
        CooldownManager.putOnHardCooldown(player, cooldown);
        AttackCooldownSystem.setOrPauseAttackCooldown(player, cooldown);
    }

    public static void makeUnmovable(Player player) {
        player.setMetadata("ability_no_move", new FixedMetadataValue(nmlAbilities, true));
    }

    public static void makeUnmovable(Player player, int ticks) {
        makeUnmovable(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                makeUnmovable(player);
            }
        }.runTaskLater(nmlAbilities, ticks);
    }

    public static void breakKneecaps(Player player) {
        player.setMetadata("ability_no_jump", new FixedMetadataValue(nmlAbilities, true));
    }

    public static void makeMovable(Player player) {
        player.removeMetadata("ability_no_move", nmlAbilities);
    }

    public static void fixKneecaps(Player player) {
        player.removeMetadata("ability_no_jump", nmlAbilities);
    }

    public static HashMap<DamageType, Double> getDamageForAbility(Player player, double weaponDamagePercent, HashMap<DamageType, Double> elementalDamages) {
        Stats stats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();
        HashMap<DamageType, Double> playerDamages = DamageHelper.convertPlayerStats2Damage(stats);
        HashMap<DamageType, Double> totalDamage = DamageHelper.multiplyDamageMap(playerDamages, weaponDamagePercent);

        for (Map.Entry<DamageType, Double> entry : elementalDamages.entrySet()) { // elementalDamages being every ability's damage type and multiplier for that type
            DamageType damageType = entry.getKey();
            double playerDamage = playerDamages.get(damageType);

            if (playerDamages.containsKey(damageType)) {
                totalDamage.put(damageType, playerDamage * elementalDamages.get(damageType));
            }
        }

        return totalDamage;
    }

    public static HashMap<DamageType, Double> getDamageForAbility(Player player, double weaponDamagePercent) {
        Stats stats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();

        return DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStats2Damage(stats), weaponDamagePercent);
    }

    public static HashMap<DamageType, Double> getDamageForAbility(Player player, HashMap<DamageType, Double> elementalDamages) {
        Stats stats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();
        HashMap<DamageType, Double> playerDamages = DamageHelper.convertPlayerStats2Damage(stats);
        HashMap<DamageType, Double> totalDamage = new HashMap<>();

        for (Map.Entry<DamageType, Double> entry : elementalDamages.entrySet()) { // elementalDamages being every ability's damage type and multiplier for that type
            DamageType damageType = entry.getKey();
            double playerDamage = playerDamages.get(damageType);

            if (playerDamages.containsKey(damageType)) {
                totalDamage.put(damageType, playerDamage * elementalDamages.get(damageType));
            }
        }

        return totalDamage;
    }

    public static HashMap<DamageType, Double> getDamageForAbility(Player player, DamageType damageType, double multiplier) {
        Stats stats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();
        HashMap<DamageType, Double> playerDamages = DamageHelper.convertPlayerStats2Damage(stats);

        return new HashMap<>(){{
            if (playerDamages.containsKey(damageType)) {
                put(damageType, playerDamages.get(damageType) * multiplier);
            }
        }};
    }

    public static ArrayList<LivingEntity> getNearbyEntitiesExcludingPlayer(Player player, Location center, double x, double y, double z) {
        return new ArrayList<>(){{
            for (Entity entity : center.getWorld().getNearbyEntities(center, x, y, z)) {
                if (entity instanceof LivingEntity livingEntity && !entity.equals(player)) {
                    add(livingEntity);
                }
            }
        }};
    }

    public static ArrayList<LivingEntity> getNearbyEntitiesExcludingPlayer(Player player, Location center, double radius) {
        return new ArrayList<>(){{
            for (Entity entity : center.getWorld().getNearbyEntities(center, radius, radius, radius)) {
                if (entity instanceof LivingEntity livingEntity && !entity.equals(player)) {
                    add(livingEntity);
                }
            }
        }};
    }
}
