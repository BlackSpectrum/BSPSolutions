package eu.blackspectrum.bspsolutions.plugins;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class FalseAccessBlocker
{


	public static void onBlockBreackCancelled( final BlockBreakEvent event ) {
		final Player player = event.getPlayer();
		if ( !event.isCancelled() )
			return;

		player.setMetadata( "lastCancelledEvent", new FixedMetadataValue( BSPSolutions.instance, System.currentTimeMillis() ) );

		Vector dir = event.getBlock().getLocation().toVector().add( new Vector( 0.5, 0, 0.5 ) ).subtract( player.getLocation().toVector() );

		if ( ( dir.getBlockY() == 1 || !( (Entity) player ).isOnGround() )
				&& !event.getBlock().getRelative( BlockFace.UP ).getType().isSolid() && !player.isInsideVehicle()
				&& !BSPSolutions.isClimbing( player ) && !BSPSolutions.isSwimming( player ) )
		{
			dir = dir.setY( 0 );

			if ( dir.length() <= 1.0d )
			{
				dir = dir.setY( 0 ).multiply( -0.5 );
				player.teleport( player.getLocation().add( dir ), TeleportCause.PLUGIN );
				player.setVelocity( dir );
			}
		}

	}




	public static void onBlockCancelledInAir( final BlockPlaceEvent event ) {
		final Entity player = event.getPlayer();
		if ( !event.isCancelled() )
			return;

		if ( !player.isOnGround() && !player.isInsideVehicle() && !BSPSolutions.isClimbing( player ) && !BSPSolutions.isSwimming( player ) )
		{
			final Location placedLoc = event.getBlockPlaced().getLocation();
			final double diffX = player.getLocation().getX() - placedLoc.getX();
			final double diffZ = player.getLocation().getZ() - placedLoc.getZ();
			final double diffY = player.getLocation().getY() - placedLoc.getY();

			if ( diffX < 1.3d && diffX > -0.3d && diffZ < 1.3d && diffZ > -0.3d && diffY < 1.5d && diffY > 0.5d )
			{
				event.getPlayer().damage( 6 );
				player.teleport( player.getLocation().subtract( 0, 0.9, 0 ), TeleportCause.PLUGIN );
				player.setVelocity( new Vector( Math.signum( Math.random() - 0.5 ) * 0.3, 0, Math.signum( Math.random() - 0.5 ) * 0.3 ) );
			}
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
