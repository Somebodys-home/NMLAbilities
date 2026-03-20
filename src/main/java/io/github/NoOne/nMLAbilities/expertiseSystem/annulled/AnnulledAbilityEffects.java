package io.github.NoOne.nMLAbilities.expertiseSystem.annulled;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageHelper;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityEffects;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldownSystem.CooldownManager;
import io.github.NoOne.nMLEnergySystem.EnergyManager;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import io.github.NoOne.nMLWeapons.AttackCooldownSystem;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class AnnulledAbilityEffects {
    private static NMLAbilities nmlAbilities;
    private static ProfileManager profileManager;

    public AnnulledAbilityEffects(NMLAbilities nmlAbilities) {
        this.nmlAbilities = nmlAbilities;
        profileManager = nmlAbilities.getProfileManager();
    }

    public static void blackHole(Player player) {
        Stats stats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();
        HashMap<DamageType, Double> necroticdamage = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStat2Damage(stats, "necroticdamage"), 5);
        Particle.DustOptions blackHole = new Particle.DustOptions(Color.fromRGB(0, 0, 0), 1F);

        EnergyManager.useEnergy(player, 50);
        CooldownManager.putOnInfiniteHardCooldown(player);
        AttackCooldownSystem.pauseAttackCooldown(player);
        player.playSound(player, Sound.ENTITY_WITHER_SHOOT, 1f, 1f);

        new BukkitRunnable() {
            int duration = 0;
            Location center = player.getLocation().clone().add(0, 1, 0);
            Vector velocity = player.getLocation().getDirection().normalize().multiply(.15);

            @Override
            public void run() {
                duration++;

                center.add(velocity);

                /// tiny black hole
                AbilityEffects.dustSphere(blackHole, center, .5, 7);

                /// triggering big black hole
                Collection<Entity> triggeringEntities = player.getWorld().getNearbyEntities(center, .5, .5, .5);
                triggeringEntities.remove(player);

                /// big / collapsing black hole
                if (duration == 100 || !center.getBlock().isPassable() || !triggeringEntities.isEmpty()) {
                    cancel();
                    player.playSound(player, Sound.ITEM_ELYTRA_FLYING, 2f, 1f);
                    player.playSound(player, Sound.ENTITY_WITHER_DEATH, 1f, 1f);
                    CooldownManager.removeHardCooldown(player);
                    AttackCooldownSystem.resumeAttackCooldown(player);

                    new BukkitRunnable() {
                        int duration = 0;
                        double size = 0;
                        double particleStep;
                        int pullradius = 15;

                        @Override
                        public void run() {
                            duration++;

                            // size changing
                            if (duration < 100 && size < 8) {
                                size = Math.min(8, size + 2.5);
                            } else if (duration == 100) {
                                size = Math.max(1, size - 2);
                                player.playSound(player, Sound.ENTITY_WITHER_HURT, 1f, 1f);
                            } else if (duration == 115) {
                                size = Math.max(1, size - 2);
                                player.playSound(player, Sound.ENTITY_WITHER_HURT, 1f, 1.5f);
                            } else if (duration == 130) {
                                size = Math.max(1, size - 2);
                                player.playSound(player, Sound.ENTITY_WITHER_HURT, 1f, 2f);
                            }

                            particleStep = Math.PI / (size * 2);

                            for (double i = 0; i <= Math.PI; i += particleStep) {
                                for (double a = 0; a < Math.PI * 2; a+= particleStep) {
                                    double x = Math.cos(a) * Math.sin(i) * size;
                                    double y = Math.cos(i) * size;
                                    double z = Math.sin(a) * Math.sin(i) * size;
                                    Location particleLocation = center.clone().add(x, y, z);

                                    player.getWorld().spawnParticle(Particle.SQUID_INK, particleLocation, 3, 0, 0, 0);
                                }
                            }

                            // pull
                            for (Entity entity : center.getWorld().getNearbyEntities(center, pullradius, pullradius, pullradius)) {
                                if (entity instanceof LivingEntity && entity != player) {
                                    Vector toCenter = center.clone().subtract(entity.getLocation()).toVector();
                                    double distance = toCenter.length();
                                    Vector pull = toCenter.normalize().multiply(.03 * distance);

                                    entity.setVelocity(entity.getVelocity().add(pull));
                                }
                            }

                            // explosion
                            if (duration == 155) {
                                cancel();
                                player.stopSound(Sound.ITEM_ELYTRA_FLYING);
                                player.playSound(player, Sound.ENTITY_WITHER_SPAWN, 2f, 1f);
                                player.playSound(player, Sound.ENTITY_WITHER_DEATH, .5f, 1f);

                                double explosionRadius = 8;
                                for (double i = 0; i <= Math.PI; i += Math.PI / 60) { // vertical circles
                                    double radius = Math.sin(i) * 2; // 2 block
                                    double y = Math.cos(i) * 2; // 2 block

                                    for (double a = 0; a < Math.PI * 2; a+= Math.PI / 60) { // horizontal circles
                                        double x = Math.cos(a) * radius;
                                        double z = Math.sin(a) * radius;
                                        Location particleLocation = center.clone().add(x, y, z);
                                        Vector velocity = particleLocation.toVector().subtract(center.toVector()).normalize().multiply(0.3);

                                        if (Math.random() < .004) { // random chance to spawn explosion particle for every expanding particle
                                            double finalA = a;
                                            double finalI = i;
                                            new BukkitRunnable() {
                                                @Override
                                                public void run() {
                                                    double explosionX = Math.cos(finalA) * explosionRadius * 1.15;
                                                    double explosionY = Math.cos(finalI) * explosionRadius * 1.15;
                                                    double explosionZ = Math.sin(finalA) * explosionRadius * 1.15;
                                                    Location explosionLocation = center.clone().add(explosionX, explosionY, explosionZ);

                                                    player.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, explosionLocation, 1);
                                                }
                                            }.runTaskLater(nmlAbilities, 3L);
                                        }

                                        player.getWorld().spawnParticle(Particle.SQUID_INK, particleLocation, 0,
                                                                        velocity.getX(), velocity.getY(), velocity.getZ(), 7);
                                        particleLocation.subtract(x, y, z); // reset location
                                    }
                                }

                                for (Entity entity : player.getWorld().getNearbyEntities(center, explosionRadius, explosionRadius, explosionRadius)) {
                                    if (entity instanceof LivingEntity livingEntity && entity != player) {
                                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, necroticdamage));
                                    }
                                }
                            }
                        }
                    }.runTaskTimer(nmlAbilities, 0L, 1L);
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);
    }
}