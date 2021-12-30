package nl.rutgerkok.pokkit.player;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SplittableRandom;
import java.util.UUID;
import java.util.stream.Collectors;

import cn.nukkit.level.Position;
import cn.nukkit.network.protocol.*;
import nl.rutgerkok.pokkit.item.PokkitItemStack;
import nl.rutgerkok.pokkit.world.PokkitWorld;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.craftbukkit.v1_99_R9.CraftServer;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Scoreboard;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;

import nl.rutgerkok.pokkit.Pokkit;
import nl.rutgerkok.pokkit.PokkitGameMode;
import nl.rutgerkok.pokkit.PokkitLocation;
import nl.rutgerkok.pokkit.PokkitSound;
import nl.rutgerkok.pokkit.PokkitVector;
import nl.rutgerkok.pokkit.UniqueIdConversion;
import nl.rutgerkok.pokkit.blockdata.PokkitBlockData;
import nl.rutgerkok.pokkit.entity.PokkitHumanEntity;
import nl.rutgerkok.pokkit.inventory.PokkitInventory;
import nl.rutgerkok.pokkit.inventory.PokkitInventoryView;
import nl.rutgerkok.pokkit.metadata.PlayerMetadataStore;
import nl.rutgerkok.pokkit.particle.PokkitParticle;
import nl.rutgerkok.pokkit.permission.PokkitPermission;
import nl.rutgerkok.pokkit.permission.PokkitPermissionAttachment;
import nl.rutgerkok.pokkit.permission.PokkitPermissionAttachmentInfo;
import nl.rutgerkok.pokkit.plugin.PokkitPlugin;
import nl.rutgerkok.pokkit.potion.PokkitPotionEffect;

import cn.nukkit.AdventureSettings;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.particle.GenericParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.TextFormat;

@DelegateDeserialization(PokkitOfflinePlayer.class)
public class PokkitPlayer extends PokkitHumanEntity implements Player {

	public static final int ITEM_SLOT_NOT_INITIALIZED = -999;
	public static PokkitPlayer toBukkit(cn.nukkit.Player nukkit) {
		if (nukkit == null) {
			return null;
		}
		return ((CraftServer) Bukkit.getServer()).getOnlinePlayerData().getPlayer(nukkit);
	}

	public static cn.nukkit.Player toNukkit(Player player) {
		if (player == null) {
			return null;
		}
		return ((PokkitPlayer) player).nukkit;
	}

	private final Player.Spigot spigot;
	private final cn.nukkit.Player nukkit;
	private Scoreboard scoreboard;
	private InetSocketAddress address;
	public int lastItemSlot = ITEM_SLOT_NOT_INITIALIZED;

	/**
	 * All plugin classes that currently request the player to be hidden. If
	 * this set is empty, the player can be shown again.
	 *
	 * @see #hidePlayer(Plugin, Player)
	 * @see #showPlayer(Plugin, Player)
	 */
	private final Set<Class<?>> hidingRequests = new HashSet<>();

	public PokkitPlayer(cn.nukkit.Player player) {
		super(player);
		this.nukkit = Objects.requireNonNull(player);
		this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		PokkitPlayer instance = this;
		this.spigot = new Player.Spigot() {
			@Override
			public boolean getCollidesWithEntities() {
				return nukkit.canCollide();
			}

			@Override
			public Set<Player> getHiddenPlayers() {
				Set<Player> hiddenPlayers = new HashSet<>();
				for (Player player : getServer().getOnlinePlayers()) {
					if (!canSee(player)) {
						hiddenPlayers.add(player);
					}
				}
				return hiddenPlayers;
			}

			@Override
			public String getLocale() {
				return nukkit.getLocale().toString();
			}

			@Override
			public InetSocketAddress getRawAddress() {
				return InetSocketAddress.createUnresolved(nukkit.getAddress(), nukkit.getPort());
			}

			@Override
			public void respawn() {
				Pokkit.notImplemented();
			}

			@Override
			public void sendMessage(BaseComponent component) {
				instance.sendMessage(component.toLegacyText());
			}

			@Override
			public void sendMessage(BaseComponent... components) {
				StringBuilder text = new StringBuilder();
				for (BaseComponent component : components) {
					text.append(component.toLegacyText());
				}
				instance.sendMessage(text.toString());
			}

			@Override
			public void sendMessage(ChatMessageType position, BaseComponent component) {
				switch (position) {
					case CHAT:
					case SYSTEM:
						sendMessage(component);
						break;
					case ACTION_BAR:
					default:
						nukkit.sendActionBar(component.toLegacyText());
						break;
				}
			}

			@Override
			public void sendMessage(ChatMessageType position, BaseComponent... components) {
				switch (position) {
					case CHAT:
					case SYSTEM:
						sendMessage(components);
						break;
					case ACTION_BAR:
						StringBuilder text = new StringBuilder();
						for (BaseComponent component : components) {
							text.append(component.toLegacyText());
						}
						nukkit.sendActionBar(text.toString());
						break;
				}
			}

			@Override
			public void setCollidesWithEntities(boolean collides) {
				setCollidable(collides);
			}
		};
	}

