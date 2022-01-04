package nl.rutgerkok.pokkit.tag;

import java.util.Collections;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;

public class PokkitBlockTag implements Tag<Material> {
	
	private NamespacedKey key;
	private PokkitTagRegistry registry;
		
	public PokkitBlockTag(NamespacedKey key) {
		this.key = key;
		registry = new PokkitTagRegistry(key.getKey(), true);
	}

	/*
	 * Returns the NamespacedKey
	 */
	@Override
	public NamespacedKey getKey() {
		return key;
	}

	/*
	 * Returns true if this tag has an entry for the specified material.
	 * Ex: Tag.WOOL.isTagged(material) returns true if "material" is wool.
	 */
	@Override
	public boolean isTagged(Material item) {
		return registry.isTagged(item);
	}

	/*
	 * Returns a list of all Bukkit materials that are blocks and have the same Namespaced key.
	 */
	@Override
	public Set<Material> getValues() {
		return Collections.unmodifiableSet(registry.MaterialSet());
	}
}
