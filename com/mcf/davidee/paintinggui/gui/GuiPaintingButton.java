package com.mcf.davidee.paintinggui.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumArt;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiPaintingButton extends GuiButton {
	
	private static ResourceLocation TEXTURE = new ResourceLocation("textures/painting/paintings_kristoffer_zetterstrand.png");
    
	protected EnumArt art;
    private static final int EXT = 3;
    private static final int YELLOW = -256;
    
    public static int KZ_WIDTH = 256, KZ_HEIGHT = 256;
    private final int maxY;
    

    public GuiPaintingButton(int id, int x, int y, EnumArt art, int maxY) {
        super(id, x, y, art.sizeX, art.sizeY, art.title);
        this.art = art;
        this.maxY=maxY;
    }
    
    public void shiftY(int dy) {
    	yPosition += dy;
    }
    
    public void shiftX(int dx) {
    	xPosition += dx;
    }
    
    public int left() {
    	return xPosition;
    }
    
    public int right() {
    	return xPosition + width;
    }
    
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
    	return super.mousePressed(par1Minecraft, par2, par3) && shouldDraw();
    }
    
    @Override
    public void drawButton(Minecraft mc, int i, int j) {
        if (drawButton && shouldDraw()) {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.renderEngine.func_110577_a(TEXTURE);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;
            drawTexturedModalRect(xPosition, yPosition, art.offsetX, art.offsetY, width, height);

            if (getHoverState(flag) == 2) { //draw the 4 outlining rectangles
                drawRect(xPosition - EXT, yPosition - EXT, xPosition + width + EXT, yPosition, YELLOW); //upper left to upper right
                drawRect(xPosition - EXT, yPosition + height, xPosition + width + EXT, yPosition + height + EXT, YELLOW); //lower left to lower right
                drawRect(xPosition - EXT, yPosition, xPosition, yPosition + height, YELLOW); //middle rectangle to the left
                drawRect(xPosition + width, yPosition, xPosition + width + EXT, yPosition + height, YELLOW); //middle rectangle to the right
            }
        }
    }
    public boolean shouldDraw() {
    	return  yPosition >= GuiPaintingSelection.TOP && yPosition + height <= maxY;
    }
    
    public void drawTexturedModalRect(int x, int y, int xOffset, int yOffset, int width, int height) {
        float f = 1/ (float)KZ_WIDTH;
        float f1 =1/ (float)KZ_HEIGHT;
        
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, zLevel, (float)(xOffset + 0) * f, (float)(yOffset + height) * f1);
        tessellator.addVertexWithUV(x + width, y + height, zLevel, (float)(xOffset + width) * f, (float)(yOffset + height) * f1);
        tessellator.addVertexWithUV(x + width, y + 0, zLevel, (float)(xOffset + width) * f, (float)(yOffset + 0) * f1);
        tessellator.addVertexWithUV(x + 0, y + 0, zLevel, (float)(xOffset + 0) * f, (float)(yOffset + 0) * f1);
        tessellator.draw();
    }
}
