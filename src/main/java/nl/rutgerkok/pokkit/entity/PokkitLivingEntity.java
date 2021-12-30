package nl.rutgerkok.pokkit.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import nl.rutgerkok.pokkit.Pokkit;
import nl.rutgerkok.pokkit.attribute.PokkitAttributeInstance;
import nl.rutgerkok.pokkit.blockdata.PokkitBlockData;
import nl.rutgerkok.pokkit.player.PokkitPlayer;
import nl.rutgerkok.pokkit.potion.PokkitPotionEffect;
import nl.rutgerkok.pokkit.potion.PokkitPotionEffectType;
import nl.rutgerkok.pokkit.world.PokkitBlock;

import cn.nukkit.potion.Effect;

public class PokkitLivingEntity extends PokkitEntity implements LivingEntity {
	public static LivingEntity toBukkit(cn.nukkit.entity.EntityLiving nukkit) {
		if (nukkit == null) {
			return null;
		}

		if (nukkit instanceof cn.nukkit.Player) {
			return PokkitPlayer.toBukkit((cn.nukkit.Player) nukkit);
		}

		return new PokkitLivingEntity(nukkit);
	}

	public static cn.nukkit.entity.Entity toNukkit(LivingEntity entity) {
		if (entity == null) {
			return null;
		}
		return ((PokkitLivingEntity) entity).nukkit;
	}

	private final cn.nukkit.entity.EntityLiving nukkit;

	PokkitLivingEntity(cn.nukkit.entity.EntityLiving nukkitEntity) {
		super(nukkitEntity);
		this.nukkit = nukkitEntity;
	}

	@Override
	public boolean addPotionEffect(PotionEffect bukkitEffect) {
		return addPotionEffect(bukkitEffect, false);
	}

	@Override
	public boolean addPotionEffect(PotionEffect bukkitEffect, boolean force) {
		nukkit.addEffect(PokkitPotionEffect.toNukkit(bukkitEffect));
		return true;
	}

	@Override
	public boolean addPotionEffects(Collection<PotionEffect> bukkitEffects) {
		for (PotionEffect bukkitEffect : bukkitEffects) {
			nukkit.addEffect(PokkitPotionEffect.toNukkit(bukkitEffect));
		}
		return true;
	}

	@Override
	public void damage(double arg0) {
		cn.nukkit.event.entity.EntityDamageEvent e = new cn.nukkit.event.entity.EntityDamageEvent(nukkit, cn.nukkit.event.entity.EntityDamageEvent.DamageCause.CUSTOM, (float) arg0);
		nukkit.attack(e);
	}

	@Override
	public void damage(double arg0, Entity arg1) {
		cn.nukkit.event.entity.EntityDamageEvent e = new cn.nukkit.event.entity.EntityDamageByEntityEvent(nukkit, PokkitEntity.toNukkit(arg1), cn.nukkit.event.entity.EntityDamageEvent.DamageCause.CUSTOM, (float) arg0);
		nukkit.attack(e);
	}

	@Override
	public Collection<PotionEffect> getActivePotionEffects() {
		throw Pokkit.unsupported();
	}

	@Override
	public AttributeInstance getAttribute(Attribute attribute) {
		cn.nukkit.entity.Attribute.init();
		int nukkitAttributeType = PokkitAttributeInstance.fromBukkit(attribute);
		cn.nukkit.entity.Attribute nukkitAttribute = cn.nukkit.entity.Attribute.getAttribute(nukkitAttributeType);
		return new PokkitAttributeInstance(nukkitAttribute);
	}

	@Override
	public boolean getCanPickupItems() {
		return nukkit instanceof cn.nukkit.Player; // Currently on Nukkit only players can pick up items
	}

	@Override
	public EntityEquipment getEquipment() {
		throw Pokkit.unsupported();
	}

	@Override
	public double getEyeHeight() {
		return nukkit.getEyeHeight();
	}

	@Override
	public double getEyeHeight(boolean ignoreSneaking) {
		return nukkit.getEyeHeight();
	}

	@Override
	public Location getEyeLocation() {
		return getLocation().add(0, getEyeHeight(), 0);
	}

	@Override
	public double getHealth() {
		return nukkit.getHealth();
	}

	@Override
	public Player getKiller() {
		throw Pokkit.unsupported();
	}

	@Override
	public double getLastDamage() {
		return nukkit.getLastDamageCause().getFinalDamage();
	}

