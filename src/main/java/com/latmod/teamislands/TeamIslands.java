package com.latmod.teamislands;

import com.feed_the_beast.ftblib.FTBLib;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
		modid = TeamIslands.MOD_ID,
		name = TeamIslands.MOD_NAME,
		version = TeamIslands.VERSION,
		acceptableRemoteVersions = "*",
		acceptedMinecraftVersions = "[1.12,)",
		dependencies = "required-after:" + FTBLib.MOD_ID
)
public class TeamIslands
{
	public static final String MOD_ID = "teamislands";
	public static final String MOD_NAME = "Team Islands";
	public static final String VERSION = "@VERSION@";
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
}