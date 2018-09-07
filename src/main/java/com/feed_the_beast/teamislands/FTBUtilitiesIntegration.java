package com.feed_the_beast.teamislands;

import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftblib.lib.math.ChunkDimPos;
import com.feed_the_beast.ftblib.lib.math.MathUtils;
import com.feed_the_beast.ftbutilities.FTBUtilitiesPermissions;
import com.feed_the_beast.ftbutilities.data.ClaimedChunks;
import com.feed_the_beast.ftbutilities.data.FTBUtilitiesTeamData;
import net.minecraft.util.math.ChunkPos;

/**
 * @author LatvianModder
 */
public class FTBUtilitiesIntegration
{
	public static void onPlayerJoinedTeam(ForgeTeamPlayerJoinedEvent event)
	{
		int r = TeamIslandsConfig.islands.autoclaim_radius;

		if (r > 0 && ClaimedChunks.instance != null)
		{
			Island island = TeamIslandsUniverseData.INSTANCE.getIsland(event.getTeam());

			if (!island.isLobby())
			{
				int chunks = Math.min((r * 2 + 1) * (r * 2 + 1), FTBUtilitiesTeamData.get(event.getTeam()).getMaxClaimChunks());

				for (int i = 0; i <= chunks; i++)
				{
					ChunkPos pos0 = MathUtils.getSpiralPoint(i);
					ChunkDimPos pos = new ChunkDimPos(island.x * 32 + pos0.x, island.z * 32 + pos0.z, 0);

					if (ClaimedChunks.instance.canPlayerModify(event.getPlayer(), pos, FTBUtilitiesPermissions.CLAIMS_OTHER_CLAIM))
					{
						ClaimedChunks.instance.claimChunk(event.getPlayer(), pos);
					}
				}
			}
		}
	}
}