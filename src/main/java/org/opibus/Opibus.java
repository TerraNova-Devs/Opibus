package org.opibus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.opibus.database.HikariCP;
import org.opibus.silver.listener.BlockPlaceListener;
import org.opibus.silver.logic.SilverManager;
import org.opibus.utils.YMLHandler;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public final class Opibus extends JavaPlugin {

    HikariCP hikari;
    Logger logger;
    public static Random randomGenerator;
    public static YMLHandler groupData;
    public static Opibus instance;

    @Override
    public void onEnable() {
        randomGenerator = new Random();
        SilverManager.init();
        instance = this;
        logger = getLogger();

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        /*
        try {
            hikari = new HikariCP(this);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
         */

        listenerRegistry();
        try {
            groupData = new YMLHandler("groups.yml", this.getDataFolder());
        } catch (IOException e) {
            System.out.print(e);
        }
    }

    public void listenerRegistry() {
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(this), this);
    }

    @Override
    public void onDisable() {
        groupData.unloadYAML();
    }

    /*
    by saku
    placeholder idk i might need this in the foreseeable future
    Map<String, Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>();
        list.add("STONE");
        list.add("OAK_LOG");
        list.add("SPRUCE_LOG");
        list2.add("DIAMOND_ORE");
        list2.add("IRON_ORE");
        list3.add("STONE");
        list3.add("DIRT");
        list3.add("GRASS_BLOCK");
        map.put("1000", list.toString());
        map.put("1", list2.toString());
        groupData.modifyFile.createSection("groups", map);
        groupData.modifyFile.set("excluded", list3.toString());
     */
}
