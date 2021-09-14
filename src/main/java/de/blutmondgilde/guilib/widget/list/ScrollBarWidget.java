package de.blutmondgilde.guilib.widget.list;

import com.mojang.blaze3d.vertex.PoseStack;
import de.blutmondgilde.guilib.screen.BaseScreen;
import de.blutmondgilde.guilib.widget.Widget;
import de.blutmondgilde.otherlivingbeings.util.BeingResourceLocation;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class ScrollBarWidget extends Widget {
    private final ResourceLocation scrollBarTexture = new BeingResourceLocation("textures/gui/scrollbar.png");
    private final Color backgroundColor = new Color(56, 93, 96, 255);
    private final Color barColor = new Color(126, 180, 168, 255);
    private final ScrollListWidget parent;
    @Setter
    @Getter
    private int scrollbarY;

    public ScrollBarWidget(BaseScreen parent, ScrollListWidget parentList) {
        super(parent);
        this.parent = parentList;
        this.scrollbarY = 0;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY) {
        if (this.parent.needsScrollbar()) {
            renderBarBackground(stack, mouseX, mouseY);
            renderBar(stack, mouseX, mouseY);
        }
    }

    protected void renderBar(PoseStack stack, int mouseX, int mouseY) {
        fill(stack,
                this.getX(), this.getScrollbarY(),
                this.getX() + this.getWidth(), this.getScrollbarY() + getScrollbarHeight(),
                barColor.getRGB(), 0);
    }

    protected void renderBarBackground(PoseStack stack, int mouseX, int mouseY) {
        fill(stack,
                this.getX(), this.getY(),
                this.getX() + this.getWidth(), this.getY() + this.getHeight(),
                backgroundColor.getRGB(), 0);
    }

    public int getScrollbarHeight() {
        return this.getHeight() - (int) this.parent.getMaxScroll();
    }

    @Override
    public int getX() {
        return this.parent.getX() + this.parent.getInnerWidth();
    }

    @Override
    public int getY() {
        return this.parent.getY();
    }

    @Override
    public int getWidth() {
        return this.parent.getScrollbarWidth();
    }

    @Override
    public int getHeight() {
        return this.parent.getHeight();
    }
}
