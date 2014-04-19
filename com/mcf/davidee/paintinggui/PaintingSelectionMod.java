package com.mcf.davidee.paintinggui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.server.MinecraftServer;

import com.mcf.davidee.paintinggui.forge.ServerPacketHandler;
import com.mcf.davidee.paintinggui.forge.proxy.ServerProxy;
import com.mcf.davidee.paintinggui.gui.ArtComparator;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;


@Mod(modid = "PaintingSelGui", name = "PaintingSelectionGui", version = "1.7.2.0")

public class PaintingSelectionMod {
	
	public static final String CHANNEL = "PaintingSelGui";
	
	public static final char COLOR = '\u00A7';
	
	public static final ArtComparator ART_COMPARATOR = new ArtComparator();
	
	public static FMLEventChannel Channel;
	
	@SidedProxy(serverSide = "com.mcf.davidee.paintinggui.forge.proxy.ServerProxy",
			clientSide = "com.mcf.davidee.paintinggui.forge.proxy.ClientProxy")
	public static ServerProxy proxy;

	
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
	
	@EventHandler
	public void load (FMLInitializationEvent e){

		Channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(CHANNEL);
		Channel.register(new ServerPacketHandler());
		proxy.registerClientPacketHandler();
	}
	
	public static FMLProxyPacket createPacket(int id, String[] art) {
		try{
			ByteBuf buf = Unpooled.buffer();
			ByteBufOutputStream out = new ByteBufOutputStream(buf);
			out.writeInt(id);
			out.writeInt(art.length);
			for(String s : art)
				out.writeUTF(s);
			out.close();
			return new FMLProxyPacket(buf, CHANNEL);
		}
		catch(IOException e){
			System.out.println("exception");
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
