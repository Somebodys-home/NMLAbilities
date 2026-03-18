package io.github.NoOne.nMLAbilities.expertiseSystem.martialArtist;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageConverter;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldownSystem.CooldownManager;
import io.github.NoOne.nMLEnergySystem.EnergyManager;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLWeapons.AttackCooldownSystem;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class MartialArtistAbilityEffects {
    private static NMLAbilities nmlAbilities;
    private static ProfileManager profileManager;

    public MartialArtistAbilityEffects(NMLAbilities nmlAbilities) {
        this.nmlAbilities = nmlAbilities;
        profileManager = nmlAbilities.getProfileManager();
    }

    public static void tenHitCombo(Player user, int hotbarSlot) { // todo: fix this when
        user.setMetadata("falling", new FixedMetadataValue(nmlAbilities, true));

        HashSet<UUID> hitEntityUUIDs = new HashSet<>();
        HashMap<DamageType, Double> damageStats = DamageConverter.multiplyDamageMap(DamageConverter.convertPlayerStats2Damage(
                profileManager.getPlayerProfile(user.getUniqueId()).getStats()), .25);
        final boolean[] comboBroken = {false};

        CooldownManager.putAllOtherAbilitiesOnCooldown(user, 2, hotbarSlot);
        EnergyManager.useEnergy(user, 10);
        AttackCooldownSystem.pauseAttackCooldown(user);

        // punch 1
        dashUntilCollision(user, 2, 5, new BukkitRunnable() {
            @Override
            public void run() {
                Location baseLocation = user.getLocation().add(0, 1.5, 0);
                Vector forward = user.getLocation().getDirection().normalize().multiply(2);
                Location punch = baseLocation.clone().add(forward);

                user.swingMainHand();
                user.getWorld().spawnParticle(Particle.CRIT, punch, 100, 0.25, 0.25, 0.25);
                user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);

                for (Entity entity : user.getWorld().getNearbyEntities(punch, 1.5, 2, 1.5)) {
                    if (entity != user) {
                        hitEntityUUIDs.add(entity.getUniqueId());
                    }
                }

                for (UUID uuid : hitEntityUUIDs) {
                    if (Bukkit.getEntity(uuid) instanceof LivingEntity livingEntity) {
                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, user, damageStats));
                        livingEntity.setNoDamageTicks(0);
                    }
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (hitEntityUUIDs.isEmpty()) {
                            AttackCooldownSystem.resumeAttackCooldown(user);
                            comboBroken[0] = true;
                        }
                    }
                }.runTaskLater(nmlAbilities, 1L);
            }
        });

        // punch 2
        new BukkitRunnable() {
            @Override
            public void run() {
                if (comboBroken[0]) {
                    AttackCooldownSystem.resumeAttackCooldown(user);
                    cancel();
                    return;
                }

                hitEntityUUIDs.clear();
                CooldownManager.putAllOtherAbilitiesOnCooldown(user, .5, hotbarSlot);

                dashUntilCollision(user, 2, 5, new BukkitRunnable() {
                    @Override
                    public void run() {
                        EnergyManager.useEnergy(user, 10);

                        Location baseLocation = user.getLocation().add(0, 1.5, 0);
                        Vector forward = user.getLocation().getDirection().normalize().multiply(2);
                        Location punch = baseLocation.clone().add(forward);

                        user.swingOffHand();
                        user.getWorld().spawnParticle(Particle.CRIT, punch, 100, 0.25, 0.25, 0.25);
                        user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);

                        for (Entity entity : user.getWorld().getNearbyEntities(punch, 1.5, 2, 1.5)) {
                            if (entity != user) {
                                hitEntityUUIDs.add(entity.getUniqueId());
                            }
                        }

                        for (UUID uuid : hitEntityUUIDs) {
                            if (Bukkit.getEntity(uuid) instanceof LivingEntity livingEntity) {
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, user, damageStats));
                                livingEntity.setNoDamageTicks(0);
                            }
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (hitEntityUUIDs.isEmpty()) {
                                    AttackCooldownSystem.resumeAttackCooldown(user);
                                    comboBroken[0] = true;
                                }
                            }
                        }.runTaskLater(nmlAbilities, 1L);
                    }
                });
            }
        }.runTaskLater(nmlAbilities, 10L);

        // punch 3
        new BukkitRunnable() {
            @Override
            public void run() {
                if (comboBroken[0]) {
                    AttackCooldownSystem.resumeAttackCooldown(user);
                    cancel();
                    return;
                }

                hitEntityUUIDs.clear();
                CooldownManager.putAllOtherAbilitiesOnCooldown(user, .5, hotbarSlot);

                dashUntilCollision(user, 2, 5, new BukkitRunnable() {
                    @Override
                    public void run() {
                        EnergyManager.useEnergy(user, 10);

                        Location baseLocation = user.getLocation().add(0, 1.5, 0);
                        Vector forward = user.getLocation().getDirection().normalize().multiply(2);
                        Location punch = baseLocation.clone().add(forward);

                        user.swingMainHand();
                        user.getWorld().spawnParticle(Particle.CRIT, punch, 100, 0.25, 0.25, 0.25);
                        user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);

                        for (Entity entity : user.getWorld().getNearbyEntities(punch, 1.5, 2, 1.5)) {
                            if (entity != user) {
                                hitEntityUUIDs.add(entity.getUniqueId());
                            }
                        }

                        for (UUID uuid : hitEntityUUIDs) {
                            if (Bukkit.getEntity(uuid) instanceof LivingEntity livingEntity) {
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, user, damageStats));
                                livingEntity.setNoDamageTicks(0);
                            }
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (hitEntityUUIDs.isEmpty()) {
                                    AttackCooldownSystem.resumeAttackCooldown(user);
                                    comboBroken[0] = true;
                                }
                            }
                        }.runTaskLater(nmlAbilities, 1L);
                    }
                });
            }
        }.runTaskLater(nmlAbilities, 20L);

        // kick 4
        new BukkitRunnable() {
            @Override
            public void run() {
                if (comboBroken[0]) {
                    AttackCooldownSystem.resumeAttackCooldown(user);
                    cancel();
                    return;
                }

                hitEntityUUIDs.clear();
                CooldownManager.putAllOtherAbilitiesOnCooldown(user, .5, hotbarSlot);

                dashUntilCollision(user, 2, 5, new BukkitRunnable() {
                    @Override
                    public void run() {
                        EnergyManager.useEnergy(user, 10);

                        Vector jump = new Vector(0, 1, 0);
                        user.setVelocity(jump);
                        user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
                        user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);

                        Location baseLocation = user.getLocation().add(0, 4, 0);
                        Vector forward = user.getLocation().getDirection().normalize().multiply(2);
                        Location punch = baseLocation.clone().add(forward);

                        user.getWorld().spawnParticle(Particle.CRIT, punch, 150, 0.15, 1, 0.15);

                        for (Entity entity : user.getWorld().getNearbyEntities(punch, 1.5, 2, 1.5)) {
                            if (entity != user) {
                                hitEntityUUIDs.add(entity.getUniqueId());
                            }
                        }

                        for (UUID uuid : hitEntityUUIDs) {
                            if (Bukkit.getEntity(uuid) instanceof LivingEntity livingEntity) {
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, user, damageStats));
                                livingEntity.setNoDamageTicks(0);
                                livingEntity.setVelocity(jump); // knockback
                            }
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (hitEntityUUIDs.isEmpty()) {
                                    AttackCooldownSystem.resumeAttackCooldown(user);
                                    comboBroken[0] = true;
                                }
                            }
                        }.runTaskLater(nmlAbilities, 1L);
                    }
                });
            }
        }.runTaskLater(nmlAbilities, 30L);

        // kick 5
        new BukkitRunnable() {
            @Override
            public void run() {
                if (comboBroken[0]) {
                    AttackCooldownSystem.resumeAttackCooldown(user);
                    cancel();
                    return;
                }

                hitEntityUUIDs.clear();
                CooldownManager.putAllOtherAbilitiesOnCooldown(user, .5, hotbarSlot);

                dashUntilCollision(user, 2, 5, new BukkitRunnable() {
                    @Override
                    public void run() {
                        EnergyManager.useEnergy(user, 10);

                        Vector slam = new Vector(0, -1, 0);
                        user.setVelocity(slam);
                        user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
                        user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);

                        Location baseLocation = user.getLocation().add(0, 4, 0);
                        Vector forward = user.getLocation().getDirection().normalize().multiply(2);
                        Location punch = baseLocation.clone().add(forward);

                        user.getWorld().spawnParticle(Particle.CRIT, punch, 150, 0.15, 4, 0.15);

                        for (Entity entity : user.getWorld().getNearbyEntities(user.getLocation(), 2, 4, 2)) {
                            if (entity != user) {
                                hitEntityUUIDs.add(entity.getUniqueId());
                            }
                        }

                        for (UUID uuid : hitEntityUUIDs) {
                            if (Bukkit.getEntity(uuid) instanceof LivingEntity livingEntity) {
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, user, damageStats));
                                livingEntity.setNoDamageTicks(0);
                                livingEntity.setVelocity(slam); // knockback
                            }
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (hitEntityUUIDs.isEmpty()) {
                                    AttackCooldownSystem.resumeAttackCooldown(user);
                                    comboBroken[0] = true;
                                }
                            }
                        }.runTaskLater(nmlAbilities, 1L);
                    }
                });
            }
        }.runTaskLater(nmlAbilities, 40L);

        // kick 6
        new BukkitRunnable() {
            @Override
            public void run() {
                if (comboBroken[0]) {
                    AttackCooldownSystem.resumeAttackCooldown(user);
                    cancel();
                    return;
                }

                hitEntityUUIDs.clear();
                CooldownManager.putAllOtherAbilitiesOnCooldown(user, .5, hotbarSlot);

                dashUntilCollision(user, 2, 5, new BukkitRunnable() {
                    @Override
                    public void run() {
                        EnergyManager.useEnergy(user, 10);

                        Location baseLocation = user.getLocation().add(0, 1.5, 0);
                        Vector forward = user.getLocation().getDirection().normalize().multiply(2);
                        Location punch = baseLocation.clone().add(forward);

                        Vector tinyDashDirection = user.getLocation().getDirection().normalize();
                        Vector tinyDash = tinyDashDirection.clone().multiply(.5).setY(0);
                        user.setVelocity(tinyDash);

                        user.getWorld().spawnParticle(Particle.CRIT, punch, 100, 0.15, 0.15, 0.15);
                        user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);

                        for (Entity entity : user.getWorld().getNearbyEntities(punch, 1.5, 2, 1.5)) {
                            if (entity != user) {
                                hitEntityUUIDs.add(entity.getUniqueId());
                            }
                        }

                        for (UUID uuid : hitEntityUUIDs) {
                            if (Bukkit.getEntity(uuid) instanceof LivingEntity livingEntity) {
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, user, damageStats));
                                livingEntity.setNoDamageTicks(0);

                                Vector knockback = livingEntity.getLocation().toVector().subtract(user.getLocation().toVector()).normalize().setY(.2);
                                livingEntity.setVelocity(knockback);
                            }
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (hitEntityUUIDs.isEmpty()) {
                                    AttackCooldownSystem.resumeAttackCooldown(user);
                                    comboBroken[0] = true;
                                }
                            }
                        }.runTaskLater(nmlAbilities, 1L);
                    }
                });
            }
        }.runTaskLater(nmlAbilities, 50L);

        // punch 7
        new BukkitRunnable() {
            @Override
            public void run() {
                if (comboBroken[0]) {
                    AttackCooldownSystem.resumeAttackCooldown(user);
                    cancel();
                    return;
                }

                hitEntityUUIDs.clear();
                CooldownManager.putAllOtherAbilitiesOnCooldown(user, .8, hotbarSlot);

                dashUntilCollision(user, 2, 5, new BukkitRunnable() {
                    @Override
                    public void run() {
                        EnergyManager.useEnergy(user, 10);

                        Location baseLocation = user.getLocation().add(0, 1.5, 0);
                        Vector forward = user.getLocation().getDirection().normalize().multiply(2);
                        Location punch = baseLocation.clone().add(forward);
                        Vector tinyJump = user.getLocation().getDirection().multiply(.5).setY(.25);

                        user.swingMainHand();
                        user.setVelocity(tinyJump);
                        user.getWorld().spawnParticle(Particle.CRIT, punch, 100, 0.25, 0.25, 0.25);
                        user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);
                        user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);

                        for (int i = 0; i < 8; i++) {
                            double angle = 2 * Math.PI * i / 8;
                            double x = Math.cos(angle);
                            double z = Math.sin(angle);

                            Location particleLocation = user.getLocation().clone().add(x, 1, z);
                            user.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleLocation, 1, 0, 0, 0, 0);
                        }

                        for (Entity entity : user.getWorld().getNearbyEntities(punch, 1.5, 2, 1.5)) {
                            if (entity != user) {
                                hitEntityUUIDs.add(entity.getUniqueId());
                            }
                        }

                        for (UUID uuid : hitEntityUUIDs) {
                            if (Bukkit.getEntity(uuid) instanceof LivingEntity livingEntity) {
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, user, damageStats));
                                livingEntity.setNoDamageTicks(0);

                                Vector knockback = livingEntity.getLocation().toVector().subtract(user.getLocation().toVector()).normalize().setY(.1);
                                livingEntity.setVelocity(knockback);
                            }
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (hitEntityUUIDs.isEmpty()) {
                                    AttackCooldownSystem.resumeAttackCooldown(user);
                                    comboBroken[0] = true;
                                }
                            }
                        }.runTaskLater(nmlAbilities, 1L);
                    }
                });
            }
        }.runTaskLater(nmlAbilities, 60L);

        // punch 8
        new BukkitRunnable() {
            @Override
            public void run() {
                if (comboBroken[0]) {
                    AttackCooldownSystem.resumeAttackCooldown(user);
                    cancel();
                    return;
                }

                hitEntityUUIDs.clear();
                CooldownManager.putAllOtherAbilitiesOnCooldown(user, .8, hotbarSlot);

                dashUntilCollision(user, 2, 5, new BukkitRunnable() {
                    @Override
                    public void run() {
                        EnergyManager.useEnergy(user, 10);

                        Location baseLocation = user.getLocation().add(0, 1.5, 0);
                        Vector forward = user.getLocation().getDirection().normalize().multiply(2);
                        Location punch = baseLocation.clone().add(forward);
                        Vector tinyJump = user.getLocation().getDirection().multiply(.5).setY(.25);

                        user.swingOffHand();
                        user.setVelocity(tinyJump);
                        user.getWorld().spawnParticle(Particle.CRIT, punch, 100, 0.25, 0.25, 0.25);
                        user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);
                        user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);

                        for (int i = 0; i < 8; i++) {
                            double angle = 2 * Math.PI * i / 8;
                            double x = Math.cos(angle);
                            double z = Math.sin(angle);

                            Location particleLocation = user.getLocation().clone().add(x, 1, z);
                            user.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleLocation, 1, 0, 0, 0, 0);
                        }

                        for (Entity entity : user.getWorld().getNearbyEntities(punch, 1.5, 2, 1.5)) {
                            if (entity != user) {
                                hitEntityUUIDs.add(entity.getUniqueId());
                            }
                        }

                        for (UUID uuid : hitEntityUUIDs) {
                            if (Bukkit.getEntity(uuid) instanceof LivingEntity livingEntity) {
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, user, damageStats));
                                livingEntity.setNoDamageTicks(0);

                                Vector knockback = livingEntity.getLocation().toVector().subtract(user.getLocation().toVector()).normalize().setY(.1);
                                livingEntity.setVelocity(knockback);
                            }
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (hitEntityUUIDs.isEmpty()) {
                                    AttackCooldownSystem.resumeAttackCooldown(user);
                                    comboBroken[0] = true;
                                }
                            }
                        }.runTaskLater(nmlAbilities, 1L);
                    }
                });
            }
        }.runTaskLater(nmlAbilities, 75L);

        // punch 9
        new BukkitRunnable() {
            @Override
            public void run() {
                if (comboBroken[0]) {
                    AttackCooldownSystem.resumeAttackCooldown(user);
                    cancel();
                    return;
                }

                hitEntityUUIDs.clear();
                CooldownManager.putAllOtherAbilitiesOnCooldown(user, .8, hotbarSlot);

                dashUntilCollision(user, 2, 5, new BukkitRunnable() {
                    @Override
                    public void run() {
                        EnergyManager.useEnergy(user, 10);

                        Location baseLocation = user.getLocation().add(0, 1.5, 0);
                        Vector forward = user.getLocation().getDirection().normalize().multiply(2);
                        Location punch = baseLocation.clone().add(forward);
                        Vector tinyJump = user.getLocation().getDirection().multiply(.5).setY(.25);

                        user.swingMainHand();
                        user.setVelocity(tinyJump);
                        user.getWorld().spawnParticle(Particle.CRIT, punch, 100, 0.25, 0.25, 0.25);
                        user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);
                        user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);

                        for (int i = 0; i < 8; i++) {
                            double angle = 2 * Math.PI * i / 8;
                            double x = Math.cos(angle);
                            double z = Math.sin(angle);

                            Location particleLocation = user.getLocation().clone().add(x, 1, z);
                            user.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleLocation, 1, 0, 0, 0, 0);
                        }

                        for (Entity entity : user.getWorld().getNearbyEntities(punch, 1.5, 2, 1.5)) {
                            if (entity != user) {
                                hitEntityUUIDs.add(entity.getUniqueId());
                            }
                        }

                        for (UUID uuid : hitEntityUUIDs) {
                            if (Bukkit.getEntity(uuid) instanceof LivingEntity livingEntity) {
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, user, damageStats));
                                livingEntity.setNoDamageTicks(0);

                                Vector knockback = livingEntity.getLocation().toVector().subtract(user.getLocation().toVector()).normalize().setY(.1);
                                livingEntity.setVelocity(knockback);
                            }
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (hitEntityUUIDs.isEmpty()) {
                                    AttackCooldownSystem.resumeAttackCooldown(user);
                                    comboBroken[0] = true;
                                }
                            }
                        }.runTaskLater(nmlAbilities, 1L);
                    }
                });
            }
        }.runTaskLater(nmlAbilities, 90L);

        // uppercut 10
        new BukkitRunnable() {
            HashMap<DamageType, Double> damageStats = DamageConverter.multiplyDamageMap(DamageConverter.convertPlayerStats2Damage(
                    profileManager.getPlayerProfile(user.getUniqueId()).getStats()), .75);

            @Override
            public void run() {
                if (comboBroken[0]) {
                    AttackCooldownSystem.resumeAttackCooldown(user);
                    cancel();
                    return;
                }

                user.setMetadata("falling", new FixedMetadataValue(nmlAbilities, true));
                hitEntityUUIDs.clear();
                CooldownManager.putAllOtherAbilitiesOnCooldown(user, .8, hotbarSlot);

                dashUntilCollision(user, 2, 5, new BukkitRunnable() {
                    @Override
                    public void run() {
                        EnergyManager.useEnergy(user, 10);

                        Vector jump = user.getLocation().getDirection().multiply(1.5).setY(1.5);
                        Location baseLocation = user.getLocation().add(0, 4, 0);
                        Vector forward = user.getLocation().getDirection().normalize().multiply(2);
                        Location punch = baseLocation.clone().add(forward);

                        user.setVelocity(jump);
                        user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
                        user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);
                        user.playSound(user.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 3f, 1f);

                        // uppercut particles
                        new BukkitRunnable() {
                            int particleticks = 20;
                            int i = 0;
                            double angleStep = Math.PI / 20;

                            @Override
                            public void run() {
                                Location baseLocation = user.getLocation().clone().add(0, 1, 0);
                                Location trail = baseLocation.clone().add(user.getLocation().getDirection().normalize().multiply(-1));

                                for (double j = 0; j < 20; j++) {
                                    double radius = 1.5;
                                    double angle = (2 * (i * angleStep) + (j * 0.05)) - 180;
                                    double reverseAngle = angle - 180;
                                    double x = Math.cos(angle) * radius;
                                    double z = Math.sin(angle) * radius;
                                    double reverseX = Math.cos(reverseAngle) * radius;
                                    double reverseZ = Math.sin(reverseAngle) * radius;
                                    Location soulFireLocation = user.getLocation().clone().add(x, 2, z);
                                    Location fireLocation = user.getLocation().clone().add(reverseX,  2, reverseZ);

                                    user.getWorld().spawnParticle(Particle.SNOWFLAKE, trail, 10, .05, .05, .05, 0);
                                    user.getWorld().spawnParticle(Particle.FLAME, fireLocation, 30, 0, 0, 0, 0);
                                    user.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, soulFireLocation, 30, 0, 0, 0, 0);
                                }

                                particleticks--;
                                i++;
                                if (particleticks == 0) cancel();
                            }
                        }.runTaskTimer(nmlAbilities, 0L, 1L);

                        for (Entity entity : user.getWorld().getNearbyEntities(punch, 1.5, 2, 1.5)) {
                            if (entity != user) {
                                hitEntityUUIDs.add(entity.getUniqueId());
                            }
                        }

                        for (UUID uuid : hitEntityUUIDs) {
                            if (Bukkit.getEntity(uuid) instanceof LivingEntity livingEntity) {
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, user, damageStats));
                                livingEntity.setNoDamageTicks(0);
                                livingEntity.setVelocity(jump.multiply(2).setY(2)); // knockback
                            }
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                hitEntityUUIDs.clear();
                                AttackCooldownSystem.resumeAttackCooldown(user);
                                comboBroken[0] = true;
                            }
                        }.runTaskLater(nmlAbilities, 1L);
                    }
                });
            }
        }.runTaskLater(nmlAbilities, 105L);
    }

    private static void dashUntilCollision(Player dasher, double velocity, int fallbackTicks, BukkitRunnable onFinish) {
        Vector dashDirection = dasher.getLocation().getDirection().normalize();
        Vector dash = dashDirection.clone().multiply(velocity).setY(0);
        HashSet<UUID> hitEntityUUIDs = new HashSet<>();

        dasher.setVelocity(dash);
        dasher.getAttribute(Attribute.STEP_HEIGHT).setBaseValue(1);
        
        new BukkitRunnable() {
            int ticks = 0;
            boolean triggered = false;

            @Override
            public void run() {
                Location baseLocation = dasher.getLocation().add(0, 1, 0);
                Location hitbox = baseLocation.clone().add(dashDirection);

                for (Entity entity : dasher.getWorld().getNearbyEntities(hitbox, 1.5, 2, 1.5)) {
                    if (entity != dasher && entity instanceof LivingEntity livingEntity) {
                        hitEntityUUIDs.add(entity.getUniqueId());
                        dasher.setVelocity(new Vector(0, 0, 0));
                        triggered = true;
                    }
                }

                if (!triggered && ticks >= fallbackTicks) {
                    dasher.setVelocity(new Vector(0, 0, 0));
                    triggered = true;
                }

                if (triggered) {
                    cancel();
                    onFinish.runTaskLater(nmlAbilities, 1L);
                    dasher.getAttribute(Attribute.STEP_HEIGHT).setBaseValue(.6);
                }

                ticks++;
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);
    }
}