package eu.blackspectrum.bspsolutions;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

public class TeleportingPlayers
{


	private HashSet<UUID>				players	= null;
	private static TeleportingPlayers	instance;




	public static TeleportingPlayers Instance() {
		if ( instance == null )
			instance = new TeleportingPlayers();

		return instance;
	}




	public static void setUpConfig( final Configuration config ) {
		config.set( "CompassTP.successMessage", config.getString( "CompassTP.successMessage", "Teleport succeeded!" ) );
		config.set( "CompassTP.failMessage", config.getString( "CompassTP.failMessage", "Teleport failed!" ) );
		config.set( "CompassTP.cantUseMessage",
				config.getString( "CompassTP.cantUseMessage", "You cannot use that here. Use compass to get to the Center of the World." ) );
		config.set( "CompassTP.timerLength", BSPSolutions.config.getInt( "CompassTP.timerLength", 5 ) );
	}




	private TeleportingPlayers() {
	}




	public void abortTeleport( final Player player ) {
		if ( !this.isTeleporting( player ) )
			return;

		player.sendMessage( BSPSolutions.config.getString( "CompassTP.failMessage" ) );
		this.removePlayer( player );
	}




	public void addPlayer( final Player player ) {
		if ( this.players == null )
			this.players = new HashSet<UUID>();

		this.players.add( player.getUniqueId() );
	}




	public boolean isTeleporting( final Player player ) {
		if ( this.players == null )
			return false;

		return this.players.contains( player.getUniqueId() );
	}




	public void removePlayer( final Player player ) {
		if ( this.players == null )
			return;

		this.players.remove( player.getUniqueId() );

		if ( this.players.isEmpty() )
			this.players = null;
	}

}
