package io.github.NoOne.nMLAbilities.expertiseSystem.primordial;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.abilitySystem.abilityEffects.AbilityEffectsHelper;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class PrimordialAbilityEffects extends AbilityEffectsHelper {

    public static void chuckRock(Player player) {
        World world = player.getWorld();
        HashMap<DamageType, Double> physicalDamage = getDamageForAbility(player, DamageType.PHYSICAL, 1.5);
        Location playerLocation = player.getLocation();

        world.spawnParticle(Particle.SWEEP_ATTACK, playerLocation.add(0, 1, 0).add(playerLocation.getDirection().multiply(1.2)), 1);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, .5f, 1f);
        useEnergyAndCooldown(player, 10, .5);

        // rock
        FallingBlock rock = world.spawnFallingBlock(playerLocation, Bukkit.createBlockData(Material.STONE_BUTTON));

        rock.setCancelDrop(true);
        rock.setVelocity(playerLocation.getDirection().multiply(1.35).add(new Vector(0, .3, 0)));

        // rock effect
        new BukkitRunnable() {
            @Override
            public void run() {
                Location rockLocation = rock.getLocation();
                ArrayList<LivingEntity> hitEntities = getNearbyEntitiesExcludingPlayer(player, rockLocation, 1);

                if (!hitEntities.isEmpty() || !rock.isValid() || rock.isDead()) {
                    cancel();
                    world.spawnParticle(Particle.BLOCK, rockLocation, 100, 0, 0 ,0, 0, Bukkit.createBlockData(Material.STONE));
                    player.playSound(rockLocation, Sound.BLOCK_STONE_BREAK, 2f, 2f);
                    rock.remove();

                    for (LivingEntity livingEntity : hitEntities) {
                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, physicalDamage));
                    }
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);
    }

    public static void pumpkinBomb(Player player) {
        World world = player.getWorld();
        HashMap<DamageType, Double> damage = getDamageForAbility(player, new HashMap<>(){{
            put(DamageType.EARTH, 1.5);
            put(DamageType.FIRE, 1.5);
        }});

        // pumpkin bomb
        BlockFace face = yawToFace(player.getLocation().getYaw());
        Directional data = (Directional) Bukkit.createBlockData(Material.JACK_O_LANTERN);
        data.setFacing(face);

        FallingBlock pumpkinBomb = world.spawnFallingBlock(player.getLocation().add(0, 1, 0), data);

        pumpkinBomb.setCancelDrop(true);
        pumpkinBomb.setVelocity(player.getLocation().getDirection().multiply(.25).setY(.75));

        // sweep particle
        Location baseLocation = player.getEyeLocation().clone().subtract(0, .5, 0);
        Vector forward = baseLocation.getDirection().multiply(1.2);
        Location swing = baseLocation.clone().add(forward);
        world.spawnParticle(Particle.SWEEP_ATTACK, swing, 1);

        useEnergyAndCooldown(player, 30, 1.25);
        player.playSound(player.getLocation(), Sound.ENTITY_WITCH_CELEBRATE, 1f, 1f);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);

        new BukkitRunnable() {
            int candyInterval = 4; // interval for how long it takes for candy to pop out of the pumpkin
            int candyTimer = candyInterval;

            @Override
            public void run() {
                Location pumpkinBombLocation = pumpkinBomb.getLocation();
                Collection<Entity> nearbyEntities = world.getNearbyEntities(pumpkinBombLocation, 1, 1, 1);
                Particle.DustOptions yellowTrail = new Particle.DustOptions(Color.fromRGB(255, 244, 110), 2F);

                nearbyEntities.remove(pumpkinBomb);
                nearbyEntities.remove(player);
                world.spawnParticle(Particle.DUST, pumpkinBombLocation, 1, 0, 0, 0, yellowTrail);
                candyTimer--;

                // spawn candy
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
                    FallingBlock candy = world.spawnFallingBlock(pumpkinBombLocation.add(0, 1.5, 0), Bukkit.createBlockData(candyMaterial));
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
                    world.spawnParticle(Particle.EXPLOSION_EMITTER, pumpkinBombLocation, 1);
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

                                Firework firework = (Firework) world.spawnEntity(fireworkLocation, EntityType.FIREWORK_ROCKET);
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

                    for (LivingEntity livingEntity : getNearbyEntitiesExcludingPlayer(player, pumpkinBombLocation, 4)) {
                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damage));
                    }

                    cancel();
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);
    }

    public static void airBall(Player player) {
        HashMap<DamageType, Double> damage = getDamageForAbility(player, .5, new HashMap<>(){{put(DamageType.AIR, 2.0);}});
        Particle.DustOptions air = new Particle.DustOptions(Color.fromRGB(255, 255, 255), 1.0F);
        World world = player.getWorld();

        makeKneecapsUnbreakable(player);
        useEnergyAndCooldown(player, 15, 2);
        player.playSound(player, Sound.ENTITY_BREEZE_JUMP, 1f, 1f);

        BukkitRunnable chargeAirBall = new BukkitRunnable() {
            @Override
            public void run() {
                Location playerLocation = player.getLocation().clone().add(0, 1, 0);
                Vector forward = playerLocation.getDirection().multiply(2.25);
                Location center = playerLocation.clone().add(forward);

                particleSphere(air, center, .75, 6);
            }
        };

        // no fall damage
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnGround()) {
                    makeKneecapsBreakable(player);
                    cancel();
                }
            }
        }.runTaskTimer(nmlAbilities, 5L, 1L);

        // sequence
        new BukkitRunnable() {
            Vector jump = player.getLocation().getDirection().multiply(2.25).setY(1.15);
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
                        Vector oppDirection = player.getLocation().getDirection().multiply(-1);
                        Vector smallerJump = jump.clone().multiply(.15).add(oppDirection);
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
                            Vector airBallVelocity = player.getLocation().getDirection().multiply(.33);
                            Location playerLocation = player.getLocation().clone().add(0, 1, 0);
                            Vector forward = playerLocation.getDirection().multiply(2.25);
                            Location center = playerLocation.clone().add(forward);

                            @Override
                            public void run() {
                                duration++;
                                center.add(airBallVelocity);
                                particleSphere(air, center, .75, 6);

                                // triggering air ball
                                Collection<Entity> triggeringEntities = world.getNearbyEntities(center, 1, 1, 1);
                                triggeringEntities.remove(player);

                                // explosion when run out of time, hits a block or entity
                                if (duration == 20 || !center.getBlock().isPassable() || !triggeringEntities.isEmpty()) {
                                    int radius = 4;
                                    int particleCircles = 20;

                                    cancel();
                                    world.playSound(center, Sound.ENTITY_BREEZE_WIND_BURST, 2f, 1f);
                                    world.playSound(center, Sound.ENTITY_GENERIC_EXPLODE, .5f, 1f);
                                    expandingParticleSphere(Particle.SNOWFLAKE, center, radius, particleCircles, .3);
                                    particleSphere(air, center, radius, particleCircles);

                                    // damage
                                    for (LivingEntity livingEntity : getNearbyEntitiesExcludingPlayer(player, center, radius)) {
                                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damage));

                                        // knockback
                                        Vector direction = livingEntity.getLocation().toVector().subtract(center.toVector()).normalize();
                                        Vector knockback = direction.multiply(1.2).setY(.5);

                                        livingEntity.setVelocity(knockback);
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