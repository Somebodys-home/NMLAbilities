package io.github.NoOne.nMLAbilities.abilitySystem;

import io.github.NoOne.damagePlugin.customDamage.DamageHelper;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldownSystem.CooldownManager;
import io.github.NoOne.nMLEnergySystem.EnergyManager;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import io.github.NoOne.nMLShields.GuardingSystem;
import io.github.NoOne.nMLWeapons.AttackCooldownSystem;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// parent class for the classes that make expertise ability effects
public class AbilityEffectsHelper {
    protected static NMLAbilities nmlAbilities = NMLAbilities.getInstance();
    protected static ProfileManager profileManager = nmlAbilities.getProfileManager();
    protected static GuardingSystem guardingSystem = nmlAbilities.getGuardingSystem();

    public static void useEnergyAndCooldown(Player player, int energyUse, double cooldown) {
        EnergyManager.useEnergy(player, energyUse);
        CooldownManager.putOnHardCooldown(player, cooldown);
        AttackCooldownSystem.setOrPauseAttackCooldown(player, cooldown);
    }

    public static void putOnCooldown(Player player, double cooldown) {
        CooldownManager.putOnHardCooldown(player, cooldown);
        AttackCooldownSystem.setOrPauseAttackCooldown(player, cooldown);
    }

    public static void putOnInfiniteCooldown(Player player) {
        CooldownManager.putOnInfiniteHardCooldown(player);
        AttackCooldownSystem.pauseAttackCooldown(player);
    }

    public static void removeInfiniteCooldown(Player player) {
        CooldownManager.removeHardCooldown(player);
        AttackCooldownSystem.resumeAttackCooldown(player);
    }

