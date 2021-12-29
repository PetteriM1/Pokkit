package nl.rutgerkok.pokkit.blockdata;

import static org.junit.Assert.assertEquals;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.junit.BeforeClass;
import org.junit.Test;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;

public class BlockDataTest {

	@BeforeClass
	public static void init() {
		Block.init();
	}
	
	@Test
	public void getMaterialTest() {	
		PokkitBlockData white = new PokkitBlockData(Block.get(BlockID.WOOL, 0));
		Material white_wool = white.getMaterial();
		assertEquals(Material.WHITE_WOOL, white_wool);

		PokkitBlockData orange = new PokkitBlockData(Block.get(BlockID.WOOL, 1));
		Material orange_wool = orange.getMaterial();
		assertEquals(Material.ORANGE_WOOL, orange_wool);
		
		PokkitBlockData obsidian = new PokkitBlockData(Block.get(BlockID.OBSIDIAN));
		Material obsidian_mat = obsidian.getMaterial();
		assertEquals(Material.OBSIDIAN, obsidian_mat);
	}
}
