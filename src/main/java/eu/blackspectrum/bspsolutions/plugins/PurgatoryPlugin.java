package eu.blackspectrum.bspsolutions.plugins;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import eu.blackspectrum.bspsolutions.Purgatory;
import eu.blackspectrum.bspsolutions.util.LocationUtil;

public class PurgatoryPlugin
{


	public static void onPlayerDie( final PlayerDeathEvent event ) {
		Purgatory.Instance().addPlayer( event.getEntity() );
	}




	public static void onPlayerJoin( final PlayerJoinEvent event ) {
		final Player player = event.getPlayer();

		// If he can leave purgatory TP him
		if ( Purgatory.Instance().canPlayerLeave( player ) && !player.isDead() )
			Purgatory.Instance().freePlayer( player );
	}




	public static void onPlayerRespawn( final PlayerRespawnEvent event ) {
		final Player player = event.getPlayer();

		// If he cant leave purgatory, spawn him inside
		if ( !Purgatory.Instance().canPlayerLeave( player ) )
		{
			event.setRespawnLocation( LocationUtil.getPurgatoryWorld().getSpawnLocation() );
			player.sendMessage( "Your are stuck in the Purgatory ..." + ChatColor.RED + "FOREVER" );
		}
		else
			Purgatory.Instance().freePlayer( player, event );
	}
}
