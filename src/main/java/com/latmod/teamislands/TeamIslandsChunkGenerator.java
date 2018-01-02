package com.latmod.teamislands;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author LatvianModder
 */
public class TeamIslandsChunkGenerator implements IChunkGenerator
{
	private static final IBlockState AIR_STATE = Blocks.AIR.getDefaultState();
	private static final ChunkPrimer TEAM_ISLANDS_CHUNK_PRIMER = new ChunkPrimer()
	{
		@Override
		public IBlockState getBlockState(int x, int y, int z)
		{
			return AIR_STATE;
		}
	};

	private final World world;
	private final byte[] biomeArray = new byte[256];

	public TeamIslandsChunkGenerator(World w)
	{
		world = w;
		Arrays.fill(biomeArray, (byte) Biome.getIdForBiome(TeamIslandsWorldType.BIOME_PROVIDER.getFixedBiome()));
	}

	@Override
	public Chunk generateChunk(int x, int z)
	{
		Chunk chunk = new Chunk(world, TEAM_ISLANDS_CHUNK_PRIMER, x, z);
		System.arraycopy(biomeArray, 0, chunk.getBiomeArray(), 0, biomeArray.length);
		chunk.generateSkylightMap();
		return chunk;
	}

	@Override
	public void populate(int x, int z)
	{
		if (x == 0 && z == 0)
		{
			Random random = new Random(world.getSeed());
			IBlockState barrier = Blocks.BARRIER.getDefaultState();

			for (int bz = 1; bz < 15; bz++)
			{
				for (int bx = 1; bx < 15; bx++)
				{
					int blockx = (x << 4) + bx;
					int blockz = (z << 4) + bz;

					if (bx == 1 || bz == 1 || bx == 14 || bz == 14)
					{
						for (int y = 0; y < 5; y++)
						{
							world.setBlockState(new BlockPos(blockx, TeamIslandsConfig.lobby.height + y, blockz), TeamIslandsConfig.lobby.getState(random));
						}
					}
					else
					{
						world.setBlockState(new BlockPos(blockx, TeamIslandsConfig.lobby.height, blockz), TeamIslandsConfig.lobby.getState(random));
					}
				}
			}

			for (int bz = 0; bz < 16; bz++)
			{
				for (int bx = 0; bx < 16; bx++)
				{
					int blockx = (x << 4) + bx;
					int blockz = (z << 4) + bz;

					if (bx == 0 || bz == 0 || bx == 15 || bz == 15)
					{
						for (int y = 0; y < 5; y++)
						{
							world.setBlockState(new BlockPos(blockx, TeamIslandsConfig.lobby.height + y, blockz), barrier);
						}
					}
					else
					{
						world.setBlockState(new BlockPos((x << 4) + bx, TeamIslandsConfig.lobby.height - 1, (z << 4) + bz), barrier);
					}
				}
			}
		}
	}


	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z)
	{
		return false;
	}

	@Override
	public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
	{
		return world.getBiome(pos).getSpawnableList(creatureType);
	}

	@Nullable
	@Override
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
	{
		return null;
	}

	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z)
	{
	}

	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos)
	{
		return false;
	}
}