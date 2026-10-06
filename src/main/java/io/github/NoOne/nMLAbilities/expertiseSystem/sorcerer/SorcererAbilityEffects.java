package io.github.NoOne.nMLAbilities.expertiseSystem.sorcerer;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.abilitySystem.abilityEffects.AbilityEffectsHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Random;

public class SorcererAbilityEffects extends AbilityEffectsHelper {
    public static void magicMissileEX(Player player) {
        HashMap<DamageType, Double> damage = getDamageForAbility(player, .5);

        useEnergyAndCooldown(player, 15, 2.5);

        new BukkitRunnable() {
            int missiles = 0;
            int activeMissiles = 0;

            // actual missile
            @Override
            public void run() {
                missiles++;
                activeMissiles++;

                player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, .6f, 1f);

                Random random = new Random();
                Vector direction = player.getEyeLocation().getDirection();

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
                    Vector forward = startLocation.getDirection().multiply(16); // max range
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

                        for (LivingEntity livingEntity : getNearbyEntitiesExcludingPlayer(player, particleLocation, 2)) {
                            Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damage, 5));
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
        HashMap<DamageType, Double> damage = getDamageForAbility(player, DamageType.FIRE, .25);
        final int chargeUpTime = 40;
        final int dragonsBreathTime = 80;

        useEnergyAndCooldown(player, 25, 8);
        makeUnmovable(player, chargeUpTime);

        // charge up
        BukkitRunnable chargeUp = new BukkitRunnable() {
            int timer = chargeUpTime;

            @Override
            public void run() {
                timer--;

                Location playerLocation = player.getLocation().add(0, 1.65, 0);
                Vector forward = playerLocation.getDirection();
                Location center = playerLocation.clone().add(forward.clone().multiply(1.5));
                int particleCount = 5;
                double radius = Math.max((double) timer / 50, .1);
                Vector right = forward.clone().crossProduct(new Vector(0, 1, 0)).normalize(); // orthogonal basis vector
                Vector up = right.clone().crossProduct(forward).normalize(); // orthogonal basis vector

                // making the closing spinning circle particle effect
                for (int i = 0; i < particleCount; i++) {
                    double angle = 2 * Math.PI * i / particleCount + ((chargeUpTime - timer) * .02);

                    Vector offset = up.clone().multiply(Math.cos(angle)).add(right.clone().multiply(Math.sin(angle))).multiply(radius);
                    Location particleLocation = center.clone().add(offset);

                    player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, particleLocation, 0);
                }

                if (timer != 0 && timer % 13 == 0) {
                    player.playSound(player, Sound.ITEM_FLINTANDSTEEL_USE, 2f, 1f);
                } else if (timer == 0) {
                    cancel();
                    player.playSound(player, Sound.ITEM_ELYTRA_FLYING, 2f, .5f);
                    makeMovable(player);
                }
            }
        };

        // dragon's breath
        BukkitRunnable dragonsBreath = new BukkitRunnable() {
            int timer = dragonsBreathTime;

            @Override
            public void run() {
                timer--;

                // flamethrower effect
                Location playerLocation = player.getLocation().add(0, 1.65, 0);
                Vector forward = playerLocation.getDirection();
                Location baseLocation = playerLocation.clone().add(forward.clone().multiply(1.3));
                Vector playerDirection = player.getLocation().getDirection();
                Vector particleVector = playerDirection.clone();

                playerDirection.multiply(5); // length
                particleVector.divide(new Vector(3, 3, 3)); // Divide it by 2 to shorten length

                Location particleLocation = particleVector.toLocation(player.getWorld()).add(baseLocation);

                for (int i = 0; i < 12; i++) { // Amount of damage
                    Vector particlePath = playerDirection.clone();

                    particlePath.add(new Vector(Math.random() - Math.random(), Math.random() - Math.random(), Math.random() - Math.random()));

                    Location offsetLocation = particlePath.toLocation(player.getWorld());

                    player.getWorld().spawnParticle(Particle.FLAME, particleLocation, 0, offsetLocation.getX() * 1.5, offsetLocation.getY() * 1.5, offsetLocation.getZ() * 1.5, 0.1);
                }

                // damage
                Vector direction = player.getEyeLocation().getDirection();

                for (double d = 0; d <= 12; d += .5) {
                    Location checkLoc = player.getEyeLocation().add(direction.clone().multiply(d));

                    for (LivingEntity livingEntity : getNearbyEntitiesExcludingPlayer(player, checkLoc, 1)) {
                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damage, 5));
                    }
                }

                // flamethrower end effect
                if (timer == 0) {
                    player.stopSound(Sound.ITEM_ELYTRA_FLYING);
                    player.playSound(player, Sound.BLOCK_FIRE_EXTINGUISH, .5f, 1f);

                    playerLocation = player.getLocation().add(0, 1.65, 0);
                    forward = playerLocation.getDirection();
                    Location center = playerLocation.clone().add(forward.clone().multiply(1.5));
                    Vector right = forward.clone().crossProduct(new Vector(0, 1, 0)).normalize(); // orthogonal basis vector
                    Vector up = right.clone().crossProduct(forward).normalize(); // orthogonal basis vector
                    int particleCount = 20;

                    for (int i = 0; i < particleCount; i++) {
                        double angle = 2 * Math.PI * i / particleCount + ((chargeUpTime - dragonsBreathTime) * .02);

                        Vector offset = up.clone().multiply(Math.cos(angle)).add(right.clone().multiply(Math.sin(angle))).multiply(.1);
                        particleLocation = center.clone().add(offset);
                        Vector velocity = offset.clone().multiply(.65);

                        player.getWorld().spawnParticle(Particle.FLAME, particleLocation, 0, velocity.getX(), velocity.getY(), velocity.getZ());
                    }

                    cancel();
                }
            }
        };

        // sequence
        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {
                switch (timer) {
                    case 0 -> chargeUp.runTaskTimer(nmlAbilities, 0, 1);
                    case chargeUpTime -> {
                        chargeUp.cancel();
                        dragonsBreath.runTaskTimer(nmlAbilities, 0, 1);
                    }
                    case chargeUpTime + dragonsBreathTime -> {
                        cancel();
                    }
                };

                timer++;
            }
        }.runTaskTimer(nmlAbilities, 0, 1);
    }
}