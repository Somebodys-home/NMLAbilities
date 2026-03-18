package io.github.NoOne.nMLAbilities.expertiseSystem.cavalier;

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
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

public class CavalierAbilityEffects {
    private static NMLAbilities nmlAbilities;
    private static ProfileManager profileManager;

    public CavalierAbilityEffects(NMLAbilities nmlAbilities) {
        this.nmlAbilities = nmlAbilities;
        profileManager = nmlAbilities.getProfileManager();
    }

    public static void seismicSlam(Player user, int hotbarSlot) {
        user.setMetadata("falling", new FixedMetadataValue(nmlAbilities, true));

        Stats stats = profileManager.getPlayerProfile(user.getUniqueId()).getStats();
        HashMap<DamageType, Double> damage = DamageConverter.multiplyDamageMap(DamageConverter.convertPlayerStats2Damage(stats), 2.5);

        EnergyManager.useEnergy(user, 30);
        CooldownManager.putAllOtherAbilitiesOnCooldown(user, 4, hotbarSlot);
        AttackCooldownSystem.setOrPauseAttackCooldown(user, 3);
        user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 2f);

        /// jump
        Vector jump = user.getLocation().getDirection().multiply(.5);
        jump.setY(1.5);
        user.setVelocity(jump);

        /// trail particles
        BukkitTask flyingParticles = new BukkitRunnable() {
            @Override
            public void run() {
                user.getWorld().spawnParticle(Particle.SNOWFLAKE, user.getLocation(), 75, .15, 1, .15, 0);
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);

        /// slam
        Bukkit.getScheduler().runTaskLater(nmlAbilities, () -> {
            Vector slam = user.getLocation().getDirection().multiply(1.5);
            slam.setY(-2.2);
            user.setVelocity(slam);
            user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, .3f);
            user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, .3f);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (user.isOnGround()) {
                        user.removeMetadata("falling", nmlAbilities);

                        flyingParticles.cancel();
                        user.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, user.getLocation().add(0, .5, 0), 3, .25, 0, .25, 0);
                        user.playSound(user.getLocation(), Sound.ITEM_MACE_SMASH_GROUND_HEAVY, 3f, 1f);
                        user.playSound(user.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, .8f, 1f);

                        for (Entity entity : user.getWorld().getNearbyEntities(user.getLocation(), 4, 2, 4)) {
                            if (!entity.equals(user) && entity instanceof LivingEntity livingEntity) {
                                Vector knockback = livingEntity.getLocation().toVector().subtract(user.getLocation().toVector()).normalize().multiply(1.2);
                                knockback.setY(.75);
                                livingEntity.setVelocity(knockback);

                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, user, damage));
                            }
                        }

                        cancel();
                    }
                }
            }.runTaskTimer(nmlAbilities, 0L, 1L);
        }, 20L);
    }
}