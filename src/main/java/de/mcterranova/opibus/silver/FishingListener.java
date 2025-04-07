package de.mcterranova.opibus.silver;

import com.nexomc.nexo.api.NexoItems;
import de.mcterranova.opibus.Opibus;
import de.mcterranova.opibus.lib.SilverManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingListener implements Listener {

    private final Opibus plugin;
    private int fishingChance;

    public FishingListener(Opibus plugin) {
        this.plugin = plugin;
        fishingChance = plugin.getConfig().getInt("fishing.silverChance", 20);
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            if (SilverManager.roll(fishingChance)) {
                Location loc = event.getPlayer().getLocation();
                loc.getWorld().dropItemNaturally(loc,
                        NexoItems.itemFromId("terranova_silver").build()
                );
            }
        }
    }
}
