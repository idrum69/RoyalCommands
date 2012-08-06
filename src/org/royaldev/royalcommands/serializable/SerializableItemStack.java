package org.royaldev.royalcommands.serializable;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Serializable ItemStack
 * <p/>
 * Needed for SerializableCraftInventory
 *
 * @author jkcclemens
 * @see SerializableCraftInventory
 * @see SerializableCraftEnchantment
 * @since 0.2.4pre
 */
public class SerializableItemStack implements Serializable {

    /**
     * Amount of items in the ItemStack - defaults to 0
     */
    private int amount = 0;
    /**
     * Durability, or damage, of the items in the ItemStack - defaults to 0
     */
    private short durability = 0;
    /**
     * Item ID of the ItemStack - defaults to 0
     */
    private int type = 0;
    /**
     * Map containing enchantments and their respective levels on this ItemStack - defaults to an empty map
     */
    private Map<SerializableCraftEnchantment, Integer> enchantments = new HashMap<SerializableCraftEnchantment, Integer>();
    /**
     * Material data on this ItemStack - defaults to 0
     */
    private byte materialData = 0;

    /**
     * Serializable ItemStack for saving to files.
     *
     * @param i ItemStack to convert
     */
    public SerializableItemStack(ItemStack i) {
        if (i == null) return;
        amount = i.getAmount();
        durability = i.getDurability();
        for (Enchantment e : i.getEnchantments().keySet())
            enchantments.put(new SerializableCraftEnchantment(e), i.getEnchantments().get(e));
        type = i.getTypeId();
        materialData = i.getData().getData();
    }

    /**
     * Returns a SIS to an ItemStack (keeps enchantments, amount, data, and durability)
     *
     * @return Original ItemStack
     */
    public ItemStack getItemStack() {
        ItemStack i = new ItemStack(type, amount, durability, materialData);
        for (SerializableCraftEnchantment sce : enchantments.keySet())
            i.addUnsafeEnchantment(sce.getEnchantment(), enchantments.get(sce));
        return i;
    }

}
