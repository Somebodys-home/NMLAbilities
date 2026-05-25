package io.github.NoOne.nMLAbilities.expertiseSystem.martialArtist;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageHelper;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityEffects;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldownSystem.CooldownManager;
import io.github.NoOne.nMLAcrobatics.maneuvers.Maneuvers;
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

public class MartialArtistAbilityEffects {
    private static NMLAbilities nmlAbilities;
    private static ProfileManager profileManager;

    public MartialArtistAbilityEffects(NMLAbilities nmlAbilities) {
        this.nmlAbilities = nmlAbilities;
        profileManager = nmlAbilities.getProfileManager();
    }

    public static void dropKick(Player player) {
        Stats stats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();
        HashMap<DamageType, Double> physicalDamage = DamageHelper.convertPlayerStat2Damage(stats, "physicaldamage");
        Vector dropkickDirection = player.getLocation().getDirection().setY(0).normalize();
        double speed = Maneuvers.getSpeed(player) / 10;
        Vector dropKick = dropkickDirection.multiply(1.25 + (speed / 2)).setY(.5);
        HashSet<UUID> alreadyHitEntities = new HashSet<>();

        if (!player.isOnGround()) { // so the dropkick is about the same both in the air and the ground
            dropKick.multiply(.66).setY(.3);
        }

        EnergyManager.useEnergy(player, 15);
        CooldownManager.putOnHardCooldown(player, 1.5);
        AttackCooldownSystem.setOrPauseAttackCooldown(player, 1.5);
        player.setVelocity(dropKick);
        player.playSound(player, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);

        // dropkick loop
        new BukkitRunnable() {
            int groundGracePeriod = 5;
            boolean inHitStop = false;
            boolean hitstopGracePeriod = false; // to chain hitstops
            double incomingSpeed = Maneuvers.getSpeed(player) / 10;
            double damageMultiplier = Math.clamp(incomingSpeed, 1, 3);
            HashMap<DamageType, Double> finalPhysicalDamage = DamageHelper.multiplyDamageMap(physicalDamage, damageMultiplier);

            @Override
            public void run() {
                Location baseLocation = player.getLocation().add(0, 1, 0);
                Location hitbox = baseLocation.clone().add(dropkickDirection);
                Collection<Entity> hitEntities = player.getWorld().getNearbyEntities(hitbox, 1, 2, 1);
                Location behind = baseLocation.clone().subtract(baseLocation.getDirection().setY(0).normalize().multiply(0.5));
                Vector rebound = new Vector(0, .4, 0);

                groundGracePeriod--;
                player.getWorld().spawnParticle(Particle.SNOWFLAKE, behind, 10, .15, .15, .15, 0);

                // hit entities filtering
                hitEntities.removeIf(entity -> !(entity instanceof LivingEntity) || entity.isDead() || alreadyHitEntities.contains(entity.getUniqueId()) ||
                                    entity.hasMetadata("hologram"));
                hitEntities.remove(player);

                // stop if the player hits the ground
                if (groundGracePeriod <= 0 && player.isOnGround()) {
                    cancel();
                    return;
                }

                // update speed / damage
                if (!hitstopGracePeriod) {
                    incomingSpeed = Maneuvers.getSpeed(player) / 10;
                    damageMultiplier = Math.clamp(incomingSpeed, 1, 3);
                    finalPhysicalDamage = DamageHelper.multiplyDamageMap(physicalDamage, damageMultiplier);
                }

                // you've hit something
                if (!hitEntities.isEmpty() && !inHitStop) {
                    if (damageMultiplier < 2.5) { // low velocity effect
                        player.setVelocity(rebound);
                        player.playSound(player, Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, 2f, 1f);

                        // hit effect
                        AbilityEffects.verticalParticleCircleFacingEntity(
                                new Particle.DustOptions(Color.fromRGB(255, 255, 255), 1.0F),
                                player,
                                1,
                                30,
                                1.5
                        );

                        // damage and knockback
                        for (Entity hitEntity : hitEntities) {
                            Vector knockback = hitEntity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(.5 + damageMultiplier).setY(.25);

                            Bukkit.getPluginManager().callEvent(new CustomDamageEvent((LivingEntity) hitEntity, player, finalPhysicalDamage));
                            hitEntity.setVelocity(knockback);
                        }

                        cancel();
                    } else { // high velocity effect
                        Vector restoredVelocity = player.getVelocity();
                        inHitStop = true;
                        hitstopGracePeriod = true;

                        for (Entity hitEntity : hitEntities) {
                            Vector knockback = hitEntity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(.5 + damageMultiplier).setY(.25);
                            Location hitEntityLocation = hitEntity.getLocation().add(knockback.clone().normalize());// capture BEFORE creating the inner runnable

                            alreadyHitEntities.add(hitEntity.getUniqueId());
                            player.playSound(player, Sound.BLOCK_ANVIL_PLACE, 1f, 2f);
                            hitEntity.teleport(hitEntityLocation);
                            AttackCooldownSystem.setOrPauseAttackCooldown(player, .66);

                            // hitstop
                            new BukkitRunnable() {
                                Location prevLocation = player.getLocation();
                                int timer = 14;

                                @Override
                                public void run() {
                                    player.teleport(prevLocation);
                                    hitEntity.teleport(hitEntityLocation);
                                    AbilityEffects.verticalParticleCircleBetweenEntities(
                                            new Particle.DustOptions(Color.fromRGB(230, 185, 9), 1.0F),
                                            player,
                                            hitEntity,
                                            1.25,
                                            30
                                    );

                                    timer--;

                                    if (timer == 0) {
                                        player.setVelocity(restoredVelocity);
                                        hitEntity.setVelocity(knockback);
                                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent((LivingEntity) hitEntity, player, finalPhysicalDamage));
                                        player.playSound(player, Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, .5f);
                                        inHitStop = false;

                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                hitstopGracePeriod = false;
                                            }
                                        }.runTaskLater(nmlAbilities, 2);

                                        cancel();
                                    }
                                }
                            }.runTaskTimer(nmlAbilities, 0, 1);
                        }
                    }
                }
            }
        }.runTaskTimer(nmlAbilities, 0, 1);
    }
}