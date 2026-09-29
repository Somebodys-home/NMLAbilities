package io.github.NoOne.nMLAbilities.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetRotationCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            float yaw = Float.parseFloat(args[0]);
            float pitch = Float.parseFloat(args[1]);

            player.setRotation(yaw, pitch);
        }

        return true;
    }
}
