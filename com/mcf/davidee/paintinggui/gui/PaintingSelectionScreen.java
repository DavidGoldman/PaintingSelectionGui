package com.mcf.davidee.paintinggui.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.EnumArt;

import com.mcf.davidee.guilib.basic.BasicScreen;
import com.mcf.davidee.guilib.basic.Label;
import com.mcf.davidee.guilib.core.Button;
import com.mcf.davidee.guilib.core.Button.ButtonHandler;
import com.mcf.davidee.guilib.core.Container;
import com.mcf.davidee.guilib.core.Scrollbar;
import com.mcf.davidee.guilib.vanilla.ButtonVanilla;
import com.mcf.davidee.guilib.vanilla.ScrollbarVanilla;
import com.mcf.davidee.paintinggui.PaintingSelectionMod;

import org.lwjgl.input.Keyboard;
import cpw.mods.fml.common.network.PacketDispatcher;

public class PaintingSelectionScreen extends BasicScreen implements ButtonHandler {

	private Container container, paintingContainer;
	private Scrollbar scrollbar;
	private Label title;
	private Button back;
	private PaintingButton[] buttons;

	private final int paintingID;
	private final String[] art;

	public PaintingSelectionScreen(String[] art, int paintingID) {
		super(null);

		this.art = art;
		this.paintingID = paintingID;
	}

	@Override
	public void keyTyped(char c, int code) {
		super.keyTyped(c, code);
		if (code == Keyboard.KEY_ESCAPE || code == Keyboard.KEY_E)
		{
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		}
	}

	@Override
	protected void reopenedGui() { }

	@Override
	public void drawBackground() {
		super.drawBackground();
		drawRect(paintingContainer.left(), paintingContainer.top(), paintingContainer.right()-10, paintingContainer.bottom(), 0x44444444);
	}

	public void updateScreen() {
		super.updateScreen();
		if (mc.thePlayer == null || !mc.thePlayer.isEntityAlive())
			mc.displayGuiScreen(null);
	}

	@Override
	protected void revalidateGui() {
		final int START_X = 10, START_Y = 30;
		final int END_X = width - 10;
		final int GAP = 5;

		title.setPosition(width/2, 10);
		back.setPosition(width/2 - 100, height - 25);
		scrollbar.setPosition(width - 10, START_Y - 2);

		int x = START_X;
		int y = START_Y, prevHeight = buttons[0].getHeight();
		int rowStartIndex = 0;

		for (int index = 0; index < buttons.length; ++index) {
			PaintingButton cur = buttons[index];

			if (x + cur.getWidth() > END_X || prevHeight > cur.getHeight()) {
				centerRow(rowStartIndex, index - 1);
				rowStartIndex = index;
				x = START_X;
				y += GAP + prevHeight;
				prevHeight = cur.getHeight();
			}

			cur.setPosition(x, y);
			x += cur.getWidth() + GAP;
		}

		centerRow(rowStartIndex, buttons.length - 1);

		paintingContainer.revalidate(START_X, START_Y - 2, width - 10, height - 55);
		container.revalidate(0, 0, width, height);
	}

	private void centerRow(int start, int end) {
		int left = buttons[start].getX();
		int right = buttons[end].getX() + buttons[end].getWidth();

		//We're 10 pixels away from each edge
		int correction = (width - 20 - (right - left)) / 2;
		for (int i = start; i <= end; ++i) 
			buttons[i].shiftX(correction);
	}

	@Override
	protected void createGui() {
		scrollbar = new ScrollbarVanilla(10);
		paintingContainer = new Container(scrollbar, 0, 4);
		container = new Container();
		title = new Label("Select a Painting");
		back = new ButtonVanilla("Cancel", new CloseHandler());

		EnumArt[] enumArts = EnumArt.values();
		ArrayList<EnumArt> validArts = new ArrayList<EnumArt>();
		for (String s : art)
			for (EnumArt enumArt : enumArts)
				if (enumArt.title.equals(s)){
					validArts.add(enumArt);
					break;
				}

		EnumArt[] validArtsArray = validArts.toArray(new EnumArt[0]);
		buttons = new PaintingButton[validArtsArray.length];
		for (int i = 0; i < validArtsArray.length; ++i)
			buttons[i] = new PaintingButton(validArtsArray[i], this);

		container.addWidgets(title, back);
		paintingContainer.addWidgets(buttons);

		containers.add(paintingContainer);
		containers.add(container);
		selectedContainer = paintingContainer;
	}

	@Override
	public void buttonClicked(Button button) {
		String artTitle = ((PaintingButton)button).art.title;
		Packet250CustomPayload packet = PaintingSelectionMod.createPacket(paintingID, new String[] {artTitle});
		if (packet != null)
			PacketDispatcher.sendPacketToServer(packet);
		mc.displayGuiScreen(null);
	}

}
