package de.mcterranova.opibus.silver;

import com.jeff_media.customblockdata.CustomBlockData;
import de.mcterranova.opibus.Opibus;
import de.mcterranova.opibus.lib.Chat;
import de.mcterranova.opibus.lib.SilverManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
        Block block = event.getBlock();
        PersistentDataContainer customBlockData = new CustomBlockData(block, plugin);
        if (customBlockData.has(key, PersistentDataType.BOOLEAN))
            return;
        Material material = event.getBlock().getType();
        if (!isPresent(material))
            return;
        if(SilverManager.roll(getProbability(material))){
            SilverManager.generate(block.getLocation());
        }
    }

    public boolean isPresent(Material material) {
        Map<String, Object> data = Opibus.groupData.modifyFile.getConfigurationSection("groups").getValues(false);
        for (Object object : data.values()) {
            String rawData = ((String) object);
            String[] array = rawData.substring(1, rawData.length() - 1).split(",");
            List<String> list = Arrays.asList(array);
            if (list.contains(material.toString()))
                return true;
        }
        return false;
    }

    public boolean isExcluded(Material material) {
        String rawData = ((String) Opibus.groupData.modifyFile.get("excluded"));
        String[] data = rawData.substring(1, rawData.length() - 1).split(",");
        List<String> list = Arrays.asList(data);

        return list.contains(material.toString());
    }

    public int getProbability(Material material) {
        Map<String, Object> data = Opibus.groupData.modifyFile.getConfigurationSection("groups").getValues(false);
        for (String rawChance : data.keySet()) {
            String rawData = ((String) data.get(rawChance));
            String[] array = rawData.substring(1, rawData.length() - 1).split(",");
            List<String> list = Arrays.asList(array);
            if (list.contains((material.toString())))
                return Integer.parseInt(rawChance);
        }
        return 0;
    }
}
