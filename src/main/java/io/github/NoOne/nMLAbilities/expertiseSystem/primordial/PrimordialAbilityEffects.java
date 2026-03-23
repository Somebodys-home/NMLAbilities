package io.github.NoOne.nMLAbilities.expertiseSystem.primordial;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageHelper;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityEffects;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldown.CooldownManager;
import io.github.NoOne.nMLEnergySystem.EnergyManager;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLWeapons.AttackCooldownSystem;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class PrimordialAbilityEffects {
    private static NMLAbilities nmlAbilities;
    private static ProfileManager profileManager;

    public PrimordialAbilityEffects(NMLAbilities nmlAbilities) {
        this.nmlAbilities = nmlAbilities;
        profileManager = nmlAbilities.getProfileManager();
    }

    public static void chuckRock(Player player) {
        HashSet<UUID> hitEntityUUIDs = new HashSet<>();
        HashMap<DamageType, Double> physicalDamage = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStat2Damage(
                                            profileManager.getPlayerProfile(player.getUniqueId()).getStats(), "physicaldamage"), 1.5);
        FallingBlock rock = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 1.5, 0), Bukkit.createBlockData(Material.STONE_BUTTON));

        EnergyManager.useEnergy(player, 10);
        CooldownManager.putOnHardCooldown(player, .5);
        AttackCooldownSystem.setOrPauseAttackCooldown(player, .5);

        rock.setCancelDrop(true);
        rock.setVelocity(player.getLocation().getDirection().multiply(2).add(new Vector(0, .3, 0)));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, .5f, 2f);

        // sweep particle
        Location baseLocation = player.getEyeLocation().clone().subtract(0, .5, 0);
        Vector forward = baseLocation.getDirection().normalize().multiply(1.2);
        Location swing = baseLocation.clone().add(forward);
        player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, swing, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                Location stoneLocation = rock.getLocation();

                for (Entity entity : player.getWorld().getNearbyEntities(stoneLocation, 1, 1, 1)) {
                    if (entity instanceof LivingEntity livingEntity && entity != player) {
                        hitEntityUUIDs.add(livingEntity.getUniqueId());
                    }
                }

                if (!hitEntityUUIDs.isEmpty()) {
                    for (UUID uuid : hitEntityUUIDs) {
                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent((LivingEntity) Bukkit.getEntity(uuid), player, physicalDamage));
                        hitEntityUUIDs.remove(uuid);
                    }

                    player.getWorld().spawnParticle(Particle.BLOCK, stoneLocation, 100, 0, 0 ,0, 0, Bukkit.createBlockData(Material.STONE));
                    player.playSound(stoneLocation, Sound.BLOCK_STONE_BREAK, 2f, 2f);
                    cancel();
                }

                if (!rock.isValid() || rock.isDead()) {
                    player.getWorld().spawnParticle(Particle.BLOCK, stoneLocation, 100, 0, 0 ,0, 0, Bukkit.createBlockData(Material.STONE));
                    player.playSound(stoneLocation, Sound.BLOCK_STONE_BREAK, 2f, 2f);
                    cancel();
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);
    }

    public static void pumpkinBomb(Player player) {
        HashSet<UUID> hitEntityUUIDs = new HashSet<>();
        HashMap<DamageType, Double> earth = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStat2Damage(profileManager.getPlayerProfile(player.getUniqueId()).getStats(), "earthdamage"), 1.5);
        HashMap<DamageType, Double> fire = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStat2Damage(profileManager.getPlayerProfile(player.getUniqueId()).getStats(), "firedamage"), 1.5);
        HashMap<DamageType, Double> totalDamage = DamageHelper.convertPlayerStats2Damage(profileManager.getPlayerProfile(player.getUniqueId()).getStats());

        totalDamage.remove("earthdamage");
        totalDamage.remove("firedamage");
        totalDamage.putAll(earth);
        totalDamage.putAll(fire);

        // blocks
        BlockFace face = yawToFace(player.getLocation().getYaw());
        Directional data = (Directional) Bukkit.createBlockData(Material.JACK_O_LANTERN);
        data.setFacing(face);
        FallingBlock pumpkinBomb = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 1, 0), data);

        // sweep particle
        Location baseLocation = player.getEyeLocation().clone().subtract(0, .5, 0);
        Vector forward = baseLocation.getDirection().normalize().multiply(1.2);
        Location swing = baseLocation.clone().add(forward);
        player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, swing, 1);

        EnergyManager.useEnergy(player, 30);
        CooldownManager.putOnHardCooldown(player, 1.25);
        AttackCooldownSystem.setOrPauseAttackCooldown(player, 1.25);
        pumpkinBomb.setCancelDrop(true);
        pumpkinBomb.setVelocity(player.getLocation().getDirection().multiply(.5).add(new Vector(0, 1, 0)));
        player.playSound(player.getLocation(), Sound.ENTITY_WITCH_CELEBRATE, 1f, 1f);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);

        new BukkitRunnable() {
            boolean kaboom = false;
            int candyTimer = 0;

            @Override
            public void run() {
                Location pumpkinBombLocation = pumpkinBomb.getLocation();
                Collection<Entity> nearbyEntities = player.getWorld().getNearbyEntities(pumpkinBombLocation, 1, 1, 1);
                Particle.DustOptions yellow = new Particle.DustOptions(Color.fromRGB(255, 244, 110), 2F);

                nearbyEntities.remove(pumpkinBomb);
                nearbyEntities.remove(player);
                player.getWorld().spawnParticle(Particle.DUST, pumpkinBombLocation, 1, 0, 0, 0, yellow);

                // candy
                if (candyTimer == 5) {
                    candyTimer = 0;

                    Material candyMaterial = null;

                    switch ((int) (Math.random() * (8 - 1 + 1) + 1)) {
                        case 1 -> candyMaterial = Material.PINK_CONCRETE_POWDER;
                        case 2 -> candyMaterial = Material.LIME_CONCRETE_POWDER;
                        case 3 -> candyMaterial = Material.LIGHT_BLUE_CONCRETE_POWDER;
                        case 4 -> candyMaterial = Material.WHITE_CONCRETE_POWDER;
                        case 5 -> candyMaterial = Material.RED_CONCRETE_POWDER;
                        case 6 -> candyMaterial = Material.ORANGE_CONCRETE_POWDER;
                        case 7 -> candyMaterial = Material.PURPLE_CONCRETE_POWDER;
                        case 8 -> candyMaterial = Material.YELLOW_CONCRETE_POWDER;
                    }

                    FallingBlock candy = player.getWorld().spawnFallingBlock(pumpkinBombLocation.add(0, 1.5, 0), Bukkit.createBlockData(candyMaterial));
                    double randomX = (Math.random() - 0.5) * 0.5;
                    double randomZ = (Math.random() - 0.5) * 0.5;

                    candy.setMetadata("pumpkin_candy", new FixedMetadataValue(nmlAbilities, true));
                    candy.setVelocity(new Vector(randomX, .75, randomZ));
                    candy.setCancelDrop(true);
                }

                // trigger
                boolean triggered = false;

                for (Entity entity : nearbyEntities) {
                    if (!entity.hasMetadata("pumpkin_candy")) {
                        triggered = true;
                        break;
                    }
                }

                if (triggered || !pumpkinBomb.isValid() || pumpkinBomb.isDead()) {
                    kaboom = true;
                }

                // explosion
                if (kaboom) {
                    pumpkinBomb.remove();
                    player.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, pumpkinBombLocation, 1);
                    player.playSound(pumpkinBombLocation, Sound.ENTITY_GENERIC_EXPLODE, 2f, 1f);
                    player.playSound(pumpkinBombLocation, Sound.ENTITY_WITHER_DEATH, 1.5f, 1f);

                    // fireworks
                    new BukkitRunnable() {
                        int times = 0;

                        @Override
                        public void run() {
                            times++;

                            for (int i = 0; i < 3; i++) {
                                Location fireworkLocation = pumpkinBombLocation.clone();
                                fireworkLocation.add((Math.random() - .5) * 12, (Math.random() - .5) * 12, (Math.random() - .5) * 12);

                                Firework firework = (Firework) player.getWorld().spawnEntity(fireworkLocation, EntityType.FIREWORK_ROCKET);
                                FireworkMeta fireworkMeta = firework.getFireworkMeta();

                                fireworkMeta.addEffect(FireworkEffect.builder()
                                        .withColor(Color.ORANGE)
                                        .withFade(Color.YELLOW)
                                        .with(FireworkEffect.Type.BALL)
                                        .flicker(true)
                                        .build());
                                fireworkMeta.setPower(0);
                                firework.setFireworkMeta(fireworkMeta);
                                firework.setMetadata("ability_firework", new FixedMetadataValue(nmlAbilities, true));
                                firework.detonate();
                            }


                            if (times == 6) cancel();
                        }
                    }.runTaskTimer(nmlAbilities,  6L, 2L);

                    // damage
                    for (Entity entity : player.getWorld().getNearbyEntities(pumpkinBombLocation, 4, 4, 4)) {
                        if (entity instanceof LivingEntity livingEntity && entity != player) {
                            hitEntityUUIDs.add(livingEntity.getUniqueId());
                        }
                    }

                    for (UUID uuid : hitEntityUUIDs) {
                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent((LivingEntity) Bukkit.getEntity(uuid), player, totalDamage));
                    }

                    cancel();
                }

                candyTimer++;
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);
    }

    public static void airBall(Player player) {
        player.setMetadata("ability_falling", new FixedMetadataValue(nmlAbilities, true));

        HashSet<UUID> hitEntityUUIDs = new HashSet<>();
        Particle.DustOptions air = new Particle.DustOptions(Color.fromRGB(255, 255, 255), 1.0F);
        HashMap<DamageType, Double> airDamage = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStat2Damage(profileManager.getPlayerProfile(player.getUniqueId()).getStats(), "airdamage"), 2.5);
        HashMap<DamageType, Double> totalDamage = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStats2Damage(profileManager.getPlayerProfile(player.getUniqueId()).getStats()), .5);

        totalDamage.remove("airdamage");
        totalDamage.putAll(airDamage);

        EnergyManager.useEnergy(player, 15);
        CooldownManager.putOnHardCooldown(player, 2);
        AttackCooldownSystem.setOrPauseAttackCooldown(player, 2);
        player.playSound(player, Sound.ENTITY_BREEZE_JUMP, 1f, 1f);

        // jump
        Vector jump = player.getLocation().getDirection().multiply(3);
        jump.setY(1.15);
        player.setVelocity(jump);

        /// charge air ball
        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {
                timer++;

                int particleCircles = 6;
                Location playerLocation = player.getLocation().clone().add(0, 1, 0);
                Vector forward = playerLocation.getDirection().normalize().multiply(2.25);
                Location center = playerLocation.clone().add(forward);

                AbilityEffects.dustSphere(air, center, .75, particleCircles);

                /// air ball
                if (timer == 20) {
                    cancel();
                    player.playSound(player, Sound.ENTITY_BREEZE_SHOOT, 1f, 1f);

                    // recoil
                    Vector recoil = player.getLocation().getDirection().normalize().multiply(-1);

                    recoil.setY(recoil.getY() * .75);
                    jump.multiply(.3);
                    jump.setY(0);
                    player.setVelocity(jump.add(recoil));

                    new BukkitRunnable() {
                        int duration = 0;
                        Vector velocity = player.getLocation().getDirection().normalize().multiply(.33);

                        @Override
                        public void run() {
                            duration++;

                             center.add(velocity);

                            AbilityEffects.dustSphere(air, center, .75, particleCircles);

                            // triggering air ball
                            Collection<Entity> triggeringEntities = player.getWorld().getNearbyEntities(center, 1, 1, 1);
                            triggeringEntities.remove(player);

                            /// explosion
                            if (duration == 20 || !center.getBlock().isPassable() || !triggeringEntities.isEmpty()) {
                                cancel();

                                player.getWorld().playSound(center, Sound.ENTITY_BREEZE_WIND_BURST, 2f, 1f);
                                player.getWorld().playSound(center, Sound.ENTITY_GENERIC_EXPLODE, .5f, 1f);
                                AbilityEffects.expandingParticleSphere(Particle.SNOWFLAKE, center, 6, 45, .3);
                                AbilityEffects.dustSphere(air, center, 6, 45);

                                // damage
                                for (Entity entity : player.getWorld().getNearbyEntities(center, 6, 6, 6)) {
                                    if (entity instanceof LivingEntity livingEntity && entity != player) {
                                        hitEntityUUIDs.add(livingEntity.getUniqueId());
                                    }
                                }

                                if (!hitEntityUUIDs.isEmpty()) {
                                    for (UUID uuid : hitEntityUUIDs) {
                                        LivingEntity livingEntity = (LivingEntity) Bukkit.getEntity(uuid);
                                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, totalDamage));

                                        // knockback
                                        Vector direction = livingEntity.getLocation().toVector().subtract(center.toVector()).normalize();
                                        Vector knockback = direction.multiply(1.5);
                                        knockback.setY(.5);
                                        livingEntity.setVelocity(knockback);
                                    }
                                }

                            }
                        }
                    }.runTaskTimer(nmlAbilities, 0L, 1L);
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);

        /// landing
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnGround()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.removeMetadata("ability_falling", nmlAbilities);
                        }
                    }.runTaskLater(nmlAbilities, 1L);

                    cancel();
                }
            }
        }.runTaskTimer(nmlAbilities, 5L, 1L);
    }

    private static BlockFace yawToFace(float yaw) {
        yaw = (yaw % 360 + 360) % 360;
        if (yaw < 45 || yaw >= 315) return BlockFace.NORTH;
        if (yaw < 135) return BlockFace.EAST;
        if (yaw < 225) return BlockFace.SOUTH;
        return BlockFace.WEST;
    }
}