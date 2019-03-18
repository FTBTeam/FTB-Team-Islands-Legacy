package com.feed_the_beast.mods.teamislands.data;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.gen.structure.template.Template;

/**
 * @author LatvianModder
 */
public class IslandTemplate
{
	public final String name;
	public final String path;
	public final Template template;
	public BlockPos spawn = BlockPos.ORIGIN;
	public ITextComponent displayName = null;
	public String icon = "";

	public IslandTemplate(String n, String p, Template t)
	{
		name = n;
		path = p;
		template = t;
	}
}