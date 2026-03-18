package io.github.NoOne.nMLAbilities.expertiseSystem.assassin;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageConverter;
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

    public static void slashAndDash(Player user, int hotbarSlot) {
        Stats stats = profileManager.getPlayerProfile(user.getUniqueId()).getStats();
        HashMap<DamageType, Double> damage = DamageConverter.multiplyDamageMap(DamageConverter.convertPlayerStats2Damage(stats), 1.5);

        EnergyManager.useEnergy(user, 20);
        CooldownManager.putAllOtherAbilitiesOnCooldown(user, 1.2, hotbarSlot);
        user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
        AttackCooldownSystem.setOrPauseAttackCooldown(user, 1.2);

        /// dash
        Vector knockback = user.getLocation().getDirection().multiply(4);
        knockback.setY(-2);
        user.setVelocity(knockback);
        user.setInvulnerable(true);

        /// slash
        new BukkitRunnable() {
            int dashTicks = 6;

            @Override
            public void run() {
                Location particleLocation = user.getLocation().add(0, 1, 0);
                Vector direction = particleLocation.getDirection().multiply(1.2); // distance in blocks of particle from player

                particleLocation.add(direction);
                user.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleLocation, 0, 0, 0, 0, 0);
                dashTicks--;

                for (Entity entity : user.getWorld().getNearbyEntities(user.getLocation(), 2, 1, 2)) {
                    if (entity instanceof LivingEntity livingEntity && !entity.equals(user)) {
                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, user, damage));
                    }
                }

                if (dashTicks == 0) {
                    this.cancel();
                }

            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);

        Bukkit.getScheduler().runTaskLater(nmlAbilities, () -> {
            user.setInvulnerable(false);
        }, 6L);
    }
}