package io.github.NoOne.nMLAbilities.expertiseSystem.sorcerer;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageConverter;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldownSystem.CooldownManager;
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

    public static void magicMissileEX(Player user, int hotbarSlot) {
        HashMap<DamageType, Double> damage = DamageConverter.multiplyDamageMap(DamageConverter.convertPlayerStats2Damage(
                profileManager.getPlayerProfile(user.getUniqueId()).getStats()), .5);

        EnergyManager.useEnergy(user, 15);
        CooldownManager.putAllOtherAbilitiesOnCooldown(user, 2.5, hotbarSlot);
        AttackCooldownSystem.setOrPauseAttackCooldown(user, 2.5);

        new BukkitRunnable() {
            int missiles = 0;
            int activeMissiles = 0;

            // actual missile
            @Override
            public void run() {
                missiles++;
                activeMissiles++;

                user.playSound(user.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, .6f, 1f);

                Set<UUID> hitEntityUUIDs = new HashSet<>();
                Random random = new Random();
                Vector direction = user.getEyeLocation().getDirection().normalize();

                Vector randomVec = new Vector(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5);
                if (randomVec.lengthSquared() < 1e-6) randomVec = new Vector(0.001, 0.001, 0.001);
                randomVec.normalize();

                Vector curveAxis = direction.clone().crossProduct(randomVec);
                if (curveAxis.lengthSquared() < 1e-6) {
                    double yaw = Math.toRadians(user.getEyeLocation().getYaw());
                    curveAxis = new Vector(-Math.sin(yaw), 0, Math.cos(yaw));
                }
                curveAxis.normalize();

                // start on the player's right
                Vector littleBitRight = direction.clone().crossProduct(new Vector(0, 1, 0));
                if (littleBitRight.lengthSquared() < 1e-6) {
                    double yaw = Math.toRadians(user.getEyeLocation().getYaw());
                    littleBitRight = new Vector(-Math.sin(yaw), 0, Math.cos(yaw));
                }
                littleBitRight.normalize();

                Location start = user.getLocation().add(0, 1.2, 0).add(littleBitRight.multiply(0.4));

                // where to end
                Location end;
                RayTraceResult rayTraceResult = user.getWorld().rayTraceEntities(
                        user.getEyeLocation(),
                        user.getLocation().getDirection(),
                        16,
                        entity -> entity instanceof LivingEntity && !entity.equals(user)
                );

                if (rayTraceResult != null && rayTraceResult.getHitEntity() instanceof LivingEntity livingEntity) { // successfully traced a target
                    end = livingEntity.getLocation().add(0, 0.5, 0);
                } else {
                    Location startLocation = user.getLocation().add(0, 1, 0);
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
                            user.playSound(user.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, .8f, 1f);
                            user.getWorld().spawnParticle(Particle.EXPLOSION, end, 1, 0, 1, 0, 0);
                            cancel();
                            return;
                        }

                        double progress = (double) i / particleInstances;

                        if (i == 0) {
                            user.getWorld().spawnParticle(Particle.GLOW, start, 50, 0.1, 0.075, 0.1, 0);
                            i++;
                            return;
                        } else if (i == particleInstances) {
                            user.getWorld().spawnParticle(Particle.GLOW, end, 60, 0.1, 0.1, 0.1, 0);
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
                        int worldMinY = user.getWorld().getMinHeight();

                        if (Double.isNaN(finalY) || finalY < worldMinY + 0.1) finalY = worldMinY + 0.1;

                        Location particleLocation = new Location(user.getWorld(), finalX, finalY, finalZ);
                        Collection<Entity> nearbyEntities = user.getWorld().getNearbyEntities(particleLocation, 2, 2, 2);

                        // entity collision
                        for (Entity entity : nearbyEntities) {
                            if (entity instanceof LivingEntity livingEntity && entity != user) {
                                if (hitEntityUUIDs.add(entity.getUniqueId())) { // still works
                                    Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, user, damage));
                                    livingEntity.setNoDamageTicks(0);
                                }
                            }
                        }

                        user.getWorld().spawnParticle(Particle.GLOW, particleLocation, 50, 0.1, 0.075, 0.1, 0);
                        i++;
                    }
                }.runTaskTimer(nmlAbilities, 0L, 1L);

                if (missiles == 5) cancel();
            }
        }.runTaskTimer(nmlAbilities, 0L, 5L);
    }

    public static void dragonsBreath(Player user, int hotbarSlot) {
        HashMap<DamageType, Double> fire = DamageConverter.convertPlayerStat2Damage(profileManager.getPlayerProfile(user.getUniqueId()).getStats(), "firedamage");
        HashSet<LivingEntity> hitEntities = new HashSet<>();
        int chargeUpTime = 40;

        CooldownManager.putAllOtherAbilitiesOnCooldown(user, 8, hotbarSlot);
        AttackCooldownSystem.setOrPauseAttackCooldown(user, 8);
        EnergyManager.useEnergy(user, 25);
        user.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, chargeUpTime, 10, false, false, false));
        user.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, chargeUpTime, 255, false, false, false));

        /// charge up
        new BukkitRunnable() {
            int timer = chargeUpTime;

            @Override
            public void run() {
                timer--;

                Location userLocation = user.getLocation().add(0, 1.65, 0);
                Vector forward = userLocation.getDirection().normalize();
                Location center = userLocation.clone().add(forward.clone().multiply(1.5));
                int particleCount = 5;
                double radius = Math.max((double) timer / 50, .1);
                Vector right = forward.clone().crossProduct(new Vector(0, 1, 0)).normalize(); // orthogonal basis vector
                Vector up = right.clone().crossProduct(forward).normalize(); // orthogonal basis vector

                for (int i = 0; i < particleCount; i++) {
                    double angle = 2 * Math.PI * i / particleCount + ((chargeUpTime - timer) * .02);

                    Vector offset = up.clone().multiply(Math.cos(angle)).add(right.clone().multiply(Math.sin(angle))).multiply(radius);
                    Location particleLocation = center.clone().add(offset);

                    user.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, particleLocation, 0);
                }

                if (timer != 0 && timer % 13 == 0) user.playSound(user, Sound.ITEM_FLINTANDSTEEL_USE, 2f, 1f);
                if (timer == 0) {
                    cancel();
                    user.playSound(user, Sound.ITEM_ELYTRA_FLYING, 2f, .5f);
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
                Location userLocation = user.getLocation().add(0, 1.65, 0);
                Vector forward = userLocation.getDirection().normalize();
                Location baseLocation = userLocation.clone().add(forward.clone().multiply(1.3));
                Vector playerDirection = user.getLocation().getDirection();
                Vector particleVector = playerDirection.clone();

                playerDirection.multiply(5); // length
                particleVector.divide(new Vector(3, 3, 3)); // Divide it by 2 to shorten length

                Location particleLocation = particleVector.toLocation(user.getWorld()).add(baseLocation);

                for (int i = 0; i < 12; i++) { // Amount of fire
                    Vector particlePath = playerDirection.clone();

                    particlePath.add(new Vector(Math.random() - Math.random(), Math.random() - Math.random(), Math.random() - Math.random()));

                    Location offsetLocation = particlePath.toLocation(user.getWorld());

                    user.getWorld().spawnParticle(Particle.FLAME, particleLocation, 0, offsetLocation.getX() * 1.5, offsetLocation.getY() * 1.5, offsetLocation.getZ() * 1.5, 0.1);
                }

                // damage
                if (timer % 5 == 0) {
                    Vector direction = user.getEyeLocation().getDirection().normalize();

                    for (double d = 0; d <= 12; d += .5) {
                        Location checkLoc = user.getEyeLocation().add(direction.clone().multiply(d));
                        Collection<Entity> nearby = checkLoc.getWorld().getNearbyEntities(checkLoc, 1, 1, 1);

                        for (Entity entity : nearby) {
                            if (entity instanceof LivingEntity livingEntity && entity != user) {
                                hitEntities.add(livingEntity);
                            }
                        }
                    }

                    for (LivingEntity livingEntity : hitEntities) {
                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, user, fire));
                    }

                    hitEntities.clear();
                }


                if (timer == 80) {
                    user.stopSound(Sound.ITEM_ELYTRA_FLYING);
                    user.playSound(user, Sound.BLOCK_FIRE_EXTINGUISH, .5f, 1f);

                    userLocation = user.getLocation().add(0, 1.65, 0);
                    forward = userLocation.getDirection().normalize();
                    Location center = userLocation.clone().add(forward.clone().multiply(1.5));
                    Vector right = forward.clone().crossProduct(new Vector(0, 1, 0)).normalize(); // orthogonal basis vector
                    Vector up = right.clone().crossProduct(forward).normalize(); // orthogonal basis vector
                    int particleCount = 20;

                    for (int i = 0; i < particleCount; i++) {
                        double angle = 2 * Math.PI * i / particleCount + ((chargeUpTime - timer) * .02);

                        Vector offset = up.clone().multiply(Math.cos(angle)).add(right.clone().multiply(Math.sin(angle))).multiply(.1);
                        particleLocation = center.clone().add(offset);
                        Vector velocity = offset.clone().multiply(.65);

                        user.getWorld().spawnParticle(Particle.FLAME, particleLocation, 0, velocity.getX(), velocity.getY(), velocity.getZ());
                    }

                    cancel();
                }
            }
        }.runTaskTimer(nmlAbilities, chargeUpTime, 1);
    }
}