	@Override
	public void abandonConversation(Conversation arg0) {
		Pokkit.notImplemented();
	}

	@Override
	public void abandonConversation(Conversation arg0, ConversationAbandonedEvent arg1) {
		Pokkit.notImplemented();
	}

	@Override
	public void acceptConversationInput(String arg0) {
		Pokkit.notImplemented();
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		return PokkitPermissionAttachment.toBukkit(nukkit.addAttachment(PokkitPlugin.toNukkit(plugin)));
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		cn.nukkit.plugin.Plugin nukkitPlugin = PokkitPlugin.toNukkit(plugin);
		cn.nukkit.permission.PermissionAttachment nukkitAttachment = nukkit.addAttachment(nukkitPlugin);
		nukkit.getServer().getScheduler().scheduleDelayedTask(nukkitPlugin, () -> {
			nukkit.removeAttachment(nukkitAttachment);
		}, ticks);

		return PokkitPermissionAttachment.toBukkit(nukkitAttachment);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
		return PokkitPermissionAttachment.toBukkit(nukkit.addAttachment(PokkitPlugin.toNukkit(plugin), name, value));
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
		return PokkitPermissionAttachment.toBukkit(nukkit.addAttachment(PokkitPlugin.toNukkit(plugin), name, value));
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

	/*public void removePotionEffect(PotionEffect bukkitEffect) {
		nukkit.removeEffect(PokkitPotionEffect.toNukkit(bukkitEffect).getId());
	}*/

	@Override
	public void awardAchievement(@SuppressWarnings("deprecation") org.bukkit.Achievement achievement) {
		// Silently unsupported!
	}

	@Override
	public boolean beginConversation(Conversation arg0) {
		throw Pokkit.unsupported();
	}

	@Override
	public boolean canSee(Player other) {
		return nukkit.canSee(PokkitPlayer.toNukkit(other));
	}

	@Override
	public void chat(String message) {
		 String msg = message = nukkit.getRemoveFormat() ? TextFormat.clean(message) : message;
		 PlayerChatEvent chatEvent = new PlayerChatEvent(toNukkit(this), msg);
		nukkit.getServer().getPluginManager().callEvent(chatEvent);
		if (!chatEvent.isCancelled()) {
			nukkit.getServer().broadcastMessage(
					nukkit.getServer().getLanguage().translateString(chatEvent.getFormat(),
							new String[] { chatEvent.getPlayer().getDisplayName(), chatEvent.getMessage() }),
					chatEvent.getRecipients());
		}
	}

	@Override
	public void closeInventory() {
		nukkit.removeAllWindows();
	}

	@Override
	public void decrementStatistic(Statistic arg0) throws IllegalArgumentException {
		Pokkit.notImplemented();
	}

	@Override
	public void decrementStatistic(Statistic arg0, EntityType arg1) throws IllegalArgumentException {
		Pokkit.notImplemented();
	}

	@Override
	public void decrementStatistic(Statistic arg0, EntityType arg1, int arg2) {
		Pokkit.notImplemented();
	}

	@Override
	public void decrementStatistic(Statistic arg0, int arg1) throws IllegalArgumentException {
		Pokkit.notImplemented();
	}

	@Override
	public void decrementStatistic(Statistic arg0, Material arg1) throws IllegalArgumentException {
		Pokkit.notImplemented();
	}

	@Override
	public void decrementStatistic(Statistic arg0, Material arg1, int arg2) throws IllegalArgumentException {
		Pokkit.notImplemented();
	}

	@Override
	public boolean eject() {
		return super.eject();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PokkitPlayer other = (PokkitPlayer) obj;
		if (!getName().equals(other.getName()))
			return false;
		return true;
	}

	@Override
	public InetSocketAddress getAddress() {
		InetSocketAddress address = this.address;
		if (address == null || address.getAddress() == null) {
			address = new InetSocketAddress(nukkit.getAddress(), nukkit.getPort());
			this.address = address;
		}
		return address;
	}

	@Override
	public AdvancementProgress getAdvancementProgress(Advancement advancement) {
		throw Pokkit.unsupported();
	}

	@Override
	public int getClientViewDistance() {
		return nukkit.getViewDistance();
	}

	@Override
	public boolean getAllowFlight() {
		return nukkit.getAdventureSettings().get(AdventureSettings.Type.ALLOW_FLIGHT);
	}

	@Override
	public AttributeInstance getAttribute(Attribute arg0) {
		return getAttribute(arg0);
	}

	@Override
	public Location getBedSpawnLocation() {
		return PokkitLocation.toBukkit(nukkit.getSpawn());
	}

	@Override
	public boolean getCanPickupItems() {
		return isValid();
	}

	@Override
	public Location getCompassTarget() {
		// Revise if Nukkit adds custom compass locations
		return PokkitLocation.toBukkit(nukkit.getLevel().getSpawnLocation());
	}

	@Override
	public String getCustomName() {
		return nukkit.getNameTag();
	}

	@Override
	public String getDisplayName() {
		return nukkit.getDisplayName();
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return nukkit.getEffectivePermissions().values().stream().map(PokkitPermissionAttachmentInfo::toBukkit)
				.collect(Collectors.toSet());
	}

	@Override
	public int getEntityId() {
		return (int) nukkit.getId();
	}

	@Override
	public EntityEquipment getEquipment() {
		throw Pokkit.unsupported();

	}

	@Override
	public float getExhaustion() {
		throw Pokkit.unsupported();

	}

	@Override
	public float getExp() {
		return cn.nukkit.entity.Attribute.getAttribute(cn.nukkit.entity.Attribute.EXPERIENCE).getValue();
	}

	@Override
	public double getEyeHeight() {
		return this.getEyeHeight(false);
	}

	@Override
	public double getEyeHeight(boolean ignoreSneaking) {
		if (ignoreSneaking) {
			return 1.62;
		}
		if (this.isSneaking()) {
			return 1.54;
		}
		return 1.62;
	}

	@Override
	public Location getEyeLocation() {
		final Location loc = this.getLocation();
		loc.setY(loc.getY() + this.getEyeHeight());
		return loc;
	}

	@Override
	public long getFirstPlayed() {
		Long firstPlayed = nukkit.getFirstPlayed();
		if (firstPlayed == null) {
			return 0;
		}
		return firstPlayed;
	}

	@Override
	public float getFlySpeed() {
		return 1F;
	}

	@Override
	public int getFoodLevel() {
		return nukkit.getFoodData().getLevel();

	}

	@Override
	public GameMode getGameMode() {
		return PokkitGameMode.toBukkit(nukkit.getGamemode());
	}

	@Override
	public double getHealth() {
		return nukkit.getHealth();
	}

	@Override
	public double getHealthScale() {
		return 1;
	}

	@Override
	public long getLastPlayed() {
		Long lastPlayed = nukkit.getLastPlayed();
		if (lastPlayed == null) {
			return 0;
		}
		return lastPlayed;
	}

	@Override
	public int getLevel() {
		return nukkit.getExperienceLevel();
	}

	@Override
	public Set<String> getListeningPluginChannels() {
		throw Pokkit.unsupported();

	}

	@Override
	public String getLocale() {
		return nukkit.getLoginChainData().getLanguageCode();
	}

	@Override
	public List<MetadataValue> getMetadata(String metadataKey) {
		return getMetadataStore().getMetadata(this, metadataKey);
	}

	private PlayerMetadataStore getMetadataStore() {
		return ((CraftServer) Bukkit.getServer()).getMetadata().getPlayerMetadata();
	}

	@Override
	public Player getPlayer() {
		return this;
	}

	@Override
	public String getPlayerListFooter() {
		// Not implemented
		return null;
	}

	@Override
	public String getPlayerListHeader() {
		// Not implemented
		return null;
	}

	@Override
	public String getPlayerListName() {
		return nukkit.getDisplayName(); // In Nukkit, if you change the player's display name, it also changes in the player list, so...
	}

	@Override
	public long getPlayerTime() {
		return nukkit.getLevel().getTime(); // The player time will be always the same as the level's time.
	}

	@Override
	public long getPlayerTimeOffset() {
		return 0; // The player time will be always the same as the level's time.
	}

	@Override
	public WeatherType getPlayerWeather() {
		return nukkit.getLevel().isRaining() ? WeatherType.DOWNFALL : WeatherType.CLEAR; // Always same as on the server
	}

	@Override
	public int getPortalCooldown() {
		return 80;
	}

	@Override
	public float getSaturation() {
		return nukkit.getFoodData().getFoodSaturationLevel();
	}

	@Override
	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	@Override
	public int getSleepTicks() {
		throw Pokkit.unsupported();

	}

	@Override
	public Entity getSpectatorTarget() {
		throw Pokkit.unsupported();

	}

	@Override
	public int getStatistic(Statistic arg0) throws IllegalArgumentException {
		switch (arg0) {
		case PLAY_ONE_MINUTE:
			long first = nukkit.getFirstPlayed();
			long diff = System.currentTimeMillis() - first; // This is the
															// difference in
															// millis
			int seconds = (int) (diff / 1000) % 60; // And this is the
													// difference in seconds
			return seconds * 20; // And this is in ticks
		default:
			break;
		}
		throw Pokkit.unsupported();
	}

	@Override
	public int getStatistic(Statistic arg0, EntityType arg1) throws IllegalArgumentException {
		throw Pokkit.unsupported();

	}

	@Override
	public int getStatistic(Statistic arg0, Material arg1) throws IllegalArgumentException {
		throw Pokkit.unsupported();

	}

	@Override
	public int getTotalExperience() {
		int level = nukkit.getExperienceLevel();
		if (level <= 16) {
			return nukkit.getExperience() + (int) (Math.pow(level, 2) + 6 * level);
		} else if (level <= 31) {
			return nukkit.getExperience() + (int) (2.5 * Math.pow(level, 2) - 40.5 * level + 360);
		} else {
			return nukkit.getExperience() + (int) (4.5 * Math.pow(level, 2) - 162.5 * level + 2220);
		}
	}

	@Override
	public EntityType getType() {
		return EntityType.PLAYER;
	}

	@Override
	public UUID getUniqueId() {
		return UniqueIdConversion.playerNameToId(getName());
	}

	@Override
	public float getWalkSpeed() {
		return nukkit.getMovementSpeed();
	}

	@Override
	public void giveExp(int exp) {
		nukkit.addExperience(exp);
	}

	@Override
	public void giveExpLevels(int levels) {
		nukkit.setExperience(nukkit.getExperience(), nukkit.getExperienceLevel() + levels);
	}

	@Override
	public boolean hasAchievement(@SuppressWarnings("deprecation") org.bukkit.Achievement achievement) {
		return true; // When achievements are properly implemented in Nukkit,
						// change this to use Nukkit's API!
	}

	@Override
	public boolean hasAI() {
		return true; // I think it is a bit difficult a player to not have AI...
	}

	@Override
	public boolean hasGravity() {
		return nukkit.getDataFlag(cn.nukkit.entity.Entity.DATA_FLAGS, cn.nukkit.entity.Entity.DATA_FLAG_GRAVITY);
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public boolean hasMetadata(String metadataKey) {
		return getMetadataStore().hasMetadata(this, metadataKey);
	}

	@Override
	public boolean hasPermission(Permission permission) {
		return nukkit.hasPermission(PokkitPermission.toNukkit(permission));
	}

	@Override
	public boolean hasPermission(String permission) {
		return nukkit.hasPermission(permission);
	}

	@Override
	public boolean hasPlayedBefore() {
		return nukkit.hasPlayedBefore();
	}

	private void hidePlayer(Class<?> responsible, Player player) {
		this.hidingRequests.add(responsible);
		nukkit.hidePlayer(PokkitPlayer.toNukkit(player));
	}

	@Override
	@Deprecated
	public void hidePlayer(Player player) {
		// No information available on which plugin requested the hide, so just use our own class as a stand-in
		hidePlayer(PokkitPlayer.class, player);
	}

	@Override
	public void hidePlayer(Plugin plugin, Player player) {
		hidePlayer(plugin.getClass(), player);
	}

	@Override
	public void incrementStatistic(Statistic arg0) throws IllegalArgumentException {
		Pokkit.notImplemented();
	}

	@Override
	public void incrementStatistic(Statistic arg0, EntityType arg1) throws IllegalArgumentException {
		Pokkit.notImplemented();
	}

	@Override
	public void incrementStatistic(Statistic arg0, EntityType arg1, int arg2) throws IllegalArgumentException {
		Pokkit.notImplemented();
	}

	@Override
	public void incrementStatistic(Statistic arg0, int arg1) throws IllegalArgumentException {
		Pokkit.notImplemented();
	}

	@Override
	public void incrementStatistic(Statistic arg0, Material arg1) throws IllegalArgumentException {
		Pokkit.notImplemented();
	}

	@Override
	public void incrementStatistic(Statistic arg0, Material arg1, int arg2) throws IllegalArgumentException {
		Pokkit.notImplemented();
	}

	@Override
	public boolean isBanned() {
		return nukkit.isBanned();
	}

	@Override
	public boolean isConversing() {
		throw Pokkit.unsupported();

	}

	@Override
	public boolean isFlying() {
		return nukkit.getAdventureSettings().get(AdventureSettings.Type.ALLOW_FLIGHT) && !nukkit.isOnGround();
	}

	@Override
	public boolean isHandRaised() {
		return false;
	}

	@Override
	public boolean isHealthScaled() {
		return false;
	}

	@Override
	public boolean isOnline() {
		return nukkit.isOnline();
	}

	@Override
	public boolean isOp() {
		return nukkit.isOp();
	}

	@Override
	public boolean isPermissionSet(Permission permission) {
		return nukkit.isPermissionSet(PokkitPermission.toNukkit(permission));
	}

	@Override
	public boolean isPermissionSet(String permission) {
		try {
			return nukkit.isPermissionSet(permission);
		} catch (NullPointerException e) { // WorldGuard workaround
			return false;
		}
	}

	@Override
	public boolean isPlayerTimeRelative() {
		return true; // You can't change the player's time with Nukkit, so this will be always true.
	}

	@Override
	public boolean isSleeping() {
		return nukkit.isSleeping();
	}

	@Override
	public boolean isSleepingIgnored() {
		return false; // Silently unsupported!
	}

	@Override
	public boolean isSneaking() {
		return nukkit.isSneaking();
	}

	@Override
	public boolean isSprinting() {
		return nukkit.isSprinting();
	}

	@Override
	public boolean isValid() {
		return isOnline() && !isDead();
	}

	@Override
	public boolean isWhitelisted() {
		return nukkit.isWhitelisted();
	}

	@Override
	public void kickPlayer(String reason) {
		nukkit.kick(reason);
	}

	@Override
	public void loadData() {
		Pokkit.notImplemented();
	}

	@Override
	public InventoryView openEnchanting(Location arg0, boolean arg1) {
		throw Pokkit.unsupported();

	}

	@Override
	public InventoryView openInventory(Inventory inventory) {
		nukkit.addWindow(PokkitInventory.toNukkit(inventory));
		return new PokkitInventoryView(inventory, this);
	}

	@Override
	public void openInventory(InventoryView inventoryView) {
		openInventory(inventoryView.getTopInventory());
	}

	@Override
	public InventoryView openMerchant(Villager arg0, boolean arg1) {
		throw Pokkit.unsupported();

	}

	@Override
	public InventoryView openWorkbench(Location arg0, boolean arg1) {
		throw Pokkit.unsupported();

	}

	@Override
	public boolean performCommand(String arg0) {
		return nukkit.getServer().dispatchCommand(nukkit, arg0);
	}

	@Override
	public void playEffect(Location arg0, Effect arg1, int arg2) {
		// Silently unsupported!
	}

	@Override
	public <T> void playEffect(Location arg0, Effect arg1, T arg2) {
		// Silently unsupported!
	}

	@Override
	public void playNote(Location arg0, byte arg1, byte arg2) {
		playNote(arg0, Instrument.getByType(arg1), new Note(arg2));
	}

	@Override
	public void playNote(Location arg0, Instrument arg1, Note arg2) {
		cn.nukkit.level.Sound sound;
		switch (arg1.getType()) {
			case 0:
				sound = cn.nukkit.level.Sound.NOTE_HARP;
				break;
			case 1:
				sound = cn.nukkit.level.Sound.NOTE_BD;
				break;
			case 2:
				sound = cn.nukkit.level.Sound.NOTE_SNARE;
				break;
			case 3:
				sound = cn.nukkit.level.Sound.NOTE_HAT;
				break;
			case 4:
				sound = cn.nukkit.level.Sound.NOTE_BASS;
				break;
			case 5:
				sound = cn.nukkit.level.Sound.NOTE_FLUTE;
				break;
			case 6:
				sound = cn.nukkit.level.Sound.NOTE_BELL;
				break;
			case 7:
				sound = cn.nukkit.level.Sound.NOTE_GUITAR;
				break;
			case 8:
				sound = cn.nukkit.level.Sound.NOTE_CHIME;
				break;
			case 9:
				sound = cn.nukkit.level.Sound.NOTE_XYLOPHONE;
				break;
			default:
				sound = null;
		}
		float note;
		switch (arg2.getId()) {
			case 0: note = 0.5f; break;
			case 1: note = 0.529732f; break;
			case 2: note = 0.561231f; break;
			case 3: note = 0.594604f; break;
			case 4: note = 0.629961f; break;
			case 5: note = 0.667420f; break;
			case 6: note = 0.707107f; break;
			case 7: note = 0.749154f; break;
			case 8: note = 0.793701f; break;
			case 9: note = 0.840896f; break;
			case 10: note = 0.890899f; break;
			case 11: note = 0.943874f; break;
			case 12: note = 1.0f; break;
			case 13: note = 1.059463f; break;
			case 14: note = 1.122462f; break;
			case 15: note = 1.189207f; break;
			case 16: note = 1.259921f; break;
			case 17: note = 1.334840f; break;
			case 18: note = 1.414214f; break;
			case 19: note = 1.498307f; break;
			case 20: note = 1.587401f; break;
			case 21: note = 1.681793f; break;
			case 22: note = 1.781797f; break;
			case 23: note = 1.887749f; break;
			case 24: note = 2.0f; break;
			default: note = 0f;
		}
		PlaySoundPacket soundPk = new PlaySoundPacket();
		soundPk.name = sound.getSound();
		soundPk.volume = 1f;
		soundPk.pitch = note;
		soundPk.x = arg0.getBlockX();
		soundPk.y = arg0.getBlockY();
		soundPk.z = arg0.getBlockZ();
		nukkit.dataPacket(soundPk);
	}

	@Override
	public void playSound(Location location, Sound sound, float volume, float pitch) {
		playSound(location, sound, SoundCategory.NEUTRAL, volume, pitch);
	}

	@Override
	public void playSound(Location location, Sound sound, SoundCategory category, float volume, float pitch) {
		if (location == null || sound == null) {
			return;
		}

		cn.nukkit.level.Sound nukkitSound = PokkitSound.toNukkit(location, sound, pitch);
		if (nukkitSound == null) {
			return;
		}

		if (pitch < 0) pitch = 0;
		if (volume < 0) volume = 0;
		if (volume > 1) volume = 1;

		Vector3 pos = PokkitVector.toNukkit(location.toVector());
		nukkit.level.addSound(pos, nukkitSound, volume, pitch);
	}

	@Override
	public void playSound(Location location, String soundString, float volume, float pitch) {
		playSound(location, soundString, SoundCategory.NEUTRAL, volume, pitch);
	}

	@Override
	public void playSound(Location location, String soundString, SoundCategory category, float volume, float pitch) {
		if (location == null || soundString == null) {
			return;
		}
		try {
			Sound sound = Sound.valueOf(soundString.replace("minecraft:", "").toUpperCase());
			playSound(location, sound, volume, pitch);
		} catch (IllegalArgumentException e) {
			// Ignore non-existing sound
		}
	}

	@Override
	public void recalculatePermissions() {
		nukkit.recalculatePermissions();
	}

	@Override
	public void removeAchievement(@SuppressWarnings("deprecation") org.bukkit.Achievement achievement) {
		// Silently unsupported!
	}

	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		nukkit.removeAttachment(PokkitPermissionAttachment.toNukkit(attachment));
	}

	@Override
	public void removeMetadata(String metadataKey, Plugin owningPlugin) {
		getMetadataStore().removeMetadata(this, metadataKey, owningPlugin);
	}

	@Override
	public void resetPlayerTime() {
		nukkit.level.sendTime(nukkit);
	}

	@Override
	public void resetPlayerWeather() {
		nukkit.level.sendWeather(nukkit);
	}

	@Override
	public void resetTitle() {
		nukkit.clearTitle();
	}

	@Override
	public void saveData() {
		nukkit.save();
	}

	@Override
	public void sendBlockChange(Location location, BlockData block) {
		PokkitBlockData materialData = (PokkitBlockData) block;

		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		int nukkitBlockId = materialData.getNukkitId();
		int nukkitBlockData = materialData.getNukkitData();
		int flags = UpdateBlockPacket.FLAG_ALL_PRIORITY;

		UpdateBlockPacket packet = new UpdateBlockPacket();
		packet.x = x;
		packet.y = y;
		packet.z = z;
		packet.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(nukkitBlockId, nukkitBlockData);
		packet.flags = flags;
		nukkit.dataPacket(packet, false);
	}

	@Override
	public void sendBlockChange(Location location, Material material, byte blockData) {
		this.sendBlockChange(location, PokkitBlockData.createBlockData(material, blockData));
	}

	@Override
	public boolean sendChunkChange(Location arg0, int arg1, int arg2, int arg3, byte[] arg4) {
		throw Pokkit.unsupported();

	}

	@Override
	public void sendMap(MapView arg0) {
		Pokkit.notImplemented();
	}

	@Override
	public void sendMessage(String message) {
		nukkit.sendMessage(message);
	}

	@Override
	public void sendMessage(String[] messages) {
		for (String message : messages) {
			nukkit.sendMessage(message);
		}
	}

	@Override
	public void sendPluginMessage(Plugin arg0, String arg1, byte[] arg2) {
		Pokkit.notImplemented();
	}

	@Override
	public void sendRawMessage(String message) {
		nukkit.sendMessage(message);
	}

	@Override
	public void sendSignChange(Location arg0, String[] arg1) throws IllegalArgumentException {
		Pokkit.notImplemented();
	}

	@Override
	public void sendTitle(String title, String subtitle) {
		sendTitle(title, subtitle, 10, 70, 20); // Copied from CraftBukkit
	}

	@Override
	public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		if (title != null) {
			nukkit.sendTitle(title);
		}
		if (subtitle != null) {
			nukkit.setSubtitle(subtitle);
		}
		nukkit.setTitleAnimationTimes(fadeIn, stay, fadeOut);
	}

