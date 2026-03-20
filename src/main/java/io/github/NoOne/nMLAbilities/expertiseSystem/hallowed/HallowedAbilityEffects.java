package io.github.NoOne.nMLAbilities.expertiseSystem.hallowed;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageHelper;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityEffects;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldownSystem.CooldownManager;
import io.github.NoOne.nMLEnergySystem.EnergyManager;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class HallowedAbilityEffects {
    private static NMLAbilities nmlAbilities;
    private static ProfileManager profileManager;

    public HallowedAbilityEffects(NMLAbilities nmlAbilities) {
        this.nmlAbilities = nmlAbilities;
        profileManager = nmlAbilities.getProfileManager();
    }

    public static void halo(Player player) {
        Stats stats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();
        HashMap<DamageType, Double> radiantDamage = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStat2Damage(stats, "radiantdamage"), .35);
        HashMap<DamageType, Double> damage = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStats2Damage(stats), .15);
        HashSet<LivingEntity> hitEntities = new HashSet<>();

        damage.remove("radiantdamage");
        damage.putAll(radiantDamage);

        EnergyManager.useEnergy(player, 25);
        CooldownManager.putOnInfiniteHardCooldown(player);
        player.playSound(player, Sound.ITEM_TRIDENT_RIPTIDE_1, 1f, 1f);
        player.playSound(player, Sound.ITEM_ELYTRA_FLYING, .5f, 1f);

        new BukkitRunnable() {
            int ticks = 0;
            int maxTicks = 80;
            Location center = player.getLocation().clone().add(0, 1, 0);
            Vector baseVelocity = player.getLocation().getDirection().normalize().multiply(.4);

            @Override
            public void run() {
                ticks++;

                double progress = (double) ticks / ((double) maxTicks / 2);
                double speedFactor;

                if (progress <= 1) { // slowdown going forwards
                    speedFactor = 1 - (progress * progress);
                    center.add(baseVelocity.clone().multiply(speedFactor * 1.5));
                } else { // coming back, tracking the player
                    double t = progress - 1;
                    speedFactor = t * t;

                    Location playerCenter = player.getLocation().clone().add(0, 2, 0);
                    Vector toPlayer = playerCenter.toVector().subtract(center.toVector()).normalize();
                    Vector blended = baseVelocity.clone().normalize().multiply(1 - t).add(toPlayer.multiply(t)).normalize();
                    Vector step = blended.multiply(speedFactor * 3);

                    if (step.lengthSquared() > center.distanceSquared(playerCenter)) {
                        CooldownManager.removeHardCooldown(player);
                        player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_PLACE, 1f, 1f);
                        player.stopSound(Sound.ITEM_ELYTRA_FLYING);
                        center = playerCenter.clone(); // snap to player
                        this.cancel(); // stop the main loop right away

                        // mini halo
                        new BukkitRunnable() {
                            int timer = 0;

                            @Override
                            public void run() {
                                timer++;

                                Location center = player.getLocation();
                                double radius = .5; // <- max radius
                                int particleCount = 100;

                                if (timer != 40) {
                                    AbilityEffects.horizontalParticleCircle(Particle.ELECTRIC_SPARK, center.add(0, 2, 0), radius, particleCount);
                                } else { // burst
                                    player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1f, 1f);
                                    AbilityEffects.expandingHorizontalParticleCircle(Particle.END_ROD, center.add(0, 2, 0), radius, particleCount, .3);
                                    cancel();
                                }
                            }
                        }.runTaskTimer(nmlAbilities, 0L, 1L);
                        return;
                    } else {
                        center.add(step);
                    }
                }

                // halo
                Location playerCenter = player.getLocation().clone().add(0, 2, 0);
                double distance = center.distance(playerCenter);
                double shrinkStart = 12; // what distance from the player to start shrinking
                double currentRadius;

                if (progress <= 1 || distance > shrinkStart) {
                    currentRadius = 4; // outbound
                } else {
                    double shrinkProgress = Math.min(1.0, (shrinkStart - distance) / shrinkStart);
                    currentRadius = 4 - (3.5 * shrinkProgress); // 4 → 0.5
                }

                AbilityEffects.horizontalParticleCircle(Particle.END_ROD, center, currentRadius, 100);
                AbilityEffects.horizontalParticleCircle(Particle.ELECTRIC_SPARK, center, currentRadius - .1, 120);

                // damage
                if (ticks % 5 == 0) {
                    for (Entity entity : player.getWorld().getNearbyEntities(center, 4, .5, 4)) {
                        if (entity instanceof LivingEntity livingEntity && entity != player) {
                            Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damage));
                        }
                    }
                }

                hitEntities.clear();
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);
    }
}