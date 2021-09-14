package de.blutmondgilde.guilib.widget.list;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import de.blutmondgilde.guilib.listener.mouse.IMouseClickListener;
import de.blutmondgilde.guilib.screen.BaseScreen;
import de.blutmondgilde.guilib.widget.Widget;
import de.blutmondgilde.otherlivingbeings.OtherLivingBeings;
import lombok.Getter;
import lombok.Setter;

public abstract class ScrollListEntry extends Widget implements IMouseClickListener {
    @Setter
    @Getter
    private int x, y;
    @Getter
    private final int width, height;

    public ScrollListEntry(BaseScreen parent, int x, int y, int width, int height) {
        super(parent);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY) {
        //Do Nothing since we render it using the ScrollList
    }

    public abstract void renderEntry(PoseStack stack, int mouseX, int mouseY,int relativY);

    @Override
    public void onClick(double mouseX, double mouseY, int mouseButtonId) {
        if (!this.isHovered(mouseX, mouseY)) return;
        OtherLivingBeings.getLogger().info("Passed hover check");
        if (mouseButtonId != InputConstants.MOUSE_BUTTON_LEFT) return;
        OtherLivingBeings.getLogger().info("Passed button check");
        onLeftClick();
    }

    public abstract void onLeftClick();
}
