package com.mcf.davidee.paintinggui.forge;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.EnumArt;
import net.minecraft.util.MovingObjectPosition;

import com.mcf.davidee.paintinggui.PaintingSelectionMod;
import com.mcf.davidee.paintinggui.gui.PaintingSelectionScreen;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class ClientPacketHandler implements IPacketHandler{

	@Override
	public void onPacketData(INetworkManager manager,Packet250CustomPayload packet, Player player) {
		clientPayload(packet.data, (EntityPlayer)player);
	}

	private void clientPayload(byte[] data, EntityPlayer player){
		try{
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
			int id = dis.readInt();
			String[] art = new String[dis.readInt()];
			for (int i = 0; i < art.length; ++i)
				art[i] = dis.readUTF();
			
			if (id == -1) { //What painting is selected?
				MovingObjectPosition pos = Minecraft.getMinecraft().objectMouseOver;
				if (pos != null && pos.entityHit instanceof EntityPainting) 
					PacketDispatcher.sendPacketToServer(PaintingSelectionMod.createPacket(pos.entityHit.entityId, new String[0]));
				else
					player.addChatMessage(PaintingSelectionMod.COLOR + "cError - No painting selected");
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
						mc.displayGuiScreen(new PaintingSelectionScreen(art, id));
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	

}
