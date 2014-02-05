package com.mcf.davidee.paintinggui.forge;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ChatComponentText;

import com.mcf.davidee.paintinggui.PaintingSelectionMod;
import com.mcf.davidee.paintinggui.forge.ServerPacketHandler;
import com.mcf.davidee.paintinggui.forge.PaintingPacket;
import com.mcf.davidee.paintinggui.gui.PaintingSelectionScreen;


public class ClientPacketHandler extends ServerPacketHandler {

    private void addChatMessage(ChatComponentText msg) {
        //getChatGUI.unknown
        Minecraft.getMinecraft().ingameGUI.func_146158_b().func_146227_a(msg);
    }
    
    @Override
    public void onClientPacket(EntityPlayer player, PaintingPacket packet) {
        if (packet.id == -1) {
            MovingObjectPosition pos = Minecraft.getMinecraft().objectMouseOver;
            if (pos != null && pos.entityHit instanceof EntityPainting) {
                EntityPainting e = (EntityPainting)(pos.entityHit);
                PaintingSelectionMod.dispatcher.sendToServer(new PaintingPacket(e.func_145782_y() /*getEntityId*/, new String[0]));
            }
            else
                addChatMessage(new ChatComponentText(COLOR + "cError - No painting selected"));
        } 
        else {
            if (packet.art.length == 1) {
                EnumArt enumArt = getEnumArt(packet.art[0]);
                Entity e = player.worldObj.getEntityByID(packet.id);
                if (e instanceof EntityPainting)
                    setPaintingArt((EntityPainting)e, enumArt);
            }
            else { //Show art GUI
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.currentScreen == null)
                    //displayGuiScreen
                    mc.func_147108_a(new PaintingSelectionScreen(packet.art, packet.id));
            }
        }
    }
}
