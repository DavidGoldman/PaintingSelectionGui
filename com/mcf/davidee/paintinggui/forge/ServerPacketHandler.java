package com.mcf.davidee.paintinggui.forge;

import static com.mcf.davidee.paintinggui.PaintingSelectionMod.setPaintingArt;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.mcf.davidee.paintinggui.PaintingSelectionMod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;

public class ServerPacketHandler {

	
	@SubscribeEvent
	public void onServerPacket(ServerCustomPacketEvent event) {

		EntityPlayerMP player = ((NetHandlerPlayServer) event.handler).playerEntity;
		ByteBufInputStream dis = new ByteBufInputStream(event.packet.payload());
		ByteBuf buf = event.packet.payload();

		World world = player.worldObj;
		System.out.println("Server packet");

		try {
			int id = dis.readInt();
			if (dis.readInt() == 1)
				setPainting(id, dis.readUTF(), player);
			else 
				sendPossiblePaintings(id, player);
			
			dis.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	private void setPainting(int id, String art, EntityPlayerMP player) {
		Entity e = player.getServerForPlayer().getEntityByID(id);
		if (e instanceof EntityPainting) {
			EntityPainting painting = (EntityPainting)e;
			setPaintingArt(painting, PaintingSelectionMod.getEnumArt(art));
			PaintingSelectionMod.Channel.sendToAll(PaintingSelectionMod.createPacket(id, new String[] {art})/*,painting.dimension*/);
		}
		else
			player.addChatMessage(new ChatComponentText( PaintingSelectionMod.COLOR + "cError - Could not locate painting"));
	}

	private void sendPossiblePaintings(int id, EntityPlayerMP player) {
		Entity e = player.getServerForPlayer().getEntityByID(id);
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
			
			PaintingSelectionMod.Channel.sendTo(PaintingSelectionMod.createPacket(id, names), player);
//			player.playerNetServerHandler.sendPacketToPlayer(PaintingSelectionMod.createPacket(id, names));

			//Reset the art
			setPaintingArt(painting, origArt);
		}
		else
			player.addChatMessage(new ChatComponentText( PaintingSelectionMod.COLOR + "cError - Could not locate painting"));
	}

}
