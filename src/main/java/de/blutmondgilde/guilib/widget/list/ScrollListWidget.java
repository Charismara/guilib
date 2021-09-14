package de.blutmondgilde.guilib.widget.list;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import de.blutmondgilde.guilib.listener.mouse.IMouseScrollListener;
import de.blutmondgilde.guilib.screen.BaseScreen;
import de.blutmondgilde.guilib.widget.Widget;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class ScrollListWidget extends Widget implements IMouseScrollListener {
    private final int x, y, width, height, scrollBarWidth;
    @Getter
    private final ArrayList<ScrollListEntry> entries;
    private double scrollAmount;
    private final ScrollBarWidget scrollBarWidget;

    public ScrollListWidget(BaseScreen parent, int x, int y, int width, int height, int scrollBarWidth) {
        super(parent);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.entries = Lists.newArrayList();
        this.scrollAmount = 0;
        this.scrollBarWidth = scrollBarWidth;
        this.scrollBarWidget = new ScrollBarWidget(parent, this);
    }

    public void addEntry(ScrollListEntry entry) {
        this.entries.add(entry);
    }

    public void addEntries(ScrollListEntry... entries) {
        this.entries.addAll(Arrays.stream(entries).toList());
    }

    public void clearEntries() {
        this.entries.clear();
    }

    public void removeEntry(ScrollListEntry entry) {
        this.entries.remove(entry);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY) {
        double currentY = this.y - this.scrollAmount;
        for (ScrollListEntry entry : this.entries) {
            if (entryIsVisible(entry, currentY)) entry.renderEntry(stack, mouseX, mouseY, (int) currentY);
            currentY += entry.getHeight();
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    public boolean entryIsVisible(ScrollListEntry entry, double currentY) {
        if (this.y > currentY + entry.getHeight()) return false;
        if (currentY > this.height) return false;
        return true;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    public int getInnerWidth() {
        if (needsScrollbar()) {
            return getWidth() - getScrollbarWidth();
        } else {
            return getWidth();
        }
    }

    public int getScrollbarWidth() {
        return this.scrollBarWidth;
    }

    public double getMaxScroll() {
        return this.entries.stream()
                .flatMapToInt(scrollListEntry -> IntStream.of(scrollListEntry.getHeight()))
                .sum() - this.height;
    }

    @Override
    public void onScroll(double mouseX, double mouseY, double delta) {
        if (this.needsScrollbar()) {
            this.scrollAmount = Math.min(getMaxScroll(), Math.max(0, this.scrollAmount - delta * 4F));
            this.scrollBarWidget.setScrollbarY((int) this.scrollAmount);
        }
    }

    public boolean needsScrollbar() {
        return getMaxScroll() > 0d;
    }
}
