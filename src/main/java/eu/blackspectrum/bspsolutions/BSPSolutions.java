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
import com.massivecraft.massivecore.ps.PS;

import eu.blackspectrum.bspsolutions.listeners.BlockListener;
import eu.blackspectrum.bspsolutions.listeners.EntityListener;
import eu.blackspectrum.bspsolutions.listeners.PlayerListener;
import eu.blackspectrum.bspsolutions.plugins.EndReset;

public class BSPSolutions extends JavaPlugin
{


	public static Plugin		instance;
	public static Configuration	config;




	public void onEnable() {
		instance = this;

		setUpConfig();

		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents( new PlayerListener(), this );
		pm.registerEvents( new EntityListener(), this );
		pm.registerEvents( new BlockListener(), this );

	}




	public void onDisable() {

	}




	private void setUpConfig() {
		config = this.getConfig();

		EndReset.setUpConfig( config );

		saveConfig();
	}

	public static boolean isInSafeZone( final Location location ) {
		return BoardColls.get().getFactionAt( PS.valueOf( location ) ).getName().equalsIgnoreCase( "SafeZone" );
	}


	public static World getWorld( final String name ) {
		return Bukkit.getServer().getWorld( name );
	}




	public static void dropItemNaturally( final Entity e, final ItemStack i ) {
		if ( i != null && !i.getType().equals( Material.AIR ) )
			e.getLocation().getWorld().dropItemNaturally( e.getLocation(), i );
	}




	public static void dropItemNaturally( final Entity e, final MaterialData m, int amount ) {
		dropItemNaturally( e, m.toItemStack( amount ) );
	}
}
