package com.latmod.teamislands;

import net.minecraft.init.Biomes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;

/**
 * @author LatvianModder
 */
public class TeamIslandsWorldType extends WorldType
{
	public static final BiomeProviderSingle BIOME_PROVIDER = new BiomeProviderSingle(Biomes.PLAINS);
	public static final TeamIslandsWorldType INSTANCE = new TeamIslandsWorldType();

	public void init()
	{
	}

	private TeamIslandsWorldType()
	{
		super(TeamIslandsFinals.MOD_ID);
	}

	public boolean is(World world)
	{
		return world.getWorldType() == this;
	}

	@Override
	public BiomeProvider getBiomeProvider(World world)
	{
		return BIOME_PROVIDER;
	}

	@Override
	public IChunkGenerator getChunkGenerator(World world, String generatorOptions)
	{
		return new TeamIslandsChunkGenerator(world);
	}

	@Override
	public int getMinimumSpawnHeight(World world)
	{
		return TeamIslandsConfig.islands.height + 1;
	}

	@Override
	public double getHorizon(World world)
	{
		return 0D;
	}

	@Override
	public double voidFadeMagnitude()
	{
		return 1D;
	}

	@Override
	public int getSpawnFuzz(WorldServer world, net.minecraft.server.MinecraftServer server)
	{
		return 0;
	}
}