package net.minecraft.src.buildcraft.zeldo;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;

public class ContainerTeleportPipe extends Container {
	
	public boolean isUsableByPlayer(EntityPlayer entityplayer)
    {
		return true;
    }
	public boolean canInteractWith(EntityPlayer entityplayer) {
		// TODO Auto-generated method stub
		return true;
	}
}
