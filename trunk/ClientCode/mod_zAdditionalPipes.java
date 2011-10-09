package net.minecraft.src;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.src.buildcraft.api.APIProxy;
import net.minecraft.src.buildcraft.core.CoreProxy;
import net.minecraft.src.buildcraft.transport.BlockGenericPipe;
import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.buildcraft.transport.TileGenericPipe;
import net.minecraft.src.buildcraft.zeldo.MutiPlayerProxy;
import net.minecraft.src.buildcraft.zeldo.gui.GuiAdvancedWoodPipe;
import net.minecraft.src.buildcraft.zeldo.gui.GuiItemTeleportPipe;
import net.minecraft.src.buildcraft.zeldo.gui.GuiLiquidTeleportPipe;
import net.minecraft.src.buildcraft.zeldo.gui.GuiPowerTeleportPipe;
import net.minecraft.src.buildcraft.zeldo.logic.PipeLogicAdvancedWood;
import net.minecraft.src.buildcraft.zeldo.pipes.PipeItemTeleport;
import net.minecraft.src.buildcraft.zeldo.pipes.PipeItemsAdvancedWood;
import net.minecraft.src.buildcraft.zeldo.pipes.PipeItemsDistributor;
import net.minecraft.src.buildcraft.zeldo.pipes.PipeLiquidsTeleport;
import net.minecraft.src.buildcraft.zeldo.pipes.PipePowerTeleport;
import net.minecraft.src.forge.Configuration;
import net.minecraft.src.forge.MinecraftForgeClient;
import net.minecraft.src.forge.Property;



public class mod_zAdditionalPipes extends BaseModMp {
	@SuppressWarnings("serial")
	public static class chunkXZ implements Serializable
	{
		public int x;
		public int z;

		public chunkXZ(int ax, int az)
		{
			x = ax;
			z = az;
		}
	}

	//Item Teleport
	public static Item pipeItemTeleport;
	public static int DEFUALT_ITEM_TELEPORT_ID = 4047;
	public static int DEFUALT_ITEM_TELEPORT_TEXTURE = 8 * 16 + 0;
	public static String DEFUALT_ITEM_TELEPORT_TEXTURE_FILE = "/net/minecraft/src/buildcraft/zeldo/gui/BlueItem.png";

	//Liquid Teleport
	public static Item pipeLiquidTeleport;
	public static int DEFUALT_LIQUID_TELEPORT_ID = 4048;
	public static int DEFUALT_LIQUID_TELEPORT_TEXTURE = 8 * 16 + 2;
	public static String DEFUALT_LIQUID_TELEPORT_TEXTURE_FILE = "/net/minecraft/src/buildcraft/zeldo/gui/BlueLiquid.png";

	//Power Teleport
	public static Item pipePowerTeleport;
	public static int DEFUALT_POWER_TELEPORT_ID = 4049;
	public static int DEFUALT_POWER_TELEPORT_TEXTURE = 8 * 16 + 3;
	public static String DEFUALT_POWER_TELEPORT_TEXTURE_FILE = "/net/minecraft/src/buildcraft/zeldo/gui/BluePower.png";

	//Distributor
	public static Item pipeDistributor;
	public static int DEFUALT_DISTRIBUTOR_TELEPORT_ID = 4046;
	public static int DEFUALT_DISTRIBUTOR_TEXTURE = 8*16+4;
	public static int DEFUALT_DISTRIBUTOR_TEXTURE_CLOSED = 8*16+5;
	public static String DEFUALT_DISTRIBUTOR_TEXTURE_FILE = "/net/minecraft/src/buildcraft/zeldo/gui/DistributionOpen.png";
	public static String DEFUALT_DISTRIBUTOR_TEXTURE_FILE_CLOSED = "/net/minecraft/src/buildcraft/zeldo/gui/DistributionClosed.png";

	//Advanced Wood
	public static Item pipeAdvancedWood;
	public static int DEFUALT_ADVANCEDWOOD_ID = 4045;
	public static int DEFUALT_ADVANCEDWOOD_TEXTURE = 8*16+6;
	public static int DEFUALT_ADVANCEDWOOD_TEXTURE_CLOSED = 8*16+7;
	public static String DEFUALT_ADVANCEDWOOD_FILE = "/net/minecraft/src/buildcraft/zeldo/gui/AdvancedWood.png";
	public static String DEFUALT_ADVANCEDWOOD_FILE_CLOSED = "/net/minecraft/src/buildcraft/zeldo/gui/AdvancedWoodClosed.png";

