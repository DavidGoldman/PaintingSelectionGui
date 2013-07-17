package com.mcf.davidee.paintinggui.forge;

import static com.mcf.davidee.paintinggui.PaintingSelectionMod.setPaintingArt;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.EnumArt;

import com.mcf.davidee.paintinggui.PaintingSelectionMod;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class ServerPacketHandler implements IPacketHandler{

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if (player instanceof EntityPlayerMP)
			serverPayload((EntityPlayerMP)player,packet.data);
	}

	private void serverPayload(EntityPlayerMP player, byte[] data){
		try {
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
			int id = dis.readInt();
			if (dis.readInt() == 1)
				setPainting(id, dis.readUTF(), player);
			else 
				sendPossiblePaintings(id, player);
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
			PacketDispatcher.sendPacketToAllInDimension(PaintingSelectionMod.createPacket(id, new String[] {art}), painting.dimension);
		}
		else
			player.addChatMessage(PaintingSelectionMod.COLOR + "cError - Could not locate painting");
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
			player.playerNetServerHandler.sendPacketToPlayer(PaintingSelectionMod.createPacket(id, names));

			//Reset the art
			setPaintingArt(painting, origArt);
		}
		else
			player.addChatMessage(PaintingSelectionMod.COLOR + "cError - Could not locate painting");
	}

}
