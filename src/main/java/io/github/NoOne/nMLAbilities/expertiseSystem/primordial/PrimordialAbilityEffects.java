package io.github.NoOne.nMLAbilities.expertiseSystem.primordial;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageHelper;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.ExpertiseEffectsHelper;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityEffects;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldownSystem.CooldownManager;
import io.github.NoOne.nMLEnergySystem.EnergyManager;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import io.github.NoOne.nMLWeapons.AttackCooldownSystem;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PrimordialAbilityEffects extends ExpertiseEffectsHelper {

    public static void chuckRock(Player player) {
        World world = player.getWorld();
        HashMap<DamageType, Double> physicalDamage = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStat2Damage(
                                            profileManager.getPlayerProfile(player.getUniqueId()).getStats(), "physicaldamage"), 1.5);

        useEnergyAndCooldown(player, 10, .5);

        // rock
        FallingBlock rock = world.spawnFallingBlock(player.getLocation().add(0, 1.5, 0), Bukkit.createBlockData(Material.STONE_BUTTON));

        rock.setCancelDrop(true);
        rock.setVelocity(player.getLocation().getDirection().multiply(2).add(new Vector(0, .3, 0)));

        // sweep particle
        Location baseLocation = player.getEyeLocation().clone().subtract(0, .5, 0);
        Vector forward = baseLocation.getDirection().normalize().multiply(1.2);
        Location swing = baseLocation.clone().add(forward);

        world.spawnParticle(Particle.SWEEP_ATTACK, swing, 1);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, .5f, 2f);

        // on hit effect
        new BukkitRunnable() {
            @Override
            public void run() {
                Location stoneLocation = rock.getLocation();
                Collection<Entity> hitEntities = world.getNearbyEntities(stoneLocation, 1, 1, 1);

                for (Entity entity :  hitEntities) {
                    if (entity instanceof LivingEntity livingEntity && entity != player) {
                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, physicalDamage));
                    }
                }

                if (!hitEntities.isEmpty() || !rock.isValid() || rock.isDead()) {
                    world.spawnParticle(Particle.BLOCK, stoneLocation, 100, 0, 0 ,0, 0, Bukkit.createBlockData(Material.STONE));
                    player.playSound(stoneLocation, Sound.BLOCK_STONE_BREAK, 2f, 2f);
                    cancel();
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);
    }

    public static void pumpkinBomb(Player player) {
        Stats stats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();
        HashMap<DamageType, Double> earth = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStat2Damage(stats, "earthdamage"), 1.5);
        HashMap<DamageType, Double> fire = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStat2Damage(stats, "firedamage"), 1.5);
        HashMap<DamageType, Double> totalDamage = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStats2Damage(stats), .8);

        totalDamage.remove("earthdamage");
        totalDamage.remove("firedamage");
        totalDamage.putAll(earth);
        totalDamage.putAll(fire);

        // pumpkin bomb
        BlockFace face = yawToFace(player.getLocation().getYaw());
        Directional data = (Directional) Bukkit.createBlockData(Material.JACK_O_LANTERN);
        data.setFacing(face);

        FallingBlock pumpkinBomb = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 1, 0), data);

        pumpkinBomb.setCancelDrop(true);
        pumpkinBomb.setVelocity(player.getLocation().getDirection().multiply(.25).setY(.75));

        // sweep particle
        Location baseLocation = player.getEyeLocation().clone().subtract(0, .5, 0);
        Vector forward = baseLocation.getDirection().normalize().multiply(1.2);
        Location swing = baseLocation.clone().add(forward);
        player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, swing, 1);

        EnergyManager.useEnergy(player, 30);
        CooldownManager.putOnHardCooldown(player, 1.25);
        AttackCooldownSystem.setOrPauseAttackCooldown(player, 1.25);
        player.playSound(player.getLocation(), Sound.ENTITY_WITCH_CELEBRATE, 1f, 1f);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);

        new BukkitRunnable() {
            int candyInterval = 4; // interval for how long it takes for candy to pop out of the pumpkin
            int candyTimer = candyInterval;

            @Override
            public void run() {
                Location pumpkinBombLocation = pumpkinBomb.getLocation();
                Collection<Entity> nearbyEntities = player.getWorld().getNearbyEntities(pumpkinBombLocation, 1, 1, 1);
                Particle.DustOptions yellowTrail = new Particle.DustOptions(Color.fromRGB(255, 244, 110), 2F);

                nearbyEntities.remove(pumpkinBomb);
                nearbyEntities.remove(player);
                player.getWorld().spawnParticle(Particle.DUST, pumpkinBombLocation, 1, 0, 0, 0, yellowTrail);
                candyTimer--;

                // candy
                if (candyTimer == 0) {
                    candyTimer = candyInterval;

                    List<Material> candyColors = List.of(
                            Material.PINK_CONCRETE_POWDER,
                            Material.LIME_CONCRETE_POWDER,
                            Material.LIGHT_BLUE_CONCRETE_POWDER,
                            Material.WHITE_CONCRETE_POWDER,
                            Material.RED_CONCRETE_POWDER,
                            Material.ORANGE_CONCRETE_POWDER,
                            Material.PURPLE_CONCRETE_POWDER,
                            Material.YELLOW_CONCRETE_POWDER
                    );
                    Material candyMaterial = candyColors.get(new Random().nextInt(candyColors.size()));
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

                // explosion
                if (triggered || !pumpkinBomb.isValid() || pumpkinBomb.isDead()) {
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
                                Location fireworkLocation = pumpkinBombLocation.clone().add(
                                        (Math.random() - .5) * 12, (Math.random() - .5) * 12, (Math.random() - .5) * 12);

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
                                firework.setMetadata("no_damage", new FixedMetadataValue(nmlAbilities, true));
                                firework.detonate();
                            }

                            if (times == 6) cancel();
                        }
                    }.runTaskTimer(nmlAbilities,6L, 2L);

                    // damage
                    for (Entity entity : player.getWorld().getNearbyEntities(pumpkinBombLocation, 4, 4, 4)) {
                        if (entity instanceof LivingEntity livingEntity && entity != player) {
                            Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, totalDamage));
                        }
                    }

                    cancel();
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);
    }

    public static void airBall(Player player) {
        Stats stats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();
        HashMap<DamageType, Double> airDamage = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStat2Damage(stats, "airdamage"), 2);
        HashMap<DamageType, Double> totalDamage = DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStats2Damage(stats), .5);
        Particle.DustOptions air = new Particle.DustOptions(Color.fromRGB(255, 255, 255), 1.0F);
        World world = player.getWorld();

        totalDamage.remove("airdamage");
        totalDamage.putAll(airDamage);
        player.setMetadata("no_fall_damage", new FixedMetadataValue(nmlAbilities, true));
        EnergyManager.useEnergy(player, 15);
        CooldownManager.putOnHardCooldown(player, 2);
        AttackCooldownSystem.setOrPauseAttackCooldown(player, 2);
        player.playSound(player, Sound.ENTITY_BREEZE_JUMP, 1f, 1f);

        BukkitRunnable chargeAirBall = new BukkitRunnable() {
            @Override
            public void run() {
                Location playerLocation = player.getLocation().clone().add(0, 1, 0);
                Vector forward = playerLocation.getDirection().normalize().multiply(2.25);
                Location center = playerLocation.clone().add(forward);

                AbilityEffects.particleSphere(air, center, .75, 4);
            }
        };

        // no fall damage
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnGround()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.removeMetadata("no_fall_damage", nmlAbilities);
                        }
                    }.runTaskLater(nmlAbilities, 1L);

                    cancel();
                }
            }
        }.runTaskTimer(nmlAbilities, 5L, 1L);

        // sequence
        new BukkitRunnable() {
            Vector jump = player.getLocation().getDirection().multiply(2.75).setY(1.15);
            int timer = 0;

            @Override
            public void run() {
                switch (timer) {
                    case 0 -> { // jump and charge air ball
                        player.setVelocity(jump);
                        chargeAirBall.runTaskTimer(nmlAbilities, 0, 1);
                    }
                    case 20 -> { // fire airball with recoil
                        chargeAirBall.cancel();
                        player.playSound(player, Sound.ENTITY_BREEZE_SHOOT, 1f, 1f);

                        // recoil
                        // the x/z of the recoil should be the addition of the inverse of the players direction and a smaller version of the jump itself
                        // (would use the player's current velocity if it worked properly so we guesstimate with a lesser version of the original jump)
                        // this makes it so the player still gets knock backed relative to how they fired the ball while respecting what their jump was

                        // the y is the opposite of the direction (that the ball will fly = player's looking)
                        Vector oppDirection = player.getLocation().getDirection().normalize().multiply(-1);
                        Vector smallerJump = jump.clone().multiply(.2).add(oppDirection);
                        Vector recoil = new Vector(smallerJump.getX(), oppDirection.getY() * .4,  smallerJump.getZ());

                        // try to make the distance of the recoil similar across all angles
                        // doing this by multiplying the x and z values by their distance to the median while keeping the y the same
                        double speed = recoil.length();
                        double median = 1.05;

                        if (speed < median - .1 || speed > median + .1) {
                            double y = recoil.getY();
                            double multiplier = 1 + (median - speed);

                            recoil.multiply(multiplier).setY(y);
                        }

                        player.setVelocity(recoil);
                        cancel();

                        // shooting air ball
                        new BukkitRunnable() {
                            int duration = 0;
                            Vector airBallVelocity = player.getLocation().getDirection().normalize().multiply(.33);
                            Location playerLocation = player.getLocation().clone().add(0, 1, 0);
                            Vector forward = playerLocation.getDirection().normalize().multiply(2.25);
                            Location center = playerLocation.clone().add(forward);

                            @Override
                            public void run() {
                                duration++;
                                center.add(airBallVelocity);
                                AbilityEffects.particleSphere(air, center, .75, 4);

                                // triggering air ball
                                Collection<Entity> triggeringEntities = world.getNearbyEntities(center, 1, 1, 1);
                                triggeringEntities.remove(player);

                                // explosion when run out of time, hits a block or entity
                                if (duration == 20 || !center.getBlock().isPassable() || !triggeringEntities.isEmpty()) {
                                    int radius = 4;
                                    int particleCircles = 15;

                                    cancel();
                                    world.playSound(center, Sound.ENTITY_BREEZE_WIND_BURST, 2f, 1f);
                                    world.playSound(center, Sound.ENTITY_GENERIC_EXPLODE, .5f, 1f);
                                    AbilityEffects.expandingParticleSphere(Particle.SNOWFLAKE, center, radius, particleCircles, .3);
                                    AbilityEffects.particleSphere(air, center, radius, particleCircles);

                                    // damage
                                    for (Entity entity : world.getNearbyEntities(center, radius, radius, radius)) {
                                        if (entity instanceof LivingEntity livingEntity && entity != player) {
                                            Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, totalDamage));

                                            // knockback
                                            Vector direction = livingEntity.getLocation().toVector().subtract(center.toVector()).normalize();
                                            Vector knockback = direction.multiply(1.2).setY(.5);

                                            livingEntity.setVelocity(knockback);
                                        }
                                    }
                                }
                            }
                        }.runTaskTimer(nmlAbilities, 0, 1);
                    }
                }

                timer++;
            }
        }.runTaskTimer(nmlAbilities, 0, 1);
    }

    private static BlockFace yawToFace(float yaw) {
        yaw = (yaw % 360 + 360) % 360;
        if (yaw < 45 || yaw >= 315) return BlockFace.NORTH;
        if (yaw < 135) return BlockFace.EAST;
        if (yaw < 225) return BlockFace.SOUTH;
        return BlockFace.WEST;
    }
}