package io.github.NoOne.nMLAbilities.expertiseSystem.assassin;

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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class AssassinAbilityEffects {
    private static NMLAbilities nmlAbilities;
    private static ProfileManager profileManager;

    public AssassinAbilityEffects(NMLAbilities nmlAbilities) {
        this.nmlAbilities = nmlAbilities;
        profileManager = nmlAbilities.getProfileManager();
    }

    public static void slashAndDash(Player player) {
        Stats stats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();
        HashMap<DamageType, Double> damage = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStats2Damage(stats), 1.5);

        EnergyManager.useEnergy(player, 20);
        CooldownManager.putOnHardCooldown(player, 1.2);
        AttackCooldownSystem.setOrPauseAttackCooldown(player, 1.2);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);

        /// dash
        Vector knockback = player.getLocation().getDirection().multiply(4);
        knockback.setY(-2);
        player.setVelocity(knockback);
        player.setInvulnerable(true);

        /// slash
        new BukkitRunnable() {
            int dashTicks = 6;

            @Override
            public void run() {
                Location particleLocation = player.getLocation().add(0, 1, 0);
                Vector direction = particleLocation.getDirection().multiply(1.2); // distance in blocks of particle from player

                particleLocation.add(direction);
                player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleLocation, 0, 0, 0, 0, 0);
                dashTicks--;

                for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), 2, 1, 2)) {
                    if (entity instanceof LivingEntity livingEntity && !entity.equals(player)) {
                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damage));
                    }
                }

                if (dashTicks == 0) {
                    this.cancel();
                }

            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);

        Bukkit.getScheduler().runTaskLater(nmlAbilities, () -> {
            player.setInvulnerable(false);
        }, 6L);
    }
}