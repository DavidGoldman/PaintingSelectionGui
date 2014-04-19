package com.mcf.davidee.paintinggui;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandPainting extends CommandBase {

	@Override
	public String getCommandName() {
		return "painting";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/painting";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		EntityPlayerMP player = (EntityPlayerMP)icommandsender;
		PaintingSelectionMod.Channel.sendTo(PaintingSelectionMod.createPacket(-1, new String[0]), player);
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender s){
		return s instanceof EntityPlayer;
	}

}
