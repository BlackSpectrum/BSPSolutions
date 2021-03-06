package eu.blackspectrum.bspsolutions.renderers;

import java.lang.ref.WeakReference;

import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;

import eu.blackspectrum.bspsolutions.FMaps;
import eu.blackspectrum.bspsolutions.entities.BSPPlayer;
import eu.blackspectrum.bspsolutions.entities.FMap;
import eu.blackspectrum.bspsolutions.util.FactionsUtil;

public class FMapRenderer extends MapRenderer
{


	// WeakReference for garbage collecting
	private WeakReference<FMap>	fMapRef			= new WeakReference<FMap>( null );

	// Maximum view distance on the map in chunks
	private final int			VIEW_DISTANCE	= 8;




	public FMapRenderer() {
		super( true );
	}




	@SuppressWarnings("deprecation")
	@Override
	public void render( final MapView map, final MapCanvas canvas, final Player player ) {
		FMap fMap = this.fMapRef.get();

		// Did the fMap get thrown to garbage?
		if ( fMap == null || fMap.isDumped() )
		{
			this.fMapRef = new WeakReference<FMap>( FMaps.get().getFMap( map.getId() ) );
			fMap = this.fMapRef.get();
		}

		fMap.setSecondChance( true );
		final BSPPlayer bspPlayer = BSPPlayer.get( player );

		// Remove cursors
		final MapCursorCollection cursors = canvas.getCursors();

		while ( cursors.size() > 0 )
			cursors.removeCursor( cursors.getCursor( 0 ) );

		// Render current data
		if ( bspPlayer.isFMap( fMap.getId() ) )
			fMap.renderFactions( canvas );
		else
			fMap.renderTerrain( canvas );

		// Is he holding map and is in same world?
		if ( player.getItemInHand().getDurability() != fMap.getId() || !player.getWorld().equals( map.getWorld() ) )
			return;

		final int scaleMod = map.getScale().ordinal();
		// Faction territory discovering
		{
			final String playerWorld = player.getWorld().getName();

			final int step = 16 >> scaleMod;

			// These are WORLD coordinates
			final int topLeftX = map.getCenterX() - ( 64 << scaleMod );
			final int topLeftZ = map.getCenterZ() - ( 64 << scaleMod );

			final int playerMapX = player.getLocation().getBlockX() - topLeftX >> scaleMod;
			final int playerMapZ = player.getLocation().getBlockZ() - topLeftZ >> scaleMod;

			// Minimum value needs to be rounded to multiple of step
			for ( int x = Math.max( ( playerMapX - this.VIEW_DISTANCE * step ) / step * step, 0 ); x <= Math.min( playerMapX
					+ this.VIEW_DISTANCE * step, 127 ); x += step )
				for ( int z = Math.max( ( playerMapZ - this.VIEW_DISTANCE * step ) / step * step, 0 ); z <= Math.min( playerMapZ
						+ this.VIEW_DISTANCE * step, 127 ); z += step )
				{
					// Convert current pixel to world chunk coordinates
					final byte cachedColour = this.getColour( topLeftX + ( x << scaleMod ) >> 4, topLeftZ + ( z << scaleMod ) >> 4,
							playerWorld );
					for ( int xi = x; xi < x + step; xi++ )
						for ( int zi = z; zi < z + step; zi++ )
							if ( ( xi - playerMapX ) * ( xi - playerMapX ) + ( zi - playerMapZ ) * ( zi - playerMapZ ) <= this.VIEW_DISTANCE
									* this.VIEW_DISTANCE * step * step )
								fMap.setPixel( xi, zi, cachedColour );
				}
		}

		// Draw cursors if not end/nether
		if ( map.getWorld().getEnvironment().equals( Environment.NORMAL ) )
		{
			final int xDiff = player.getLocation().getBlockX() - map.getCenterX() >> scaleMod;
			final int zDiff = player.getLocation().getBlockZ() - map.getCenterZ() >> scaleMod;

			byte cursorX = (byte) ( xDiff << 1 );
			byte cursorZ = (byte) ( zDiff << 1 );

			byte direction = 0;
			final float yaw = player.getLocation().getYaw();

			if ( xDiff >= -63 && zDiff >= -63 && xDiff <= 63 && zDiff <= 63 )
			{
				direction = (byte) ( (int) Math.round( yaw / 22.5 ) & 15 );

				// Add cursor with proper direction
				cursors.addCursor( cursorX, cursorZ, direction );
			}
			else
			{
				// only is he still renders stuff on the map draw his cursor
				if ( Math.abs( xDiff ) >= 63 + this.VIEW_DISTANCE * ( 16 >> scaleMod )
						|| Math.abs( zDiff ) >= 63 + this.VIEW_DISTANCE * ( 16 >> scaleMod ) )
					return;

				if ( xDiff <= -63 )
					cursorX = (byte) 128;

				if ( zDiff <= -63 )
					cursorZ = (byte) 128;

				if ( xDiff >= 63 )
					cursorX = (byte) 127;

				if ( zDiff >= 63 )
					cursorZ = (byte) 127;

				// Add cursor ass dot
				cursors.addCursor( cursorX, cursorZ, direction, (byte) 6 );
			}
		}
	}




	private byte getColour( final int chunkX, final int chunkZ, final String world ) {
		final Faction faction = BoardColls.get().getFactionAt( PS.valueOf( world, chunkX, chunkZ ) );

		if ( faction == null )
			return 4; // green

		if ( faction.isNone() )
			return 4; // green

		if ( FactionsUtil.isSafeZone( faction ) )
			return 72; // Gold

		if ( FactionsUtil.isWarZone( faction ) )
			return (byte) 142; // dark red

		return 58;

	}
}
