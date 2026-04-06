package io.github.NoOne.nMLAbilities.expertiseSystem.sorcerer;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageHelper;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldown.CooldownManager;
import io.github.NoOne.nMLEnergySystem.EnergyManager;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLWeapons.AttackCooldownSystem;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;

public class SorcererAbilityEffects {
    private static NMLAbilities nmlAbilities;
    private static ProfileManager profileManager;

    public SorcererAbilityEffects(NMLAbilities nmlAbilities) {
        this.nmlAbilities = nmlAbilities;
        profileManager = nmlAbilities.getProfileManager();
    }

    public static void magicMissileEX(Player player) {
        HashMap<DamageType, Double> damage = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStats2Damage(
                profileManager.getPlayerProfile(player.getUniqueId()).getStats()), .5);

        EnergyManager.useEnergy(player, 15);
        CooldownManager.putOnHardCooldown(player, 2.5);
        AttackCooldownSystem.setOrPauseAttackCooldown(player, 2.5);

        new BukkitRunnable() {
            int missiles = 0;
            int activeMissiles = 0;

            // actual missile
            @Override
            public void run() {
                missiles++;
                activeMissiles++;

                player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, .6f, 1f);

                Set<UUID> hitEntityUUIDs = new HashSet<>();
                Random random = new Random();
                Vector direction = player.getEyeLocation().getDirection().normalize();

                Vector randomVec = new Vector(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5);
                if (randomVec.lengthSquared() < 1e-6) randomVec = new Vector(0.001, 0.001, 0.001);
                randomVec.normalize();

                Vector curveAxis = direction.clone().crossProduct(randomVec);
                if (curveAxis.lengthSquared() < 1e-6) {
                    double yaw = Math.toRadians(player.getEyeLocation().getYaw());
                    curveAxis = new Vector(-Math.sin(yaw), 0, Math.cos(yaw));
                }
                curveAxis.normalize();

                // start on the player's right
                Vector littleBitRight = direction.clone().crossProduct(new Vector(0, 1, 0));
                if (littleBitRight.lengthSquared() < 1e-6) {
                    double yaw = Math.toRadians(player.getEyeLocation().getYaw());
                    littleBitRight = new Vector(-Math.sin(yaw), 0, Math.cos(yaw));
                }
                littleBitRight.normalize();

                Location start = player.getLocation().add(0, 1.2, 0).add(littleBitRight.multiply(0.4));

                // where to end
                Location end;
                RayTraceResult rayTraceResult = player.getWorld().rayTraceEntities(
                        player.getEyeLocation(),
                        player.getLocation().getDirection(),
                        16,
                        entity -> entity instanceof LivingEntity && !entity.equals(player)
                );

                if (rayTraceResult != null) { // successfully traced a target
                    end = rayTraceResult.getHitEntity().getLocation().add(0, 0.5, 0);
                } else {
                    Location startLocation = player.getLocation().add(0, 1, 0);
                    Vector forward = startLocation.getDirection().normalize().multiply(16); // max range
                    end = startLocation.clone().add(forward);
                }

                double curveDirection = random.nextBoolean() ? 1 : -1;
                double verticalCurveDirection = random.nextBoolean() ? 1 : -1;
                double curveAmount = 1.5 + random.nextDouble() * 3.0;
                double minHeight = 0.2 + random.nextDouble();
                double maxHeight = 1.0 + random.nextDouble() * 1.5;
                int particleInstances = 10;
                Vector finalCurveAxis = curveAxis;

                // particles
                new BukkitRunnable() {
                    int i = 0;

                    @Override
                    public void run() {
                        if (i > particleInstances) {
                            activeMissiles--;
                            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, .8f, 1f);
                            player.getWorld().spawnParticle(Particle.EXPLOSION, end, 1, 0, 1, 0, 0);
                            cancel();
                            return;
                        }

                        double progress = (double) i / particleInstances;

                        if (i == 0) {
                            player.getWorld().spawnParticle(Particle.GLOW, start, 50, 0.1, 0.075, 0.1, 0);
                            i++;
                            return;
                        } else if (i == particleInstances) {
                            player.getWorld().spawnParticle(Particle.GLOW, end, 60, 0.1, 0.1, 0.1, 0);
                            i++;
                            return;
                        }

                        double baseX = start.getX() + (end.getX() - start.getX()) * progress;
                        double baseY = start.getY() + (end.getY() - start.getY()) * progress;
                        double baseZ = start.getZ() + (end.getZ() - start.getZ()) * progress;
                        double curveOffset = curveDirection * curveAmount * Math.sin(progress * Math.PI);
                        double heightFactor = minHeight + (maxHeight - minHeight) * Math.sin(progress * Math.PI);
                        double finalX = baseX + finalCurveAxis.getX() * curveOffset;
                        double finalY = baseY + heightFactor * verticalCurveDirection;
                        double finalZ = baseZ + finalCurveAxis.getZ() * curveOffset;
                        int worldMinY = player.getWorld().getMinHeight();

                        if (Double.isNaN(finalY) || finalY < worldMinY + 0.1) finalY = worldMinY + 0.1;

                        Location particleLocation = new Location(player.getWorld(), finalX, finalY, finalZ);
                        Collection<Entity> nearbyEntities = player.getWorld().getNearbyEntities(particleLocation, 2, 2, 2);

                        // entity collision
                        for (Entity entity : nearbyEntities) {
                            if (entity instanceof LivingEntity livingEntity && entity != player) {
                                if (hitEntityUUIDs.add(entity.getUniqueId())) { // still works
                                    Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damage, 0));
                                }
                            }
                        }

