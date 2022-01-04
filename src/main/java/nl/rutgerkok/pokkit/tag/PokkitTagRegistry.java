package nl.rutgerkok.pokkit.tag;

import java.lang.reflect.Field;
import java.util.HashSet;

import org.bukkit.Material;

public class PokkitTagRegistry {
	
	HashSet<Material> TagRegistry = new HashSet<Material>();
	boolean blockTags = false;
	boolean itemTags = true;
	String tagName = "";
	
	/*
	 * tagName is the name of the tag.
	 * blockTags sets whether we want Block Tags or Item Tags.
	 */
	public PokkitTagRegistry(String tagName, boolean blockTags) {
		this.tagName = tagName;
		this.blockTags = blockTags; // There are more tag types, but these are all that are supported in Spigot 1.13.2.
		this.itemTags = !blockTags;
		
		PopulateWithTag(tagName);
	}
	
	public boolean isTagged(Material material) {
		return TagRegistry.contains(material);
	}
	
	public HashSet<Material> MaterialSet() {
		return TagRegistry;
	}
	
	private void PopulateWithTag(String tagName) {
		switch (tagName) {
		// Items
		case "banners": ReflectAndRegister("banner", ""); break;
		case "boats": ReflectAndRegister("boat", ""); break;
		case "fishes": AddToTagRegistry(CombineMaterialEntries(Material.COD, Material.COOKED_COD, Material.SALMON, Material.PUFFERFISH, Material.TROPICAL_FISH)); break;
		// Blocks
		case "acacia_logs": AddToTagRegistry(CombineMaterialEntries(Material.ACACIA_LOG, Material.STRIPPED_ACACIA_LOG, Material.ACACIA_WOOD, Material.STRIPPED_ACACIA_WOOD)); break;
		case "anvil": ReflectAndRegister("anvil", ""); break;
		case "birch_logs": AddToTagRegistry(CombineMaterialEntries(Material.BIRCH_LOG, Material.STRIPPED_BIRCH_LOG, Material.BIRCH_WOOD, Material.STRIPPED_BIRCH_WOOD)); break;
		case "beds": ReflectAndRegister("bed", ""); break;
		case "buttons": ReflectAndRegister("button", ""); break;
		case "carpets": ReflectAndRegister("carpet", ""); break;
		case "coral_blocks": ReflectAndRegister("coral_block", ""); break;
		case "coral_plants": ReflectAndRegister("coral_fan", ""); break;
		case "corals": ReflectAndRegister("coral", ""); break;
		case "dark_oak_logs": AddToTagRegistry(CombineMaterialEntries(Material.DARK_OAK_LOG, Material.STRIPPED_DARK_OAK_LOG, Material.DARK_OAK_WOOD, Material.STRIPPED_DARK_OAK_WOOD)); break;
		case "dirt": AddToTagRegistry(CombineMaterialEntries(Material.DIRT, Material.PODZOL, Material.COARSE_DIRT, Material.MYCELIUM, Material.GRASS_BLOCK)); break;
		case "doors": ReflectAndRegister("_door", ""); break; // "_" added to exclude _TrapDoors"
		case "enderman_holdable": 
			PopulateWithTag("small_flowers");
			PopulateWithTag("dirt");
			PopulateWithTag("sand");
			AddToTagRegistry(CombineMaterialEntries(Material.GRAVEL, Material.CACTUS, Material.CLAY, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.TNT, Material.PUMPKIN, Material.CARVED_PUMPKIN, Material.MELON)); 
			break;
		case "fence_gates": ReflectAndRegister("fence_gate", ""); break;
		case "fences": ReflectAndRegister("fence", "gate"); break;
		case "flower_pots": AddToTagRegistry(CombineMaterialEntries(ReflectionMaterialEntries("potted", ""), Material.FLOWER_POT)); break;
		case "ice": AddToTagRegistry(CombineMaterialEntries(Material.ICE, Material.BLUE_ICE, Material.FROSTED_ICE, Material.PACKED_ICE)); break;
		case "impermeable": AddToTagRegistry(CombineMaterialEntries(ReflectionMaterialEntries("stained_glass", "pane"), Material.GLASS)); break;
		case "jungle_logs": AddToTagRegistry(CombineMaterialEntries(Material.JUNGLE_LOG, Material.STRIPPED_JUNGLE_LOG, Material.JUNGLE_WOOD, Material.STRIPPED_JUNGLE_WOOD)); break;
		case "leaves": ReflectAndRegister("leaves", ""); break;
		case "logs": ReflectAndRegister("log", ""); break;
		case "oak_logs": AddToTagRegistry(CombineMaterialEntries(Material.OAK_LOG, Material.STRIPPED_OAK_LOG, Material.OAK_WOOD, Material.STRIPPED_OAK_WOOD)); break;
		case "planks": ReflectAndRegister("planks", ""); break;
		case "portals": AddToTagRegistry(CombineMaterialEntries(Material.END_PORTAL, Material.END_GATEWAY, Material.NETHER_PORTAL)); break;
		case "pressure_plates": ReflectAndRegister("_plate", ""); break; // "_" added to exclude _ChestPlates
		case "rails": ReflectAndRegister("rail", ""); break;
		case "sand": AddToTagRegistry(CombineMaterialEntries(Material.SAND, Material.RED_SAND)); break;
		case "saplings": ReflectAndRegister("sapling", "potted"); break;
		case "slabs": ReflectAndRegister("slab", ""); break;
		case "shulker_boxes": ReflectAndRegister("shulker_box", ""); break;
		case "signs": AddToTagRegistry(CombineMaterialEntries(Material.SIGN, Material.WALL_SIGN)); break;
		case "snow": ReflectAndRegister("snow", ""); break;
		case "spruce_logs": AddToTagRegistry(CombineMaterialEntries(Material.SPRUCE_LOG, Material.STRIPPED_SPRUCE_LOG, Material.SPRUCE_WOOD, Material.STRIPPED_SPRUCE_WOOD)); break;
		case "stairs": ReflectAndRegister("stairs", ""); break;
		case "stone_bricks": AddToTagRegistry(CombineMaterialEntries(Material.STONE_BRICKS, Material.CHISELED_STONE_BRICKS, Material.CRACKED_STONE_BRICKS, Material.MOSSY_STONE_BRICKS)); break;
		case "terracotta": ReflectAndRegister("terracotta", "glazed"); break;
		case "trapdoors": ReflectAndRegister("trapdoor", ""); break;
		case "underwater_bonemeals": 
			PopulateWithTag("corals"); 
			PopulateWithTag("wall_corals"); 
			AddToTagRegistry(CombineMaterialEntries(Material.SEAGRASS)); 
			break;
		case "valid_spawn": AddToTagRegistry(CombineMaterialEntries(Material.GRASS_BLOCK, Material.PODZOL)); break;
		case "walls": AddToTagRegistry(CombineMaterialEntries(Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL)); break;
		case "wall_signs": AddToTagRegistry(CombineMaterialEntries(Material.WALL_SIGN)); break;
		case "wall_corals": ReflectAndRegister("coral_wall_fan", ""); break;
		case "wart_blocks": AddToTagRegistry(CombineMaterialEntries(Material.NETHER_WART, Material.NETHER_WART_BLOCK)); break;
		case "wooden_buttons": AddToTagRegistry(CombineMaterialEntries(Material.ACACIA_BUTTON, Material.BIRCH_BUTTON, Material.DARK_OAK_BUTTON, Material.JUNGLE_BUTTON, Material.OAK_BUTTON, Material.SPRUCE_BUTTON)); break;
		case "wooden_doors": AddToTagRegistry(CombineMaterialEntries(Material.ACACIA_DOOR, Material.BIRCH_DOOR, Material.DARK_OAK_DOOR, Material.JUNGLE_DOOR, Material.OAK_DOOR, Material.SPRUCE_DOOR)); break;
		case "wooden_fences": AddToTagRegistry(CombineMaterialEntries(Material.ACACIA_FENCE, Material.BIRCH_FENCE, Material.DARK_OAK_FENCE, Material.JUNGLE_FENCE, Material.OAK_FENCE, Material.SPRUCE_FENCE)); break;
		case "wooden_pressure_plates": AddToTagRegistry(CombineMaterialEntries(Material.ACACIA_PRESSURE_PLATE, Material.BIRCH_PRESSURE_PLATE, Material.DARK_OAK_PRESSURE_PLATE, Material.JUNGLE_PRESSURE_PLATE, Material.OAK_PRESSURE_PLATE, Material.SPRUCE_PRESSURE_PLATE)); break;
		case "wooden_slabs": AddToTagRegistry(CombineMaterialEntries(Material.ACACIA_SLAB, Material.BIRCH_SLAB, Material.DARK_OAK_SLAB, Material.JUNGLE_SLAB, Material.OAK_SLAB, Material.SPRUCE_SLAB)); break;
		case "wooden_stairs": AddToTagRegistry(CombineMaterialEntries(Material.ACACIA_STAIRS, Material.BIRCH_STAIRS, Material.DARK_OAK_STAIRS, Material.JUNGLE_STAIRS, Material.OAK_STAIRS, Material.SPRUCE_STAIRS)); break;
		case "wooden_trapdoors": ReflectAndRegister("trapdoor", "iron"); break;
		case "wool": ReflectAndRegister("wool", ""); break;
		default:
			break;
		}
	}
	
