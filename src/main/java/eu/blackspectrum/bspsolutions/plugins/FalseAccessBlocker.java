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
import org.bukkit.util.Vector;

import eu.blackspectrum.bspsolutions.BSPSolutions;
import eu.blackspectrum.bspsolutions.Consts;
import eu.blackspectrum.bspsolutions.entities.BSPPlayer;

public class FalseAccessBlocker
{


	public static void onBlockBreackCancelled( final BlockBreakEvent event ) {
		final Player player = event.getPlayer();
		final BSPPlayer bspPlayer = BSPPlayer.get( player );
		if ( !event.isCancelled() )
			return;

		bspPlayer.setLastCancelledEvent( System.currentTimeMillis() );

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
		final BSPPlayer bspPlayer = BSPPlayer.get( player );

		if ( event.getAction() == Action.RIGHT_CLICK_BLOCK )
			if ( event.isCancelled() )
				bspPlayer.setLastCancelledEvent( System.currentTimeMillis() );
			else if ( bspPlayer.getLastCancelledEvent() + ((long) 0.75f * Consts.MILIS_IN_SECOND) > System.currentTimeMillis() )
				event.setCancelled( true );
			else
				bspPlayer.setLastCancelledEvent( null );
	}
}