	@Override
	public Map<String, Object> serialize() {
		throw Pokkit.unsupported();

	}

	@Override
	public void setAllowFlight(boolean value) {
		nukkit.getAdventureSettings().set(AdventureSettings.Type.ALLOW_FLIGHT, value);
	}

	@Override
	public void setBedSpawnLocation(Location arg0) {
		this.nukkit.setSpawn(new Vector3(arg0.getX(), arg0.getY(), arg0.getZ()));
	}

	@Override
	public void setBedSpawnLocation(Location arg0, boolean arg1) {
		this.nukkit.setSpawn(new Vector3(arg0.getX(), arg0.getY(), arg0.getZ()));
	}

	@Override
	public boolean sleep(Location location, boolean b) {
		return nukkit.sleepOn(PokkitLocation.toNukkit(location));
	}

	@Override
	public void wakeup(boolean b) {
		nukkit.stopSleep();

		if (b) {
			nukkit.setSpawn(nukkit);
		}
	}

	@Override
	public Location getBedLocation() {
		if (!nukkit.isSleeping()) {
			throw new IllegalStateException("Cannot use getBedLocation() while player is not sleeping");
		} else {
			return PokkitLocation.toBukkit(nukkit);
		}
	}

	@Override
	public void setCompassTarget(Location arg0) {
		// this may not be the best idea
		SetSpawnPositionPacket pk = new SetSpawnPositionPacket();
		pk.spawnType = SetSpawnPositionPacket.TYPE_WORLD_SPAWN;
		Position spawn = PokkitWorld.toNukkit(arg0.getWorld()).getSpawnLocation();
		pk.x = spawn.getFloorX();
		pk.y = spawn.getFloorY();
		pk.z = spawn.getFloorZ();
		nukkit.dataPacket(pk);
	}

