package de.blutmondgilde.guilib.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import de.blutmondgilde.guilib.listener.ICloseListener;
import de.blutmondgilde.guilib.listener.IHoverListener;
import de.blutmondgilde.guilib.listener.ITickListener;
import de.blutmondgilde.guilib.listener.keyboard.IKeyPressedListener;
import de.blutmondgilde.guilib.listener.keyboard.IKeyReleasedListener;
import de.blutmondgilde.guilib.listener.mouse.IMouseClickListener;
import de.blutmondgilde.guilib.listener.mouse.IMouseDragListener;
import de.blutmondgilde.guilib.listener.mouse.IMouseReleasedListener;
import de.blutmondgilde.guilib.listener.mouse.IMouseScrollListener;
import de.blutmondgilde.guilib.widget.Widget;
import lombok.Setter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

import java.util.ArrayList;

public abstract class BaseScreen extends Screen {
    private double baseGuiScale;
    private double restoreGuiScale;
    private final ArrayList<Widget> children;
    @Setter
    private Screen returnScreen;


    protected BaseScreen() {
        super(new TextComponent(""));
        this.baseGuiScale = 2;
        this.returnScreen = null;
        this.children = Lists.newArrayList();
    }

    @Override
    protected void init() {

        getMinecraft().keyboardHandler.setSendRepeatsToGui(true);
        this.removeWidgets(); //Clear existing widgets
        initGui();
    }

    protected abstract void initGui();

    @Override
    public void onClose() {
        children.stream()
                .filter(widget -> widget instanceof ICloseListener)
                .map(widget -> (ICloseListener) widget)
                .forEach(ICloseListener::onClose);

        getMinecraft().keyboardHandler.setSendRepeatsToGui(false);
        getMinecraft().getWindow().setGuiScale(this.restoreGuiScale);
        getMinecraft().setScreen(this.returnScreen);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTick) {
        this.children.forEach(widget -> widget.render(stack, mouseX, mouseY));
        this.children.stream()
                .filter(widget -> widget instanceof IHoverListener)
                .filter(widget -> widget.isHovered(mouseX, mouseY))
                .map(widget -> (IHoverListener) widget)
                .forEach(iHoverListener -> iHoverListener.onHover(stack, mouseX, mouseY));
    }

    @Override
    public void tick() {
        children.stream()
                .filter(widget -> widget instanceof ITickListener)
                .map(widget -> (ITickListener) widget)
                .forEach(ITickListener::tick);
    }

    public void addWidget(Widget widget) {
        this.children.add(widget);
    }

    public void removeWidgets() {
        this.children.clear();
    }

    public void removeWidget(Widget widget) {
        this.children.remove(widget);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        children.stream()
                .filter(widget -> widget instanceof IMouseScrollListener)
                .map(widget -> (IMouseScrollListener) widget)
                .forEach(iMouseScrollListener -> iMouseScrollListener.onScroll(mouseX, mouseY, delta));
        return super.mouseScrolled(mouseX, mouseX, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButtonId) {
        children.stream()
                .filter(widget -> widget instanceof IMouseClickListener)
                .map(widget -> (IMouseClickListener) widget)
                .forEach(iMouseScrollListener -> iMouseScrollListener.onClick(mouseX, mouseY, mouseButtonId));
        return super.mouseClicked(mouseX, mouseY, mouseButtonId);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButtonId, double lastDraggedX, double lastDraggedY) {
        children.stream()
                .filter(widget -> widget instanceof IMouseDragListener)
                .map(widget -> (IMouseDragListener) widget)
                .forEach(iMouseScrollListener -> iMouseScrollListener.onMouseDrag(mouseX, mouseY, mouseButtonId, lastDraggedX, lastDraggedY));
        return super.mouseDragged(mouseX, mouseY, mouseButtonId, lastDraggedX, lastDraggedY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButtonId) {
        children.stream()
                .filter(widget -> widget instanceof IMouseReleasedListener)
                .map(widget -> (IMouseReleasedListener) widget)
                .forEach(iMouseScrollListener -> iMouseScrollListener.onRelease(mouseX, mouseY, mouseButtonId));
        return super.mouseReleased(mouseX, mouseY, mouseButtonId);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifierCode) {
        children.stream()
                .filter(widget -> widget instanceof IKeyPressedListener)
                .map(widget -> (IKeyPressedListener) widget)
                .forEach(iMouseScrollListener -> iMouseScrollListener.onPress(keyCode, scanCode, modifierCode));
        return super.keyPressed(keyCode, scanCode, modifierCode);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifierCode) {
        children.stream()
                .filter(widget -> widget instanceof IKeyReleasedListener)
                .map(widget -> (IKeyReleasedListener) widget)
                .forEach(iMouseScrollListener -> iMouseScrollListener.onRelease(keyCode, scanCode, modifierCode));
        return super.keyReleased(keyCode, scanCode, modifierCode);
    }

    public Font getFont() {
        return this.font;
    }

    public float getScreenScaleFactor() {
        return 1f / 1920 * this.width;
    }

    public int scalePos(int initialValue) {
        return Math.round(initialValue * getScreenScaleFactor());
    }
}