	//Advanced Insertion
	public static Item pipeAdvancedInsertion;
	public static int DEFUALT_Insertion_ID = 4044;
	public static int DEFUALT_Insertion_TEXTURE = 8*16+6;
	public static int DEFUALT_Insertion_TEXTURE_CLOSED = 8*16+7;
	public static String DEFUALT_Insertion_FILE = "/net/minecraft/src/buildcraft/zeldo/gui/AdvancedWood.png";

	//GUI Packet Ids
	public static int GUI_ITEM_SEND = 255;
	public static int GUI_LIQUID_SEND = 254;
	public static int GUI_ENERGY_SEND = 253;
	public static int GUI_ADVANCEDWOOD_SEND = 252;
	public static int GUI_ITEM_REC = -1;
	public static int GUI_LIQUID_REC = -2;
	public static int GUI_ENERGY_REC = -3;
	public static int GUI_ADVANCEDWOOD_REC = -4;

	//Main Packet ID's
		public static int PACKET_SET_AW = 1;
		public static int PACKET_SET_ITEM = 2;
		public static int PACKET_SET_LIQUID = 3;
		public static int PACKET_SET_POWER = 4;
		public static int PACKET_REQ_ITEM = 5;
		public static int PACKET_REQ_LIQUID = 6;
		public static int PACKET_REQ_POWER = 7;
		public static int PACKET_GUI_COUNT = 8;
		
		public static int CurrentGUICount = 0;




	private static Configuration config;
	public static mod_zAdditionalPipes instance;
	public boolean isInGame = false;
	public boolean lagFix = false;


	//ChunkLoader Variables
	public int chunkTestTime = 500;
	public long lastCheckTime = 0;
	public static List<chunkXZ> keepLoadedChunks = new ArrayList<chunkXZ>();

