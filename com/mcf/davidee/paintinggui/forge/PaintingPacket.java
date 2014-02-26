package com.mcf.davidee.paintinggui.forge;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.mcf.davidee.paintinggui.PaintingSelectionMod;

import cpw.mods.fml.common.network.ByteBufUtils;

public class PaintingPacket implements IPacket {

	public int id;
	public String[] art;

	public PaintingPacket() {
		id = -1;
		art = new String[0];
	}

	public PaintingPacket(int id, String[] art) {
		this.id = id;
		this.art = art;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeInt(art.length);
		for (String s : art)
			ByteBufUtils.writeUTF8String(buffer, s);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		id = buffer.readInt();
		art = new String[buffer.readInt()];
		for (int i = 0; i < art.length; ++i)
			art[i] = ByteBufUtils.readUTF8String(buffer);
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		PaintingSelectionMod.proxy.onClientPacket(player, this);
	}

	@Override
	public void handleServerSide(EntityPlayerMP player) {
		PaintingSelectionMod.proxy.onServerPacket(player, this);
	}

}
