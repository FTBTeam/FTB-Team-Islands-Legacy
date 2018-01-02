package com.latmod.teamislands;

import com.feed_the_beast.ftblib.FTBLibFinals;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = TeamIslandsFinals.MOD_ID, name = TeamIslandsFinals.MOD_NAME, version = TeamIslandsFinals.VERSION, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.12,)", dependencies = "required-after:" + FTBLibFinals.MOD_ID)
public class TeamIslands
{
	@Mod.Instance(TeamIslandsFinals.MOD_ID)
	public static TeamIslands INST;

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{
	}

	@Mod.EventHandler
	public void onPostInit(FMLPostInitializationEvent event)
	{
		TeamIslandsWorldType.INSTANCE.init();
	}
}