package com.mcf.davidee.paintinggui;

import java.util.List;
import java.util.ArrayList;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.mcf.davidee.paintinggui.PaintingSelectionMod;
import com.mcf.davidee.paintinggui.forge.PaintingPacket;


public class CommandPainting implements ICommand {

    private List aliases;
    
    public CommandPainting() {
        this.aliases = new ArrayList();
        this.aliases.add("painting");
        this.aliases.add("paint");
    }

	@Override
	public String getCommandName() {
		return "painting";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/painting";
	}
    
    @Override
    public List getCommandAliases()
    {
        return this.aliases;
    }

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
        if (icommandsender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)icommandsender;
            PaintingSelectionMod.dispatcher.sendTo(new PaintingPacket(-1, new String[0]), player);
        }
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender s){
		return s instanceof EntityPlayer;
	}
    
    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring)
    {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i)
    {
        return false;
    }

    @Override
    public int compareTo(Object o)
    {
        return 0;
    }
}
