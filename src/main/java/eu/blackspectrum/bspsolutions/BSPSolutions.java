package eu.blackspectrum.bspsolutions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.massivecraft.factions.Factions;
import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.AspectColl;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.xlib.gson.GsonBuilder;

import eu.blackspectrum.bspsolutions.adapters.BedBoardAdapter;
import eu.blackspectrum.bspsolutions.adapters.BedBoardMapAdapter;
import eu.blackspectrum.bspsolutions.commands.BSPCommand;
import eu.blackspectrum.bspsolutions.commands.FactionsPermCommand;
import eu.blackspectrum.bspsolutions.commands.InfoCommand;
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

		super.onDisable();
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
				try
				{
					Translate.incoming( event.getPacket(), BSPPlayer.get( event.getPlayer() ) );
				}
				catch ( final Exception e )
				{
					event.setCancelled( true );
				}
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

		final File folder = new File( this.getDataFolder(), "infos" );
		for ( final File f : folder.listFiles() )
			if ( f.isFile() )
				new InfoCommand( f.getName().split( "\\." )[0] ).register();

		// ***************************

		// ***************************
		// Schedule tasks
		// ***************************
		PurgatoryCheckTask.get().activate( this );
		GarbageCollectTask.get().activate( this );
		// ***************************

		// ***************************
		// Replace factions perms command with custom one
		// ***************************
		final Iterator<MassiveCommand> it = Factions.get().getOuterCmdFactions().getSubCommands().iterator();

		while ( it.hasNext() )
		{
			final MassiveCommand cmd = it.next();

			if ( cmd == Factions.get().getOuterCmdFactions().cmdFactionsPerm )
			{
				it.remove();
				break;
			}
		}

		Factions.get().getOuterCmdFactions().addSubCommand( new FactionsPermCommand() );
		// ***************************

		this.postEnable();
	}




	private void makeDirs() {
		new File( this.getDataFolder(), "pics" ).mkdirs();
		new File( this.getDataFolder(), "maps" ).mkdirs();
		new File( this.getDataFolder(), "infos" ).mkdirs();
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