                        player.getWorld().spawnParticle(Particle.GLOW, particleLocation, 50, 0.1, 0.075, 0.1, 0);
                        i++;
                    }
                }.runTaskTimer(nmlAbilities, 0L, 1L);

                if (missiles == 5) cancel();
            }
        }.runTaskTimer(nmlAbilities, 0L, 5L);
    }

    public static void dragonsBreath(Player player) {
        HashMap<DamageType, Double> fire = DamageHelper.convertPlayerStat2Damage(profileManager.getPlayerProfile(player.getUniqueId()).getStats(), "firedamage");
        HashSet<LivingEntity> hitEntities = new HashSet<>();
        int chargeUpTime = 40;

        CooldownManager.putOnHardCooldown(player, 8);
        AttackCooldownSystem.setOrPauseAttackCooldown(player, 8);
        EnergyManager.useEnergy(player, 25);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, chargeUpTime, 10, false, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, chargeUpTime, 255, false, false, false));

        /// charge up
        new BukkitRunnable() {
            int timer = chargeUpTime;

            @Override
            public void run() {
                timer--;

                Location playerLocation = player.getLocation().add(0, 1.65, 0);
                Vector forward = playerLocation.getDirection().normalize();
                Location center = playerLocation.clone().add(forward.clone().multiply(1.5));
                int particleCount = 5;
                double radius = Math.max((double) timer / 50, .1);
                Vector right = forward.clone().crossProduct(new Vector(0, 1, 0)).normalize(); // orthogonal basis vector
                Vector up = right.clone().crossProduct(forward).normalize(); // orthogonal basis vector

                for (int i = 0; i < particleCount; i++) {
                    double angle = 2 * Math.PI * i / particleCount + ((chargeUpTime - timer) * .02);

                    Vector offset = up.clone().multiply(Math.cos(angle)).add(right.clone().multiply(Math.sin(angle))).multiply(radius);
                    Location particleLocation = center.clone().add(offset);

                    player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, particleLocation, 0);
                }

                if (timer != 0 && timer % 13 == 0) player.playSound(player, Sound.ITEM_FLINTANDSTEEL_USE, 2f, 1f);
                if (timer == 0) {
                    cancel();
                    player.playSound(player, Sound.ITEM_ELYTRA_FLYING, 2f, .5f);
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);


        /// dragon's breath
        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {
                timer++;

                /// flamethrower
                Location playerLocation = player.getLocation().add(0, 1.65, 0);
                Vector forward = playerLocation.getDirection().normalize();
                Location baseLocation = playerLocation.clone().add(forward.clone().multiply(1.3));
                Vector playerDirection = player.getLocation().getDirection();
                Vector particleVector = playerDirection.clone();

                playerDirection.multiply(5); // length
                particleVector.divide(new Vector(3, 3, 3)); // Divide it by 2 to shorten length

                Location particleLocation = particleVector.toLocation(player.getWorld()).add(baseLocation);

                for (int i = 0; i < 12; i++) { // Amount of fire
                    Vector particlePath = playerDirection.clone();

                    particlePath.add(new Vector(Math.random() - Math.random(), Math.random() - Math.random(), Math.random() - Math.random()));

                    Location offsetLocation = particlePath.toLocation(player.getWorld());

                    player.getWorld().spawnParticle(Particle.FLAME, particleLocation, 0, offsetLocation.getX() * 1.5, offsetLocation.getY() * 1.5, offsetLocation.getZ() * 1.5, 0.1);
                }

                // damage
                if (timer % 5 == 0) {
                    Vector direction = player.getEyeLocation().getDirection().normalize();

                    for (double d = 0; d <= 12; d += .5) {
                        Location checkLoc = player.getEyeLocation().add(direction.clone().multiply(d));
                        Collection<Entity> nearby = checkLoc.getWorld().getNearbyEntities(checkLoc, 1, 1, 1);

                        for (Entity entity : nearby) {
                            if (entity instanceof LivingEntity livingEntity && entity != player) {
                                hitEntities.add(livingEntity);
                            }
                        }
                    }

                    for (LivingEntity livingEntity : hitEntities) {
                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, fire));
                    }

                    hitEntities.clear();
                }


                if (timer == 80) {
                    player.stopSound(Sound.ITEM_ELYTRA_FLYING);
                    player.playSound(player, Sound.BLOCK_FIRE_EXTINGUISH, .5f, 1f);

                    playerLocation = player.getLocation().add(0, 1.65, 0);
                    forward = playerLocation.getDirection().normalize();
                    Location center = playerLocation.clone().add(forward.clone().multiply(1.5));
                    Vector right = forward.clone().crossProduct(new Vector(0, 1, 0)).normalize(); // orthogonal basis vector
                    Vector up = right.clone().crossProduct(forward).normalize(); // orthogonal basis vector
                    int particleCount = 20;

                    for (int i = 0; i < particleCount; i++) {
                        double angle = 2 * Math.PI * i / particleCount + ((chargeUpTime - timer) * .02);

                        Vector offset = up.clone().multiply(Math.cos(angle)).add(right.clone().multiply(Math.sin(angle))).multiply(.1);
                        particleLocation = center.clone().add(offset);
                        Vector velocity = offset.clone().multiply(.65);

                        player.getWorld().spawnParticle(Particle.FLAME, particleLocation, 0, velocity.getX(), velocity.getY(), velocity.getZ());
                    }

                    cancel();
                }
            }
        }.runTaskTimer(nmlAbilities, chargeUpTime, 1);
    }
}