	@Override
	public void setCustomName(String customName) {
		nukkit.setNameTag(customName);
	}

	@Override
	public void setCustomNameVisible(boolean customNameVisible) {
		nukkit.setNameTagVisible(customNameVisible);
	}

	@Override
	public void setDisplayName(String displayName) {
		nukkit.setDisplayName(displayName);
	}

	@Override
	public void setExhaustion(float arg0) {
		Pokkit.notImplemented();
	}

	@Override
	public void setExp(float exp) {
		if (nukkit.spawned) {
			nukkit.setAttribute(cn.nukkit.entity.Attribute.getAttribute(cn.nukkit.entity.Attribute.EXPERIENCE).setValue(exp));
		}
	}

	@Override
	public void setFallDistance(float fallDistance) {
		nukkit.fallDistance = fallDistance;
	}

	@Override
	public void setFireTicks(int fireTicks) {
		nukkit.fireTicks = fireTicks;
	}

	@Override
	public void setFlying(boolean flying) {
		nukkit.getAdventureSettings().set(AdventureSettings.Type.FLYING, flying);
		nukkit.getAdventureSettings().update();
	}

	@Override
	public void setFlySpeed(float arg0) throws IllegalArgumentException {
		Pokkit.notImplemented();
	}

	@Override
	public void setFoodLevel(int arg0) {
		nukkit.getFoodData().setLevel(arg0);
	}

