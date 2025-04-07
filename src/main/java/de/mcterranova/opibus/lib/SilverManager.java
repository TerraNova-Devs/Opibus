package de.mcterranova.opibus.lib;

import de.mcterranova.opibus.Opibus;
import de.mcterranova.terranovaLib.utils.Chat;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SilverManager {

    public static boolean roll(int chance) {
        if (chance <= 0) {
            return false; // Prevent invalid values and division by zero
        }
        return Opibus.randomGenerator.nextInt(chance) == 0;
    }
}
