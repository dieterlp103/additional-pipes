/** 
 * BuildCraft is open-source. It is distributed under the terms of the 
 * BuildCraft Open Source License. It grants rights to read, modify, compile
 * or run the code. It does *NOT* grant the right to redistribute this software
 * or its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package net.minecraft.src.buildcraft.zeldo.gui;

import net.minecraft.src.IInventory;
import net.minecraft.src.Slot;
import net.minecraft.src.buildcraft.core.BuildCraftContainer;

class CraftingAdvancedWoodPipe extends BuildCraftContainer {
	
	IInventory playerIInventory;
	IInventory filterIInventory;
	
	public CraftingAdvancedWoodPipe (IInventory playerInventory, IInventory filterInventory) {		
		super (filterInventory.getSizeInventory());		
		this.playerIInventory = playerInventory;
		this.filterIInventory = filterInventory;
		
		int k=0;
            for(int j1 = 0; j1 < 9; j1++)
            {
                addSlot(new Slot(filterInventory, j1 + k * 9, 8 + j1 * 18, 18 + k * 18));
            }


        for(int l = 0; l < 3; l++)
        {
            for(int k1 = 0; k1 < 9; k1++)
            {
                addSlot(new Slot(playerInventory, k1 + l * 9 + 9, 8 + k1 * 18, 76 + l * 18));
            }

        }

        for(int i1 = 0; i1 < 9; i1++)
        {
            addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 134));
        }
	}
	
}