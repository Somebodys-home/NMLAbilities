package io.github.NoOne.nMLAbilities.expertiseSystem.soldier;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageHelper;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldownSystem.CooldownManager;
import io.github.NoOne.nMLEnergySystem.EnergyManager;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import io.github.NoOne.nMLWeapons.AttackCooldownSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

public class SoldierAbilityEffects {
    private static ProfileManager profileManager;
    public SoldierAbilityEffects(NMLAbilities nmlAbilities) {
        profileManager = nmlAbilities.getProfileManager();
    }

    public static void slash(Player player) {
        Stats stats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();
        HashMap<DamageType, Double> damageStats = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStats2Damage(stats), 1.2);
        Location location = player.getLocation();
        HashSet<LivingEntity> hitEntities = new HashSet<>();

        CooldownManager.putOnHardCooldown(player, 1);
        AttackCooldownSystem.setOrPauseAttackCooldown(player, 1);
        EnergyManager.useEnergy(player, 15);
        player.playSound(location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);

        for (double i = -Math.PI / 2; i <= Math.PI / 2; i += Math.PI / 10) {
            double x = Math.sin(i) * 2;
            double z = Math.cos(i) * 2;
            Vector offset = new Vector(x, 1, z).rotateAroundY(-Math.toRadians(location.getYaw()));
            Location particleLocation = location.clone().add(offset);

            player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleLocation, 1);

            for (Entity entity : player.getWorld().getNearbyEntities(particleLocation, 1.5, 1.5, 1.5)) {
                if (!entity.equals(player) && entity instanceof LivingEntity livingEntity) {
                    hitEntities.add(livingEntity);
                }
            }
        }

        for (LivingEntity livingEntity : hitEntities) {
            Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damageStats));
        }
    }
}