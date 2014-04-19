package com.mcf.davidee.paintinggui.gui;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.item.EntityPainting.EnumArt;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.mcf.davidee.paintinggui.PaintingSelectionMod;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class GuiPaintingSelection extends GuiScreen
{
	private String screenTitle;
	private static final int CLOSE_ID = 300;

	private int id;
	public static final int TOP = 21;
	private int bottom;
	private int offset;
	private int minYOffset;
	private String[] art;


	public GuiPaintingSelection(String[] art, int id) {
		super();
		
		this.art = art;
		this.id = id;
		screenTitle = "Select a Painting";
		Keyboard.enableRepeatEvents(true);
	}

	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == CLOSE_ID)
			mc.displayGuiScreen(null);
		else if (guibutton instanceof GuiPaintingButton) {
			String s = ((GuiPaintingButton)guibutton).art.title;
			FMLProxyPacket packet = PaintingSelectionMod.createPacket(id, new String[] {s});
			if (packet != null)
				PaintingSelectionMod.Channel.sendToServer(packet);
			mc.displayGuiScreen(null);
		}

	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	public void initGui() {
		buttonList.clear();
		bottom = height-30;

		EnumArt[] enumArts = EnumArt.values();
		ArrayList<EnumArt> validArts = new ArrayList<EnumArt>();

		for (String s : art){
			for (EnumArt enumArt : enumArts){
				if (enumArt.title.equals(s)){
					validArts.add(enumArt);
					break;
				}
			}
		}

		EnumArt[] validArtsArray = validArts.toArray(new EnumArt[0]);

		final int START_X = 10;
		final int END_X = width - 10;
		final int GAP = 5;

		int x = START_X;
		int y = 30, maxY = validArtsArray[0].sizeY;
		int id = 0;
		int rowStartIndex = 0;

		for (int index = 0; index < validArtsArray.length; ++index) {
			EnumArt cur = validArtsArray[index];

			if (x + cur.sizeX > END_X) {
				centerRow(buttonList, rowStartIndex, index - 1);
				rowStartIndex = index;
				x = START_X;
				y += GAP + maxY;
				maxY = cur.sizeY;
			}
			else if (cur.sizeY > maxY)
				maxY = cur.sizeY;

			buttonList.add(new GuiPaintingButton(++id, x, y, cur,bottom));
			x += cur.sizeX + GAP;
		}

		//Center the last row
		centerRow(buttonList, rowStartIndex, validArtsArray.length - 1);

		minYOffset = bottom - (y + maxY);
		if (minYOffset >0)
			minYOffset = 0;

		buttonList.add(new GuiButton(CLOSE_ID, width / 2 - 100, this.height - 25, "Cancel"));
	}

	private void centerRow(List list, int start, int end) {
		int left = ((GuiPaintingButton)list.get(start)).left();
		int right = ((GuiPaintingButton)list.get(end)).right();

		//We're 10 pixels away from each edge
		int correction = (width - 20 - (right - left)) / 2;
		for (int i = start; i <= end; ++i) {
			GuiPaintingButton b = (GuiPaintingButton)list.get(i);
			b.shiftX(correction);
		}
	}

	public void updateScreen() {
		if (mc.thePlayer == null || !mc.thePlayer.isEntityAlive())
			mc.displayGuiScreen(null);
	}

	public void handleMouseInput() {
		int l = Mouse.getEventDWheel();

		if (l != 0)
		{
			if (l > 0)
			{
				l = 3;
			}
			else if (l < 0)
			{
				l = -3;
			}
			offset(l);
		}
		super.handleMouseInput();
	}

	public void handleKeyboardInput() {
		if (Keyboard.getEventKeyState()){
			switch(Keyboard.getEventKey()){
			case Keyboard.KEY_UP:
				offset(3);
				break;
			case Keyboard.KEY_DOWN:
				offset(-3);
				break;
			}
		}
	}
	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, screenTitle, width / 2, 10, 0xffffff);
		super.drawScreen(par1, par2, par3);
	}

	private void offset(int i) {
		int dif = offset + i;
		if (dif > 0)
			dif = 0;
		if (dif < minYOffset)
			dif = minYOffset;
		for (Object o : buttonList){
			if (o instanceof GuiPaintingButton)
				((GuiPaintingButton)o).shiftY(dif-offset);
		}
		offset = dif; 
	}
}
