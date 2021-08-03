package de.rexlnico.rexltech.utils.handler;

import de.rexlnico.rexltech.item.Jetpack;
import de.rexlnico.rexltech.utils.networking.PacketHandler;
import de.rexlnico.rexltech.utils.networking.UpdateInputMessage;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class KeyinputHandler {

    private static KeyBinding keyEngine;
    private static KeyBinding keyHover;

    public KeyinputHandler() {
        keyEngine = new KeyBinding("keybind.rexltech.engine", GLFW.GLFW_KEY_V, "RexlTech");
        keyHover = new KeyBinding("keybind.rexltech.hover", GLFW.GLFW_KEY_G, "RexlTech");
        ClientRegistry.registerKeyBinding(keyEngine);
        ClientRegistry.registerKeyBinding(keyHover);
    }

    private static boolean up = false;
    private static boolean down = false;
    private static boolean forwards = false;
    private static boolean backwards = false;
    private static boolean left = false;
    private static boolean right = false;
    private static boolean sprint = false;

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null)
            return;

        ItemStack chest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
        Item item = chest.getItem();

        if (item instanceof Jetpack) {
            handleInput(player, chest);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Minecraft mc = Minecraft.getInstance();
            GameSettings settings = mc.gameSettings;

            if (mc.getConnection() == null)
                return;

            boolean upNow = settings.keyBindJump.isKeyDown();
            boolean downNow = settings.keyBindSneak.isKeyDown();
            boolean forwardsNow = settings.keyBindForward.isKeyDown();
            boolean backwardsNow = settings.keyBindBack.isKeyDown();
            boolean leftNow = settings.keyBindLeft.isKeyDown();
            boolean rightNow = settings.keyBindRight.isKeyDown();
            boolean sprintNow = settings.keyBindSprint.isKeyDown();

            if (upNow != up || downNow != down || forwardsNow != forwards || backwardsNow != backwards || leftNow != left || rightNow != right || sprintNow != sprint) {
                up = upNow;
                down = downNow;
                forwards = forwardsNow;
                backwards = backwardsNow;
                left = leftNow;
                right = rightNow;
                sprint = sprintNow;

                PacketHandler.INSTANCE.sendToServer(new UpdateInputMessage(upNow, downNow, forwardsNow, backwardsNow, leftNow, rightNow, sprintNow));
                InputHandler.update(mc.player, upNow, downNow, forwardsNow, backwardsNow, leftNow, rightNow, sprintNow);
            }
        }
    }

    private static void handleInput(PlayerEntity player, ItemStack stack) {
        if (keyEngine.isPressed()) {
            boolean engine = stack.getOrCreateTag().getBoolean("Active");
            stack.getOrCreateTag().putBoolean("Active", !engine);
//            ITextComponent state = on ? ModTooltips.ON.color(TextFormatting.GREEN).build() : ModTooltips.OFF.color(TextFormatting.RED).build();
//            NetworkHandler.INSTANCE.sendToServer(new ToggleEngineMessage());
//            player.displayClientMessage(ModTooltips.TOGGLE_ENGINE.args(state).build(), true); Active
        }

        if (keyHover.isPressed()) {
            boolean hover = stack.getOrCreateTag().getBoolean("Hover");
            stack.getOrCreateTag().putBoolean("Hover", !hover);

//            ITextComponent state = on ? ModTooltips.ON.color(TextFormatting.GREEN).build() : ModTooltips.OFF.color(TextFormatting.RED).build();
//            NetworkHandler.INSTANCE.sendToServer(new ToggleHoverMessage());
//            player.displayClientMessage(ModTooltips.TOGGLE_HOVER.args(state).build(), true);
        }

    }

}
