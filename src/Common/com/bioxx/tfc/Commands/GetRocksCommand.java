package com.bioxx.tfc.Commands;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.bioxx.tfc.Core.TFC_Climate;
import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.Items.ItemLooseRock;
import com.bioxx.tfc.WorldGen.DataLayer;
import com.bioxx.tfc.api.TFCBlocks;
import com.bioxx.tfc.api.TFCItems;

public class GetRocksCommand extends CommandBase
{
	@Override
	public String getCommandName()
	{
		return "gr";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] params)
	{
		//MinecraftServer var3 = MinecraftServer.getServer();
		EntityPlayerMP var4 = getCommandSenderAsPlayer(sender);

		DataLayer[] layers = TFC_Climate.getAllRockLayers(var4.worldObj, (int)var4.posX, (int)var4.posZ);
		String format = Arrays.stream(layers).map(rl -> ((ItemLooseRock)TFCItems.looseRock).metaNames[rl.data1]).collect(Collectors.joining("   ", "Rock Layer %d: ", ""));
		int[] index = new int[layers.length];
		for (int i = 0; i < index.length; i++) index[i] = i + 1;

		throw new PlayerNotFoundException(String.format(format, index));
	}

	public static int getSoilMetaFromStone(Block inType, int inMeta)
	{
		return TFC_Core.getSoilMetaFromStone(inType, inMeta);
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "";
	}
}
