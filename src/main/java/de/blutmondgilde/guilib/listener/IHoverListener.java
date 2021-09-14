package de.blutmondgilde.guilib.listener;

import com.mojang.blaze3d.vertex.PoseStack;

public interface IHoverListener {
    void onHover(PoseStack stack, int mouseX, int mouseY);
}
