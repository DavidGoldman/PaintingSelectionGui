package com.mcf.davidee.paintinggui.forge.proxy;

import com.mcf.davidee.paintinggui.PaintingSelectionMod;
import com.mcf.davidee.paintinggui.forge.ClientPacketHandler;

public class ClientProxy extends ServerProxy {

	@Override
	public void registerClientPacketHandler(){
		PaintingSelectionMod.Channel.register(new ClientPacketHandler());
	}

}
