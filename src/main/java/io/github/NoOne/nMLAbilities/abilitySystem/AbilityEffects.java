package io.github.NoOne.nMLAbilities.abilitySystem;

import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseEffectsHelper;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class AbilityEffects extends ExpertiseEffectsHelper { 
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
}
