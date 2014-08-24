package eu.blackspectrum.bspsolutions;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.entity.Player;

import eu.blackspectrum.bspsolutions.tasks.TeleportTask;

public class TeleportingPlayers
{


	private HashSet<UUID>				players	= null;
	private static TeleportingPlayers	instance;




	public static TeleportingPlayers Instance() {
		if ( instance == null )
			instance = new TeleportingPlayers();

		return instance;
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




	public void startTeleport( final Player player ) {
		this.addPlayer( player );
		new TeleportTask( player ).runTaskTimer( BSPSolutions.instance, 0, 20 );
	}

}
