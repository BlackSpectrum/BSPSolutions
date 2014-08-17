package eu.blackspectrum.bspsolutions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.massivecore.ps.PS;

import eu.blackspectrum.bspsolutions.listeners.BlockListener;
import eu.blackspectrum.bspsolutions.listeners.EntityListener;
import eu.blackspectrum.bspsolutions.listeners.FactionListener;
import eu.blackspectrum.bspsolutions.listeners.PlayerListener;
import eu.blackspectrum.bspsolutions.plugins.EndReset;

public class BSPSolutions extends JavaPlugin
{


	public static Plugin		instance;
	public static Configuration	config;




	public static void dropItemNaturally( final Entity e, final ItemStack i ) {
		if ( i != null && i.getType() != Material.AIR )
			e.getLocation().getWorld().dropItemNaturally( e.getLocation(), i );
	}




	public static void dropItemNaturally( final Entity e, final MaterialData m, final int amount ) {
		dropItemNaturally( e, m.toItemStack( amount ) );
	}




	public static World getWorld( final String name ) {
		return Bukkit.getServer().getWorld( name );
	}




	public static boolean isClimbing( final Entity e ) {
		final Material m = e.getLocation().getBlock().getType();
		return !e.isOnGround() && m == Material.LADDER || m == Material.VINE;
	}




	public static boolean isInSafeZone( final Location location ) {
		return isSafeZone( BoardColls.get().getFactionAt( PS.valueOf( location ) ) );
	}




	public static boolean isSafeZone( final Faction faction ) {
		return faction.equals( FactionColls.get().getForUniverse( faction.getUniverse() ).getSafezone() );
	}




	public static boolean isSwimming( final Entity e ) {
		return e.getLocation().getBlock().isLiquid();
	}




	public static boolean isWarZone( final Faction faction ) {
		return faction.equals( FactionColls.get().getForUniverse( faction.getUniverse() ).getWarzone() );
	}




	@Override
	public void onDisable() {

	}




	@Override
	public void onEnable() {
		instance = this;

		this.setUpConfig();

		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents( new PlayerListener(), this );
		pm.registerEvents( new EntityListener(), this );
		pm.registerEvents( new BlockListener(), this );
		pm.registerEvents( new FactionListener(), this );

		EndReset.onEnable();

	}




	private void setUpConfig() {
		config = this.getConfig();

		EndReset.setUpConfig( config );

		config.set( "Factions.offlineDelay", config.getLong( "Factions.offlineDelay", 300 ) );

		this.saveConfig();
	}
}
