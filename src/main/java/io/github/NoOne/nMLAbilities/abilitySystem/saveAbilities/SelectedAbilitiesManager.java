package io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SelectedAbilitiesManager {
    private static Map<UUID, SelectedAbilities> selectedAbilitiesMap = new HashMap<>(); // hashmap of all the profiles of all the players online atm
    private FileConfiguration config;
    private SelectedAbilitiesConfig selectedAbilitiesConfig;

    public SelectedAbilitiesManager(NMLAbilities nmlAbilities) {
        selectedAbilitiesConfig = nmlAbilities.getSelectedConfig();
        config = selectedAbilitiesConfig.getConfig();
    }

    public void createDefaultSelectedAbilities(Player player) {
        SelectedAbilities selectedAbilities = new SelectedAbilities(
                player.getInventory().getItem(0).getItemMeta().getDisplayName(),
                player.getInventory().getItem(1).getItemMeta().getDisplayName(),
                player.getInventory().getItem(2).getItemMeta().getDisplayName(),
                player.getInventory().getItem(3).getItemMeta().getDisplayName());

        selectedAbilitiesMap.put(player.getUniqueId(), selectedAbilities);
    }

    public SelectedAbilities getSelectedAbilities(UUID uuid) {
        return selectedAbilitiesMap.get(uuid);
    }

    public void loadSelectedAbilitiesFromConfig() {
        for (String id : config.getConfigurationSection("").getKeys(false)) {
            UUID uuid = UUID.fromString(id);
            String style1 = config.getString(id + ".abilities.style");
            String expertise1 = config.getString(id + ".abilities.expertise1");
            String expertise2 = config.getString(id + ".abilities.expertise2");
            String expertise3 = config.getString(id + ".abilities.expertise3");
            SelectedAbilities selectedAbilities = new SelectedAbilities(style1, expertise1, expertise2, expertise3);

            selectedAbilitiesMap.put(uuid, selectedAbilities);
        }
    }

    public void saveAllSelectedAbilitiesToConfig() {
        for (UUID uuid : selectedAbilitiesMap.keySet()) {
            SelectedAbilities selectedAbilities = selectedAbilitiesMap.get(uuid);

            selectedAbilities.syncSelectedAbilitiesToInventory(Bukkit.getPlayer(uuid));
            config.set(uuid + ".abilities.style", selectedAbilities.getStyle());
            config.set(uuid + ".abilities.expertise1", selectedAbilities.getExpertise1());
            config.set(uuid + ".abilities.expertise2", selectedAbilities.getExpertise2());
            config.set(uuid + ".abilities.expertise3", selectedAbilities.getExpertise3());
        }
    }

    public void saveSelectedAbilitiesToConfig(Player player) {
        UUID uuid = player.getUniqueId();
        SelectedAbilities selectedAbilities = selectedAbilitiesMap.get(uuid);

        config.set(uuid + ".abilities.style", selectedAbilities.getStyle());
        config.set(uuid + ".abilities.expertise1", selectedAbilities.getExpertise1());
        config.set(uuid + ".abilities.expertise2", selectedAbilities.getExpertise2());
        config.set(uuid + ".abilities.expertise3", selectedAbilities.getExpertise3());
    }
}
