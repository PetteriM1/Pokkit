package nl.rutgerkok.pokkit.boss;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.BossBarColor;
import cn.nukkit.utils.DummyBossBar;
import nl.rutgerkok.pokkit.Pokkit;
import nl.rutgerkok.pokkit.player.PokkitPlayer;

public class PokkitBossBar implements BossBar {
	
	ArrayList<cn.nukkit.utils.DummyBossBar> dummyBossBars = new ArrayList<cn.nukkit.utils.DummyBossBar>();
	BarColor color;
	String title;
	float progressLength;

	public PokkitBossBar(String arg0, BarColor arg1, BarStyle arg2, BarFlag[] arg3) {
		setTitle(arg0);
		setColor(arg1);
		/*setStyle(arg2);
		for(int i = 0; i < arg3.length; i++)
		{
			addFlag(arg3[i]);
		}*/
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		for(int i = 0; i < dummyBossBars.size(); i++)
		{
			dummyBossBars.get(i).setText(title);
		}
		this.title = title;
	}

	@Override
	public BarColor getColor() {
		return color;
	}
	
	public BossBarColor BarColorToBossBarColor(BarColor color) {
		switch(color)
		{
		case BLUE:
			return BossBarColor.BLUE; 
		case GREEN:
			return BossBarColor.GREEN;
		case PINK:
			return BossBarColor.PINK;
		case RED:
			return BossBarColor.RED;
		case WHITE:
			return BossBarColor.WHITE;
		case YELLOW:
			return BossBarColor.YELLOW;
		case PURPLE:
		default:
			return BossBarColor.PURPLE;
		}
	}

	@Override
	public void setColor(BarColor color) {
		this.color = color;
		BossBarColor bcolor = BarColorToBossBarColor(color);

		for(int i = 0; i < dummyBossBars.size(); i++)
		{
			dummyBossBars.get(i).setColor(bcolor);
		}
	}

	@Override
	public BarStyle getStyle() {
		return BarStyle.SOLID;
	}

	@Override
	public void setStyle(BarStyle style) {
		Pokkit.notImplemented();
	}

	@Override
	public void removeFlag(BarFlag flag) {
		Pokkit.notImplemented();
	}

	@Override
	public void addFlag(BarFlag flag) {
		Pokkit.notImplemented();
	}

	@Override
	public boolean hasFlag(BarFlag flag) {
		throw Pokkit.unsupported();
	}

	@Override
	public void setProgress(double progress) {
		for(int i = 0; i < dummyBossBars.size(); i++)
		{
			dummyBossBars.get(i).setLength((float) progress*100);
		}
		progressLength = (float) progress;
	}

	@Override
	public double getProgress() {
		return progressLength;
	}

	@Override
	public void addPlayer(Player player) {
		DummyBossBar build = new DummyBossBar.Builder(PokkitPlayer.toNukkit(player))
				.color(BarColorToBossBarColor(getColor()))
				.text(getTitle())
				.length((float) getProgress())
				.build();
		dummyBossBars.add(build);
		build.create();
	}

	@Override
	public void removePlayer(Player player) {
		for(int i = 0; i < dummyBossBars.size(); i++)
		{
			if(dummyBossBars.get(i).getPlayer().equals(PokkitPlayer.toNukkit(player)))
			{
				dummyBossBars.get(i).destroy();
				dummyBossBars.remove(i);
				i = dummyBossBars.size();
			}
		}
	}

	@Override
	public void removeAll() {
		for(int i = 0; i < dummyBossBars.size(); i++)
		{
			dummyBossBars.get(i).destroy();
			dummyBossBars.remove(i);
		}
	}

	@Override
	public List<Player> getPlayers() {
		ArrayList<Player> players = new ArrayList<Player>();
		for(int i = 0; i < dummyBossBars.size(); i++)
		{
			Player p = PokkitPlayer.toBukkit(dummyBossBars.get(i).getPlayer());
			players.add(p);
		}
		return players;
	}

	@Override
	public void setVisible(boolean visible) {
		Pokkit.notImplemented();
	}

	@Override
	public boolean isVisible() {
		throw Pokkit.unsupported();
	}

	@Override
	public void show() {
		for(int i = 0; i < dummyBossBars.size(); i++)
		{
			dummyBossBars.get(i).create();
		}
		
	}

	@Override
	public void hide() {
		for(int i = 0; i < dummyBossBars.size(); i++)
		{
			dummyBossBars.get(i).destroy();
		}
	}
}
