package com.direwolf20.buildinggadgets.network;

import com.direwolf20.buildinggadgets.blocks.templatemanager.TemplateManagerCommands;
import com.direwolf20.buildinggadgets.blocks.templatemanager.TemplateManagerContainer;
import com.direwolf20.buildinggadgets.blocks.templatemanager.TemplateManagerTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTemplateManagerPaste implements IMessage {

    NBTTagCompound tag = new NBTTagCompound();
    private BlockPos pos;

    @Override
    public void fromBytes(ByteBuf buf) {
        tag = ByteBufUtils.readTag(buf);
        pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, tag);
        buf.writeLong(pos.toLong());
    }

    public PacketTemplateManagerPaste() {
    }

    public PacketTemplateManagerPaste(NBTTagCompound tagCompound, BlockPos TMpos) {
        tag = tagCompound.copy();
        pos = TMpos;
    }

    public static class Handler implements IMessageHandler<PacketTemplateManagerPaste, IMessage> {
        @Override
        public IMessage onMessage(PacketTemplateManagerPaste message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketTemplateManagerPaste message, MessageContext ctx) {
            if (message.tag.equals(new NBTTagCompound())) {
                return;
            }
            EntityPlayerMP player = ctx.getServerHandler().player;
            World world = player.world;
            BlockPos pos = message.pos;
            TileEntity te = world.getTileEntity(pos);
            if (!(te instanceof TemplateManagerTileEntity)) return;
            TemplateManagerContainer container = ((TemplateManagerTileEntity) te).getContainer(player);
            TemplateManagerCommands.PasteTemplate(container, player, message.tag);
        }
    }
}
