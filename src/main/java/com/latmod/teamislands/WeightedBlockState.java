package com.latmod.teamislands;

import com.feed_the_beast.ftblib.lib.math.MathUtils;
import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import java.util.Random;

/**
 * @author LatvianModder
 */
public class WeightedBlockState
{
	public static class List
	{
		private final WeightedBlockState[] states;
		private int statesSize;

		public List(String[] blocks)
		{
			states = new WeightedBlockState[blocks.length];
			int index = 0;

			for (String s : blocks)
			{
				String[] s1 = s.split(" ");

				if (s1.length == 2 && MathUtils.canParseInt(s1[1]))
				{
					states[index] = new WeightedBlockState(CommonUtils.getStateFromName(s1[0]), Integer.parseInt(s1[1]));
					statesSize += states[index].weight;
					index++;
				}
			}
		}

		public IBlockState get(Random random)
		{
			if (states.length == 0)
			{
				return Blocks.AIR.getDefaultState();
			}
			else if (states.length == 1)
			{
				return states[0].state;
			}

			int index = random.nextInt(statesSize);

			for (WeightedBlockState w : states)
			{
				index -= w.weight;

				if (index < 0)
				{
					return w.state;
				}
			}

			return Blocks.AIR.getDefaultState();
		}
	}

	public final IBlockState state;
	public final int weight;

	public WeightedBlockState(IBlockState s, int w)
	{
		state = s;
		weight = w;
	}
}