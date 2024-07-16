package org.opibus;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.opibus.database.HikariCP;
import org.opibus.silver.listener.BlockPlaceListener;
import org.opibus.silver.logic.SilverManager;
import org.opibus.utils.YMLHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
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
        } catch (IOException ignored) {
        }
    }

    public void listenerRegistry() {
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(this), this);
    }

    @Override
    public void onDisable() {
        groupData.unloadYAML();
    }
}
