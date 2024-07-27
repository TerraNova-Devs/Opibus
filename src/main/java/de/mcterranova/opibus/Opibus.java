package de.mcterranova.opibus;

import com.jeff_media.customblockdata.CustomBlockData;
import de.mcterranova.bona.lib.YMLHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import de.mcterranova.opibus.database.HikariCP;
import de.mcterranova.opibus.silver.BlockPlaceListener;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;

public final class Opibus extends JavaPlugin {

    public static Random randomGenerator;
    public static YMLHandler groupData;
    public static Opibus instance;
    HikariCP hikari;
    Logger logger;

    @Override
    public void onEnable() {
        //Listener der bei Pistenbewegung/Abbau des Blocks die Blockdata mitnimmt/löscht
        CustomBlockData.registerListener(this);

        randomGenerator = new Random();
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
