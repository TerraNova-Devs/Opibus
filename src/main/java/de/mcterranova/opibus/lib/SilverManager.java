package de.mcterranova.opibus.lib;

import de.mcterranova.opibus.Opibus;
import de.mcterranova.opibus.lib.Chat;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author sakubami
 * @since 0.0.1
 */
public class SilverManager {

    private static ItemStack placeHolder;

    public static boolean roll(int chance) {
        if (chance <= 0) {
            return false; // Prevent invalid values and division by zero
        }
        Bukkit.getServer().sendMessage(Chat.blueFade("rolled with chance " + chance));
        return Opibus.randomGenerator.nextInt(chance) == 0;
    }

    /**
     * generates an {@link ItemStack} at given location.
     *
     * @param location defines the location of the spawn where the {@link ItemStack} is ought to generate.
     * @since 0.0.1
     */
    public static void generate(Location location) {
        World world = location.getWorld();
        location.getWorld().dropItemNaturally(new Location(world, location.getBlockX(), location.getBlockY(), location.getBlockZ()), placeholder());
        world.playSound(location, Sound.BLOCK_BELL_RESONATE, 2, 14);
    }
    /**
     * sets the {@link ItemStack} placeholder which will be used instead of the default {@link ItemStack} placeholder
     *
     * @param newPlaceHolder the {@link ItemStack} to set
     * @since 0.0.1
     */
    public static void setPlaceHolder(ItemStack newPlaceHolder) {
        placeHolder = newPlaceHolder;
    }

    /**
     * uses this placeholder if the {@link ItemStack} placeholder has not been changed
     *
     * @return the {@link ItemStack} that is used by this class
     * @since 0.0.1
     */
    private static ItemStack placeholder() {
        if (placeHolder != null)
            return placeHolder;
        ItemStack itemStack = new ItemStack(Material.IRON_NUGGET);
        ItemMeta meta = itemStack.getItemMeta();
        meta.addEnchant(Enchantment.CHANNELING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.displayName(Chat.stringToComponent("<gradient:#AAA9AD:#D8D8D8><bold>Silber</bold></gradient>"));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * is meant to be accessed via interactions with an {@link org.bukkit.inventory.Inventory}
     * or another type of GUI where the {@link org.bukkit.entity.Player} can choose from 3 different
     * actions where the state can directly be passed on to this method
     * or used by the methods {@code compact} or {@code deCompact}
     *
     * @param convertible the {@link ItemStack} that is to be converted
     * @param to          the {@link SilverState} that the {@link ItemStack} is converted to
     * @return the conversion of the initial {@link ItemStack}
     * @since 0.0.1
     */
    public static ItemStack convert(ItemStack convertible, SilverState to) {
        if (!convertible.hasItemMeta())
            return convertible;
        if (convertible.getType().equals(to.getMaterial()))
            return convertible;
        ItemStack newStack = new ItemStack(to.getMaterial());
        ItemMeta oldMeta = convertible.getItemMeta();
        newStack.setItemMeta(oldMeta);
        return newStack;
    }

    /**
     * @param convertible the {@link ItemStack} that is to be converted
     * @return the compacted version of the initial {@link ItemStack}
     * @since 0.1.0
     */
    public static ItemStack compact(ItemStack convertible) {
        return convert(convertible, SilverState.valueOf(convertible.getType()).getNext());
    }

    /**
     * @param convertible the {@link ItemStack} that is to be converted
     * @return the deCompacted version of the initial {@link ItemStack}
     * @since 0.1.0
     */
    public static ItemStack deCompact(ItemStack convertible) {
        return convert(convertible, SilverState.valueOf(convertible.getType()).getPrevious());
    }
}
