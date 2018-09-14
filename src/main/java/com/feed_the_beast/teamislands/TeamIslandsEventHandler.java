package com.feed_the_beast.teamislands;

import com.feed_the_beast.ftblib.events.team.ForgeTeamDataEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamDeletedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerLeftEvent;
import com.feed_the_beast.ftbutilities.FTBUtilities;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = TeamIslands.MOD_ID)
public class TeamIslandsEventHandler
{
	@SubscribeEvent
	public static void registerTeamData(ForgeTeamDataEvent event)
	{
		event.register(new TeamIslandsTeamData(event.getTeam()));
	}

	@SubscribeEvent
	public static void onPlayerJoinedTeam(ForgeTeamPlayerJoinedEvent event)
	{
		if (!TeamIslandsConfig.general.isEnabled(event.getUniverse().server))
		{
			return;
		}

		Island island = TeamIslandsUniverseData.INSTANCE.getIsland(event.getTeam());

		if (!island.spawned)
		{
			island.spawned = true;
			World w = event.getUniverse().world;
			BlockPos pos = island.getBlockPos();
			TeamIslandsUniverseData.INSTANCE.islandTemplate.addBlocksToWorld(w, pos, new PlacementSettings(), 2);
			w.getPendingBlockUpdates(new StructureBoundingBox(pos, pos.add(TeamIslandsUniverseData.INSTANCE.islandTemplate.getSize())), true);
		}

		if (TeamIslandsConfig.lobby.autoteleport_to_island && event.getPlayer().isOnline())
		{
			event.getPlayer().getPlayer().setSpawnChunk(island.getEntitySpawnPos(), true, 0);
			island.teleport(event.getPlayer().getPlayer());
		}

		if (Loader.isModLoaded(FTBUtilities.MOD_ID))
		{
			FTBUtilitiesIntegration.onPlayerJoinedTeam(event);
		}
	}

	@SubscribeEvent
	public static void onPlayerLeftTeam(ForgeTeamPlayerLeftEvent event)
	{
		if (TeamIslandsConfig.general.isEnabled(event.getUniverse().server) && event.getPlayer().isOnline())
		{
			if (TeamIslandsConfig.general.clear_inv_when_team_left)
			{
				event.getPlayer().getPlayer().inventory.clear();
			}

			event.getPlayer().getPlayer().setSpawnChunk(TeamIslandsUniverseData.INSTANCE.getIsland(0).getEntitySpawnPos(), true, 0);
			TeamIslandsUniverseData.INSTANCE.getIsland(0).teleport(event.getPlayer().getPlayer());
		}
	}

	@SubscribeEvent
	public static void onTeamDeleted(ForgeTeamDeletedEvent event)
	{
		if (!TeamIslandsConfig.general.isEnabled(event.getUniverse().server))
		{
			return;
		}

		Island island = TeamIslandsUniverseData.INSTANCE.getIsland(event.getTeam());

		if (!island.isLobby())
		{
			island.active = false;
		}
	}
}