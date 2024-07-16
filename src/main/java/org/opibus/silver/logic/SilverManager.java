package org.opibus.silver.logic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.opibus.Opibus;
import org.opibus.utils.ChatUtils;

public class SilverManager {

    public static SilverManager manager;

    public static void init() {
        if (manager != null) return;
        manager = new SilverManager();
    }

    public void roll(int chance, Location location) {
        if (chance >= Opibus.randomGenerator.nextInt(1000)) {
            generate(location);
        }
    }

    public void generate(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        World world = location.getWorld();
        location.getWorld().dropItemNaturally(new Location(world, x, y, z), placeholder());
        world.playSound(location, Sound.BLOCK_BELL_RESONATE, 2, 8);
    }

    private ItemStack placeholder() {
        ItemStack itemStack = new ItemStack(Material.IRON_NUGGET);
        ItemMeta meta = itemStack.getItemMeta();
        meta.addEnchant(Enchantment.CHANNELING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.displayName(ChatUtils.stringToComponent("<gradient:#AAA9AD:#D8D8D8><bold>Silber</bold></gradient>"));
        return itemStack;
    }

    public static SilverManager get() {
        return manager;
    }
}
