package io.github.NoOne.nMLAbilities.expertiseSystem.cavalier;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseEffectsHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class CavalierAbilityEffects extends ExpertiseEffectsHelper {

    public static void seismicSlam(Player player) {
        HashMap<DamageType, Double> damage = getDamageForAbility(player, 2.5);

        useEnergyAndCooldown(player, 30, 1.5);
        makeKneecapsUnbreakable(player);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 2f);

        // jump
        Vector jump = player.getLocation().getDirection().multiply(.5).setY(1.5);

        player.setVelocity(jump);

        // trail particles
        BukkitTask flyingParticles = new BukkitRunnable() {
            @Override
            public void run() {
                player.getWorld().spawnParticle(Particle.SNOWFLAKE, player.getLocation(), 75, .15, 1, .15, 0);
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);

        // slam
        Bukkit.getScheduler().runTaskLater(nmlAbilities, () -> {
            Vector slam = player.getLocation().getDirection().multiply(1.5).setY(-2.2);

            player.setVelocity(slam);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 2f, .3f);

            // landing effect
            new BukkitRunnable() {
                @Override
                public void run() {
                    Location playerLocation = player.getLocation();

                    if (player.isOnGround()) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                               makeKneecapsBreakable(player);
                            }
                        }.runTaskLater(nmlAbilities, 5);

                        flyingParticles.cancel();
                        player.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, playerLocation.add(0, .5, 0), 3, .25, 0, .25, 0);
                        player.playSound(playerLocation, Sound.ITEM_MACE_SMASH_GROUND_HEAVY, 3f, 1f);
                        player.playSound(playerLocation, Sound.ENTITY_GENERIC_EXPLODE, .8f, 1f);

                        for (LivingEntity livingEntity : getNearbyEntitiesExcludingPlayer(player, playerLocation, 4, 2 ,4)) {
                            Vector knockback = livingEntity.getLocation().toVector().subtract(playerLocation.toVector()).normalize().multiply(1.2).setY(.75);

                            livingEntity.setVelocity(knockback);
                            Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damage));
                        }

                        cancel();
                    }
                }
            }.runTaskTimer(nmlAbilities, 0L, 1L);
        }, 20L);
    }
}