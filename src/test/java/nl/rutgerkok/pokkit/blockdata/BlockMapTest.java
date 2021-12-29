package nl.rutgerkok.pokkit.blockdata;

import static org.junit.Assert.assertEquals;

import org.bukkit.Material;
import org.junit.BeforeClass;
import org.junit.Test;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;

public class BlockMapTest {

	@BeforeClass
	public static void init() {
		Block.init();
	}
	
	@Test
	public void TestWoolNukkitToBukkit() {
		Material[] woolColors = new Material[16];
		for (int i = 0; i < 16; i++)
			woolColors[i] = BlockMap.getMaterial(BlockID.WOOL, i);
		assertEquals(Material.WHITE_WOOL, woolColors[0]);
		assertEquals(Material.ORANGE_WOOL, woolColors[1]);
		assertEquals(Material.MAGENTA_WOOL, woolColors[2]);
		assertEquals(Material.LIGHT_BLUE_WOOL, woolColors[3]);
		assertEquals(Material.YELLOW_WOOL, woolColors[4]);
		assertEquals(Material.LIME_WOOL, woolColors[5]);
		assertEquals(Material.PINK_WOOL, woolColors[6]);
		assertEquals(Material.GRAY_WOOL, woolColors[7]);
		assertEquals(Material.LIGHT_GRAY_WOOL, woolColors[8]);
		assertEquals(Material.CYAN_WOOL, woolColors[9]);
		assertEquals(Material.PURPLE_WOOL, woolColors[10]);
		assertEquals(Material.BLUE_WOOL, woolColors[11]);
		assertEquals(Material.BROWN_WOOL, woolColors[12]);
		assertEquals(Material.GREEN_WOOL, woolColors[13]);
		assertEquals(Material.RED_WOOL, woolColors[14]);
		assertEquals(Material.BLACK_WOOL, woolColors[15]);
	}
	
	@Test
	public void TestWoolBukkitToNukkit() {
		Block white = BlockMap.getNukkitBlock(Material.WHITE_WOOL, 0);
		assertEquals(Block.get(BlockID.WOOL, 0), white);
		Block white_OrangeMetadata = BlockMap.getNukkitBlock(Material.WHITE_WOOL, 1);
		assertEquals(Block.get(BlockID.WOOL, 0), white_OrangeMetadata);
		Block orange = BlockMap.getNukkitBlock(Material.ORANGE_WOOL, 0);
		assertEquals(Block.get(BlockID.WOOL, 1), orange);
		Block magenta = BlockMap.getNukkitBlock(Material.MAGENTA_WOOL, 0);
		assertEquals(Block.get(BlockID.WOOL, 2), magenta);
		Block lightblue = BlockMap.getNukkitBlock(Material.LIGHT_BLUE_WOOL, 0);
		assertEquals(Block.get(BlockID.WOOL, 3), lightblue);
		Block yellow = BlockMap.getNukkitBlock(Material.YELLOW_WOOL, 0);
		assertEquals(Block.get(BlockID.WOOL, 4), yellow);
		Block lime = BlockMap.getNukkitBlock(Material.LIME_WOOL, 0);
		assertEquals(Block.get(BlockID.WOOL, 5), lime);
		Block pink = BlockMap.getNukkitBlock(Material.PINK_WOOL, 0);
		assertEquals(Block.get(BlockID.WOOL, 6), pink);
		Block gray = BlockMap.getNukkitBlock(Material.GRAY_WOOL, 0);
		assertEquals(Block.get(BlockID.WOOL, 7), gray);
		Block lightgray = BlockMap.getNukkitBlock(Material.LIGHT_GRAY_WOOL, 0);
		assertEquals(Block.get(BlockID.WOOL, 8), lightgray);
		Block cyan = BlockMap.getNukkitBlock(Material.CYAN_WOOL, 0);
		assertEquals(Block.get(BlockID.WOOL, 9), cyan);
		Block purple = BlockMap.getNukkitBlock(Material.PURPLE_WOOL, 0);
		assertEquals(Block.get(BlockID.WOOL, 10), purple);
		Block blue = BlockMap.getNukkitBlock(Material.BLUE_WOOL, 0);
		assertEquals(Block.get(BlockID.WOOL, 11), blue);
		Block brown = BlockMap.getNukkitBlock(Material.BROWN_WOOL, 0);
		assertEquals(Block.get(BlockID.WOOL, 12), brown);
		Block green = BlockMap.getNukkitBlock(Material.GREEN_WOOL, 0);
		assertEquals(Block.get(BlockID.WOOL, 13), green);
		Block red = BlockMap.getNukkitBlock(Material.RED_WOOL, 0);
		assertEquals(Block.get(BlockID.WOOL, 14), red);
		Block black = BlockMap.getNukkitBlock(Material.BLACK_WOOL, 0);
		assertEquals(Block.get(BlockID.WOOL, 15), black);
	}
	
