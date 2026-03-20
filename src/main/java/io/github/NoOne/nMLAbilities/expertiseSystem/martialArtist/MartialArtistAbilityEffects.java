package io.github.NoOne.nMLAbilities.expertiseSystem.martialArtist;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageHelper;
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

    public static void tenHitCombo(Player player) {
        player.setMetadata("falling", new FixedMetadataValue(nmlAbilities, true));

        HashSet<UUID> hitEntityUUIDs = new HashSet<>();
        HashMap<DamageType, Double> damageStats = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStats2Damage(
                profileManager.getPlayerProfile(player.getUniqueId()).getStats()), .25);
        final boolean[] comboBroken = {false};

        EnergyManager.useEnergy(player, 10);
        CooldownManager.putOnHardCooldown(player, .7);
        AttackCooldownSystem.pauseAttackCooldown(player);

        // punch 1
        dashUntilCollision(player, 2, 5, new BukkitRunnable() {
            @Override
            public void run() {
                Location baseLocation = player.getLocation().add(0, 1.5, 0);
                Vector forward = player.getLocation().getDirection().normalize().multiply(2);
                Location punch = baseLocation.clone().add(forward);

                player.swingMainHand();
                player.getWorld().spawnParticle(Particle.CRIT, punch, 100, 0.25, 0.25, 0.25);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);
                CooldownManager.putOnHardCooldown(player, .5);

                for (Entity entity : player.getWorld().getNearbyEntities(punch, 1.5, 2, 1.5)) {
                    if (entity != player) {
                        hitEntityUUIDs.add(entity.getUniqueId());
                    }
                }

                for (UUID uuid : hitEntityUUIDs) {
                    if (Bukkit.getEntity(uuid) instanceof LivingEntity livingEntity) {
                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damageStats));
                        livingEntity.setNoDamageTicks(0);
                    }
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (hitEntityUUIDs.isEmpty()) {
                            AttackCooldownSystem.resumeAttackCooldown(player);
                            CooldownManager.removeHardCooldown(player);
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
                    AttackCooldownSystem.resumeAttackCooldown(player);
                    cancel();
                    return;
                }

                hitEntityUUIDs.clear();
                dashUntilCollision(player, 2, 5, new BukkitRunnable() {
                    @Override
                    public void run() {
                        EnergyManager.useEnergy(player, 10);

                        Location baseLocation = player.getLocation().add(0, 1.5, 0);
                        Vector forward = player.getLocation().getDirection().normalize().multiply(2);
                        Location punch = baseLocation.clone().add(forward);

                        player.swingOffHand();
                        player.getWorld().spawnParticle(Particle.CRIT, punch, 100, 0.25, 0.25, 0.25);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);
                        CooldownManager.putOnHardCooldown(player, .5);

                        for (Entity entity : player.getWorld().getNearbyEntities(punch, 1.5, 2, 1.5)) {
                            if (entity != player) {
                                hitEntityUUIDs.add(entity.getUniqueId());
                            }
                        }

                        for (UUID uuid : hitEntityUUIDs) {
                            if (Bukkit.getEntity(uuid) instanceof LivingEntity livingEntity) {
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damageStats));
                                livingEntity.setNoDamageTicks(0);
                            }
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (hitEntityUUIDs.isEmpty()) {
                                    AttackCooldownSystem.resumeAttackCooldown(player);
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
                    AttackCooldownSystem.resumeAttackCooldown(player);
                    cancel();
                    return;
                }

                hitEntityUUIDs.clear();

                dashUntilCollision(player, 2, 5, new BukkitRunnable() {
                    @Override
                    public void run() {
                        EnergyManager.useEnergy(player, 10);

                        Location baseLocation = player.getLocation().add(0, 1.5, 0);
                        Vector forward = player.getLocation().getDirection().normalize().multiply(2);
                        Location punch = baseLocation.clone().add(forward);

                        player.swingMainHand();
                        player.getWorld().spawnParticle(Particle.CRIT, punch, 100, 0.25, 0.25, 0.25);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);
                        CooldownManager.putOnHardCooldown(player, .5);

                        for (Entity entity : player.getWorld().getNearbyEntities(punch, 1.5, 2, 1.5)) {
                            if (entity != player) {
                                hitEntityUUIDs.add(entity.getUniqueId());
                            }
                        }

                        for (UUID uuid : hitEntityUUIDs) {
                            if (Bukkit.getEntity(uuid) instanceof LivingEntity livingEntity) {
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damageStats));
                                livingEntity.setNoDamageTicks(0);
                            }
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (hitEntityUUIDs.isEmpty()) {
                                    AttackCooldownSystem.resumeAttackCooldown(player);
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
                    AttackCooldownSystem.resumeAttackCooldown(player);
                    cancel();
                    return;
                }

                hitEntityUUIDs.clear();

                dashUntilCollision(player, 2, 5, new BukkitRunnable() {
                    @Override
                    public void run() {
                        EnergyManager.useEnergy(player, 10);

                        Vector jump = new Vector(0, 1, 0);
                        player.setVelocity(jump);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);

                        Location baseLocation = player.getLocation().add(0, 4, 0);
                        Vector forward = player.getLocation().getDirection().normalize().multiply(2);
                        Location kick = baseLocation.clone().add(forward);

                        player.getWorld().spawnParticle(Particle.CRIT, kick, 150, 0.15, 1, 0.15);
                        CooldownManager.putOnHardCooldown(player, .5);

                        for (Entity entity : player.getWorld().getNearbyEntities(kick, 1.5, 2, 1.5)) {
                            if (entity != player) {
                                hitEntityUUIDs.add(entity.getUniqueId());
                            }
                        }

                        for (UUID uuid : hitEntityUUIDs) {
                            if (Bukkit.getEntity(uuid) instanceof LivingEntity livingEntity) {
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damageStats));
                                livingEntity.setNoDamageTicks(0);
                                livingEntity.setVelocity(jump); // knockback
                            }
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (hitEntityUUIDs.isEmpty()) {
                                    AttackCooldownSystem.resumeAttackCooldown(player);
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
                    AttackCooldownSystem.resumeAttackCooldown(player);
                    cancel();
                    return;
                }

                hitEntityUUIDs.clear();

                dashUntilCollision(player, 2, 5, new BukkitRunnable() {
                    @Override
                    public void run() {
                        EnergyManager.useEnergy(player, 10);

                        Vector slam = new Vector(0, -1, 0);
                        player.setVelocity(slam);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);

                        Location baseLocation = player.getLocation().add(0, 4, 0);
                        Vector forward = player.getLocation().getDirection().normalize().multiply(2);
                        Location kick = baseLocation.clone().add(forward);

                        player.getWorld().spawnParticle(Particle.CRIT, kick, 150, 0.15, 4, 0.15);
                        CooldownManager.putOnHardCooldown(player, .5);

                        for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), 2, 4, 2)) {
                            if (entity != player) {
                                hitEntityUUIDs.add(entity.getUniqueId());
                            }
                        }

                        for (UUID uuid : hitEntityUUIDs) {
                            if (Bukkit.getEntity(uuid) instanceof LivingEntity livingEntity) {
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damageStats));
                                livingEntity.setNoDamageTicks(0);
                                livingEntity.setVelocity(slam); // knockback
                            }
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (hitEntityUUIDs.isEmpty()) {
                                    AttackCooldownSystem.resumeAttackCooldown(player);
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
                    AttackCooldownSystem.resumeAttackCooldown(player);
                    cancel();
                    return;
                }

                hitEntityUUIDs.clear();

                dashUntilCollision(player, 2, 5, new BukkitRunnable() {
                    @Override
                    public void run() {
                        EnergyManager.useEnergy(player, 10);

                        Location baseLocation = player.getLocation().add(0, 1.5, 0);
                        Vector forward = player.getLocation().getDirection().normalize().multiply(2);
                        Location kick = baseLocation.clone().add(forward);

                        Vector tinyDashDirection = player.getLocation().getDirection().normalize();
                        Vector tinyDash = tinyDashDirection.clone().multiply(.5).setY(0);

                        player.setVelocity(tinyDash);
                        player.getWorld().spawnParticle(Particle.CRIT, kick, 100, 0.15, 0.15, 0.15);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);
                        CooldownManager.putOnHardCooldown(player, .5);

                        for (Entity entity : player.getWorld().getNearbyEntities(kick, 1.5, 2, 1.5)) {
                            if (entity != player) {
                                hitEntityUUIDs.add(entity.getUniqueId());
                            }
                        }

                        for (UUID uuid : hitEntityUUIDs) {
                            if (Bukkit.getEntity(uuid) instanceof LivingEntity livingEntity) {
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damageStats));
                                livingEntity.setNoDamageTicks(0);

                                Vector knockback = livingEntity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().setY(.2);
                                livingEntity.setVelocity(knockback);
                            }
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (hitEntityUUIDs.isEmpty()) {
                                    AttackCooldownSystem.resumeAttackCooldown(player);
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
                    AttackCooldownSystem.resumeAttackCooldown(player);
                    cancel();
                    return;
                }

                hitEntityUUIDs.clear();
                dashUntilCollision(player, 2, 5, new BukkitRunnable() {
                    @Override
                    public void run() {
                        EnergyManager.useEnergy(player, 10);

                        Location baseLocation = player.getLocation().add(0, 1.5, 0);
                        Vector forward = player.getLocation().getDirection().normalize().multiply(2);
                        Location punch = baseLocation.clone().add(forward);
                        Vector tinyJump = player.getLocation().getDirection().multiply(.5).setY(.25);

                        player.swingMainHand();
                        player.setVelocity(tinyJump);
                        player.getWorld().spawnParticle(Particle.CRIT, punch, 100, 0.25, 0.25, 0.25);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
                        CooldownManager.putOnHardCooldown(player, .5);

                        for (int i = 0; i < 8; i++) {
                            double angle = 2 * Math.PI * i / 8;
                            double x = Math.cos(angle);
                            double z = Math.sin(angle);

                            Location particleLocation = player.getLocation().clone().add(x, 1, z);
                            player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleLocation, 1, 0, 0, 0, 0);
                        }

                        for (Entity entity : player.getWorld().getNearbyEntities(punch, 1.5, 2, 1.5)) {
                            if (entity != player) {
                                hitEntityUUIDs.add(entity.getUniqueId());
                            }
                        }

                        for (UUID uuid : hitEntityUUIDs) {
                            if (Bukkit.getEntity(uuid) instanceof LivingEntity livingEntity) {
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damageStats));
                                livingEntity.setNoDamageTicks(0);

                                Vector knockback = livingEntity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().setY(.1);
                                livingEntity.setVelocity(knockback);
                            }
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (hitEntityUUIDs.isEmpty()) {
                                    AttackCooldownSystem.resumeAttackCooldown(player);
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
                    AttackCooldownSystem.resumeAttackCooldown(player);
                    cancel();
                    return;
                }

                hitEntityUUIDs.clear();

                dashUntilCollision(player, 2, 5, new BukkitRunnable() {
                    @Override
                    public void run() {
                        EnergyManager.useEnergy(player, 10);

                        Location baseLocation = player.getLocation().add(0, 1.5, 0);
                        Vector forward = player.getLocation().getDirection().normalize().multiply(2);
                        Location punch = baseLocation.clone().add(forward);
                        Vector tinyJump = player.getLocation().getDirection().multiply(.5).setY(.25);

                        player.swingOffHand();
                        player.setVelocity(tinyJump);
                        player.getWorld().spawnParticle(Particle.CRIT, punch, 100, 0.25, 0.25, 0.25);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
                        CooldownManager.putOnHardCooldown(player, .5);

                        for (int i = 0; i < 8; i++) {
                            double angle = 2 * Math.PI * i / 8;
                            double x = Math.cos(angle);
                            double z = Math.sin(angle);

                            Location particleLocation = player.getLocation().clone().add(x, 1, z);
                            player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleLocation, 1, 0, 0, 0, 0);
                        }

                        for (Entity entity : player.getWorld().getNearbyEntities(punch, 1.5, 2, 1.5)) {
                            if (entity != player) {
                                hitEntityUUIDs.add(entity.getUniqueId());
                            }
                        }

                        for (UUID uuid : hitEntityUUIDs) {
                            if (Bukkit.getEntity(uuid) instanceof LivingEntity livingEntity) {
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damageStats));
                                livingEntity.setNoDamageTicks(0);

                                Vector knockback = livingEntity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().setY(.1);
                                livingEntity.setVelocity(knockback);
                            }
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (hitEntityUUIDs.isEmpty()) {
                                    AttackCooldownSystem.resumeAttackCooldown(player);
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
                    AttackCooldownSystem.resumeAttackCooldown(player);
                    cancel();
                    return;
                }

                hitEntityUUIDs.clear();

                dashUntilCollision(player, 2, 5, new BukkitRunnable() {
                    @Override
                    public void run() {
                        EnergyManager.useEnergy(player, 10);

                        Location baseLocation = player.getLocation().add(0, 1.5, 0);
                        Vector forward = player.getLocation().getDirection().normalize().multiply(2);
                        Location punch = baseLocation.clone().add(forward);
                        Vector tinyJump = player.getLocation().getDirection().multiply(.5).setY(.25);

                        player.swingMainHand();
                        player.setVelocity(tinyJump);
                        player.getWorld().spawnParticle(Particle.CRIT, punch, 100, 0.25, 0.25, 0.25);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
                        CooldownManager.putOnHardCooldown(player, .5);

                        for (int i = 0; i < 8; i++) {
                            double angle = 2 * Math.PI * i / 8;
                            double x = Math.cos(angle);
                            double z = Math.sin(angle);

                            Location particleLocation = player.getLocation().clone().add(x, 1, z);
                            player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleLocation, 1, 0, 0, 0, 0);
                        }

                        for (Entity entity : player.getWorld().getNearbyEntities(punch, 1.5, 2, 1.5)) {
                            if (entity != player) {
                                hitEntityUUIDs.add(entity.getUniqueId());
                            }
                        }

                        for (UUID uuid : hitEntityUUIDs) {
                            if (Bukkit.getEntity(uuid) instanceof LivingEntity livingEntity) {
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damageStats));
                                livingEntity.setNoDamageTicks(0);

                                Vector knockback = livingEntity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().setY(.1);
                                livingEntity.setVelocity(knockback);
                            }
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (hitEntityUUIDs.isEmpty()) {
                                    AttackCooldownSystem.resumeAttackCooldown(player);
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
            HashMap<DamageType, Double> damageStats = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStats2Damage(
                    profileManager.getPlayerProfile(player.getUniqueId()).getStats()), .75);

            @Override
            public void run() {
                if (comboBroken[0]) {
                    AttackCooldownSystem.resumeAttackCooldown(player);
                    cancel();
                    return;
                }

                player.setMetadata("falling", new FixedMetadataValue(nmlAbilities, true));
                hitEntityUUIDs.clear();
                CooldownManager.putOnHardCooldown(player, 1);

                dashUntilCollision(player, 2, 5, new BukkitRunnable() {
                    @Override
                    public void run() {
                        EnergyManager.useEnergy(player, 10);

                        Vector jump = player.getLocation().getDirection().multiply(1.5).setY(1.5);
                        Location baseLocation = player.getLocation().add(0, 4, 0);
                        Vector forward = player.getLocation().getDirection().normalize().multiply(2);
                        Location punch = baseLocation.clone().add(forward);

                        player.setVelocity(jump);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 3f, 1f);

                        // uppercut particles
                        new BukkitRunnable() {
                            int particleticks = 20;
                            int i = 0;
                            double angleStep = Math.PI / 20;

                            @Override
                            public void run() {
                                Location baseLocation = player.getLocation().clone().add(0, 1, 0);
                                Location trail = baseLocation.clone().add(player.getLocation().getDirection().normalize().multiply(-1));

                                for (double j = 0; j < 20; j++) {
                                    double radius = 1.5;
                                    double angle = (2 * (i * angleStep) + (j * 0.05)) - 180;
                                    double reverseAngle = angle - 180;
                                    double x = Math.cos(angle) * radius;
                                    double z = Math.sin(angle) * radius;
                                    double reverseX = Math.cos(reverseAngle) * radius;
                                    double reverseZ = Math.sin(reverseAngle) * radius;
                                    Location soulFireLocation = player.getLocation().clone().add(x, 2, z);
                                    Location fireLocation = player.getLocation().clone().add(reverseX,  2, reverseZ);

                                    player.getWorld().spawnParticle(Particle.SNOWFLAKE, trail, 10, .05, .05, .05, 0);
                                    player.getWorld().spawnParticle(Particle.FLAME, fireLocation, 30, 0, 0, 0, 0);
                                    player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, soulFireLocation, 30, 0, 0, 0, 0);
                                }

                                particleticks--;
                                i++;
                                if (particleticks == 0) cancel();
                            }
                        }.runTaskTimer(nmlAbilities, 0L, 1L);

                        for (Entity entity : player.getWorld().getNearbyEntities(punch, 1.5, 2, 1.5)) {
                            if (entity != player) {
                                hitEntityUUIDs.add(entity.getUniqueId());
                            }
                        }

                        for (UUID uuid : hitEntityUUIDs) {
                            if (Bukkit.getEntity(uuid) instanceof LivingEntity livingEntity) {
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damageStats));
                                livingEntity.setNoDamageTicks(0);
                                livingEntity.setVelocity(jump.multiply(2).setY(2)); // knockback
                            }
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                hitEntityUUIDs.clear();
                                AttackCooldownSystem.resumeAttackCooldown(player);
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
        CooldownManager.putOnHardCooldown(dasher, .7);
        
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