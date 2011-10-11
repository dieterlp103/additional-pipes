/** 
 * BuildCraft is open-source. It is distributed under the terms of the 
 * BuildCraft Open Source License. It grants rights to read, modify, compile
 * or run the code. It does *NOT* grant the right to redistribute this software
 * or its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package net.minecraft.src.buildcraft.zeldo.logic;

import net.minecraft.src.BuildCraftTransport;
import net.minecraft.src.TileEntity;
import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.buildcraft.transport.PipeLogic;
import net.minecraft.src.buildcraft.transport.PipeLogicCobblestone;
import net.minecraft.src.buildcraft.transport.TileGenericPipe;

public class PipeLogicAdvancedInsertion extends PipeLogic {
	
	@Override
	public boolean isPipeConnected(TileEntity tile) {
		Pipe pipe2 = null;

		if (tile instanceof TileGenericPipe) {
			pipe2 = ((TileGenericPipe) tile).pipe;
		}

		if (BuildCraftTransport.alwaysConnectPipes) {
			return super.isPipeConnected(tile);
		} else {
			return (pipe2 == null || !(pipe2.logic instanceof PipeLogicCobblestone))
					&& super.isPipeConnected(tile);
		}		
	}

}