    public static void makeUnmovable(Player player, int ticks) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, ticks, 7, false, false, false));
        breakKneecaps(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                fixKneecaps(player);
            }
        }.runTaskLater(nmlAbilities, ticks);
    }

    public static void breakKneecaps(Player player) {
        player.setMetadata("ability_no_jump", new FixedMetadataValue(nmlAbilities, true));
    }

    public static void makeKneecapsUnbreakable(Player player) {
        player.setMetadata("no_fall_damage", new FixedMetadataValue(nmlAbilities, true));
    }

    public static void makeInvincible(Player player) {
        player.setMetadata("invincible", new FixedMetadataValue(nmlAbilities, true));
    }

    public static void makeMovable(Player player) {
        player.removeMetadata("ability_no_move", nmlAbilities);
    }

    public static void fixKneecaps(Player player) {
        player.removeMetadata("ability_no_jump", nmlAbilities);
    }

    public static void makeKneecapsBreakable(Player player) {
        player.removeMetadata("no_fall_damage", nmlAbilities);
    }

    public static void makeVincible(Player player) {
        player.removeMetadata("invincible", nmlAbilities);
    }

    public static void particleSphere(Particle particle, Location center, double radius, int particleCircles) {
        for (double i = 0; i <= Math.PI; i += Math.PI / particleCircles) { // vertical circles
            double r = Math.sin(i) * radius;
            double y = Math.cos(i) * radius;

            for (double a = 0; a < Math.PI * 2; a+= Math.PI / particleCircles) { // horizontal circles
                double x = Math.cos(a) * r;
                double z = Math.sin(a) * r;
                Location particleLocation = center.clone().add(x, y, z);

                center.getWorld().spawnParticle(particle, particleLocation, 1, 0, 0, 0);
            }
        }
    }

    public static void particleSphere(Particle.DustOptions dustOptions, Location center, double radius, int particleCircles) {
        for (double i = 0; i <= Math.PI; i += Math.PI / particleCircles) { // vertical circles
            double r = Math.sin(i) * radius;
            double y = Math.cos(i) * radius;

            for (double a = 0; a < Math.PI * 2; a+= Math.PI / particleCircles) { // horizontal circles
                double x = Math.cos(a) * r;
                double z = Math.sin(a) * r;
                Location particleLocation = center.clone().add(x, y, z);

                center.getWorld().spawnParticle(Particle.DUST, particleLocation, 1, 0, 0, 0, dustOptions);
            }
        }
    }

    public static void expandingParticleSphere(Particle particle, Location center, double radius, int particleCircles, double speed) {
        for (double i = 0; i <= Math.PI; i += Math.PI / particleCircles) { // vertical circles
            double r = Math.sin(i) * radius;
            double y = Math.cos(i) * radius;

            for (double a = 0; a < Math.PI * 2; a+= Math.PI / particleCircles) { // horizontal circles
                double x = Math.cos(a) * r;
                double z = Math.sin(a) * r;
                Location particleLocation = center.clone().add(x, y, z);
                Vector velocity = particleLocation.toVector().subtract(center.toVector()).normalize().multiply(speed);

                center.getWorld().spawnParticle(particle, particleLocation, 0, velocity.getX(), velocity.getY(), velocity.getZ());
            }
        }
    }

    public static void horizontalParticleCircle(Particle particle, Location center, double radius, int particleCount) {
        for (int i = 0; i < particleCount; i++) {
            double angle = 2 * Math.PI * i / particleCount;
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;
            Location particleLocation = center.clone().add(x, 0, z);

            center.getWorld().spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0);
        }
    }

    public static void horizontalParticleCircle(Particle.DustOptions dustOptions, Location center, double radius, int particleCount) {
        for (int i = 0; i < particleCount; i++) {
            double angle = 2 * Math.PI * i / particleCount;
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;
            Location particleLocation = center.clone().add(x, 0, z);

            center.getWorld().spawnParticle(Particle.DUST, particleLocation, 1, 0, 0, 0, 0, dustOptions);
        }
    }

    public static void expandingHorizontalParticleCircle(Particle particle, Location center, double radius, int particleCount, double speed) {
        for (int i = 0; i < particleCount; i++) {
            double angle = 2 * Math.PI * i / particleCount;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            Location particleLocation = center.clone().add(x, 0, z);
            Vector velocity = particleLocation.toVector().subtract(center.toVector()).normalize().multiply(speed);

            center.getWorld().spawnParticle(particle, particleLocation,0, velocity.getX(), velocity.getY(), velocity.getZ());
        }
    }

    public static void verticalParticleCircleFacingEntity(Particle.DustOptions dustOptions, Entity entity, double radius, int particleCount, double distanceFromEntity) {
        Location center = entity.getLocation().add(0, 1.5, 0).add(entity.getLocation().getDirection().multiply(distanceFromEntity)); // blocks in front
        Vector dirX = entity.getLocation().getDirection(); // Face forward vector
        Vector dirY = new Vector(0, 1, 0); // Up vector
        Vector dirZ = dirX.clone().crossProduct(dirY).normalize(); // Right vector

        for (int i = 0; i < particleCount; i++) {
            double angle = (2 * Math.PI / particleCount) * i;
            double xOffset = Math.cos(angle) * radius;
            double yOffset = Math.sin(angle) * radius;
            Location loc = center.clone().add(dirZ.clone().multiply(xOffset)).add(dirY.clone().multiply(yOffset));

            entity.getWorld().spawnParticle(Particle.DUST, loc, 1, 0, 0, 0, 0, dustOptions);
        }
    }

    public static void verticalParticleCircleBetweenEntities(Particle.DustOptions dustOptions, Entity entity1, Entity entity2, double radius, int particleCount) {
        Location e1Loc = entity1.getLocation().add(0, 1, 0);
        Location e2Loc = entity2.getLocation().add(0, 1, 0);
        Location center = e1Loc.clone().add(e2Loc.toVector().subtract(e1Loc.toVector()).multiply(0.5));
        Vector dirX = e2Loc.toVector().subtract(e1Loc.toVector()).normalize();
        Vector dirY = new Vector(0, 1, 0);
        Vector dirZ = dirX.clone().crossProduct(dirY).normalize();

        for (int i = 0; i < particleCount; i++) {
            double angle = (2 * Math.PI / particleCount) * i;
            double xOffset = Math.cos(angle) * radius;
            double yOffset = Math.sin(angle) * radius;
            Location loc = center.clone().add(dirZ.clone().multiply(xOffset)).add(dirY.clone().multiply(yOffset));

            entity1.getWorld().spawnParticle(Particle.DUST, loc, 1, 0, 0, 0, 0, dustOptions);
        }
    }

    public static void particleLine(Particle particle, Location start, Location end, int particleCount) {
        World world = start.getWorld();
        Vector startVec = start.toVector();
        Vector direction = end.toVector().subtract(startVec);

        for (int i = 0; i < particleCount; i++) {
            double t = (double) i / (particleCount - 1); // 0 → 1 inclusive

            Vector point = startVec.clone().add(direction.clone().multiply(t));
            world.spawnParticle(particle, point.toLocation(world), 1, 0, 0, 0, 0);
        }
    }

    public static void makeWireFrameRectangle(Location center, double length, double height, double width, int ticks) {
        new BukkitRunnable() {
            World world = center.getWorld();
            Location corner1 = center.clone().add(-length, -height, -width);
            Location corner2 = center.clone().add(length, height, width);
            double minX = Math.min(corner1.getX(), corner2.getX());
            double minY = Math.min(corner1.getY(), corner2.getY());
            double minZ = Math.min(corner1.getZ(), corner2.getZ());
            double maxX = Math.max(corner1.getX(), corner2.getX());
            double maxY = Math.max(corner1.getY(), corner2.getY());
            double maxZ = Math.max(corner1.getZ(), corner2.getZ());
            double step = .5;
            int finalTicks = ticks;
            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.FUCHSIA, 1f);

            @Override
            public void run() {
                finalTicks--;

                if (finalTicks == 0) {
                    cancel();
                }

                for (double x = minX; x <= maxX; x += step) {
                    world.spawnParticle(Particle.DUST, new Location(world, x, minY, minZ), 1, 0, 0, 0, 0, dustOptions);
                    world.spawnParticle(Particle.DUST, new Location(world, x, maxY, minZ), 1, 0, 0, 0, 0, dustOptions);
                    world.spawnParticle(Particle.DUST, new Location(world, x, minY, maxZ), 1, 0, 0, 0, 0, dustOptions);
                    world.spawnParticle(Particle.DUST, new Location(world, x, maxY, maxZ), 1, 0, 0, 0, 0, dustOptions);
                }

                for (double y = minY; y <= maxY; y += step) {
                    world.spawnParticle(Particle.DUST, new Location(world, minX, y, minZ), 1, 0, 0, 0, 0, dustOptions);
                    world.spawnParticle(Particle.DUST, new Location(world, maxX, y, minZ), 1, 0, 0, 0, 0, dustOptions);
                    world.spawnParticle(Particle.DUST, new Location(world, minX, y, maxZ), 1, 0, 0, 0, 0, dustOptions);
                    world.spawnParticle(Particle.DUST, new Location(world, maxX, y, maxZ), 1, 0, 0, 0, 0, dustOptions);
                }

                for (double z = minZ; z <= maxZ; z += step) {
                    world.spawnParticle(Particle.DUST, new Location(world, minX, minY, z), 1, 0, 0, 0, 0, dustOptions);
                    world.spawnParticle(Particle.DUST, new Location(world, maxX, minY, z), 1, 0, 0, 0, 0, dustOptions);
                    world.spawnParticle(Particle.DUST, new Location(world, minX, maxY, z), 1, 0, 0, 0, 0, dustOptions);
                    world.spawnParticle(Particle.DUST, new Location(world, maxX, maxY, z), 1, 0, 0, 0, 0, dustOptions);
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);
    }

    public static void makeWireFrameRectangle(Location center, double radius, int ticks) {
        makeWireFrameRectangle(center, radius, radius, radius, ticks);
    }

    public static Vector makeKnockbackVector(Location entityLocation, Location epicenter, double scale, double y) {
        return entityLocation.toVector().subtract(epicenter.toVector()).normalize().multiply(scale).setY(y);
    }
    
    public static HashMap<DamageType, Double> getDamageForAbility(Player player, double weaponDamageMultiplier, HashMap<DamageType, Double> elementalDamages) {
        Stats stats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();
        HashMap<DamageType, Double> playerDamages = DamageHelper.convertPlayerStats2Damage(stats);
        HashMap<DamageType, Double> totalDamage = DamageHelper.multiplyDamageMap(playerDamages, weaponDamageMultiplier);

        for (Map.Entry<DamageType, Double> entry : elementalDamages.entrySet()) { // elementalDamages being every ability's damage type and multiplier for that type
            DamageType damageType = entry.getKey();

            if (playerDamages.containsKey(damageType)) {
                totalDamage.put(damageType, playerDamages.get(damageType) * elementalDamages.get(damageType));
            }
        }

        return totalDamage;
    }

    public static HashMap<DamageType, Double> getDamageForAbility(Player player, double weaponDamageMultiplier) {
        Stats stats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();

        return DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStats2Damage(stats), weaponDamageMultiplier);
    }

    public static HashMap<DamageType, Double> getDamageForAbility(Player player, HashMap<DamageType, Double> elementalDamages) {
        Stats stats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();
        HashMap<DamageType, Double> playerDamages = DamageHelper.convertPlayerStats2Damage(stats);
        HashMap<DamageType, Double> totalDamage = new HashMap<>();

        for (Map.Entry<DamageType, Double> entry : elementalDamages.entrySet()) { // elementalDamages being every ability's damage type and multiplier for that type
            DamageType damageType = entry.getKey();

            if (playerDamages.containsKey(damageType)) {
                totalDamage.put(damageType, playerDamages.get(damageType) * elementalDamages.get(damageType));
            }
        }

        return totalDamage;
    }

    public static HashMap<DamageType, Double> getDamageForAbility(Player player, double weaponDamageMultiplier, DamageType damageType, double multiplier) {
        Stats stats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();
        HashMap<DamageType, Double> playerDamages = DamageHelper.convertPlayerStats2Damage(stats);

        return new HashMap<>() {{
            putAll(DamageHelper.multiplyDamageMap(DamageHelper.convertPlayerStats2Damage(stats), weaponDamageMultiplier));

            if (playerDamages.containsKey(damageType)) {
                put(damageType, playerDamages.get(damageType) * multiplier);
            }
        }};
    }

    public static HashMap<DamageType, Double> getDamageForAbility(Player player, DamageType damageType, double multiplier) {
        Stats stats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();
        HashMap<DamageType, Double> playerDamages = DamageHelper.convertPlayerStats2Damage(stats);

        return new HashMap<>(){{
            if (playerDamages.containsKey(damageType)) {
                put(damageType, playerDamages.get(damageType) * multiplier);
            }
        }};
    }

    public static ArrayList<LivingEntity> getNearbyEntitiesExcludingPlayer(Player player, Location center, double x, double y, double z) {
        return new ArrayList<>(){{
            for (LivingEntity livingEntity : center.getWorld().getNearbyLivingEntities(center, x, y, z)) {
                if (!livingEntity.equals(player)) {
                    add(livingEntity);
                }
            }
        }};
    }

    public static ArrayList<LivingEntity> getNearbyEntitiesExcludingPlayer(Player player, Location center, double radius) {
        return new ArrayList<>(){{
            for (LivingEntity livingEntity : center.getWorld().getNearbyLivingEntities(center, radius)) {
                if (!livingEntity.equals(player)) {
                    add(livingEntity);
                }
            }
        }};
    }
}
