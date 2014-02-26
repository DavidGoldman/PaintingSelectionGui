package com.mcf.davidee.paintinggui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import com.mcf.davidee.paintinggui.forge.PaintingPacket;

public class NetProxy {
	
	public EntityPlayer getClientPlayer() { 
		return null; 
	}
	
	public void onClientPacket(EntityPlayer player, PaintingPacket packet) { }
	
	public void onServerPacket(EntityPlayerMP player, PaintingPacket packet) {
		if (packet.art.length == 1) { //Set Painting
			EnumArt enumArt = getEnumArt(packet.art[0]);
			Entity e = player.worldObj.getEntityByID(packet.id);
			if (e instanceof EntityPainting) {
				setPaintingArt((EntityPainting)e, enumArt);
				PaintingSelectionMod.DISPATCHER.sendToDimension(packet, e.dimension);
			}
			else
				player.addChatMessage(new ChatComponentText(PaintingSelectionMod.COLOR + "cError - Could not locate painting"));
		}
		else { //Send possible paintings
			Entity e = player.worldObj.getEntityByID(packet.id);
			if (e instanceof EntityPainting) {
				EntityPainting painting = (EntityPainting)e;
				EnumArt origArt = painting.art;
				
				List<EnumArt> validArts = new ArrayList<EnumArt>();
				for(EnumArt art : EnumArt.values()){
					setPaintingArt(painting, art);
					if (painting.onValidSurface())
						validArts.add(art);
				}
				EnumArt[] validArtsArray = validArts.toArray(new EnumArt[0]);
				Arrays.sort(validArtsArray, PaintingSelectionMod.ART_COMPARATOR);
				String[] names = new String[validArtsArray.length];
				for (int i =0; i < validArtsArray.length; ++i)
					names[i] = validArtsArray[i].title;
				
				PaintingSelectionMod.DISPATCHER.sendTo(new PaintingPacket(packet.id, names), player);

				//Reset the art
				setPaintingArt(painting, origArt);
			}
			else
				player.addChatMessage(new ChatComponentText(PaintingSelectionMod.COLOR + "cError - Could not locate painting"));
		}
	}
	
	protected EnumArt getEnumArt(String artName) {
		for (EnumArt art : EnumArt.values())
			if (art.title.equals(artName))
				return art;
		return EnumArt.Kebab;
	}
	
	protected void setPaintingArt(EntityPainting p, EnumArt art) {
		p.art = art;
		p.setDirection(p.hangingDirection);
	}
	
}
