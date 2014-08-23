package eu.blackspectrum.bspsolutions.plugins;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import eu.blackspectrum.bspsolutions.BSPSolutions;
import eu.blackspectrum.bspsolutions.TeleportingPlayers;
import eu.blackspectrum.bspsolutions.tasks.TeleportTask;
import eu.blackspectrum.bspsolutions.util.LocationUtil;

public class CompassTeleport
{


	public static void onPlayerHurt( final EntityDamageEvent event ) {
		if ( event.getEntity() instanceof Player )
		{
			final Player player = (Player) event.getEntity();
			if ( TeleportingPlayers.Instance().isTeleporting( player ) )
				TeleportingPlayers.Instance().abortTeleport( player );
		}
	}




	public static void onPlayerJoin( final PlayerJoinEvent event ) {
		if ( event.getPlayer().getWorld().equals( LocationUtil.getOverWorld() ) )
			event.getPlayer().setCompassTarget( LocationUtil.getCenterOfWorld() );
	}




	public static void onRightClick( final PlayerInteractEvent event ) {
		final Player player = event.getPlayer();

		if ( event.getAction() == Action.RIGHT_CLICK_AIR )
			if ( event.hasItem() && event.getItem().getType() == Material.COMPASS )
				if ( TeleportingPlayers.Instance().isTeleporting( player ) )
					TeleportingPlayers.Instance().abortTeleport( player );
				else if ( LocationUtil.isCloseToCenter( player ) )
					new TeleportTask( player ).runTaskTimer( BSPSolutions.instance, 0, 20 );
				else
					player.sendMessage( BSPSolutions.config.getString( "CompassTP.cantUseMessage" ) );
	}




	public static void onWorldChange( final PlayerChangedWorldEvent event ) {
		if ( event.getPlayer().getWorld().equals( LocationUtil.getOverWorld() ) )
			event.getPlayer().setCompassTarget( LocationUtil.getCenterOfWorld() );
	}

}
