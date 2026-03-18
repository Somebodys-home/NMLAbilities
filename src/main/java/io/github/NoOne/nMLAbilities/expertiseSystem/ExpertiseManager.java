package io.github.NoOne.nMLAbilities.expertiseSystem;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLItems.ItemSystem;
import io.github.NoOne.nMLItems.enums.ItemType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.ArrayList;
import java.util.List;
import static io.github.NoOne.nMLItems.enums.ItemType.*;

public class ExpertiseManager {
    private static NMLAbilities nmlAbilities;

    public ExpertiseManager(NMLAbilities nmlAbilities) {
        ExpertiseManager.nmlAbilities = nmlAbilities;
    }

    public static List<ItemType> getWeaponsForAbility(ItemStack item) {
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        List<ItemType> weapons = new ArrayList<>(List.of(SWORD, DAGGER, AXE, HAMMER, SPEAR, GLOVE, BOW, WAND, STAFF, CATALYST, SHIELD));
        List<ItemType> weaponsToRemove = new ArrayList<>();

        for (ItemType weapon : weapons) {
            NamespacedKey weaponKey = new NamespacedKey(nmlAbilities, ItemType.getItemTypeString(weapon));

            if (!pdc.has(weaponKey)) {
                weaponsToRemove.add(weapon);
            }
        }

        weapons.removeAll(weaponsToRemove);
        return weapons;
    }

    public static boolean isHoldingWeaponForAbility(Player player, ItemStack abilityItem, ItemStack mainhand) {
        List<ItemType> requiredWeapons = getWeaponsForAbility(abilityItem);
        ItemStack offhand = player.getInventory().getItemInOffHand();

        // if an ability uses any weapon
        if (requiredWeapons.contains(SWORD) && requiredWeapons.contains(DAGGER) && requiredWeapons.contains(AXE) && requiredWeapons.contains(HAMMER) &&
            requiredWeapons.contains(SPEAR) && requiredWeapons.contains(GLOVE) && requiredWeapons.contains(BOW) && requiredWeapons.contains(WAND) &&
            requiredWeapons.contains(STAFF) && requiredWeapons.contains(CATALYST)) {

            for (ItemType itemType : requiredWeapons) {
                if (ItemSystem.isItemType(mainhand, itemType)) {
                    return true;
                }
            }

            return false;
        }

        if (requiredWeapons.contains(GLOVE)) {
            return ItemSystem.isItemType(mainhand, GLOVE) && ItemSystem.isItemType(offhand, GLOVE);
        } else if (requiredWeapons.contains(BOW)) {
            return ItemSystem.isItemType(mainhand, BOW) && ItemSystem.isItemType(offhand, QUIVER);
        } else {
            for (ItemType itemType : requiredWeapons) {
                if (ItemSystem.isItemType(mainhand, itemType)) {
                    return true;
                }
            }

            return false;
        }
    }
}