	@Override
	public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
		throw Pokkit.unsupported();
	}

	@Override
	public Block getTargetBlockExact(int i) {
		throw Pokkit.unsupported();
	}

	@Override
	public Block getTargetBlockExact(int i, FluidCollisionMode fluidCollisionMode) {
		throw Pokkit.unsupported();
	}

	@Override
	public RayTraceResult rayTraceBlocks(double v) {
		throw Pokkit.unsupported();
	}

	@Override
	public RayTraceResult rayTraceBlocks(double v, FluidCollisionMode fluidCollisionMode) {
		throw Pokkit.unsupported();
	}

	@Override
	public Entity getLeashHolder() throws IllegalStateException {
		throw new IllegalStateException("Not leashed"); // Spigot javadoc
	}

	@Override
	public List<Block> getLineOfSight(Set<Material> bukkitTransparent, int maxDistance) {
		cn.nukkit.block.Block[] nukkitBlocks = nukkit.getLineOfSight(maxDistance, 0,
				toNukkitBlockIds(bukkitTransparent));

		List<Block> bukkitBlocks = new ArrayList<>(nukkitBlocks.length);
		for (cn.nukkit.block.Block nukkitBlock : nukkitBlocks) {
			bukkitBlocks.add(PokkitBlock.toBukkit(nukkitBlock));
		}

		return bukkitBlocks;
	}

	@Override
	public double getMaxHealth() {
		return nukkit.getMaxHealth();
	}

	@Override
	public int getMaximumAir() {
		return 400;
	}

	@Override
	public int getMaximumNoDamageTicks() {
		return 60; // From Nukkit source code
	}

	@Override
	public int getNoDamageTicks() {
		return nukkit.noDamageTicks;
	}

	@Override
	public PotionEffect getPotionEffect(PotionEffectType type) {
		throw Pokkit.unsupported();
	}

	@Override
	public int getRemainingAir() {
		return nukkit.getAirTicks();
	}

	@Override
	public boolean getRemoveWhenFarAway() {
		return false;
	}

	@Override
	public Block getTargetBlock(Set<Material> bukkitTransparent, int maxDistance) {
		cn.nukkit.block.Block nukkitBlock = nukkit.getTargetBlock(maxDistance, toNukkitBlockIds(bukkitTransparent));

		return PokkitBlock.toBukkit(nukkitBlock);
	}

	@Override
	public boolean hasAI() {
		return !nukkit.getDataFlag(cn.nukkit.entity.Entity.DATA_FLAGS, cn.nukkit.entity.Entity.DATA_FLAG_NO_AI);
	}

	@Override
	public boolean hasLineOfSight(Entity other) {
		return nukkit.hasLineOfSight(PokkitEntity.toNukkit(other));
	}

	@Override
	public boolean hasPotionEffect(PotionEffectType potionEffect) {
		Effect nukkitEffect = PokkitPotionEffectType.toNukkit(potionEffect);
		for (cn.nukkit.potion.Effect eff : nukkit.getEffects().values()) {
			if (eff.getId() == nukkitEffect.getId()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isCollidable() {
		return nukkit.canCollide();
	}

	@Override
	public boolean isGliding() {
		return false;
	}

	@Override
	public boolean isLeashed() {
		return false; // Not supported by Nukkit yet
	}

	@Override
	public boolean isRiptiding() {
		return false;
	}

	@Override
	public boolean isSwimming() {
		return nukkit.isSwimming();
	}

	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
		throw Pokkit.unsupported();
	}

	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
		throw Pokkit.unsupported();
	}

	@Override
	public void removePotionEffect(PotionEffectType type) {
		nukkit.removeEffect(type.getId());
	}

	@Override
	public void resetMaxHealth() {
		Pokkit.notImplemented();
	}

	@Override
	public void setAI(boolean ai) {
		nukkit.setDataFlag(cn.nukkit.entity.Entity.DATA_FLAGS, cn.nukkit.entity.Entity.DATA_FLAG_NO_AI, !ai);
	}

	@Override
	public void setCanPickupItems(boolean pickup) {
		Pokkit.notImplemented();
	}

	@Override
	public void setCollidable(boolean collidable) {
		nukkit.setDataFlag(cn.nukkit.entity.Entity.DATA_FLAGS, cn.nukkit.entity.Entity.DATA_FLAG_HAS_COLLISION, collidable);
	}

	@Override
	public void setGliding(boolean gliding) {
		nukkit.setDataFlag(cn.nukkit.entity.Entity.DATA_FLAGS, cn.nukkit.entity.Entity.DATA_FLAG_GLIDING, gliding);
	}

	@Override
	public void setHealth(double health) {
		nukkit.setHealth((float) health);
	}

	@Override
	public void setLastDamage(double damage) {
		nukkit.getLastDamageCause().setDamage((float) damage);
	}

	@Override
	public boolean setLeashHolder(Entity holder) {
		throw Pokkit.unsupported();
	}

	@Override
	public void setMaxHealth(double health) {
		nukkit.setMaxHealth((int) Math.ceil(health));
	}

	@Override
	public void setMaximumAir(int ticks) {
		Pokkit.notImplemented();
	}

	@Override
	public void setMaximumNoDamageTicks(int ticks) {
		Pokkit.notImplemented();
	}

	@Override
	public void setNoDamageTicks(int ticks) {
		nukkit.noDamageTicks = ticks;
	}

	@Override
	public void setRemainingAir(int ticks) {
		nukkit.setAirTicks(ticks);
	}

	@Override
	public void setRemoveWhenFarAway(boolean remove) {
		Pokkit.notImplemented();
	}

	@Override
	public void setSwimming(boolean swimming) {
		nukkit.setSwimming(swimming);
	}

	private Integer[] toNukkitBlockIds(Set<Material> bukkitMaterials) {
		if (bukkitMaterials == null) {
			return new Integer[cn.nukkit.block.Block.AIR];
		}
		return bukkitMaterials.stream()
				.map(material -> PokkitBlockData.createBlockData(material, 0).getNukkitId())
				.toArray(Integer[]::new);
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(nukkit.getBoundingBox().getMinX(), nukkit.getBoundingBox().getMinY(), nukkit.getBoundingBox().getMinZ(), nukkit.getBoundingBox().getMaxX(), nukkit.getBoundingBox().getMaxY(), nukkit.getBoundingBox().getMaxZ());
	}

	@Override
	public void setRotation(float v, float v1) {
		nukkit.setRotation(v, v1);
	}
}
