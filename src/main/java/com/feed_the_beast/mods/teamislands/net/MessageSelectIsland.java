package com.feed_the_beast.mods.teamislands.net;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToServer;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbutilities.FTBUtilities;
import com.feed_the_beast.mods.teamislands.FTBUtilitiesIntegration;
import com.feed_the_beast.mods.teamislands.TeamIslandsConfig;
import com.feed_the_beast.mods.teamislands.data.Island;
import com.feed_the_beast.mods.teamislands.data.IslandTemplate;
import com.feed_the_beast.mods.teamislands.data.TeamIslandsUniverseData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.fml.common.Loader;

/**
 * @author LatvianModder
 */
public class MessageSelectIsland extends MessageToServer
{
	public String path;

	public MessageSelectIsland()
	{
	}

	public MessageSelectIsland(String p)
	{
		path = p;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return TeamIslandsNetHandler.NET;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeString(path);
	}

	@Override
	public void readData(DataIn data)
	{
		path = data.readString();
	}

	@Override
	public void onMessage(EntityPlayerMP player)
	{
		TeamIslandsUniverseData data = TeamIslandsUniverseData.INSTANCE;
		ForgePlayer forgePlayer = Universe.get().getPlayer(player);
		Island island = data.getIsland(forgePlayer.team);

		if (!island.spawned)
		{
			island.spawned = true;
			World w = Universe.get().world;
			BlockPos pos = island.getBlockPos();
			island.template = path;
			IslandTemplate template = island.getTemplate();
			template.template.addBlocksToWorld(w, pos, new PlacementSettings(), 2);
			w.getPendingBlockUpdates(new StructureBoundingBox(pos, pos.add(template.template.getSize())), true);

			if (Loader.isModLoaded(FTBUtilities.MOD_ID))
			{
				FTBUtilitiesIntegration.claimChunks(forgePlayer);
			}
		}

		if (TeamIslandsConfig.lobby.autoteleport_to_island)
		{
			for (ForgePlayer player1 : forgePlayer.team.getMembers())
			{
				if (player1.isOnline())
				{
					player1.getPlayer().setSpawnChunk(island.getEntitySpawnPos(), true, 0);
					island.teleport(player1.getPlayer());
				}
			}
		}
	}
}