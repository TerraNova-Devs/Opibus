package de.mcterranova.opibus.silver;

import com.jeff_media.customblockdata.CustomBlockData;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.mcterranova.opibus.DependencyChecker;
import de.mcterranova.opibus.Opibus;
import de.mcterranova.opibus.lib.SilverManager;
import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BlockPlaceListener implements Listener {

    private final Opibus plugin;

    private final NamespacedKey key;

    public BlockPlaceListener(Opibus plugin) {
        this.plugin = plugin;
        key = new NamespacedKey(plugin, "wasPlaced");
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!isPresent(event.getBlockPlaced().getType()) || isExcluded(event.getBlock().getType()))
            return;
        PersistentDataContainer customBlockData = new CustomBlockData(event.getBlockPlaced(), plugin);
        customBlockData.set(key, PersistentDataType.BOOLEAN, true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        //So dass in Worldguard Regionen kein Silber droppt
        if(DependencyChecker.worldguard) {
            com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(event.getPlayer().getLocation());
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regions = container.get(BukkitAdapter.adapt(event.getPlayer().getWorld()));
            if(regions != null) {
                ApplicableRegionSet set = regions.getApplicableRegions(loc.toVector().toBlockPoint());
                if(set.size() >= 1) return;
            }
        }

        Block block = event.getBlock();
        PersistentDataContainer customBlockData = new CustomBlockData(block, plugin);
        if (customBlockData.has(key, PersistentDataType.BOOLEAN))
            return;
        Material material = event.getBlock().getType();
        if (!isPresent(material))
            return;
        if(SilverManager.roll(getProbability(material))){
            World world = block.getLocation().getWorld();
            block.getLocation().getWorld().dropItemNaturally(new Location(world, block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()), OraxenItems.getItemById("terranova_silver").build());
        }
    }

    public boolean isPresent(Material material) {
        Map<String, Object> data = Opibus.groupData.modifyFile.getConfigurationSection("groups").getValues(false);
        for (Object object : data.values()) {
            String rawData = ((String) object);
            String[] array = rawData.substring(1, rawData.length() - 1).split(", ");
            List<String> list = Arrays.asList(array);
            if (list.contains(material.toString()))
                return true;
        }
        return false;
    }

    public boolean isExcluded(Material material) {
        String rawData = ((String) Opibus.groupData.modifyFile.get("excluded"));
        String[] data = rawData.substring(1, rawData.length() - 1).split(", ");
        List<String> list = Arrays.asList(data);

        return list.contains(material.toString());
    }

    public int getProbability(Material material) {
        Map<String, Object> data = Opibus.groupData.modifyFile.getConfigurationSection("groups").getValues(false);
        for (String rawChance : data.keySet()) {
            String rawData = ((String) data.get(rawChance));
            String[] array = rawData.substring(1, rawData.length() - 1).split(", ");
            List<String> list = Arrays.asList(array);
            if (list.contains((material.toString())))
                return Integer.parseInt(rawChance);
        }
        return 0;
    }
}
