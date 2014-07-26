package eu.blackspectrum.bspsolutions.plugins;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class FalseAccessBlocker
{


	public static void onBlockBreack( final BlockBreakEvent event ) {
		final Player player = event.getPlayer();
		if ( event.isCancelled() )
		{
			player.setMetadata( "lastCancelledEvent", new FixedMetadataValue( BSPSolutions.instance, System.currentTimeMillis() ) );
			return;
		}
	}




	public static void onPlayerInteractBlock( final PlayerInteractEvent event ) {
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

				if ( lastCancelledEvent > 0 && ( lastCancelledEvent + 750L ) > System.currentTimeMillis() )
					event.setCancelled( true );

				player.removeMetadata( "lastCancelledEvent", BSPSolutions.instance );

			}
		}
	}




	public static void onPlayerJump( PlayerMoveEvent event ) {
		Entity player = event.getPlayer();
		Location from = event.getFrom();
		Location to = event.getTo();

		if ( player.isOnGround() && to.getY() > from.getY() )
		{
			if ( from.getY() - Math.floor( from.getY() ) < 0.1d && !from.getBlock().getRelative( BlockFace.DOWN ).getType().isSolid() )
			{
				Bukkit.getServer().broadcastMessage( "Player gltich jumped" );
				// player.setVelocity( new Vector( 0, -1, 0 ) );
			}
		}
	}
}
