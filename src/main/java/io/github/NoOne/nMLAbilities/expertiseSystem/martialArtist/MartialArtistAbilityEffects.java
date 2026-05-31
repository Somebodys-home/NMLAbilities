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
        Vector dropKick = dropkickDirection.multiply(1.25 + (speed / 2)).setY(.4);
        HashSet<UUID> alreadyHitEntities = new HashSet<>();

        if (!player.isOnGround()) { // so the dropkick is about the same both in the air and the ground
            dropKick = player.getVelocity().add(dropkickDirection.multiply(.75).setY(.3)).setY(.3);
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
            int hitstopGracePeriodTimer = 0; // the time that you have to chain hitstops
            double incomingSpeed = Maneuvers.getSpeed(player) / 10;

            @Override
            public void run() {
                Location baseLocation = player.getLocation().add(0, 1, 0);
                Location hitbox = baseLocation.clone().add(dropkickDirection);
                Collection<Entity> hitEntities = player.getWorld().getNearbyEntities(hitbox, 1, 2, 1);
                Location behind = baseLocation.clone().subtract(baseLocation.getDirection().setY(0).normalize().multiply(0.5));

                groundGracePeriod--;
                hitstopGracePeriodTimer--;
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

                // update speed when not in hitstop or its grace period
                if (hitstopGracePeriodTimer <= 0 && !inHitStop) {
                    incomingSpeed = Maneuvers.getSpeed(player) / 10;
                }

                /// you've hit something
                if (!hitEntities.isEmpty() && !inHitStop) {
                    double damageMultiplier = Math.clamp(incomingSpeed * .85, 1, 3);
                    HashMap<DamageType, Double> finalPhysicalDamage = DamageHelper.multiplyDamageMap(physicalDamage, damageMultiplier);

                    if (damageMultiplier < 2.5) { // low velocity effect
                        player.setVelocity(new Vector(0, .4, 0));
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
                        player.playSound(player, Sound.BLOCK_ANVIL_PLACE, 1f, 2f);
                        AttackCooldownSystem.setOrPauseAttackCooldown(player, .66);
                        inHitStop = true;

                        // saving entities outside the hitbox to be paused during hitstop and what velocity they had beforehand
                        Collection<Entity> collection = player.getWorld().getNearbyEntities(hitbox, 5, 5, 5);
                        HashMap<Entity, Location> nearbyEntities = new HashMap<>();
                        HashMap<Entity, Vector> prevVelocities = new HashMap<>();

                        collection.removeIf(hitEntities::contains);

                        for (Entity hitEntity : collection) {
                            nearbyEntities.put(hitEntity, hitEntity.getLocation());
                            prevVelocities.put(hitEntity, hitEntity.getVelocity().clone());
                        }

                        // saving the knockback that every hit entity will take and where they'll be frozen to during hitstop
                        HashMap<Entity, Vector> knockbacks = new HashMap<>();
                        HashMap<Entity, Location> freezeLocations = new HashMap<>();

                        for (Entity hitEntity : hitEntities) {
                            Vector knockback = hitEntity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(.5 + damageMultiplier)
                                    .setY(.25);
                            Location freezeLocation = hitEntity.getLocation().add(knockback.clone().normalize()); // as if they took 1 block of kb

                            knockbacks.put(hitEntity, knockback);
                            freezeLocations.put(hitEntity, freezeLocation);
                        }

                        /// hitstop effect
                        new BukkitRunnable() {
                            int timer = 14;

                            @Override
                            public void run() {
                                timer--;
                                player.setVelocity(new Vector());

                                for (Entity hitEntity : hitEntities) { // for every hit entity
                                    hitEntity.teleport(freezeLocations.get(hitEntity)); // freeze them in place
                                    AbilityEffects.verticalParticleCircleBetweenEntities( // make the hit effect
                                            new Particle.DustOptions(Color.fromRGB(230, 185, 9), 1.0F),
                                            player,
                                            hitEntity,
                                            1.25,
                                            30
                                    );
                                }

                                // freeze the player and every entity nearby the direct hit
                                for (Map.Entry<Entity, Location> entry : nearbyEntities.entrySet()) {
                                    entry.getKey().teleport(entry.getValue());
                                }

                                // when hitstop ends
                                if (timer == 0) {
                                    for (Entity hitEntity : hitEntities) { // apply knockback and damage for every hit enemy
                                        hitEntity.setVelocity(knockbacks.get(hitEntity));
                                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent((LivingEntity) hitEntity, player, finalPhysicalDamage));
                                        alreadyHitEntities.add(hitEntity.getUniqueId()); // and make sure they can't be hit again
                                    }

                                    for (Entity nearbyEntity : prevVelocities.keySet()) { // and knockback for every nearby guy that isn't the player
                                        nearbyEntity.setVelocity(prevVelocities.get(nearbyEntity));
                                    }

                                    player.playSound(player, Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, .5f);
                                    inHitStop = false;
                                    hitstopGracePeriodTimer = 5; // ticks in when the hitstop can be chained

                                    cancel();
                                }
                            }
                        }.runTaskTimer(nmlAbilities, 0, 1);
                    }
                }
            }
        }.runTaskTimer(nmlAbilities, 0, 1);
    }
}