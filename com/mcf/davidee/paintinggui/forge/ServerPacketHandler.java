package com.mcf.davidee.paintinggui.forge;

import java.util.*;

import net.minecraft.server.MinecraftServer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.util.ChatComponentText;

import com.mcf.davidee.paintinggui.PaintingSelectionMod;
import com.mcf.davidee.paintinggui.forge.PaintingPacket;


public class ServerPacketHandler {

	public static final char COLOR = '\u00A7';
    
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

    public void initialize() {
        PaintingSelectionMod.dispatcher.registerPacket(PaintingPacket.class);
    }
    
    public void onServerPacket(EntityPlayerMP player, PaintingPacket packet) {
        if (packet.art.length == 1) {
            setPainting(packet.id, packet.art[0], player);
        } else {
            sendPossiblePaintings(packet.id, player);
        }
    }
    
    public void onClientPacket(EntityPlayer player, PaintingPacket packet) {
    }
    
    private void setPainting(int id, String art, EntityPlayerMP player) {
        Entity e = player.worldObj.getEntityByID(id);
        if (e instanceof EntityPainting) {
            EntityPainting painting = (EntityPainting)e;
            setPaintingArt(painting, getEnumArt(art));
            PaintingSelectionMod.dispatcher.sendToDimension(new PaintingPacket(id, new String[] {art}), e.dimension);
        }
        else
            //addChatMessage
            player.func_145747_a(new ChatComponentText(COLOR + "cError - Could not locate painting"));
    }
    
    private void sendPossiblePaintings(int id, EntityPlayerMP player) {
        Entity e = player.worldObj.getEntityByID(id);
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

            if (validArtsArray.length > 0) {
                String[] names = new String[validArtsArray.length];
                for (int i =0; i < validArtsArray.length; ++i)
                    names[i] = validArtsArray[i].title;
                PaintingSelectionMod.dispatcher.sendTo(new PaintingPacket(id, names), player);
            }
            
            //Reset the art
			setPaintingArt(painting, origArt);
		}
        else
            //addChatMessage
            player.func_145747_a(new ChatComponentText(COLOR + "cError - Could not locate painting"));
    }
}