	@Test
	public void NukkitToBukkitMetadata()
	{
		Material granite = BlockMap.getMaterial(BlockID.STONE, 1);
		assertEquals(Material.GRANITE, granite);
		Material polishedAndesite = BlockMap.getMaterial(BlockID.STONE, 6);
		assertEquals(Material.POLISHED_ANDESITE, polishedAndesite);
		Material coarseDirt = BlockMap.getMaterial(BlockID.DIRT, 1);
		assertEquals(Material.COARSE_DIRT, coarseDirt);
		Material birchPlanks = BlockMap.getMaterial(BlockID.WOODEN_PLANKS, 2);
		assertEquals(Material.BIRCH_PLANKS, birchPlanks);
		Material acaciaSaplings = BlockMap.getMaterial(BlockID.SAPLING, 4);
		assertEquals(Material.ACACIA_SAPLING, acaciaSaplings);
		Material birchLog = BlockMap.getMaterial(BlockID.LOG, 2);
		assertEquals(Material.BIRCH_LOG, birchLog);
		Material darkOakLog = BlockMap.getMaterial(BlockID.LOG2, 1);
		assertEquals(Material.DARK_OAK_LOG, darkOakLog);
		Material jungleLeaves = BlockMap.getMaterial(BlockID.LEAVES, 3);
		assertEquals(Material.JUNGLE_LEAVES, jungleLeaves);
		Material darkOakLeaves = BlockMap.getMaterial(BlockID.LEAVES2, 1);
		assertEquals(Material.DARK_OAK_LEAVES, darkOakLeaves);
		Material wetSponge = BlockMap.getMaterial(BlockID.SPONGE, 1);
		assertEquals(Material.WET_SPONGE, wetSponge);
		Material chiseledSandstone = BlockMap.getMaterial(BlockID.SANDSTONE, 1);
		assertEquals(Material.CHISELED_SANDSTONE, chiseledSandstone);
		Material fern = BlockMap.getMaterial(BlockID.TALL_GRASS, 2);
		assertEquals(Material.FERN, fern);
		Material allium = BlockMap.getMaterial(BlockID.POPPY, 2);
		assertEquals(Material.ALLIUM, allium);
		Material oxeye = BlockMap.getMaterial(BlockID.POPPY, 8);
		assertEquals(Material.OXEYE_DAISY, oxeye);
		Material quartzSlab = BlockMap.getMaterial(BlockID.SLAB, 7);
		assertEquals(Material.QUARTZ_SLAB, quartzSlab);
		Material chiseledStoneBricks = BlockMap.getMaterial(BlockID.STONE_BRICKS, 3);
		assertEquals(Material.CHISELED_STONE_BRICKS, chiseledStoneBricks);
		Material blackGlassPane = BlockMap.getMaterial(BlockID.STAINED_GLASS_PANE, 15);
		assertEquals(Material.BLACK_STAINED_GLASS_PANE, blackGlassPane);
		Material mossyWall = BlockMap.getMaterial(BlockID.COBBLESTONE_WALL, 1);
		assertEquals(Material.MOSSY_COBBLESTONE_WALL, mossyWall);
		Material skeletonSkull = BlockMap.getMaterial(BlockID.SKULL_BLOCK, 0);
		assertEquals(Material.SKELETON_SKULL, skeletonSkull);
		Material witherSkull = BlockMap.getMaterial(BlockID.SKULL_BLOCK, 1);
		assertEquals(Material.WITHER_SKELETON_SKULL, witherSkull);
		Material zombieHead = BlockMap.getMaterial(BlockID.SKULL_BLOCK, 2);
		assertEquals(Material.ZOMBIE_HEAD, zombieHead);
		Material playerHead = BlockMap.getMaterial(BlockID.SKULL_BLOCK, 3);
		assertEquals(Material.PLAYER_HEAD, playerHead);
		Material creeperHead = BlockMap.getMaterial(BlockID.SKULL_BLOCK, 4);
		assertEquals(Material.CREEPER_HEAD, creeperHead);
		Material dragonHead = BlockMap.getMaterial(BlockID.SKULL_BLOCK, 5);
		assertEquals(Material.DRAGON_HEAD, dragonHead);
		Material birchSlab = BlockMap.getMaterial(BlockID.WOODEN_SLABS, 2);
		assertEquals(Material.BIRCH_SLAB, birchSlab);
		Material blackTerracotta = BlockMap.getMaterial(BlockID.STAINED_HARDENED_CLAY, 15);
		assertEquals(Material.BLACK_TERRACOTTA, blackTerracotta);
		Material darkPrismarine = BlockMap.getMaterial(BlockID.PRISMARINE, 2);
		assertEquals(Material.DARK_PRISMARINE, darkPrismarine);
		Material redCarpet = BlockMap.getMaterial(BlockID.CARPET, 14);
		assertEquals(Material.RED_CARPET, redCarpet);
		Material peony = BlockMap.getMaterial(BlockID.DOUBLE_PLANT, 5);
		assertEquals(Material.PEONY, peony);
		Material smoothRedSandstone = BlockMap.getMaterial(BlockID.RED_SANDSTONE, 2);
		assertEquals(Material.SMOOTH_RED_SANDSTONE, smoothRedSandstone);
		Material cyanConcrete = BlockMap.getMaterial(BlockID.CONCRETE, 9);
		assertEquals(Material.CYAN_CONCRETE, cyanConcrete);
		Material greenConcretePowder = BlockMap.getMaterial(BlockID.CONCRETE_POWDER, 13);
		assertEquals(Material.GREEN_CONCRETE_POWDER, greenConcretePowder);
		Material purpleGlass = BlockMap.getMaterial(BlockID.STAINED_GLASS, 10);
		assertEquals(Material.PURPLE_STAINED_GLASS, purpleGlass);
		Material whiteShulkerBox = BlockMap.getMaterial(BlockID.SHULKER_BOX, 0);
		assertEquals(Material.WHITE_SHULKER_BOX, whiteShulkerBox);
		Material blackShulkerBox = BlockMap.getMaterial(BlockID.SHULKER_BOX, 15);
		assertEquals(Material.BLACK_SHULKER_BOX, blackShulkerBox);
	}
	
