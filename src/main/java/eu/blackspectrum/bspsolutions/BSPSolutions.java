package eu.blackspectrum.bspsolutions;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.AspectColl;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.xlib.gson.GsonBuilder;

import eu.blackspectrum.bspsolutions.adapters.BedBoardAdapter;
import eu.blackspectrum.bspsolutions.adapters.BedBoardMapAdapter;
import eu.blackspectrum.bspsolutions.commands.BSPCommand;
import eu.blackspectrum.bspsolutions.commands.PurgatoryCommand;
import eu.blackspectrum.bspsolutions.commands.RandomTeleportCommand;
import eu.blackspectrum.bspsolutions.entities.BSPBedColl;
import eu.blackspectrum.bspsolutions.entities.BSPPlayerColl;
import eu.blackspectrum.bspsolutions.entities.BedBoard;
import eu.blackspectrum.bspsolutions.entities.BedBoardColl;
import eu.blackspectrum.bspsolutions.listeners.BlockListener;
import eu.blackspectrum.bspsolutions.listeners.EntityListener;
import eu.blackspectrum.bspsolutions.listeners.FactionListener;
import eu.blackspectrum.bspsolutions.listeners.MiscListener;
import eu.blackspectrum.bspsolutions.listeners.PlayerListener;
import eu.blackspectrum.bspsolutions.plugins.CompassTeleport;
import eu.blackspectrum.bspsolutions.plugins.EndReset;
import eu.blackspectrum.bspsolutions.plugins.Purgatory;
import eu.blackspectrum.bspsolutions.plugins.SpawnSafe;
import eu.blackspectrum.bspsolutions.tasks.GarbageCollectTask;
import eu.blackspectrum.bspsolutions.tasks.PurgatoryCheckTask;
import eu.blackspectrum.bspsolutions.util.FactionsUtil;
import eu.blackspectrum.bspsolutions.util.LocationUtil;

public class BSPSolutions extends MassivePlugin
{


	private static BSPSolutions		instance;
	private static Configuration	config;
	private static String			pluginName;

	private Aspect					aspect;




	public static BSPSolutions get() {
		return instance;
	}




	public static Configuration getConfig2() {
		return config;
	}




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




	public static String getPluginName() {
		return pluginName;
	}




	public static boolean isClimbing( final Entity e ) {
		final Material m = e.getLocation().getBlock().getType();
		return !e.isOnGround() && m == Material.LADDER || m == Material.VINE;
	}




	public static boolean isSwimming( final Entity e ) {
		return e.getLocation().getBlock().isLiquid();
	}




	public BSPSolutions() {
		instance = this;

		if ( getPluginName() == null || getPluginName().isEmpty() )
			pluginName = this.getName();
	}




	public Aspect getAspect() {
		return this.aspect;
	}




	@Override
	public GsonBuilder getGsonBuilder() {
		return super.getGsonBuilder().registerTypeAdapter( BedBoard.class, BedBoardAdapter.get() )
				.registerTypeAdapter( BedBoard.MAP_TYPE, BedBoardMapAdapter.get() );
	}




	@Override
	public void onDisable() {
		final BukkitScheduler scheduler = this.getServer().getScheduler();

		// Unregister all tasks
		scheduler.cancelTasks( this );

		FMaps.get().collectGarbage();
		FMaps.get().dump();
	}




	@Override
	public void onEnable() {

		if ( !this.preEnable() )
			return;

		// ***************************
		// SetUp config
		// ***************************
		this.setUpConfig();
		// ***************************

		// ***************************
		// SetUp Aspect
		// ***************************
		this.aspect = AspectColl.get().get( "bsp", true );
		this.aspect.register();
		// ***************************

		// ***************************
		// Init colls
		// ***************************
		BSPPlayerColl.get().init();
		BSPBedColl.get().init();
		BedBoardColl.get().init();
		// ***************************

		// ***************************
		// Register listener
		// ***************************
		PlayerListener.get().register();
		EntityListener.get().register();
		BlockListener.get().register();
		FactionListener.get().register();
		MiscListener.get().register();
		// ***************************

		// ***************************
		// Start plugin enables
		// ***************************
		EndReset.onEnable();
		// ***************************

		// ***************************
		// Initialize misc
		// ***************************
		FMaps.get().initialize();
		// ***************************

		// ***************************
		// Add commands
		// ***************************
		new PurgatoryCommand().register();
		new BSPCommand().register();
		new RandomTeleportCommand().register();
		// ***************************

		// ***************************
		// Schedule tasks
		// ***************************
		PurgatoryCheckTask.get().schedule( 1200 );
		GarbageCollectTask.get().schedule( 12000 );
		// ***************************

		this.postEnable();
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
		SpawnSafe.setUpConfig( config );

		this.saveConfig();
	}

}