	private void ReflectAndRegister(String tagName, String doesNotContain) {
		HashSet<Material> materials = ReflectionMaterialEntries(tagName, doesNotContain);
		TagRegistry.addAll(materials);
	}
	
	private HashSet<Material> ReflectionMaterialEntries(String stringContains, String doesNotContain) {
		Field[] MaterialFields = Material.class.getFields();
		
		HashSet<Material> materialsWithTagName = new HashSet<Material>();
		for (int i = 0; i < MaterialFields.length; i++) {
			if (!MaterialFields[i].getName().toLowerCase().contains("legacy")) {
				if (doesNotContain.length() == 0 || !MaterialFields[i].getName().toLowerCase().contains(doesNotContain))
				{
					if (MaterialFields[i].getName().toLowerCase().contains(stringContains)) {
						try {
							Material materialField = (Material) MaterialFields[i].get(null);
							if ((blockTags && materialField.isBlock()) || (!blockTags && materialField.isItem())) {
								materialsWithTagName.add(materialField);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}	
					}
				}
			}
		}
		return materialsWithTagName;
	}
	
	private void AddToTagRegistry(HashSet<Material> materials) {
		TagRegistry.addAll(materials);
	}
	
	private HashSet<Material> CombineMaterialEntries(HashSet<Material> materialsWithTagName, Material...materials) {
		for (int i = 0; i < materials.length; i++) {
			materialsWithTagName.add(materials[i]);
		}
		return materialsWithTagName;
	}
	
	private HashSet<Material> CombineMaterialEntries(Material...materials) {
		return CombineMaterialEntries(new HashSet<Material>(), materials);
	}
}
