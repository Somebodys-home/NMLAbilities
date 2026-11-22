package io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLConfig.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class SelectedAbilitiesManager extends ConfigManager {
    private HashMap<UUID, SelectedAbilities> selectedAbilitiesMap = new HashMap<>(); // hashmap of all the selected abilities of all the players

    public SelectedAbilitiesManager(NMLAbilities nmlAbilities) {
        super(nmlAbilities.getSelectedAbilitiesConfig());
    }

    public void addSelectedAbilities(Player player, SelectedAbilities selectedAbilities) {
        selectedAbilitiesMap.put(player.getUniqueId(), selectedAbilities);
    }

    public void loadSelectedAbilitiesFromConfig() {
        for (String id : config.getConfigurationSection("").getKeys(false)) {
            UUID uuid = UUID.fromString(id);
            ItemStack style1 = config.getItemStack(id + ".abilities.style");
            ItemStack expertise1 = config.getItemStack(id + ".abilities.expertise1");
            ItemStack expertise2 = config.getItemStack(id + ".abilities.expertise2");
            ItemStack expertise3 = config.getItemStack(id + ".abilities.expertise3");
            SelectedAbilities selectedAbilities = new SelectedAbilities(style1, expertise1, expertise2, expertise3);

            selectedAbilitiesMap.put(uuid, selectedAbilities);
        }
    }

    public void saveAllSelectedAbilitiesToConfig() {
        for (UUID uuid : selectedAbilitiesMap.keySet()) {
            SelectedAbilities selectedAbilities = selectedAbilitiesMap.get(uuid);

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

    public SelectedAbilities getSelectedAbilities(UUID uuid) {
        return selectedAbilitiesMap.get(uuid);
    }
}