	@Test
	public void BukkitToNukkitMetadata()
	{
		Block white = BlockMap.getNukkitBlock(Material.WHITE_WOOL, 0);
		assertEquals(Block.get(BlockID.WOOL, 0), white);
		Block black = BlockMap.getNukkitBlock(Material.BLACK_WOOL, 0);
		assertEquals(Block.get(BlockID.WOOL, 15), black);
		Block granite = BlockMap.getNukkitBlock(Material.GRANITE, 0);
		assertEquals(Block.get(BlockID.STONE, 1), granite);
		Block polishedAndesite = BlockMap.getNukkitBlock(Material.POLISHED_ANDESITE, 0);
		assertEquals(Block.get(BlockID.STONE, 6), polishedAndesite);
		Block coarseDirt = BlockMap.getNukkitBlock(Material.COARSE_DIRT, 0);
		assertEquals(Block.get(BlockID.DIRT, 1), coarseDirt);
		Block birchPlanks = BlockMap.getNukkitBlock(Material.BIRCH_PLANKS, 0);
		assertEquals(Block.get(BlockID.WOODEN_PLANKS, 2), birchPlanks);
		Block acaciaSaplings = BlockMap.getNukkitBlock(Material.ACACIA_SAPLING, 0);
		assertEquals(Block.get(BlockID.SAPLING, 2), acaciaSaplings);
		Block birchLog = BlockMap.getNukkitBlock(Material.BIRCH_LOG, 0);
		assertEquals(Block.get(BlockID.LOG, 2), birchLog);
		Block darkOakLog = BlockMap.getNukkitBlock(Material.DARK_OAK_LOG, 0);
		assertEquals(Block.get(BlockID.LOG2, 1), darkOakLog);
		Block jungleLeaves = BlockMap.getNukkitBlock(Material.JUNGLE_LEAVES, 3);
		assertEquals(Block.get(BlockID.LEAVES, 3), jungleLeaves);
		Block darkOakLeaves = BlockMap.getNukkitBlock(Material.DARK_OAK_LEAVES, 0);
		assertEquals(Block.get(BlockID.LEAVES2, 1), darkOakLeaves);
		Block wetSponge = BlockMap.getNukkitBlock(Material.WET_SPONGE, 0);
		assertEquals(Block.get(BlockID.SPONGE, 1), wetSponge);
		Block chiseledSandstone = BlockMap.getNukkitBlock(Material.CHISELED_SANDSTONE, 0);
		assertEquals(Block.get(BlockID.SANDSTONE, 1), chiseledSandstone);
		Block fern = BlockMap.getNukkitBlock(Material.FERN, 0);
		assertEquals(Block.get(BlockID.TALL_GRASS, 1), fern);
		Block allium = BlockMap.getNukkitBlock(Material.ALLIUM, 0);
		assertEquals(Block.get(BlockID.POPPY, 1), allium);
		Block oxeye = BlockMap.getNukkitBlock(Material.OXEYE_DAISY, 0);
		assertEquals(Block.get(BlockID.POPPY, 8), oxeye);
		Block quartzSlab = BlockMap.getNukkitBlock(Material.QUARTZ_SLAB, 0);
		assertEquals(Block.get(BlockID.SLAB, 7), quartzSlab);
		Block chiseledStoneBricks = BlockMap.getNukkitBlock(Material.CHISELED_STONE_BRICKS, 0);
		assertEquals(Block.get(BlockID.STONE_BRICKS, 3), chiseledStoneBricks);
		Block blackGlassPane = BlockMap.getNukkitBlock(Material.BLACK_STAINED_GLASS_PANE, 0);
		assertEquals(Block.get(BlockID.STAINED_GLASS_PANE, 15), blackGlassPane);
		Block mossyWall = BlockMap.getNukkitBlock(Material.MOSSY_COBBLESTONE_WALL, 0);
		assertEquals(Block.get(BlockID.COBBLESTONE_WALL, 1), mossyWall);
		Block skeletonSkull = BlockMap.getNukkitBlock(Material.SKELETON_SKULL, 0);
		assertEquals(Block.get(BlockID.SKULL_BLOCK, 0), skeletonSkull);
		Block witherSkull = BlockMap.getNukkitBlock(Material.WITHER_SKELETON_SKULL, 0);
		assertEquals(Block.get(BlockID.SKULL_BLOCK, 1), witherSkull);
		Block zombieHead = BlockMap.getNukkitBlock(Material.ZOMBIE_HEAD, 0);
		assertEquals(Block.get(BlockID.SKULL_BLOCK, 2), zombieHead);
		Block playerHead = BlockMap.getNukkitBlock(Material.PLAYER_HEAD, 0);
		assertEquals(Block.get(BlockID.SKULL_BLOCK, 3), playerHead);
		Block creeperHead = BlockMap.getNukkitBlock(Material.CREEPER_HEAD, 0);
		assertEquals(Block.get(BlockID.SKULL_BLOCK, 4), creeperHead);
		Block dragonHead = BlockMap.getNukkitBlock(Material.DRAGON_HEAD, 0);
		assertEquals(Block.get(BlockID.SKULL_BLOCK, 5), dragonHead);
		Block birchSlab = BlockMap.getNukkitBlock(Material.BIRCH_SLAB, 0);
		assertEquals(Block.get(BlockID.WOODEN_SLABS, 2), birchSlab);
		Block blackTerracotta = BlockMap.getNukkitBlock(Material.BLACK_TERRACOTTA, 0);
		assertEquals(Block.get(BlockID.STAINED_HARDENED_CLAY, 15), blackTerracotta);
		Block darkPrismarine = BlockMap.getNukkitBlock(Material.DARK_PRISMARINE, 0);
		assertEquals(Block.get(BlockID.PRISMARINE, 2), darkPrismarine);
		Block redCarpet = BlockMap.getNukkitBlock(Material.RED_CARPET, 0);
		assertEquals(Block.get(BlockID.CARPET, 14), redCarpet);
		Block peony = BlockMap.getNukkitBlock(Material.PEONY, 0);
		assertEquals(Block.get(BlockID.DOUBLE_PLANT, 5), peony);
		Block smoothRedSandstone = BlockMap.getNukkitBlock(Material.SMOOTH_RED_SANDSTONE, 0);
		assertEquals(Block.get(BlockID.RED_SANDSTONE, 2), smoothRedSandstone);
		Block cyanConcrete = BlockMap.getNukkitBlock(Material.CYAN_CONCRETE, 0);
		assertEquals(Block.get(BlockID.CONCRETE, 9), cyanConcrete);
		Block greenConcretePowder = BlockMap.getNukkitBlock(Material.GREEN_CONCRETE_POWDER, 0);
		assertEquals(Block.get(BlockID.CONCRETE_POWDER, 13), greenConcretePowder);
		Block purpleGlass = BlockMap.getNukkitBlock(Material.PURPLE_STAINED_GLASS, 0);
		assertEquals(Block.get(BlockID.STAINED_GLASS, 10), purpleGlass);
		Block whiteShulkerBox = BlockMap.getNukkitBlock(Material.WHITE_SHULKER_BOX, 0);
		assertEquals(Block.get(BlockID.SHULKER_BOX, 0), whiteShulkerBox);
		Block blackShulkerBox = BlockMap.getNukkitBlock(Material.BLACK_SHULKER_BOX, 0);
		assertEquals(Block.get(BlockID.SHULKER_BOX, 15), blackShulkerBox);
	}
}