	public mod_zAdditionalPipes() {
		ModLoader.SetInGameHook(this, true, true);
		ModLoader.SetInGUIHook(this, true, true);
	}
	public boolean OnTickInGame(Minecraft minecraft)
	{
		if (System.currentTimeMillis() - chunkTestTime >= lastCheckTime) {
			lastCheckTime = System.currentTimeMillis();
			if (lagFix) {
				minecraft.theWorld.autosavePeriod = 6000;
			}
			Iterator<chunkXZ> chunks = keepLoadedChunks.iterator();
			while (chunks.hasNext()) {
				chunkXZ curChunk = (chunkXZ)chunks.next();
				if (minecraft.theWorld.chunkProvider.chunkExists(curChunk.x,curChunk.z)) {
					//System.out.print("A: " + minecraft.theWorld.chunkProvider.provideChunk(curChunk.x,curChunk.z).isChunkLoaded + "\n");
				} else {
					//System.out.print("Does not exist...\n");
					minecraft.theWorld.chunkProvider.provideChunk(curChunk.x,curChunk.z);
					minecraft.theWorld.chunkProvider.provideChunk(curChunk.x-1,curChunk.z);
					minecraft.theWorld.chunkProvider.provideChunk(curChunk.x+1,curChunk.z);
					minecraft.theWorld.chunkProvider.provideChunk(curChunk.x,curChunk.z-1);
					minecraft.theWorld.chunkProvider.provideChunk(curChunk.x,curChunk.z+1);
					//System.out.println("Loaded Chunk @ " + curChunk.x + "," + curChunk.z);
				}
			}

		}
		return true;
	}
	public boolean OnTickInGUI(Minecraft minecraft, GuiScreen guiscreen)
	{

		//System.out.print("World: " + (minecraft.theWorld == null) + "\n");
		if (minecraft.theWorld == null)
		{

			if (isInGame)
			{
				System.out.print("Cleared TeleportPipes...\n");
				PipeItemTeleport.ItemTeleportPipes.clear();
				PipeLiquidsTeleport.LiquidTeleportPipes.clear();
				PipePowerTeleport.PowerTeleportPipes.clear();
				keepLoadedChunks.clear();
				MutiPlayerProxy.NeedsLoad = true;
				isInGame = false;
			}
		} else {
			isInGame = true;	
		}
		return true;
	}
	public static File getSaveDirectory() { return ((SaveHandler)ModLoader.getMinecraftInstance().theWorld.saveHandler).getSaveDirectory(); }
	public void ModsLoaded () {	
		ModLoaderMp.RegisterGUI(this, GUI_ADVANCEDWOOD_REC);
		ModLoaderMp.RegisterGUI(this, GUI_ENERGY_REC);
		ModLoaderMp.RegisterGUI(this, GUI_ITEM_REC);
		ModLoaderMp.RegisterGUI(this, GUI_LIQUID_REC);
		instance = this;

		config = new Configuration(new File(CoreProxy.getBuildCraftBase(), "config/AdditionalPipes.cfg"));
		config.load();

		lagFix = Boolean.parseBoolean(config.getOrCreateBooleanProperty("saveLagFix", Configuration.GENERAL_PROPERTY, false).value);
		config.save();


		//BuildCraftCore.customBuildCraftTexture = mod_AdditionalPipes.BUILDCRAFT_OVERRIDE_TEXTURE;
		//MinecraftForgeClient.preloadTexture(BuildCraftCore.customBuildCraftTexture);
		//ModLoader.addOverride(BuildCraftCore.customBuildCraftTexture, "/net/minecraft/src/zeldo/gui/pipe.png");
		//MinecraftForgeClient.preloadTexture("/buildcraft/zeldo/gui/pipe.png");
		AddImageOverride();

		pipeItemTeleport = createPipe (mod_zAdditionalPipes.DEFUALT_ITEM_TELEPORT_ID, PipeItemTeleport.class, "Item Teleport Pipe", BuildCraftCore.diamondGearItem, Block.glass, BuildCraftCore.diamondGearItem, null);
		pipeLiquidTeleport = createPipe (mod_zAdditionalPipes.DEFUALT_LIQUID_TELEPORT_ID, PipeLiquidsTeleport.class, "Waterproof Teleport Pipe", BuildCraftTransport.pipeWaterproof, pipeItemTeleport, null, null);
		pipePowerTeleport = createPipe (mod_zAdditionalPipes.DEFUALT_POWER_TELEPORT_ID, PipePowerTeleport.class, "Power Teleport Pipe", Item.redstone, pipeItemTeleport, null, null);
		pipeDistributor = createPipe(mod_zAdditionalPipes.DEFUALT_DISTRIBUTOR_TELEPORT_ID, PipeItemsDistributor.class, "Distribution Transport Pipe", Item.redstone, Item.ingotIron, Block.glass, Item.ingotIron);
		pipeAdvancedWood = createPipe(mod_zAdditionalPipes.DEFUALT_ADVANCEDWOOD_ID, PipeItemsAdvancedWood.class, "Advanced Wood Transport Pipe", Item.redstone, Block.planks, Block.glass, Block.planks);
		MinecraftForgeClient.registerCustomItemRenderer(pipeItemTeleport.shiftedIndex, mod_BuildCraftTransport.instance);
		MinecraftForgeClient.registerCustomItemRenderer(pipeLiquidTeleport.shiftedIndex, mod_BuildCraftTransport.instance);
		MinecraftForgeClient.registerCustomItemRenderer(pipePowerTeleport.shiftedIndex, mod_BuildCraftTransport.instance);
		MinecraftForgeClient.registerCustomItemRenderer(pipeDistributor.shiftedIndex, mod_BuildCraftTransport.instance);
		MinecraftForgeClient.registerCustomItemRenderer(pipeAdvancedWood.shiftedIndex, mod_BuildCraftTransport.instance);



	}
	public GuiScreen HandleGUI(int inventoryType) 
	{
		System.out.println("InvType: " + inventoryType);
		if(inventoryType == GUI_LIQUID_REC)
			return new GuiLiquidTeleportPipe((TileGenericPipe)ModLoader.getMinecraftInstance().theWorld.getBlockTileEntity(ModLoader.getMinecraftInstance().objectMouseOver.blockX, ModLoader.getMinecraftInstance().objectMouseOver.blockY, ModLoader.getMinecraftInstance().objectMouseOver.blockZ));
		else if (inventoryType == GUI_ITEM_REC)
			return new GuiItemTeleportPipe((TileGenericPipe)ModLoader.getMinecraftInstance().theWorld.getBlockTileEntity(ModLoader.getMinecraftInstance().objectMouseOver.blockX, ModLoader.getMinecraftInstance().objectMouseOver.blockY, ModLoader.getMinecraftInstance().objectMouseOver.blockZ));
		else if (inventoryType == GUI_ENERGY_REC)
			return new GuiPowerTeleportPipe((TileGenericPipe)ModLoader.getMinecraftInstance().theWorld.getBlockTileEntity(ModLoader.getMinecraftInstance().objectMouseOver.blockX, ModLoader.getMinecraftInstance().objectMouseOver.blockY, ModLoader.getMinecraftInstance().objectMouseOver.blockZ));
		else if (inventoryType == GUI_ADVANCEDWOOD_REC)
		{
			TileGenericPipe tmp = new TileGenericPipe();
			tmp.pipe = new PipeItemsAdvancedWood(pipeAdvancedWood.shiftedIndex);
			return new GuiAdvancedWoodPipe(ModLoader.getMinecraftInstance().thePlayer.inventory, tmp, (TileGenericPipe)ModLoader.getMinecraftInstance().theWorld.getBlockTileEntity(ModLoader.getMinecraftInstance().objectMouseOver.blockX, ModLoader.getMinecraftInstance().objectMouseOver.blockY, ModLoader.getMinecraftInstance().objectMouseOver.blockZ));

		}


		return null;
	}
	@SuppressWarnings("rawtypes")
	public void AddImageOverride()
	{
		try {
			HashMap textures = new HashMap();
			textures = (HashMap) ModLoader.getPrivateValue(RenderEngine.class, ModLoader.getMinecraftInstance().renderEngine, 1);
			int i = (Integer) textures.get(BuildCraftCore.customBuildCraftTexture);
			ModTextureStatic modtexturestatic = new ModTextureStatic(mod_zAdditionalPipes.DEFUALT_ITEM_TELEPORT_TEXTURE, i, ModLoader.loadImage(ModLoader.getMinecraftInstance().renderEngine, mod_zAdditionalPipes.DEFUALT_ITEM_TELEPORT_TEXTURE_FILE));
			ModLoader.getMinecraftInstance().renderEngine.registerTextureFX(modtexturestatic);
			modtexturestatic = new ModTextureStatic(mod_zAdditionalPipes.DEFUALT_LIQUID_TELEPORT_TEXTURE, i, ModLoader.loadImage(ModLoader.getMinecraftInstance().renderEngine, mod_zAdditionalPipes.DEFUALT_LIQUID_TELEPORT_TEXTURE_FILE));
			ModLoader.getMinecraftInstance().renderEngine.registerTextureFX(modtexturestatic);
			modtexturestatic = new ModTextureStatic(mod_zAdditionalPipes.DEFUALT_POWER_TELEPORT_TEXTURE, i, ModLoader.loadImage(ModLoader.getMinecraftInstance().renderEngine, mod_zAdditionalPipes.DEFUALT_POWER_TELEPORT_TEXTURE_FILE));
			ModLoader.getMinecraftInstance().renderEngine.registerTextureFX(modtexturestatic);
			modtexturestatic = new ModTextureStatic(mod_zAdditionalPipes.DEFUALT_DISTRIBUTOR_TEXTURE, i, ModLoader.loadImage(ModLoader.getMinecraftInstance().renderEngine, mod_zAdditionalPipes.DEFUALT_DISTRIBUTOR_TEXTURE_FILE));
			ModLoader.getMinecraftInstance().renderEngine.registerTextureFX(modtexturestatic);
			modtexturestatic = new ModTextureStatic(mod_zAdditionalPipes.DEFUALT_DISTRIBUTOR_TEXTURE_CLOSED, i, ModLoader.loadImage(ModLoader.getMinecraftInstance().renderEngine, mod_zAdditionalPipes.DEFUALT_DISTRIBUTOR_TEXTURE_FILE_CLOSED));
			ModLoader.getMinecraftInstance().renderEngine.registerTextureFX(modtexturestatic);
			modtexturestatic = new ModTextureStatic(mod_zAdditionalPipes.DEFUALT_ADVANCEDWOOD_TEXTURE_CLOSED, i, ModLoader.loadImage(ModLoader.getMinecraftInstance().renderEngine, mod_zAdditionalPipes.DEFUALT_ADVANCEDWOOD_FILE_CLOSED));
			ModLoader.getMinecraftInstance().renderEngine.registerTextureFX(modtexturestatic);
			modtexturestatic = new ModTextureStatic(mod_zAdditionalPipes.DEFUALT_ADVANCEDWOOD_TEXTURE, i, ModLoader.loadImage(ModLoader.getMinecraftInstance().renderEngine, mod_zAdditionalPipes.DEFUALT_ADVANCEDWOOD_FILE));
			ModLoader.getMinecraftInstance().renderEngine.registerTextureFX(modtexturestatic);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String Version() {
		return "1.4";
	}
	public void HandlePacket(Packet230ModLoader packet) {
		System.out.println("Packet: " + packet.packetType);
		if (packet.packetType == PACKET_SET_AW) {
			int x = packet.dataInt [0];
			int y = packet.dataInt [1];
			int z = packet.dataInt [2];
			if (APIProxy.getWorld().blockExists(x, y, z)) {
				TileGenericPipe tile = (TileGenericPipe) APIProxy.getWorld().getBlockTileEntity(x, y, z);
				boolean Exclude = intToBool(packet.dataInt[3]);
				((PipeLogicAdvancedWood)tile.pipe.logic).exclude = Exclude;
			}
		}
		if (packet.packetType == PACKET_SET_ITEM) {
			int x = packet.dataInt [0];
			int y = packet.dataInt [1];
			int z = packet.dataInt [2];
			if (APIProxy.getWorld().blockExists(x, y, z)) {
				TileGenericPipe tile = (TileGenericPipe) APIProxy.getWorld().getBlockTileEntity(x, y, z);
				int freq = packet.dataInt[3];
				boolean canRec = intToBool(packet.dataInt[4]);
				String own = packet.dataString[0];
				((PipeItemTeleport)tile.pipe).canReceive = canRec;
				((PipeItemTeleport)tile.pipe).myFreq = freq;
				((PipeItemTeleport)tile.pipe).Owner = own;
			}
		}
		if (packet.packetType == PACKET_SET_LIQUID) {
			int x = packet.dataInt [0];
			int y = packet.dataInt [1];
			int z = packet.dataInt [2];
			if (APIProxy.getWorld().blockExists(x, y, z)) {
				TileGenericPipe tile = (TileGenericPipe) APIProxy.getWorld().getBlockTileEntity(x, y, z);
				int freq = packet.dataInt[3];
				boolean canRec = intToBool(packet.dataInt[4]);
				String own = packet.dataString[0];
				((PipeLiquidsTeleport)tile.pipe).canReceive = canRec;
				((PipeLiquidsTeleport)tile.pipe).myFreq = freq;
				((PipeLiquidsTeleport)tile.pipe).Owner = own;
			}
		}
		if (packet.packetType == PACKET_SET_POWER) {
			int x = packet.dataInt [0];
			int y = packet.dataInt [1];
			int z = packet.dataInt [2];
			if (APIProxy.getWorld().blockExists(x, y, z)) {
				TileGenericPipe tile = (TileGenericPipe) APIProxy.getWorld().getBlockTileEntity(x, y, z);
				int freq = packet.dataInt[3];
				boolean canRec = intToBool(packet.dataInt[4]);
				String own = packet.dataString[0];
				((PipePowerTeleport)tile.pipe).canReceive = canRec;
				((PipePowerTeleport)tile.pipe).myFreq = freq;
				((PipePowerTeleport)tile.pipe).Owner = own;
			}
		}
		if (packet.packetType == PACKET_GUI_COUNT) {
			CurrentGUICount = packet.dataInt[0];
		}
	}
	public static boolean intToBool(int a) {
		return (a == 1);
	}
	//	public void HandlePacket(Packet230ModLoader packet) {
	//		//System.out.print("PacketID: " + packet.packetType+"\n");
	//		if (packet.packetType == 1) {	
	//			int x = packet.dataInt [0];
	//			int y = packet.dataInt [1];
	//			int z = packet.dataInt [2];
	//			if (APIProxy.getWorld().blockExists(x, y, z)) {
	//				TileEntity tile = APIProxy.getWorld().getBlockTileEntity(x, y, z);
	//				
	//				if (tile instanceof TileTeleportPipe) {
	//					int freq = packet.dataInt[3];
	//					//System.out.print("Owner: " +packet.dataString[0]+"\n");
	//					((TileTeleportPipe) tile).handleGuiPacket(x, y, z, freq, (packet.dataInt[4] == 1), packet.dataString[0]);
	//					return;
	//				}
	//			}
	//		}
	//		if (packet.packetType == 3) {	
	//			int x = packet.dataInt [0];
	//			int y = packet.dataInt [1];
	//			int z = packet.dataInt [2];
	//			if (APIProxy.getWorld().blockExists(x, y, z)) {
	//				TileEntity tile = APIProxy.getWorld().getBlockTileEntity(x, y, z);
	//				
	//				if (tile instanceof TileOilTeleportPipe) {
	//					int freq = packet.dataInt[3];
	//					((TileOilTeleportPipe) tile).handleGuiPacket(x, y, z, freq, (packet.dataInt[4] == 1), packet.dataString[0]);
	//					return;
	//				}
	//			}
	//		}
	//		if (packet.packetType == 2) {	//Delete Entity
	//			int x = packet.dataInt [0];
	//			int y = packet.dataInt [1];
	//			int z = packet.dataInt [2];
	//			if (APIProxy.getWorld().blockExists(x, y, z)) {
	//				TileEntity tile = APIProxy.getWorld().getBlockTileEntity(x, y, z);
	//				
	//				if (tile instanceof TileTeleportPipe) {
	//					((TileTeleportPipe) tile).travelingEntities.remove(packet.dataInt[3]);
	//					return;
	//				}
	//			}
	//		} 
	//		if (packet.packetType == 4) {	//Delete OilPipe
	//			int x = packet.dataInt [0];
	//			int y = packet.dataInt [1];
	//			int z = packet.dataInt [2];
	//			if (APIProxy.getWorld().blockExists(x, y, z)) {
	//				TileEntity tile = APIProxy.getWorld().getBlockTileEntity(x, y, z);
	//				
	//				if (tile instanceof TileOilTeleportPipe) {
	//					((TileOilTeleportPipe) tile).oilTeleportPipes.remove(packet.dataInt[3]);
	//					return;
	//				}
	//			}
	//		}
	//		if (packet.packetType == 5) {	//Delete Tele Pipe
	//			int x = packet.dataInt [0];
	//			int y = packet.dataInt [1];
	//			int z = packet.dataInt [2];
	//			if (APIProxy.getWorld().blockExists(x, y, z)) {
	//				TileEntity tile = APIProxy.getWorld().getBlockTileEntity(x, y, z);
	//				
	//				if (tile instanceof TileTeleportPipe) {
	//					((TileTeleportPipe) tile).teleportPipes.remove(packet.dataInt[3]);
	//					return;
	//				}
	//			}
	//		}
	//		if (packet.packetType == 10) {	//Delete Tele Pipe
	//			ModLoader.getMinecraftInstance().playerController.resetBlockRemoving();
	//		}
	//		
	//    }
	public static int boolToInt(boolean a) {
		if (a)
			return 1;
		return 0;
	}
	private static Item createPipe (int defaultID, Class <? extends Pipe> clas, String descr, Object r1, Object r2, Object r3, Object r4) {
		String name = Character.toLowerCase(clas.getSimpleName().charAt(0))
				+ clas.getSimpleName().substring(1);

		Property prop = config
				.getOrCreateIntProperty(name + ".id",
						Configuration.ITEM_PROPERTY, defaultID);
		config.save();
		int id = Integer.parseInt(prop.value);
		Item res =  BlockGenericPipe.registerPipe (id, clas);
		res.setItemName(clas.getSimpleName());
		CoreProxy.addName(res, descr);

		CraftingManager craftingmanager = CraftingManager.getInstance();

		if (r1 != null && r2 != null && r3 != null && r4 != null) {						
			craftingmanager.addRecipe(new ItemStack(res, 8), new Object[] {
				" D ", "ABC", "   ", 
				Character.valueOf('D'), r1,
				Character.valueOf('A'), r2,
				Character.valueOf('B'), r3,
				Character.valueOf('C'), r4});
		} else if (r1 != null && r2 != null && r3 != null) {						
			craftingmanager.addRecipe(new ItemStack(res, 8), new Object[] {
				"   ", "ABC", "   ", 
				Character.valueOf('A'), r1,
				Character.valueOf('B'), r2,
				Character.valueOf('C'), r3});
		} else if (r1 != null && r2 != null) {
			craftingmanager.addRecipe(new ItemStack(res, 1), new Object[] {
				"A ", "B ", 
				Character.valueOf('A'), r1,
				Character.valueOf('B'), r2});
		}

		return res;
	}
}
