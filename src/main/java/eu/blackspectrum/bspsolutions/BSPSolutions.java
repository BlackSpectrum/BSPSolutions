package eu.blackspectrum.bspsolutions;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import eu.blackspectrum.bspsolutions.commands.BSPCommand;
import eu.blackspectrum.bspsolutions.commands.PurgatoryCommand;
import eu.blackspectrum.bspsolutions.commands.RandomTeleportCommand;
import eu.blackspectrum.bspsolutions.listeners.BlockListener;
import eu.blackspectrum.bspsolutions.listeners.EntityListener;
import eu.blackspectrum.bspsolutions.listeners.MiscEventListener;
import eu.blackspectrum.bspsolutions.listeners.PlayerListener;
import eu.blackspectrum.bspsolutions.plugins.CompassTeleport;
import eu.blackspectrum.bspsolutions.plugins.EndReset;
import eu.blackspectrum.bspsolutions.tasks.GarbageCollectTask;
import eu.blackspectrum.bspsolutions.tasks.PurgatoryCheckTask;
import eu.blackspectrum.bspsolutions.util.FactionsUtil;
import eu.blackspectrum.bspsolutions.util.LocationUtil;

public class BSPSolutions extends JavaPlugin
{


	public static Plugin		instance;
	public static Configuration	config;
	public static String		pluginName;




	public static Player getPlayer( final String name ) {
		Player retPlayer = null;
		for ( final Player p : Bukkit.getOnlinePlayers() )
			if ( p.getName().matches( "(?i:.*" + name + ".*)" ) )

			{
				if ( retPlayer != null )
					return null;
				retPlayer = p;
			}

		return retPlayer;
	}




	public static boolean isClimbing( final Entity e ) {
		final Material m = e.getLocation().getBlock().getType();
		return !e.isOnGround() && m == Material.LADDER || m == Material.VINE;
	}




	public static boolean isSwimming( final Entity e ) {
		return e.getLocation().getBlock().isLiquid();
	}




	@Override
	public void onDisable() {
		final BukkitScheduler scheduler = this.getServer().getScheduler();

		// Unregister all tasks
		scheduler.cancelTasks( this );

		// Unregister all events
		HandlerList.unregisterAll( this );

		FMaps.Instance().collectGarbage();
		FMaps.Instance().dump();
	}




	@Override
	public void onEnable() {
		instance = this;

		if ( pluginName == null || pluginName.isEmpty() )
			pluginName = this.getName();

		this.setUpConfig();

		// Register listener
		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents( new PlayerListener(), this );
		pm.registerEvents( new EntityListener(), this );
		pm.registerEvents( new BlockListener(), this );
		pm.registerEvents( new MiscEventListener(), this );

		// Start plugin enables
		EndReset.onEnable();

		// Initialize misc
		FMaps.Instance().initialize();

		// Add commands
		new PurgatoryCommand().register();
		new BSPCommand().register();
		new RandomTeleportCommand().register();

		// Schedule tasks
		final BukkitScheduler scheduler = this.getServer().getScheduler();

		scheduler.runTaskTimer( this, new PurgatoryCheckTask(), 1200, 1200 );
		scheduler.runTaskTimer( this, new GarbageCollectTask(), 6000, 6000 );
	}




	private void setUpConfig() {
		config = this.getConfig();

		// Utils
		LocationUtil.setUpConfig( config );
		FactionsUtil.setUpConfig( config );

		// Plugins
		EndReset.setUpConfig( config );
		Purgatory.setUpConfig( config );
		CompassTeleport.setUpConfig( config );

		this.saveConfig();
	}
}
