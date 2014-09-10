package eu.blackspectrum.bspsolutions;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.reader.UnicodeReader;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.AspectColl;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.xlib.gson.GsonBuilder;

import eu.blackspectrum.bspsolutions.adapters.BedBoardAdapter;
import eu.blackspectrum.bspsolutions.adapters.BedBoardMapAdapter;
import eu.blackspectrum.bspsolutions.commands.BSPCommand;
import eu.blackspectrum.bspsolutions.commands.Pic2MapCommand;
import eu.blackspectrum.bspsolutions.commands.PurgatoryCommand;
import eu.blackspectrum.bspsolutions.commands.RandomTeleportCommand;
import eu.blackspectrum.bspsolutions.entities.BSPBedColl;
import eu.blackspectrum.bspsolutions.entities.BSPPlayer;
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
import eu.blackspectrum.bspsolutions.plugins.RandomCoords;
import eu.blackspectrum.bspsolutions.plugins.SpawnSafe;
import eu.blackspectrum.bspsolutions.tasks.GarbageCollectTask;
import eu.blackspectrum.bspsolutions.tasks.PurgatoryCheckTask;
import eu.blackspectrum.bspsolutions.util.FactionsUtil;
import eu.blackspectrum.bspsolutions.util.LocationUtil;
import eu.blackspectrum.bspsolutions.util.PacketUtil;
import eu.blackspectrum.bspsolutions.util.PicIO;
import eu.blackspectrum.bspsolutions.util.Translate;

public class BSPSolutions extends MassivePlugin
{


	private static BSPSolutions	instance;

	private Aspect				aspect;




	public static BSPSolutions get() {
		return instance;
	}




	public static Configuration getConfig2() {
		return instance.getConfig();
	}




	public static List<Player> getPlayers( final String name ) {
		final List<Player> retPlayers = new ArrayList<Player>();
		for ( final Player p : Bukkit.getOnlinePlayers() )
			if ( p.getName().matches( "(?i:.*" + name + ".*)" ) )
				retPlayers.add( p );

		return Collections.unmodifiableList( retPlayers );
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
		FMaps.get().collectGarbage();
		FMaps.get().dump();
	}




	@Override
	public void onEnable() {

		if ( !this.preEnable() )
			return;

		// Make directories
		this.makeDirs();

		// ***************************
		// Migrate
		// ***************************
		this.migratePic2Map();
		// ***************************

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
		// Register PacketAdapters
		// ***************************
		final ProtocolManager pm = ProtocolLibrary.getProtocolManager();

		pm.addPacketListener( new PacketAdapter( RandomCoords.getParameters( true ) ) {


			@Override
			public void onPacketSending( final PacketEvent event ) {

				PacketContainer packet;

				if ( event.getPacket().getType().name().equals( "TILE_ENTITY_DATA" ) )
					packet = PacketUtil.cloneTileEntityData( event.getPacket() );
				else
					packet = event.getPacket().shallowClone();

				event.setPacket( packet );

				Translate.outgoing( packet, BSPPlayer.get( event.getPlayer() ) );

			}

		} );

		pm.addPacketListener( new PacketAdapter( RandomCoords.getParameters( false ) ) {


			@Override
			public void onPacketReceiving( final PacketEvent event ) {
				Translate.incoming( event.getPacket(), BSPPlayer.get( event.getPlayer() ) );
			}
		} );
		// ***************************

		// ***************************
		// Start plugin enables
		// ***************************
		EndReset.onEnable();
		// ***************************

		// ***************************
		// Initialize misc
		// ***************************
		Maps.init();
		// ***************************

		// ***************************
		// Add commands
		// ***************************
		new PurgatoryCommand().register();
		new BSPCommand().register();
		new RandomTeleportCommand().register();
		new Pic2MapCommand().register();
		// ***************************

		// ***************************
		// Schedule tasks
		// ***************************
		PurgatoryCheckTask.get().activate( this );
		GarbageCollectTask.get().activate( this );
		// ***************************

		this.postEnable();
	}




	private void makeDirs() {
		new File( BSPSolutions.get().getDataFolder(), "pics" ).mkdirs();
		new File( BSPSolutions.get().getDataFolder(), "maps" ).mkdirs();
	}




	private void migratePic2Map() {
		final Yaml yaml = new Yaml( new SafeConstructor() );

		final File maps = new File( "plugins" + File.separator + "Pic2Map" + File.separator + "maps.yml" );
		this.log( "Migrating Pic2Map..." );
		try
		{
			if ( maps.exists() )
			{
				// Read the maps and delete files

				final FileInputStream in = new FileInputStream( maps );
				@SuppressWarnings("unchecked")
				final HashMap<Integer, String> mapsHm = (HashMap<Integer, String>) yaml.load( new UnicodeReader( in ) );
				in.close();

				maps.delete();
				new File( "plugins" + File.separator + "Pic2Map" ).delete();

				for ( final Entry<Integer, String> entry : mapsHm.entrySet() )
					PicIO.loadImgageFromURL( entry.getValue(), entry.getKey().shortValue() );
			}


			for ( File f : new File( getDataFolder(), "pics" ).listFiles() )
				if ( f.isFile() && f.getName().endsWith( ".jpg" )){
					PicIO.saveImageToDisc( ImageIO.read( f ), Short.valueOf( f.getName().split( "_" )[1].split( "\\." )[0] ) );
					f.delete();
				}

		}
		catch ( final Exception e )
		{
			e.printStackTrace();
		}
		this.log( "...done!" );

	}




	private void setUpConfig() {
		final Configuration config = this.getConfig();

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
