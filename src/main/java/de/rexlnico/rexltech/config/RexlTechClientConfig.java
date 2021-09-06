package de.rexlnico.rexltech.config;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.rexlnico.rexltech.client.HudHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.settings.IteratableOption;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nonnull;

public class RexlTechClientConfig extends Screen {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> jetpackHudPositionConfigValue;

    static {
        BUILDER.push("RexlTech Client Config");

        jetpackHudPositionConfigValue = BUILDER.comment("The position of the jetpack hud").define("jetpack hud", 1);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    private static final int TITLE_HEIGHT = 8;
    private static final int DONE_BUTTON_TOP_OFFSET = 26;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int OPTIONS_LIST_TOP_HEIGHT = 24;
    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;

    private OptionsRowList optionsRowList;
    Screen parentScreen;

    public RexlTechClientConfig(Screen parentScreen) {
        super(new TranslationTextComponent("rexltech.config.title"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        this.optionsRowList = new OptionsRowList(this.minecraft, this.width, this.height, OPTIONS_LIST_TOP_HEIGHT, this.height - OPTIONS_LIST_BOTTOM_OFFSET, OPTIONS_LIST_ITEM_HEIGHT);
        IteratableOption option = new IteratableOption("rexltech.config.jetpackHud.title", (gameSettings, integer) ->
                jetpackHudPositionConfigValue.set((jetpackHudPositionConfigValue.get() + integer) % HudHandler.HudPosition.values().length),
                (gameSettings, iteratableOption) -> {
                    TranslationTextComponent translationTextComponent = new TranslationTextComponent("rexltech.config.jetpackHud.button.title");
                    translationTextComponent.appendString(": " + HudHandler.HudPosition.values()[jetpackHudPositionConfigValue.get()].toString());
                    return translationTextComponent;
                });
//        List<IReorderingProcessor> list = new ArrayList<>();
//        Arrays.stream(HudHandler.HudPosition.values()).forEach(hudPosition -> list.add(IReorderingProcessor.fromString(hudPosition.toString(), Style.EMPTY)));
//        option.setOptionValues(list);
        this.optionsRowList.addOption(option);
        this.children.add(this.optionsRowList);
        this.addButton(new Button((this.width - BUTTON_WIDTH) / 2, this.height - DONE_BUTTON_TOP_OFFSET, BUTTON_WIDTH, BUTTON_HEIGHT, new TranslationTextComponent("gui.done"), button -> this.onClose()
        ));
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.optionsRowList.render(matrixStack, mouseX, mouseY, partialTicks);
        drawCenteredString(matrixStack, this.font, this.title.getString(), this.width / 2, TITLE_HEIGHT, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        this.minecraft.currentScreen = parentScreen;
    }
}
