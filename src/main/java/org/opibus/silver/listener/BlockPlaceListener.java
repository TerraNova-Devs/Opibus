package org.opibus.silver.listener;

import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.opibus.Opibus;
import org.opibus.silver.logic.SilverManager;

import java.util.*;

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
        SilverManager.get().roll(getProbability(material), block.getLocation());
    }

    public boolean isPresent(Material material) {
        Map<String, Object> data = Opibus.groupData.modifyFile.getConfigurationSection("groups").getValues(false);
        for (Object object : data.values()) {
            String list = (String) object;
            if (list.contains(material.name()))
                return true;
        }
        return false;
    }

    public boolean isExcluded(Material material) {
        String data = (String) Opibus.groupData.modifyFile.get("excluded");
        return data.contains(material.name());
    }

    public int getProbability(Material material) {
        Map<String, Object> data = Opibus.groupData.modifyFile.getConfigurationSection("groups").getValues(false);
        for (String rawChance : data.keySet()) {
            String list = (String) data.get(rawChance);
            if (list.contains(material.name()))
                return Integer.parseInt(rawChance);
        }
        return 0;
    }
}
