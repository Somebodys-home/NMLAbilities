package io.github.NoOne.nMLAbilities.expertiseSystem.cavalier;

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

    public static void seismicSlam(Player player) {
        player.setMetadata("ability_falling", new FixedMetadataValue(nmlAbilities, true));

        Stats stats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();
        HashMap<DamageType, Double> damage = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStats2Damage(stats), 2.5);

        EnergyManager.useEnergy(player, 30);
        CooldownManager.putOnHardCooldown(player, 1.5);
        AttackCooldownSystem.setOrPauseAttackCooldown(player, 1.5);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 2f);

        // jump
        Vector jump = player.getLocation().getDirection().multiply(.5);
        jump.setY(1.5);
        player.setVelocity(jump);

        // trail particles
        BukkitTask flyingParticles = new BukkitRunnable() {
            @Override
            public void run() {
                player.getWorld().spawnParticle(Particle.SNOWFLAKE, player.getLocation(), 75, .15, 1, .15, 0);

                if (player.isOnGround()) cancel();
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);

        // slam
        Bukkit.getScheduler().runTaskLater(nmlAbilities, () -> {
            Vector slam = player.getLocation().getDirection().multiply(1.5);
            slam.setY(-2.2);
            player.setVelocity(slam);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, .3f);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, .3f);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.isOnGround()) {
                        player.removeMetadata("ability_falling", nmlAbilities);

                        flyingParticles.cancel();
                        player.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, player.getLocation().add(0, .5, 0), 3, .25, 0, .25, 0);
                        player.playSound(player.getLocation(), Sound.ITEM_MACE_SMASH_GROUND_HEAVY, 3f, 1f);
                        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, .8f, 1f);

                        for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), 4, 2, 4)) {
                            if (!entity.equals(player) && entity instanceof LivingEntity livingEntity) {
                                Vector knockback = livingEntity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(1.2);
                                knockback.setY(.75);
                                livingEntity.setVelocity(knockback);

                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damage));
                            }
                        }

                        cancel();
                    }
                }
            }.runTaskTimer(nmlAbilities, 0L, 1L);
        }, 20L);
    }
}