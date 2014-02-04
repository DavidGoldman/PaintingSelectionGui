package com.mcf.davidee.paintinggui;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

import com.mcf.davidee.paintinggui.forge.PacketPipeline;
import com.mcf.davidee.paintinggui.forge.ServerPacketHandler;
import com.mcf.davidee.paintinggui.CommandPainting;
import com.mcf.davidee.paintinggui.gui.ArtComparator;


@Mod(modid = "PaintingSelGui", name = "PaintingSelectionGui", version = "1.7.2.0", dependencies = "after:guilib")
public class PaintingSelectionMod {
	
    public static final PacketPipeline dispatcher = new PacketPipeline();
    
	public static final ArtComparator ART_COMPARATOR = new ArtComparator();
    
    @SidedProxy(clientSide="com.mcf.davidee.paintinggui.forge.ClientPacketHandler", serverSide="com.mcf.davidee.paintinggui.forge.ServerPacketHandler")
    public static ServerPacketHandler proxy;
    
    @EventHandler
	public void initialize(FMLInitializationEvent init) {
        dispatcher.initialize();
        proxy.initialize();
    }
    
    @EventHandler
    public void postInitialize(FMLPostInitializationEvent evt) {
        dispatcher.postInitialize();
    }
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandPainting());
	}
}
