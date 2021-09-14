package de.blutmondgilde.guilib.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import de.blutmondgilde.guilib.screen.BaseScreen;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;

public abstract class Widget {
    protected final Font font;

    public Widget(BaseScreen parent) {
        parent.addWidget(this);
        this.font = parent.getFont();
    }

    public abstract void render(PoseStack stack, int mouseX, int mouseY);

    public abstract int getX();

    public abstract int getY();

    public abstract int getWidth();

    public abstract int getHeight();

    public boolean isHovered(double mouseX, double mouseY) {
        if (mouseX < this.getX()) return false;
        if (mouseX > this.getX() + this.getWidth()) return false;
        if (mouseY < this.getY()) return false;
        if (mouseY > this.getY() + this.getHeight()) return false;
        return true;
    }

    protected void fill(PoseStack stack, int x0, int y0, int x1, int y1, int color, int z) {
        fillGradient(stack, x0, y0, x1, y1, color, color, z);
    }

    protected void fillGradient(PoseStack stack, int x0, int y0, int x1, int y1, int color0, int color1, int z) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        fillGradient(stack.last().pose(), bufferbuilder, x0, y0, x1, y1, z, color0, color1);
        tesselator.end();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    @SuppressWarnings("DuplicatedCode")
    private void fillGradient(Matrix4f matrix4f, BufferBuilder bufferBuilder, int x0, int y0, int x1, int y1, int z, int color1, int color2) {
        float alpha0 = (float) (color1 >> 24 & 255) / 255.0F;
        float red0 = (float) (color1 >> 16 & 255) / 255.0F;
        float green0 = (float) (color1 >> 8 & 255) / 255.0F;
        float blue0 = (float) (color1 & 255) / 255.0F;
        float f4 = (float) (color2 >> 24 & 255) / 255.0F;
        float red1 = (float) (color2 >> 16 & 255) / 255.0F;
        float green1 = (float) (color2 >> 8 & 255) / 255.0F;
        float blue1 = (float) (color2 & 255) / 255.0F;
        bufferBuilder.vertex(matrix4f, (float) x1, (float) y0, (float) z).color(red0, green0, blue0, alpha0).endVertex();
        bufferBuilder.vertex(matrix4f, (float) x0, (float) y0, (float) z).color(red0, green0, blue0, alpha0).endVertex();
        bufferBuilder.vertex(matrix4f, (float) x0, (float) y1, (float) z).color(red1, green1, blue1, f4).endVertex();
        bufferBuilder.vertex(matrix4f, (float) x1, (float) y1, (float) z).color(red1, green1, blue1, f4).endVertex();
    }

    public static void blit(PoseStack p_93144_, int p_93145_, int p_93146_, int p_93147_, float p_93148_, float p_93149_, int p_93150_, int p_93151_, int p_93152_, int p_93153_) {
        innerBlit(p_93144_, p_93145_, p_93145_ + p_93150_, p_93146_, p_93146_ + p_93151_, p_93147_, p_93150_, p_93151_, p_93148_, p_93149_, p_93153_, p_93152_);
    }

    private static void innerBlit(PoseStack p_93188_, int p_93189_, int p_93190_, int p_93191_, int p_93192_, int p_93193_, int p_93194_, int p_93195_, float p_93196_, float p_93197_, int p_93198_, int p_93199_) {
        innerBlit(p_93188_.last()
                .pose(), p_93189_, p_93190_, p_93191_, p_93192_, p_93193_, (p_93196_ + 0.0F) / (float) p_93198_, (p_93196_ + (float) p_93194_) / (float) p_93198_, (p_93197_ + 0.0F) / (float) p_93199_, (p_93197_ + (float) p_93195_) / (float) p_93199_);
    }

    private static void innerBlit(Matrix4f p_93113_, int p_93114_, int p_93115_, int p_93116_, int p_93117_, int p_93118_, float p_93119_, float p_93120_, float p_93121_, float p_93122_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(p_93113_, (float) p_93114_, (float) p_93117_, (float) p_93118_).uv(p_93119_, p_93122_).endVertex();
        bufferbuilder.vertex(p_93113_, (float) p_93115_, (float) p_93117_, (float) p_93118_).uv(p_93120_, p_93122_).endVertex();
        bufferbuilder.vertex(p_93113_, (float) p_93115_, (float) p_93116_, (float) p_93118_).uv(p_93120_, p_93121_).endVertex();
        bufferbuilder.vertex(p_93113_, (float) p_93114_, (float) p_93116_, (float) p_93118_).uv(p_93119_, p_93121_).endVertex();
        bufferbuilder.end();
        BufferUploader.end(bufferbuilder);
    }
}
