package nl.rutgerkok.pokkit.boss;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import cn.nukkit.utils.BlockColor;
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
	
	public BlockColor BarColorToBlockColor(BarColor color)
	{
		switch(color)
		{
		case BLUE:
			return BlockColor.BLUE_BLOCK_COLOR; 
		case GREEN:
			return BlockColor.GREEN_BLOCK_COLOR;
		case PINK:
			return BlockColor.PINK_BLOCK_COLOR;
		case RED:
			return BlockColor.RED_BLOCK_COLOR;
		case WHITE:
			return BlockColor.WHITE_BLOCK_COLOR;
		case YELLOW:
			return BlockColor.YELLOW_BLOCK_COLOR;
		case PURPLE:
		default:
			return BlockColor.PURPLE_BLOCK_COLOR;
		}
	}

	@Override
	public void setColor(BarColor color) {
		this.color = color;
		BlockColor bcolor = BarColorToBlockColor(color);

		for(int i = 0; i < dummyBossBars.size(); i++)
		{
			dummyBossBars.get(i).setColor(bcolor);
		}
	}

	@Override
	public BarStyle getStyle() {
		throw Pokkit.unsupported();
	}

	@Override
	public void setStyle(BarStyle style) {
		throw Pokkit.unsupported();		
	}

	@Override
	public void removeFlag(BarFlag flag) {		
		throw Pokkit.unsupported();
	}

	@Override
	public void addFlag(BarFlag flag) {	
		throw Pokkit.unsupported();
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
		return (double) progressLength;
	}

	@Override
	public void addPlayer(Player player) {
		DummyBossBar build = new DummyBossBar.Builder(PokkitPlayer.toNukkit(player))
				.color(BarColorToBlockColor(getColor()))
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
		throw Pokkit.unsupported();
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
