package io.github.NoOne.nMLAbilities.expertiseSystem.marksman;

import io.github.NoOne.damagePlugin.customDamage.DamageHelper;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityEffects;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldown.CooldownManager;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import io.github.NoOne.nMLWeapons.AttackCooldownSystem;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;

public class MarksmanAbilityEffects {
    private static NMLAbilities nmlAbilities;
    private static ProfileManager profileManager;

    public MarksmanAbilityEffects(NMLAbilities nmlAbilities) {
        this.nmlAbilities = nmlAbilities;
        profileManager = nmlAbilities.getProfileManager();
    }

    public static void arrowHailStorm(Player player) {
        Stats stats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();
        HashMap<DamageType, Double> damage = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStats2Damage(stats), .25);
        int maxTargetingRange = 15;
        Location eyeLocation = player.getEyeLocation();
        World world = player.getWorld();
        RayTraceResult reticuleCenter = world.rayTrace(
                eyeLocation,
                eyeLocation.getDirection(),
                maxTargetingRange,
                FluidCollisionMode.NEVER,
                true,
                1,
                entity -> entity instanceof LivingEntity && entity != player,
                null
        );

        // where the center of the reticule will be
        Location reticuleCenterLocation;
        if (reticuleCenter == null) {
            Vector forward = eyeLocation.getDirection().normalize().multiply(maxTargetingRange);
            reticuleCenterLocation = eyeLocation.clone().add(forward);
        } else {
            reticuleCenterLocation = reticuleCenter.getHitPosition().toLocation(world);
        }

        reticuleCenterLocation = setLocationToFloor(reticuleCenterLocation).add(0, .2, 0);

        // locations of circle particles
        ArrayList<Location> reticuleCircleParticlesLocations = new ArrayList<>();

        for (Location location : AbilityEffects.getParticleCircleLocations(reticuleCenterLocation.add(0, -.2, 0), 5, 75)) {
            reticuleCircleParticlesLocations.add(dynamicallyUpdateLocationToBlock(location));
        }

        //EnergyManager.useEnergy(player, 30);
        AttackCooldownSystem.setOrPauseAttackCooldown(player, 3);
        CooldownManager.putOnHardCooldown(player, 3);

        // make reticle for 2s
        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {
                timer++;

                for (Location location : reticuleCircleParticlesLocations) {
                    location.getWorld().spawnParticle(Particle.COMPOSTER, location, 1, 0, 0, 0, 0);
                }

                if (timer == 40) {
                    cancel();
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);

        // arrow hail for 5s
        new BukkitRunnable() {
            final int spread = 10;
            int timer = 0;

            @Override
            public void run() {
                timer++;

                Arrow arrow = player.launchProjectile(Arrow.class);
                double x = (Math.random() - .5) * (spread * .5);
                double y = (Math.random() - .5) * (spread * .5);
                double z = (Math.random() - .5) * (spread * .5);
                Vector spreadVector = new Vector(x, y, z);

                arrow.setVelocity(player.getLocation().getDirection().multiply(speed).add(spreadVector)); // Speed multiplier
                arrow.setCritical(false);
                arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                arrow.setMetadata("ability_arrow", new FixedMetadataValue(nmlAbilities, damageMap));
                arrow.setRotation(arrow.getYaw(), -90);

                if (timer == 50) {
                    cancel();
                }
            }
        }.runTaskTimer(nmlAbilities, 20L, 2L);
    }

    
    private static void makeAbilityArrow(Arrow arrow, HashMap<DamageType, Double> damageMap) {
        arrow.setCritical(false);
        arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        arrow.setMetadata("ability_arrow", new FixedMetadataValue(nmlAbilities, damageMap));

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
    
    private static void makeArrowTrail(Arrow arrow) {
        // trail particles
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
                    arrow.getWorld().spawnParticle(Particle.CRIT, loc, particleCount,0, 0, 0, 0);
                }
            }
        }.runTaskTimer(nmlAbilities, 0, 1);
    }

    private static Location setLocationToFloor(Location location) {
        Location clone = location.clone();

        while (clone.getBlock().isPassable()) {
            clone.setY(clone.getY() - 1);
        }

        clone.setY(clone.getY() + 1);
        return clone;
    }

    private static Location dynamicallyUpdateLocationToBlock(Location location) {
        Location clone = location.clone();

        // moving y up
        for (int i = 0; i < 5; i++) {
            if (clone.getBlock().isPassable()) {
                return clone;
            }

            clone.setY(clone.getY() + 1);
        }

        // moving y down
        for (int i = 0; i < 5; i++) {
            if (clone.getBlock().isPassable()) {
                clone.setY(clone.getY() + 1);
                return clone;
            }

            clone.setY(clone.getY() - 1);
        }

        return location;
    }
}