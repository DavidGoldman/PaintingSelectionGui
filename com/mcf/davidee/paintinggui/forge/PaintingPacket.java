package com.mcf.davidee.paintinggui.forge;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.mcf.davidee.paintinggui.forge.IPacket;
import com.mcf.davidee.paintinggui.PaintingSelectionMod;


public class PaintingPacket implements IPacket {
    public int id;
    public String[] art;
    
    public PaintingPacket() {
        id = -1;
        art = new String[0];
    }
    
    public PaintingPacket(int theId, String[] theArt) {
        id = theId;
        art = new String[theArt.length];
        for (int j=0; j<theArt.length; j++) {
            art[j] = theArt[j];
        }
    }
    
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        ByteBufInputStream dis = new ByteBufInputStream(buffer);
        try {
            id = dis.readInt();
            art = new String[dis.readInt()];
            for (int i = 0; i < art.length; ++i)
                art[i] = dis.readUTF();
        }
        catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }
    
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        ByteBufOutputStream dos = new ByteBufOutputStream(buffer);
        try {
            dos.writeInt(id);
            dos.writeInt(art.length);
            for(String s : art)
                dos.writeUTF(s);
        }
        catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }
    
    public void handleClientSide(EntityPlayer player) {
        PaintingSelectionMod.proxy.onClientPacket(player, this);
    }
    
    public void handleServerSide(EntityPlayer player) {
        PaintingSelectionMod.proxy.onServerPacket((EntityPlayerMP)player, this);
    }
}
