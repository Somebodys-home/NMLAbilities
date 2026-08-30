package io.github.NoOne.nMLAbilities;

import io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus.ExpertiseMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExpertiseCommand implements CommandExecutor {
    private NMLAbilities nmlAbilities;

    public ExpertiseCommand(NMLAbilities nmlAbilities) {
        this.nmlAbilities = nmlAbilities;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            new ExpertiseMenu(nmlAbilities, player).open();
        }

        return true;
    }
}