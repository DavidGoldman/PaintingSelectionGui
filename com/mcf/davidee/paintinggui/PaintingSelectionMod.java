package com.mcf.davidee.paintinggui;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumArt;

import com.mcf.davidee.paintinggui.forge.ClientPacketHandler;
import com.mcf.davidee.paintinggui.forge.ServerPacketHandler;
import com.mcf.davidee.paintinggui.gui.ArtComparator;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;


@Mod(modid = "PaintingSelGui", name = "PaintingSelectionGui", version = "1.6.2.2", dependencies = "after:guilib")
@NetworkMod(
		clientSideRequired = true,
		serverSideRequired = false,
		clientPacketHandlerSpec = @SidedPacketHandler(channels = "PaintingSelGui",packetHandler = ClientPacketHandler.class),
		serverPacketHandlerSpec = @SidedPacketHandler(channels = "PaintingSelGui", packetHandler = ServerPacketHandler.class)
		)
public class PaintingSelectionMod {
	
	public static final String CHANNEL = "PaintingSelGui";
	
	public static final char COLOR = '\u00A7';
	
	public static final ArtComparator ART_COMPARATOR = new ArtComparator();
	
	public static EnumArt getEnumArt(String artName) {
		for (EnumArt art : EnumArt.values())
			if (art.title.equals(artName))
				return art;
		return EnumArt.Kebab;
	}

	public static void setPaintingArt(EntityPainting p, EnumArt art) {
		p.art = art;
		p.setDirection(p.hangingDirection);
	}
	
	public static Packet250CustomPayload createPacket(int id, String[] art) {
		try{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bytes);
			dos.writeInt(id);
			dos.writeInt(art.length);
			for(String s : art)
				dos.writeUTF(s);
			return new Packet250CustomPayload(CHANNEL,bytes.toByteArray()); 
		}
		catch(IOException e){
			return null;
		}
	}
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		MinecraftServer server= event.getServer();
		ServerCommandManager serverCommandManager = (ServerCommandManager) server.getCommandManager();
		serverCommandManager.registerCommand(new CommandPainting());
	}
	
}