	@Override
	public void setGameMode(GameMode gamemode) {
		nukkit.setGamemode(PokkitGameMode.toNukkit(gamemode));
	}

	@Override
	public void setHealthScale(double arg0) throws IllegalArgumentException {
		Pokkit.notImplemented();
	}

	@Override
	public void setHealthScaled(boolean arg0) {
		Pokkit.notImplemented();
	}

	@Override
	public void setItemOnCursor(ItemStack arg0) {
		nukkit.getCursorInventory().setItem(0, PokkitItemStack.toNukkitCopy(arg0));
	}

	@Override
	public void setLevel(int level) {
		nukkit.setExperience(nukkit.getExperience(), level);
	}

	@Override
	public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
		getMetadataStore().setMetadata(this, metadataKey, newMetadataValue);
	}

	@Override
	public void setNoDamageTicks(int arg0) {
		nukkit.noDamageTicks = arg0;
	}

	@Override
	public void setOp(boolean value) {
		nukkit.setOp(value);
	}

	@Override
	public void setPlayerListFooter(String footer) {
		// Ignore - not implemented
	}

	@Override
	public void setPlayerListHeader(String header) {
		// Ignore - not implemented
	}

	@Override
	public void setPlayerListHeaderFooter(String header, String footer) {
		// Ignore - not implemented
	}

	@Override
	public void setPlayerListName(String arg0) {
		Pokkit.notImplemented();
	}

	@Override
	public void setPlayerTime(long arg0, boolean arg1) {
		SetTimePacket pk = new SetTimePacket();
		pk.time = (int) arg0;
		nukkit.dataPacket(pk);
	}

	@Override
	public void setPlayerWeather(WeatherType arg0) {
		Pokkit.notImplemented();
	}

	@Override
	public void setResourcePack(String url) {
		Pokkit.notImplemented();
	}

	@Override
	public void setResourcePack(String url, byte[] hash) {
		Pokkit.notImplemented();
	}

	@Override
	public void setSaturation(float arg0) {
		nukkit.getFoodData().setFoodSaturationLevel(arg0);
	}

	@Override
	public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
		Validate.notNull(scoreboard);
		this.scoreboard = scoreboard;
	}

	@Override
	public void setSleepingIgnored(boolean arg0) {
		Pokkit.notImplemented();
	}

	@Override
	public void setSneaking(boolean value) {
		nukkit.setSneaking(value);
	}

	@Override
	public void setSpectatorTarget(Entity arg0) {
		Pokkit.notImplemented();
	}

	@Override
	public void setSprinting(boolean value) {
		nukkit.setSprinting(value);
	}

	@Override
	public void setStatistic(Statistic arg0, EntityType arg1, int arg2) {
		Pokkit.notImplemented();
	}

	@Override
	public void setStatistic(Statistic arg0, int arg1) throws IllegalArgumentException {
		Pokkit.notImplemented();
	}

	@Override
	public void setStatistic(Statistic arg0, Material arg1, int arg2) throws IllegalArgumentException {
		Pokkit.notImplemented();
	}

	@Override
	public void setTexturePack(String arg0) {
		Pokkit.notImplemented();
	}

	@Override
	public void setTotalExperience(int arg0) {
		nukkit.setExperience(0, 0);
		nukkit.addExperience(arg0);
	}

	@Override
	public void setWalkSpeed(float arg0) throws IllegalArgumentException {
		nukkit.setMovementSpeed(arg0);
	}

	@Override
	public void setWhitelisted(boolean value) {
		nukkit.setWhitelisted(value);
	}

	private void showPlayer(Class<?> clazz, Player player) {
		this.hidingRequests.remove(clazz);
		if (this.hidingRequests.isEmpty()) {
			nukkit.showPlayer(toNukkit(player));
		}
	}

	@Override
	@Deprecated
	public void showPlayer(Player player) {
		// No information available on which plugin was responsible, so just use our own class as a stand-in
		showPlayer(PokkitPlayer.class, player);
	}

	@Override
	public void showPlayer(Plugin plugin, Player player) {
		showPlayer(plugin.getClass(), player);
	}

	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count) {
		spawnParticle(particle, x, y, z, count, 0, 0 , 0);

	}

	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY,
			double offsetZ) {
		spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, 0);

	}

	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY,
			double offsetZ, double extra) {
		spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, null);
	}

	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
			double offsetY, double offsetZ, double extra, T data) {
		int id = PokkitParticle.toNukkit(particle);

		SplittableRandom random = new SplittableRandom();

		int index = 0;
		while (count > index) {
			double realOffsetX = 0;
			double realOffsetY = 0;
			double realOffsetZ = 0;
			if (offsetX != 0) {
				realOffsetX = offsetX / 2;
				x = x + random.nextDouble(-realOffsetX, realOffsetX);
			}
			if (offsetX != 0) {
				realOffsetY = offsetY / 2;
				y = y + random.nextDouble(-realOffsetY, realOffsetY);
			}
			if (offsetX != 0) {
				realOffsetZ = offsetZ / 2;
				z = z + random.nextDouble(-realOffsetZ, realOffsetZ);
			}

			GenericParticle nukkitParticle = new GenericParticle(new Vector3(x, y, z), id);

			nukkit.getLevel().addParticle(nukkitParticle, nukkit);
			index++;
		}
	}

	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
			double offsetY, double offsetZ, T data) {
		spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, 0, data);
	}

	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {
		spawnParticle(particle, x, y, z, count, 0, 0, 0, 0, data);
	}

	@Override
	public void spawnParticle(Particle particle, Location location, int count) {
		spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count);
	}

	@Override
	public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
		spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ);
	}

	@Override
	public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ,
			double extra) {
		spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
	}

	@Override
	public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ,
			double extra, T data) {
		spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data);
	}

	@Override
	public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ,
			T data) {
		spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, data);
	}

	@Override
	public <T> void spawnParticle(Particle particle, Location location, int count, T data) {
		spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, data);
	}

	@Override
	public Player.Spigot spigot() {
		return spigot;
	}

	@Override
	public void stopSound(Sound sound) {
		// Silently unsupported!
	}

	@Override
	public void stopSound(Sound sound, SoundCategory category) {
		// Silently unsupported!
	}

	@Override
	public void stopSound(String sound) {
		StopSoundPacket pk = new StopSoundPacket();
		pk.name = sound;
		pk.stopAll = false;
		nukkit.dataPacket(pk);
	}

	@Override
	public void stopSound(String sound, SoundCategory category) {
		stopSound(sound);
	}

	@Override
	public void updateCommands() {
		nukkit.sendCommandData();
	}

	@Override
	public void updateInventory() {
		nukkit.getInventory().sendContents(nukkit);
	}

}
