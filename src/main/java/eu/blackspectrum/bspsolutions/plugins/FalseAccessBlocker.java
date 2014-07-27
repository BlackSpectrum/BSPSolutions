package eu.blackspectrum.bspsolutions.plugins;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class FalseAccessBlocker
{


	public static void onBlockBreackCancelled( final BlockBreakEvent event ) {
		final Player player = event.getPlayer();
		if ( event.isCancelled() )
		{
			player.setMetadata( "lastCancelledEvent", new FixedMetadataValue( BSPSolutions.instance, System.currentTimeMillis() ) );
			return;
		}
	}




	public static void onBlockCancelledInAir( final BlockPlaceEvent event ) {
		final Entity player = event.getPlayer();
		if ( !event.isCancelled() )
			return;

		if ( !player.isOnGround() && !player.isInsideVehicle() && !BSPSolutions.isClimbing( player ) && !BSPSolutions.isSwimming( player ) )
			{
				event.getPlayer().damage( 6 );
				player.setVelocity( new Vector( Math.random() * 0.5 - 0.25, -1, Math.random() * 0.5 - 0.25 ) );
			}
	}




	public static void onPlayerInteractBlockCancelled( final PlayerInteractEvent event ) {
		final Player player = event.getPlayer();

		if ( event.getAction().equals( Action.RIGHT_CLICK_BLOCK ) )
		{
			if ( event.isCancelled() )
			{
				player.setMetadata( "lastCancelledEvent", new FixedMetadataValue( BSPSolutions.instance, System.currentTimeMillis() ) );
				return;
			}

			if ( player.hasMetadata( "lastCancelledEvent" ) )
			{
				long lastCancelledEvent = -1;
				for ( final MetadataValue meta : player.getMetadata( "lastCancelledEvent" ) )
					if ( meta.getOwningPlugin().equals( BSPSolutions.instance ) )
						lastCancelledEvent = meta.asLong();

				if ( lastCancelledEvent > 0 && lastCancelledEvent + 750L > System.currentTimeMillis() )
					event.setCancelled( true );

				player.removeMetadata( "lastCancelledEvent", BSPSolutions.instance );

			}
		}
	}
}
