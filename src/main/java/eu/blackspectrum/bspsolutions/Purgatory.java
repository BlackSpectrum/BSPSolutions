package eu.blackspectrum.bspsolutions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;

import eu.blackspectrum.bspsolutions.util.LocationUtil;

public class Purgatory
{


	private static Purgatory	instance	= null;

	// Players in purgatory
	private HashMap<UUID, Long>	players		= null;




	public static Purgatory Instance() {
		if ( instance == null )
			instance = new Purgatory();

		return instance;
	}




	public static void setUpConfig( final Configuration config ) {
		config.set( "Purgatory.time", config.getLong( "Purgatory.time", 900 ) );
	}




	public void addPlayer( final Player player ) {
		if ( this.players == null )
			this.players = new HashMap<UUID, Long>();

		this.players.put( player.getUniqueId(), System.currentTimeMillis() + BSPSolutions.config.getLong( "Purgatory.time" ) * 1000 );
	}




	public boolean canPlayerLeave( final Player player ) {
		if(players == null)
			return false;
		
		if ( this.players.containsKey( player.getUniqueId() ) )
			return System.currentTimeMillis() > this.players.get( player.getUniqueId() );

		// Check if in purgatory world, if not no need to leave
		return player.getWorld().equals( LocationUtil.getPurgatoryWorld() );
	}




	public void checkPlayers() {
		if(players == null)
			return;
		
		final Iterator<UUID> it = this.players.keySet().iterator();

		while ( it.hasNext() )
		{
			final Player player = Bukkit.getPlayer( it.next() );
			if ( player != null && this.canPlayerLeave( player ) )
			{
				it.remove();
				this.freePlayer( player );
			}
		}
	}




	public void freePlayer( final Player player ) {
		this.removePlayer( player );

		player.teleport( LocationUtil.getRespawnLocation( player ) );

		player.sendMessage( "You got freed from the Purgatory!" );
	}




	public void freePlayer( final Player player, final PlayerRespawnEvent event ) {
		this.removePlayer( player );

		event.setRespawnLocation( LocationUtil.getRespawnLocation( player ) );
		
		player.sendMessage( "You got freed from the Purgatory!" );
	}




	public void removePlayer( final Player player ) {
		if ( this.players != null )
		{
			this.players.remove( player.getUniqueId() );

			// No more players around? Remove map
			if ( this.players.size() == 0 )
				this.players = null;
		}
	}

}
