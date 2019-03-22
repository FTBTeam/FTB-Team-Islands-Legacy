package com.feed_the_beast.mods.teamislands;

import com.feed_the_beast.ftblib.events.team.ForgeTeamDataEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamDeletedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerLeftEvent;
import com.feed_the_beast.ftbutilities.FTBUtilities;
import com.feed_the_beast.mods.teamislands.data.Island;
import com.feed_the_beast.mods.teamislands.data.IslandButton;
import com.feed_the_beast.mods.teamislands.data.IslandTemplate;
import com.feed_the_beast.mods.teamislands.data.TeamIslandsTeamData;
import com.feed_the_beast.mods.teamislands.data.TeamIslandsUniverseData;
import com.feed_the_beast.mods.teamislands.net.MessageOpenGui;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

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
		if (!TeamIslandsConfig.general.isEnabled(event.getUniverse().server) || !event.getPlayer().isOnline())
		{
			return;
		}

		TeamIslandsUniverseData data = TeamIslandsUniverseData.INSTANCE;
		EntityPlayerMP player = event.getPlayer().getPlayer();
		Island island = data.getIsland(event.getTeam());

		if (!island.spawned)
		{
			if (data.islandTemplates.size() > 1 && TeamIslandsConfig.islands.select_islands)
			{
				final ArrayList<IslandButton> islands = new ArrayList<>();

				for (IslandTemplate template : data.islandTemplates)
				{
					IslandButton islandButton = new IslandButton();
					islandButton.path = template.path;
					islandButton.name = template.displayName == null ? new TextComponentString(template.name) : template.displayName;
					islandButton.icon = template.icon;
					islands.add(islandButton);
				}

				event.setDisplayGui(() -> new MessageOpenGui(islands).sendTo(player));
				return;
			}

			island.spawned = true;
			World w = event.getUniverse().world;
			BlockPos pos = island.getBlockPos();
			island.template = data.islandTemplates.get(w.rand.nextInt(data.islandTemplates.size())).path;
			IslandTemplate template = island.getTemplate();
			template.template.addBlocksToWorld(w, pos, new PlacementSettings(), 2);
			w.getPendingBlockUpdates(new StructureBoundingBox(pos, pos.add(template.template.getSize())), true);

			if (Loader.isModLoaded(FTBUtilities.MOD_ID))
			{
				FTBUtilitiesIntegration.claimChunks(event.getPlayer());
			}
		}

		if (TeamIslandsConfig.lobby.autoteleport_to_island)
		{
			player.setSpawnChunk(island.getEntitySpawnPos(), true, 0);
			island.teleport(event.getPlayer().getPlayer());
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

			event.getPlayer().getPlayer().setSpawnChunk(TeamIslandsUniverseData.INSTANCE.getLobby().getEntitySpawnPos(), true, 0);
			TeamIslandsUniverseData.INSTANCE.getLobby().teleport(event.getPlayer().getPlayer());
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