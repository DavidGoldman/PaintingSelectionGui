package com.mcf.davidee.paintinggui.gui;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumArt;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Button.ButtonHandler;
import com.mcf.davidee.guilib.core.Scrollbar.Shiftable;

public class PaintingButton extends Button implements Shiftable {
	
	public static ResourceLocation TEXTURE = new ResourceLocation("textures/painting/paintings_kristoffer_zetterstrand.png");
	public static int KZ_WIDTH = 256, KZ_HEIGHT = 256;
	
	private static final int BORDER = 3;
	private static final int YELLOW = -256;
	
	public final EnumArt art;

	public PaintingButton(EnumArt art, ButtonHandler handler) {
		super(art.sizeX, art.sizeY, handler);
		
		this.art = art;
	}


	@Override
	public void draw(int mx, int my) {
		mc.renderEngine.func_110577_a(TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(x, y, art.offsetX, art.offsetY, width, height);
		
		if (inBounds(mx, my)) { //Hover over, draw the 4 outlining rectangles
			drawRect(x - BORDER, y - BORDER, x + width + BORDER, y, YELLOW); //upper left to upper right
			drawRect(x - BORDER, y + height, x + width + BORDER, y + height + BORDER, YELLOW); //lower left to lower right
			drawRect(x - BORDER, y, x, y + height, YELLOW); //middle rectangle to the left
			drawRect(x + width, y, x + width + BORDER, y + height, YELLOW); //middle rectangle to the right
		}
	}
	
	public void handleClick(int mx, int my) {
		mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
		super.handleClick(mx, my);
	}
	
	public void drawTexturedModalRect(int x, int y, int xOffset, int yOffset, int width, int height) {
		float f = 1 /(float)KZ_WIDTH;
		float f1 =1 /(float)KZ_HEIGHT;

		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x + 0, y + height, zLevel, (float)(xOffset + 0) * f, (float)(yOffset + height) * f1);
		tessellator.addVertexWithUV(x + width, y + height, zLevel, (float)(xOffset + width) * f, (float)(yOffset + height) * f1);
		tessellator.addVertexWithUV(x + width, y + 0, zLevel, (float)(xOffset + width) * f, (float)(yOffset + 0) * f1);
		tessellator.addVertexWithUV(x + 0, y + 0, zLevel, (float)(xOffset + 0) * f, (float)(yOffset + 0) * f1);
		tessellator.draw();
	}

	@Override
	public void shiftY(int dy) {
		this.y += dy;
	}
	
	public void shiftX(int dx) {
		this.x += dx;
	}

}
