package com.feed_the_beast.teamislands;

import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftblib.lib.EventHandler;
import com.feed_the_beast.ftblib.lib.math.ChunkDimPos;
import com.feed_the_beast.ftblib.lib.math.MathUtils;
import com.feed_the_beast.ftbutilities.FTBUtilities;
import com.feed_the_beast.ftbutilities.data.ClaimedChunks;
import com.feed_the_beast.ftbutilities.data.FTBUTeamData;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author LatvianModder
 */
@EventHandler(requiredMods = FTBUtilities.MOD_ID)
public class FTBUtilitiesIntegration
{
	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onPlayerJoinedTeam(ForgeTeamPlayerJoinedEvent event)
	{
		int r = TeamIslandsConfig.islands.autoclaim_radius;

		if (r > 0 && ClaimedChunks.instance != null)
		{
			Island island = TeamIslandsUniverseData.INSTANCE.getIsland(event.getTeam());
			FTBUTeamData data = FTBUTeamData.get(event.getTeam());

			if (!island.isLobby())
			{
				int chunks = Math.min((r * 2 + 1) * (r * 2 + 1), data.getMaxClaimChunks());

				for (int i = 0; i <= chunks; i++)
				{
					ChunkPos pos = MathUtils.getSpiralPoint(i);
					ClaimedChunks.instance.claimChunk(data, new ChunkDimPos(island.pos.posX + pos.x, island.pos.posZ + pos.z, island.pos.dim));
				}
			}
		}
	}
}