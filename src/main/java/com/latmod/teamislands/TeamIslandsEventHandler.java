package com.latmod.teamislands;

import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.events.RegisterDataProvidersEvent;
import com.feed_the_beast.ftblib.events.player.ForgePlayerLoggedInEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamDeletedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerLeftEvent;
import com.feed_the_beast.ftblib.events.team.RegisterTeamGuiActionsEvent;
import com.feed_the_beast.ftblib.lib.EventHandler;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.util.LangKey;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * @author LatvianModder
 */
@EventHandler
public class TeamIslandsEventHandler
{
	public static final LangKey LOGIN_TEXT = LangKey.of(TeamIslands.MOD_ID + ".login_text");

	@SubscribeEvent
	public static void registerTeamGuiActions(RegisterTeamGuiActionsEvent event)
	{
		event.register(TeamIslandsTeamGuiActions.TP_LOBBY);
		event.register(TeamIslandsTeamGuiActions.TP_MY_ISLAND);
	}

	@SubscribeEvent
	public static void onTeamData(RegisterDataProvidersEvent.Team event)
	{
		event.register(TeamIslands.MOD_ID, team -> new TeamIslandsTeamData(TeamIslandsUniverseData.INSTANCE, team));
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(ForgePlayerLoggedInEvent event)
	{
		if (event.getPlayer().getTeam() == null && !(event.isFirstLogin() && (event.getUniverse().server.isDedicatedServer() ? FTBLibConfig.teams.autocreate_mp : FTBLibConfig.teams.autocreate_sp)))
		{
			LOGIN_TEXT.sendMessage(event.getPlayer().getPlayer());
		}
	}

	@SubscribeEvent
	public static void onPlayerJoinedTeam(ForgeTeamPlayerJoinedEvent event)
	{
		if (event.getPlayer().isOnline())
		{
			Island island = TeamIslandsUniverseData.INSTANCE.getIsland(event.getTeam());
			island.teleport(event.getPlayer().getPlayer());

			if (event.getTeam().getMembers().size() == 1)
			{
				World w = event.getUniverse().world;
				BlockPos pos = island.getBlockPos();
				int s = TeamIslandsConfig.islands.platform_radius;

				for (int z = -s; z <= s; z++)
				{
					for (int x = -s; x <= s; x++)
					{
						w.setBlockState(pos.add(x, 0, z), TeamIslandsConfig.islands.getState(w.rand));
						w.setBlockState(pos.add(x, 1, z), Blocks.AIR.getDefaultState());
						w.setBlockState(pos.add(x, 2, z), Blocks.AIR.getDefaultState());
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerLeftTeam(ForgeTeamPlayerLeftEvent event)
	{
		if (event.getPlayer().isOnline())
		{
			TeamIslandsUniverseData.INSTANCE.getIsland(0).teleport(event.getPlayer().getPlayer());
		}
	}

	@SubscribeEvent
	public static void onTeamDeleted(ForgeTeamDeletedEvent event)
	{
		Island island = TeamIslandsUniverseData.INSTANCE.getIsland(event.getTeam());

		if (!island.isLobby())
		{
			island.active = false;
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event)
	{
		if (Universe.loaded() && !event.player.world.isRemote && event.player.world.provider.getDimension() == 0)
		{
			TeamIslandsUniverseData.INSTANCE.getIsland(Universe.get().getPlayer(event.player).getTeam()).teleport(event.player);
		}
	}
}