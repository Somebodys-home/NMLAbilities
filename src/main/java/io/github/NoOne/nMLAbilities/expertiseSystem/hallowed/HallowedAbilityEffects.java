package io.github.NoOne.nMLAbilities.expertiseSystem.hallowed;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.abilitySystem.abilityEffects.AbilityEffectsHelper;
import io.github.NoOne.nMLEnergySystem.EnergyManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class HallowedAbilityEffects extends AbilityEffectsHelper {
    public static void halo(Player player) {
        HashMap<DamageType, Double> damage = getDamageForAbility(player, .15, DamageType.RADIANT, .35);

        EnergyManager.useEnergy(player, 25);
        putOnInfiniteCooldown(player);
        player.playSound(player, Sound.ITEM_TRIDENT_RIPTIDE_1, 1f, 1f);
        player.playSound(player, Sound.ITEM_ELYTRA_FLYING, .5f, 1f);

        new BukkitRunnable() {
            int ticks = 0;
            Location haloCenter = player.getLocation().clone().add(0, 1, 0);
            Vector baseHaloVelocity = player.getLocation().getDirection().multiply(.4);
            double haloRadius = 4;
            double minHaloRadius = .5;
            double shrinkTimeReduction = -1; // the multiplier on how soon the halo should start shrinking such that its the proper size when it hits the player's head
            double shrinkSpeedMultiplier = -1; // the multiplier on how quickly the halo should start shrinking such that its the proper size when it hits the player's head
            
            @Override
            public void run() {
                ticks++;

                // how long the halo has been out
                // halo starts returning when progress = 1
                double progress = (double) ticks / 40;
                double speedFactor; // the speed the halo should be moving
                Location endLocation = player.getLocation().clone().add(0, 2, 0); // halo ends on the player's had

                // halo
                horizontalParticleCircle(Particle.END_ROD, haloCenter, haloRadius, 100);
                horizontalParticleCircle(Particle.ELECTRIC_SPARK, haloCenter, haloRadius - .1, 120);

                // damage
                for (LivingEntity livingEntity : getNearbyEntitiesExcludingPlayer(player, haloCenter, haloRadius)) {
                    Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damage, 5));
                }

                if (progress <= 1) { // while halo is going forwards, slow down
                    speedFactor = (1 - (progress * progress)) * 1.5;
                    haloCenter.add(baseHaloVelocity.clone().multiply(speedFactor));
                } else { // coming back, track the player
                    double backwardsProgress = Math.min(progress - 1, 1);

                    speedFactor = Math.pow(backwardsProgress, 2); // square acceleration, such that the halo eases into its speed

                    Vector directionToPlayer = endLocation.toVector().subtract(haloCenter.toVector()).normalize();
                    Vector originalVelocityInfluence = baseHaloVelocity.clone().normalize().multiply(1 - backwardsProgress);
                    // how much of the direction of the halo's original base velocity is being applied to the halo's return velocity
                    // scales with backwards progress, such that when it equals 1, its not being applied anymore
                    // gives the halo its boomeranging curve when its returning back

                    Vector returningVelocityInfluence = directionToPlayer.multiply(backwardsProgress);
                    // how much of the return direction to the player is being applied to the halo's return velocity
                    // scales with backwards progress
                    // the mirror to originalVelocityInfluence

                    Vector blendedDirection = originalVelocityInfluence.add(returningVelocityInfluence).normalize();
                    Vector step = blendedDirection.multiply(speedFactor * 3);

                    if (step.lengthSquared() > haloCenter.distanceSquared(endLocation)) { // when the halo hits the player
                        removeInfiniteCooldown(player);
                        player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_PLACE, 1f, 1f);
                        player.stopSound(Sound.ITEM_ELYTRA_FLYING);
                        haloCenter = endLocation.clone(); // snap to player
                        cancel();

                        // mini halo
                        new BukkitRunnable() {
                            int timer = 40;

                            @Override
                            public void run() {
                                timer--;

                                Location head = player.getLocation().add(0, 2, 0);

                                if (timer != 0) {
                                    horizontalParticleCircle(Particle.ELECTRIC_SPARK, head, minHaloRadius, 20);
                                } else { // burst
                                    player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1f, 1f);
                                    expandingHorizontalParticleCircle(Particle.END_ROD, head, minHaloRadius, 100, .3);
                                    cancel();
                                }
                            }
                        }.runTaskTimer(nmlAbilities, 0L, 1L);
                    } else { // while it's still flying
                        haloCenter.add(step);

                        // start shrinking the halo when halo's coming back, its close enough to the player, and its been flying long enough
                        double distance = haloCenter.distance(endLocation);
                        double shrinkStart = 12 * (1 + (backwardsProgress / 5));
                        // the distance from the player to start shrinking, farther the longer the halo has been flying
                        // cuz that means its moving faster = has to start shrinking sooner

                        if (shrinkTimeReduction == -1 && shrinkSpeedMultiplier == -1) { // it only gets set once at the start
                            if (distance < 7) { // if you're close to the halo, have it shrink sooner
                                shrinkTimeReduction = .8;
                                shrinkSpeedMultiplier = 1;
                            } else if (distance > 20) { //  if you're far from the halo, have it shrink faster and a little sooner
                                shrinkTimeReduction = .6;
                                shrinkSpeedMultiplier = .85;
                            } else {
                                shrinkTimeReduction = 1;
                                shrinkSpeedMultiplier = 1;
                            }
                        }

                        if (backwardsProgress >= (.55 * shrinkTimeReduction) && distance <= shrinkStart) {
                            haloRadius = Math.max(minHaloRadius, haloRadius * (.775 * shrinkSpeedMultiplier));
                        }
                    }
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);
    }
}