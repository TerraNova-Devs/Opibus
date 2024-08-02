package de.mcterranova.opibus.lib;

import org.bukkit.Material;

/**
 * @author sakubami
 * @since 0.1.0
 */
public enum SilverState {
    NUGGET(Material.IRON_NUGGET),
    INGOT(Material.IRON_INGOT),
    BLOCK(Material.IRON_BLOCK);

    public Material material;

    SilverState(Material material) {
        this.material = material;
    }

    private static final SilverState[] values = values();
    public SilverState getNext() {
        if (this.ordinal() == 2)
            return BLOCK;
        return values[(this.ordinal() + 1) % values.length];
    }
    public SilverState getPrevious() {
        if (this.ordinal() == 0)
            return NUGGET;
        return values[(((this.ordinal() - 1) % values.length + values.length) % values.length)];
    }

    public Material getMaterial() {
        return material;
    }
    public static SilverState valueOf(Material material) { return SilverState.valueOf( material.name().replace("IRON_", "")); }
}
