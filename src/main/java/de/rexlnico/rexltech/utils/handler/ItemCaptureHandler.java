package de.rexlnico.rexltech.utils.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.LinkedList;

public class ItemCaptureHandler {
    public static ItemCaptureHandler INSTANCE = new ItemCaptureHandler();
    static ThreadLocal<LinkedList<ItemStack>> capturing = new ThreadLocal<>();

    public static void startCapturing() {
        if (capturing.get() != null) {
            throw new IllegalStateException();
        }
        capturing.set(new LinkedList<>());
    }

    public static LinkedList<ItemStack> stopCapturing() {
        LinkedList<ItemStack> list = capturing.get();
        if (list == null)
            throw new IllegalStateException();
        capturing.set(null);
        return list;
    }

    @SubscribeEvent
    public void onItemJoin(EntityJoinWorldEvent event) {
        LinkedList<ItemStack> list = capturing.get();
        if (list == null) return;

        Entity entity = event.getEntity();
        if (entity instanceof ItemEntity) {
            ItemStack stack = ((ItemEntity) entity).getItem();
            list.add(stack);
            event.setCanceled(true);
        }
    }
}