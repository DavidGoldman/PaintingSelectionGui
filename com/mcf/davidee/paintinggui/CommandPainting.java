package com.mcf.davidee.paintinggui;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.mcf.davidee.paintinggui.forge.PaintingPacket;


public class CommandPainting extends CommandBase {

	@Override
	public String getCommandName() {
		return "painting";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/painting";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] var2) {
		EntityPlayerMP player = (EntityPlayerMP)sender;
		PaintingSelectionMod.DISPATCHER.sendTo(new PaintingPacket(-1, new String[0]), player);
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender s){
		return s instanceof EntityPlayer;
	}


}
