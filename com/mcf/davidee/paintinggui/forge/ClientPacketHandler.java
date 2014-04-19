package com.mcf.davidee.paintinggui.forge;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.mcf.davidee.paintinggui.PaintingSelectionMod;
import com.mcf.davidee.paintinggui.gui.GuiPaintingSelection;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;

public class ClientPacketHandler extends ServerPacketHandler{

	@SubscribeEvent
	public void onClientPacket(ClientCustomPacketEvent event) {

		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		ByteBufInputStream dis = new ByteBufInputStream(event.packet.payload());
		ByteBuf buf = event.packet.payload();

		World world = player.worldObj;
		System.out.println("Client packet");
		try{
			int id = dis.readInt();
			String[] art = new String[dis.readInt()];
			for (int i = 0; i < art.length; ++i)
				art[i] = dis.readUTF();

			if (id == -1) { //What painting is selected?
				MovingObjectPosition pos = Minecraft.getMinecraft().objectMouseOver;
				if (pos != null && pos.entityHit instanceof EntityPainting) 
					PaintingSelectionMod.Channel.sendToServer(PaintingSelectionMod.createPacket(pos.entityHit.getEntityId(), new String[0]));
				else
					player.addChatMessage(new ChatComponentText( PaintingSelectionMod.COLOR + "cError - No painting selected"));
			}
			else {
				if (art.length == 1) { //Set Painting
					EnumArt enumArt = PaintingSelectionMod.getEnumArt(art[0]);
					Entity e = player.worldObj.getEntityByID(id);
					if (e instanceof EntityPainting)
						PaintingSelectionMod.setPaintingArt((EntityPainting)e, enumArt);
				}
				else { //Show art GUI
					Minecraft mc = Minecraft.getMinecraft();
					if (mc.currentScreen == null)
						mc.displayGuiScreen(new GuiPaintingSelection(art, id));
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}


}
