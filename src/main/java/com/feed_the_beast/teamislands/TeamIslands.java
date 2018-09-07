package com.feed_the_beast.teamislands;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.util.SidedUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

@Mod(
		modid = TeamIslands.MOD_ID,
		name = TeamIslands.MOD_NAME,
		version = TeamIslands.VERSION,
		acceptableRemoteVersions = "*",
		dependencies = FTBLib.THIS_DEP
)
public class TeamIslands
{
	public static final String MOD_ID = "teamislands";
	public static final String MOD_NAME = "Team Islands";
	public static final String VERSION = "0.0.0.teamislands";
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

	public static ITextComponent lang(@Nullable ICommandSender sender, String key, Object... args)
	{
		return SidedUtils.lang(sender, MOD_ID, key, args);
	}

	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CmdTeamIslands());
	}
}