package nl.rutgerkok.pokkit.tag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.craftbukkit.v1_99_R9.CraftServers;
import org.junit.BeforeClass;
import org.junit.Test;

public class PokkitBlockItemTagTest {
	
	public static CraftServers server;
	
	@BeforeClass
	public static void Setup() {
		server = new CraftServers();
	}
	
	@Test
	public void TestGetServer() {
		assertNotNull(Bukkit.getServer());
		assertEquals(Bukkit.getServer(), server);
	}
	
	@Test
	public void TestAllTagsNonEmpty() {
		Field[] TagFields = Tag.class.getFields();
		
		try {
			for (int i = 0; i < TagFields.length; i++) {
				if (TagFields[i].get(null) instanceof Tag<?>) {
					Tag<Material> TagMaterial = (Tag<Material>) TagFields[i].get(null);
					System.out.println("TagMaterial key: "+TagMaterial.getKey()+", "+TagMaterial.toString());
					assertTrue(TagMaterial.getValues().size() != 0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestGetBlockTag() {
		boolean woolCheck = Tag.WOOL.isTagged(Material.BLACK_WOOL);
		boolean notWoolCheck = Tag.WOOL.isTagged(Material.OBSIDIAN);

		assertTrue(woolCheck);
		assertFalse(notWoolCheck);
	}
	
	@Test
	public void TestGetItemTag() {
		boolean boatCheck = Tag.ITEMS_BOATS.isTagged(Material.ACACIA_BOAT);
		boolean notBoatCheck = Tag.ITEMS_BOATS.isTagged(Material.BLACK_BANNER);

		assertTrue(boatCheck);
		assertFalse(notBoatCheck);
	}
}
