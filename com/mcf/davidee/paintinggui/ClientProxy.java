package com.mcf.davidee.paintinggui;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;

import com.mcf.davidee.paintinggui.forge.PaintingPacket;
import com.mcf.davidee.paintinggui.gui.PaintingSelectionScreen;

public class ClientProxy extends NetProxy {
	
	@Override
	public EntityPlayer getClientPlayer() { 
		return Minecraft.getMinecraft().thePlayer; 
	}
	
	@Override
	public void onClientPacket(EntityPlayer player, PaintingPacket packet) { 
		if(packet.id == -1) { //What painting is selected?
			MovingObjectPosition pos = Minecraft.getMinecraft().objectMouseOver;
			if (pos != null && pos.entityHit instanceof EntityPainting) 
				PaintingSelectionMod.DISPATCHER.sendToServer(new PaintingPacket(pos.entityHit.getEntityId(), new String[0]));
			else
				player.addChatMessage(new ChatComponentText(PaintingSelectionMod.COLOR + "cError - No painting selected"));
		}
		else if (packet.art.length == 1) { //Set Painting
			EnumArt enumArt = getEnumArt(packet.art[0]);
			Entity e = player.worldObj.getEntityByID(packet.id);
			if (e instanceof EntityPainting)
				setPaintingArt((EntityPainting)e, enumArt);
		}
		else { //Show art GUI
			Minecraft mc = Minecraft.getMinecraft();
			if (mc.currentScreen == null)
				mc.displayGuiScreen(new PaintingSelectionScreen(packet.art, packet.id));
		}
	}
	
}
