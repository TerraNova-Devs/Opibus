package org.opibus.silver.listener;

import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.opibus.Opibus;
import org.opibus.silver.logic.SilverManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class BlockPlaceListener implements Listener {

    private final Opibus plugin;

    private final NamespacedKey key;

    public BlockPlaceListener(Opibus plugin) {
        this.plugin = plugin;
        key = new NamespacedKey(plugin, "wasPlaced");
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!isPresent(event.getBlock().getType()) || isExcluded(event.getBlock().getType()))
            return;
        PersistentDataContainer customBlockData = new CustomBlockData(event.getBlockPlaced(), plugin);
        customBlockData.set(key, PersistentDataType.BOOLEAN, true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Material material = event.getBlock().getType();
        PersistentDataContainer customBlockData = new CustomBlockData(block, plugin);
        if (customBlockData.has(key, PersistentDataType.BOOLEAN))
            return;
        SilverManager.get().roll(getProbability(material), block.getLocation());
    }

    // TODO REPLACE WITH OWN SAVING METHOD
    public boolean isPresent(Material material) {
        Optional<HashMap<Double, ArrayList<String>>> optional = Optional.ofNullable((HashMap<Double, ArrayList<String>>) Opibus.groupData.modifyFile.getConfigurationSection("probabilities"));
        if (optional.isEmpty())
            return false;
        return optional.get().values().stream().anyMatch(finder -> finder.contains(material.name()));
    }

    public boolean isExcluded(Material material) {
        Optional<ArrayList<String>> optional = Optional.ofNullable((ArrayList<String>) Opibus.groupData.modifyFile.get("excluded"));
        if (optional.isEmpty())
            return false;
        return optional.get().contains(material.name());
    }

    public int getProbability(Material material) {
        Optional<HashMap<Integer, ArrayList<String>>> optional = Optional.ofNullable((HashMap<Integer, ArrayList<String>>) Opibus.groupData.modifyFile.getConfigurationSection("probabilities"));
        if (optional.isEmpty())
            return 0;
        for (int chance : optional.get().keySet()) {
            if (optional.get().get(chance).contains(material.name())) {
                return chance;
            }
        }
        return 0;
    }
}
