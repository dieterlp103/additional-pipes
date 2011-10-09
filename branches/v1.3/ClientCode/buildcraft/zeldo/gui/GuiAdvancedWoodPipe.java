/** 
 * BuildCraft is open-source. It is distributed under the terms of the 
 * BuildCraft Open Source License. It grants rights to read, modify, compile
 * or run the code. It does *NOT* grant the right to redistribute this software
 * or its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package net.minecraft.src.buildcraft.zeldo.gui;

import net.minecraft.src.Block;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.buildcraft.transport.TileGenericPipe;
import net.minecraft.src.buildcraft.zeldo.pipes.PipeItemsAdvancedWood;

import org.lwjgl.opengl.GL11;

public class GuiAdvancedWoodPipe extends GuiContainer {


	IInventory playerInventory;
	IInventory filterInventory;
	TileGenericPipe container;
	private GuiButton[] buttons = new GuiButton[1];
	
	public GuiAdvancedWoodPipe(IInventory playerInventorys, IInventory filterInventorys, TileGenericPipe container) {
		super(new CraftingAdvancedWoodPipe(playerInventorys, filterInventorys));
		this.playerInventory = playerInventorys;
		this.filterInventory = filterInventorys;
		this.container = container;
		//container = theContainer;
		xSize = 175;
		ySize = 156;
	
	}
	@SuppressWarnings("unchecked")
	public void initGui()
	{
		super.initGui();
		int guiX = (width - this.xSize) / 2;
        int guiY = (height - this.ySize) / 2;
		controlList.add(this.buttons[0] =  new GuiButton(1, guiX + 8, guiY + 40, 140, 20, "These items are required"));
	}
	
    protected void drawGuiContainerForegroundLayer()
    {
    	if (((PipeItemsAdvancedWood)container.pipe).exclude) {
    		this.buttons[0].displayString = "These items are excluded";
    	} else {
    		this.buttons[0].displayString = "These items are required";
    	}
        fontRenderer.drawString(filterInventory.getInvName(), 8, 6, 0x404040);
        fontRenderer.drawString(playerInventory.getInvName(), 8, 66, 0x404040);        
    }
    protected void actionPerformed(GuiButton guibutton) {
    	if (guibutton.id == 1)
    	{
    		((PipeItemsAdvancedWood)container.pipe).exclude = !((PipeItemsAdvancedWood)container.pipe).exclude;
    	}
    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f) {
		int i = mc.renderEngine
				.getTexture("/net/minecraft/src/buildcraft/zeldo/gui/advancedwoodgui.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(i);
		int j = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
	}

	int inventoryRows = 1;
}
