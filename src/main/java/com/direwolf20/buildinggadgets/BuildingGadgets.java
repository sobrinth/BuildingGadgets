package com.direwolf20.buildinggadgets;

import com.direwolf20.buildinggadgets.commands.DeleteBlockMapsCommand;
import com.direwolf20.buildinggadgets.commands.FindBlockMapsCommand;
import com.direwolf20.buildinggadgets.eventhandlers.AnvilRepairHandler;
import com.direwolf20.buildinggadgets.eventhandlers.BreakEventHandler;
import com.direwolf20.buildinggadgets.eventhandlers.ItemPickupHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;


@Mod(modid = BuildingGadgets.MODID, name = BuildingGadgets.MODNAME, version = BuildingGadgets.VERSION, useMetadata = true)

public class BuildingGadgets {
    public static final String MODID = "buildinggadgets";
    public static final String MODNAME = "Building Gadgets";
    public static final String VERSION = "2.2.1";


    @SidedProxy(clientSide = "com.direwolf20.buildinggadgets.ClientProxy", serverSide = "com.direwolf20.buildinggadgets.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static BuildingGadgets instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
        MinecraftForge.EVENT_BUS.register(new BreakEventHandler());
        MinecraftForge.EVENT_BUS.register(new ItemPickupHandler());
        if (!Config.poweredByFE) {
            MinecraftForge.EVENT_BUS.register(new AnvilRepairHandler());
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new FindBlockMapsCommand());
        event.registerServerCommand(new DeleteBlockMapsCommand());
    }
}
