package com.feed_the_beast.teamislands;

import com.feed_the_beast.ftblib.events.RegisterFTBCommandsEvent;
import com.feed_the_beast.ftblib.events.player.ForgePlayerLoggedInEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamDataEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamDeletedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerLeftEvent;
import com.feed_the_beast.ftblib.lib.EventHandler;
import com.feed_the_beast.ftblib.lib.block.BlockFlags;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.util.LangKey;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * @author LatvianModder
 */
@EventHandler
public class TeamIslandsEventHandler
{
	public static final LangKey LOGIN_TEXT = LangKey.of(TeamIslands.MOD_ID + ".login_text");
	public static final ResourceLocation LOGIN_LOBBY = new ResourceLocation(TeamIslands.MOD_ID, "lobby");

	@SubscribeEvent
	public static void registerTeamData(ForgeTeamDataEvent event)
	{
		event.register(TeamIslands.MOD_ID, new TeamIslandsTeamData(event.getTeam()));
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(ForgePlayerLoggedInEvent event)
	{
		if (event.isFirstLogin(LOGIN_LOBBY))
		{
			TeamIslandsUniverseData.INSTANCE.getIsland(0).teleport(event.getPlayer().getPlayer());

			if (!event.getPlayer().hasTeam())
			{
				LOGIN_TEXT.sendMessage(event.getPlayer().getPlayer());
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerJoinedTeam(ForgeTeamPlayerJoinedEvent event)
	{
		Island island = TeamIslandsUniverseData.INSTANCE.getIsland(event.getTeam());

		if (!island.spawned)
		{
			island.spawned = true;

			World w = TeamIslandsConfig.islands.dimension == 0 ? event.getUniverse().world : event.getUniverse().server.getWorld(TeamIslandsConfig.islands.dimension);
			BlockPos pos = island.getBlockPos().getBlockPos();
			TeamIslandsUniverseData.INSTANCE.islandTemplate.addBlocksToWorldChunk(w, pos, new PlacementSettings());
			w.notifyBlockUpdate(pos, w.getBlockState(pos), w.getBlockState(pos), BlockFlags.DEFAULT);
			w.notifyLightSet(pos);

			island.spawnPoint = island.spawnPoint.offset(EnumFacing.UP);

			while (w.isAirBlock(island.spawnPoint))
			{
				island.spawnPoint = island.spawnPoint.down();
			}

			island.spawnPoint = island.spawnPoint.offset(EnumFacing.UP);
		}

		if (TeamIslandsConfig.lobby.autoteleport_to_island && event.getPlayer().isOnline())
		{
			island.teleport(event.getPlayer().getPlayer());
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
		if (Universe.loaded() && !event.player.world.isRemote)
		{
			Island island = TeamIslandsUniverseData.INSTANCE.getIsland(Universe.get().getPlayer(event.player).team);

			if (event.player.getBedLocation(island.pos.dim) == null)
			{
				island.teleport(event.player);
			}
		}
	}

	@SubscribeEvent
	public static void registerCommands(RegisterFTBCommandsEvent event)
	{
		event.add(new CmdTeamIslands());
	}
}