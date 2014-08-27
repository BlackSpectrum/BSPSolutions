package eu.blackspectrum.bspsolutions.plugins;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import eu.blackspectrum.bspsolutions.BSPSolutions;
import eu.blackspectrum.bspsolutions.entities.BSPPlayer;
import eu.blackspectrum.bspsolutions.util.LocationUtil;

public class Purgatory
{


	public static void onPlayerDie( final PlayerDeathEvent event ) {
		final BSPPlayer player = BSPPlayer.get( event.getEntity() );

		player.setTimeInPurgatory( System.currentTimeMillis() + BSPSolutions.Config().getLong( "Purgatory.time" ) * 1000 );
	}




	public static void onPlayerJoin( final PlayerJoinEvent event ) {
		final BSPPlayer player = BSPPlayer.get( event.getPlayer() );

		// If he can leave purgatory TP him
		if ( player.isInPurgatory() && player.canLeavePurgatory() )
			player.freeFromPurgatory();
	}




	public static void onPlayerRespawn( final PlayerRespawnEvent event ) {
		final BSPPlayer player = BSPPlayer.get( event.getPlayer() );

		// If he cant leave purgatory, spawn him inside
		if ( !player.canLeavePurgatory() )
		{
			event.setRespawnLocation( LocationUtil.getPurgatoryWorld().getSpawnLocation() );
			player.getPlayer().sendMessage( "Your are stuck in the Purgatory ..." + ChatColor.RED + "FOREVER" );
		}
		else
			player.freeFromPurgatory( event );
	}




	public static void setUpConfig( final Configuration config ) {
		config.set( "Purgatory.time", config.get( "Purgatory.time", 900 ) );
	}
}
