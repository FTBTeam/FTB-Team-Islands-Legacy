package com.feed_the_beast.mods.teamislands;

import net.minecraft.init.Biomes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * @author LatvianModder
 */
public class VoidWorldType extends WorldType
{
	public VoidWorldType()
	{
		super(TeamIslandsConfig.general.void_world_type_id);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslationKey()
	{
		return "generator.void";
	}

	@Override
	public BiomeProvider getBiomeProvider(World world)
	{
		return new BiomeProviderSingle(Biomes.PLAINS);
	}

	@Override
	public int getSpawnFuzz(WorldServer world, MinecraftServer server)
	{
		return 0;
	}

	@Override
	public float getCloudHeight()
	{
		return 260F;
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
	public int getMinimumSpawnHeight(World world)
	{
		return 1;
	}

	@Override
	@Nonnull
	public IChunkGenerator getChunkGenerator(World world, String generatorOptions)
	{
		ChunkGeneratorFlat flat = new ChunkGeneratorFlat(world, world.getSeed(), false, "3;minecraft:air;");
		world.setSeaLevel(64);
		return flat;
	}
}