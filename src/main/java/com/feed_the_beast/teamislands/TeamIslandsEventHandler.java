package com.feed_the_beast.teamislands;

import com.feed_the_beast.ftblib.events.team.ForgeTeamDataEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamDeletedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerLeftEvent;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.util.BlockUtils;
import com.feed_the_beast.ftbutilities.FTBUtilities;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

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
			TeamIslandsUniverseData.INSTANCE.islandTemplate.addBlocksToWorldChunk(w, pos, new PlacementSettings());
			BlockUtils.notifyBlockUpdate(w, pos, null);
			w.notifyLightSet(pos);

			island.spawnPoint = island.spawnPoint.offset(EnumFacing.UP);

			while (!w.isOutsideBuildHeight(island.spawnPoint) && w.isAirBlock(island.spawnPoint))
			{
				island.spawnPoint = island.spawnPoint.down();
			}

			island.spawnPoint = island.spawnPoint.offset(EnumFacing.UP);
		}

		if (TeamIslandsConfig.lobby.autoteleport_to_island && event.getPlayer().isOnline())
		{
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

	@SubscribeEvent
	public static void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event)
	{
		if (Universe.loaded() && !event.player.world.isRemote && TeamIslandsConfig.general.isEnabled(((EntityPlayerMP) event.player).server))
		{
			Island island = TeamIslandsUniverseData.INSTANCE.getIsland(Universe.get().getPlayer(event.player).team);

			if (event.player.getBedLocation(0) == null)
			{
				island.teleport(event.player);
			}
		}
	}
}