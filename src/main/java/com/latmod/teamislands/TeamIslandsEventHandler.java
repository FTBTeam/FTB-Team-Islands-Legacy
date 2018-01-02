package com.latmod.teamislands;

import com.feed_the_beast.ftblib.events.RegisterDataProvidersEvent;
import com.feed_the_beast.ftblib.events.player.ForgePlayerLoggedInEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamDeletedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerLeftEvent;
import com.feed_the_beast.ftblib.events.team.RegisterTeamGuiActionsEvent;
import com.feed_the_beast.ftblib.lib.EventHandler;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.util.LangKey;
import com.feed_the_beast.ftblib.lib.util.ServerUtils;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * @author LatvianModder
 */
@EventHandler
public class TeamIslandsEventHandler
{
	public static final LangKey LOGIN_TEXT = LangKey.of(TeamIslandsFinals.MOD_ID + ".login_text");

	@SubscribeEvent
	public static void registerTeamGuiActions(RegisterTeamGuiActionsEvent event)
	{
		event.register(TeamIslandsTeamGuiActions.LOBBY);
		event.register(TeamIslandsTeamGuiActions.MY_ISLAND);
	}

	@SubscribeEvent
	public static void onTeamData(RegisterDataProvidersEvent.Team event)
	{
		event.register(TeamIslandsUniverseData.DATA_ID, TeamIslandsTeamData::new);
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(ForgePlayerLoggedInEvent event)
	{
		if (event.getPlayer().getTeam() == null && TeamIslandsWorldType.INSTANCE.is(event.getPlayer().getPlayer().world))
		{
			LOGIN_TEXT.sendMessage(event.getPlayer().getPlayer());
		}
	}

	@SubscribeEvent
	public static void onPlayerJoinedTeam(ForgeTeamPlayerJoinedEvent event)
	{
		if (event.getPlayer().isOnline() && TeamIslandsWorldType.INSTANCE.is(event.getPlayer().getPlayer().world))
		{
			Island island = TeamIslandsUniverseData.getIsland(event.getTeam());
			island.teleport(event.getPlayer().getPlayer());

			if (event.getTeam().getMembers().size() == 1)
			{
				World w = ServerUtils.getOverworld();
				w.setBlockState(island.getBlockPos(0), Blocks.OBSIDIAN.getDefaultState());
				w.setBlockState(island.getBlockPos(1), Blocks.AIR.getDefaultState());
				w.setBlockState(island.getBlockPos(2), Blocks.AIR.getDefaultState());
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerLeftTeam(ForgeTeamPlayerLeftEvent event)
	{
		if (event.getPlayer().isOnline() && TeamIslandsWorldType.INSTANCE.is(event.getPlayer().getPlayer().world))
		{
			TeamIslandsUniverseData.getIsland(0).teleport(event.getPlayer().getPlayer());
		}
	}

	@SubscribeEvent
	public static void onTeamDeleted(ForgeTeamDeletedEvent event)
	{
		if (TeamIslandsWorldType.INSTANCE.is(ServerUtils.getOverworld()))
		{
			Island island = TeamIslandsUniverseData.getIsland(event.getTeam());

			if (!island.isLobby())
			{
				island.active = false;
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event)
	{
		if (!event.player.world.isRemote && event.player.world.provider.getDimension() == 0 && TeamIslandsWorldType.INSTANCE.is(event.player.world))
		{
			TeamIslandsUniverseData.getIsland(Universe.get().getPlayer(event.player).getTeam()).teleport(event.player);
		}
	}
}