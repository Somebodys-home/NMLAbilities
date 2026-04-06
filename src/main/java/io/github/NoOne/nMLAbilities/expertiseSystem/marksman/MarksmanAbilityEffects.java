package io.github.NoOne.nMLAbilities.expertiseSystem.marksman;

import io.github.NoOne.damagePlugin.customDamage.DamageHelper;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityEffects;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldown.CooldownManager;
import io.github.NoOne.nMLEnergySystem.EnergyManager;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import io.github.NoOne.nMLWeapons.AttackCooldownSystem;
import org.bukkit.*;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MarksmanAbilityEffects {
    private static NMLAbilities nmlAbilities;
    private static ProfileManager profileManager;

    public MarksmanAbilityEffects(NMLAbilities nmlAbilities) {
        this.nmlAbilities = nmlAbilities;
        profileManager = nmlAbilities.getProfileManager();
    }

    public static void arrowHailStorm(Player player) {
        Stats stats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();
        HashMap<DamageType, Double> damage = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStats2Damage(stats), .35);
        World world = player.getWorld();
        int maxTargetingRange = 15;
        int radius = 6;
        final int reticuleTicks = 40;
        final int arrowHailTicks = 100;

        EnergyManager.useEnergy(player, 30);
        AttackCooldownSystem.setOrPauseAttackCooldown(player, reticuleTicks / 20.0);
        CooldownManager.putOnHardCooldown(player, reticuleTicks / 20.0);

        /// shoot arrow into the sky
        Location playerLocation = player.getLocation();
        Vector direction = playerLocation.getDirection().normalize();
        double y = playerLocation.getY();
        Location start = playerLocation.clone().add(direction.clone().multiply(1.1));
        Location end = playerLocation.clone().add(direction.clone().multiply(2));

        start.setY(y);
        end.setY(y + 30);
        AbilityEffects.particleLine(Particle.COMPOSTER, start, end, 150);
        player.playSound(player, Sound.ITEM_CROSSBOW_SHOOT, 2f, 1f);

        /// reticule
        Location reticuleCenterLocation; // where the center of the reticule will be
        Location eyeLocation = player.getEyeLocation();
        Vector eyeDirection = eyeLocation.getDirection();
        RayTraceResult reticuleRayTrace = world.rayTrace(
                eyeLocation,
                eyeDirection,
                maxTargetingRange,
                FluidCollisionMode.NEVER,
                true,
                1,
                entity -> entity instanceof LivingEntity && entity != player,
                null
        );
        if (reticuleRayTrace == null) {
            Vector forward = eyeDirection.clone().normalize().multiply(maxTargetingRange);
            reticuleCenterLocation = eyeLocation.clone().add(forward);
        } else {
            if (reticuleRayTrace.getHitEntity() != null) {
                reticuleCenterLocation = reticuleRayTrace.getHitEntity().getLocation();
            } else {
                reticuleCenterLocation = reticuleRayTrace.getHitPosition().toLocation(world);
            }
        }

        reticuleCenterLocation = setLocationToFloor(reticuleCenterLocation).add(0, .2, 0);

        // actual reticule task
        Location finalReticuleCenterLocation1 = reticuleCenterLocation.clone();
        BukkitRunnable reticule = new BukkitRunnable() {
            @Override
            public void run() {
                AbilityEffects.horizontalParticleCircle(Particle.COMPOSTER, finalReticuleCenterLocation1.clone().add(0, -.2, 0), radius, 75);
                AbilityEffects.horizontalParticleCircle(Particle.COMPOSTER, finalReticuleCenterLocation1.clone().add(0, -.2, 0), radius * .75, 50);
                AbilityEffects.particleLine(
                        Particle.COMPOSTER,
                        finalReticuleCenterLocation1.clone().add(8.5, 0, 0),
                        finalReticuleCenterLocation1.clone().add(-8.5, 0, 0),
                        30
                );
                AbilityEffects.particleLine(
                        Particle.COMPOSTER,
                        finalReticuleCenterLocation1.clone().add(0, 0, 8.5),
                        finalReticuleCenterLocation1.clone().add(0, 0, -8.5),
                        30
                );
            }
        };

        /// arrow hail
        Location finalReticuleCenterLocation = reticuleCenterLocation;
        BukkitRunnable arrowHail = new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < 4; i++) {
                    double angle = ThreadLocalRandom.current().nextDouble(0, 2 * Math.PI);
                    double distance = Math.sqrt(ThreadLocalRandom.current().nextDouble()) * radius;
                    double xOffset = distance * Math.cos(angle);
                    double zOffset = distance * Math.sin(angle);
                    Location startingLocation = finalReticuleCenterLocation.clone().add(
                            xOffset,
                            30,
                            zOffset
                    );
                    Arrow arrow = world.spawnArrow(startingLocation, new Vector(0, -1, 0), 3f, 3f);

                    turnIntoAbilityArrow(arrow, player, damage);
                    setNoDamageTicks(arrow, 5);

                    if (i == 0 || i == 3) {
                        world.spawnParticle(Particle.SONIC_BOOM, startingLocation, 1);
                    }
                }
            }
        };

        /// sequence
        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {
                switch (timer) {
                    case 0 -> reticule.runTaskTimer(nmlAbilities, 0L, 1L);
                    case reticuleTicks -> {
                        reticule.cancel();
                        arrowHail.runTaskTimer(nmlAbilities, 0L, 1L);
                    }
                    case reticuleTicks + arrowHailTicks -> {
                        arrowHail.cancel();
                        cancel();
                    }
                }

                timer++;
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);
    }

    
    private static void turnIntoAbilityArrow(Arrow arrow, Player shooter, HashMap<DamageType, Double> damageMap) {
        arrow.setCritical(false);
        arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        arrow.setMetadata("custom_arrow", new FixedMetadataValue(nmlAbilities, damageMap));
        arrow.setShooter(shooter, false);

        // arrow despawn task
        new BukkitRunnable() {
            @Override
            public void run() {
                if (arrow.isDead() || arrow.isInBlock()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            arrow.remove();
                            cancel();
                        }
                    }.runTaskTimer(nmlAbilities, 100L, 2L);

                    cancel();
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 2L);
    }
    
    private static void setArrowTrail(Arrow arrow, Particle particle) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (arrow.isDead() || arrow.isOnGround()) {
                    this.cancel();
                    return;
                }

                double speed = arrow.getVelocity().length();
                int particleCount = (int) (Math.pow(speed, 2) * 5);

                if (particleCount > 0) {
                    Location loc = arrow.getLocation();
                    arrow.getWorld().spawnParticle(particle, loc, particleCount,0, 0, 0, 0);
                }
            }
        }.runTaskTimer(nmlAbilities, 0, 1);
    }

    private static void setNoDamageTicks(Arrow arrow, int noDamageTicks) {
        arrow.setMetadata("no_damage_ticks", new FixedMetadataValue(nmlAbilities, noDamageTicks));
    }

    private static Location setLocationToFloor(Location location) {
        Location clone = location.clone();

        while (clone.getBlock().isPassable()) {
            clone.setY(clone.getY() - 1);
        }

        clone.setY(clone.getY() + 1);
        return clone;
    }
}