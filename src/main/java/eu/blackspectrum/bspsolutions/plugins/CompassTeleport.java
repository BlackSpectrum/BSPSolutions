package eu.blackspectrum.bspsolutions.plugins;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import eu.blackspectrum.bspsolutions.BSPSolutions;
import eu.blackspectrum.bspsolutions.entities.BSPPlayer;
import eu.blackspectrum.bspsolutions.util.LocationUtil;

public class CompassTeleport
{


	public static void onPlayerHurt( final EntityDamageEvent event ) {
		if ( event.getEntity() instanceof Player )
		{
			final BSPPlayer player = BSPPlayer.get( event.getEntity() );
			player.abortTeleport();
		}
	}




	public static void onPlayerJoin( final PlayerJoinEvent event ) {
		if ( event.getPlayer().getWorld().equals( LocationUtil.getOverWorld() ) )
			event.getPlayer().setCompassTarget( LocationUtil.getCenterOfWorld() );
	}




	public static void onRightClick( final PlayerInteractEvent event ) {
		final BSPPlayer player = BSPPlayer.get( event.getPlayer() );

		if ( event.getAction() == Action.RIGHT_CLICK_AIR )
			if ( event.hasItem() && event.getItem().getType() == Material.COMPASS )
				if ( player.isTeleporting() )
					player.abortTeleport();
				else if ( LocationUtil.isCloseToCenter( player.getPlayer() ) )
					player.startTeleport();
				else
					player.sendMessage( BSPSolutions.Config().getString( "CompassTP.cantUseMessage" ) );
	}




	public static void onWorldChange( final PlayerChangedWorldEvent event ) {
		if ( event.getPlayer().getWorld().equals( LocationUtil.getOverWorld() ) )
			event.getPlayer().setCompassTarget( LocationUtil.getCenterOfWorld() );
	}




	public static void setUpConfig( final Configuration config ) {
		config.set( "CompassTP.successMessage", config.getString( "CompassTP.successMessage", "Teleport succeeded!" ) );
		config.set( "CompassTP.failMessage", config.getString( "CompassTP.failMessage", "Teleport failed!" ) );
		config.set( "CompassTP.cantUseMessage",
				config.getString( "CompassTP.cantUseMessage", "You cannot use that here. Use compass to get to the Center of the World." ) );
		config.set( "CompassTP.timerLength", BSPSolutions.Config().getInt( "CompassTP.timerLength", 5 ) );
	}

}
