package eu.blackspectrum.bspsolutions.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.massivecore.ps.PS;

import eu.blackspectrum.bspsolutions.BSPSolutions;
import eu.blackspectrum.spawnbed.entities.BedHead;
import eu.blackspectrum.spawnbed.util.Util;

public class LocationUtil
{


	public static Location getCenterOfWorld() {
		return new Location( getOverWorld(), BSPSolutions.config.getInt( "Locations.center.x" ), 0,
				BSPSolutions.config.getInt( "Locations.center.z" ) );
	}




	public static World getEnd() {
		return getWorld( BSPSolutions.config.getString( "Locations.worlds.end" ) );
	}




	public static World getNether() {
		return getWorld( BSPSolutions.config.getString( "Locations.worlds.nether" ) );
	}




	public static World getOverWorld() {
		return getWorld( BSPSolutions.config.getString( "Locations.worlds.world" ) );
	}




	public static World getPurgatoryWorld() {
		return getWorld( BSPSolutions.config.getString( "Locations.worlds.purgatory" ) );
	}




	public static Location getRespawnLocation( final Player player ) {
		final BedHead bedHead = Util.getBed( player, false );

		if ( bedHead != null && bedHead.isSpawnable( player, true ) )
			return bedHead.getSpawnLocation();

		return getSpawnWorld().getSpawnLocation();
	}




	public static World getSpawnWorld() {
		return getWorld( BSPSolutions.config.getString( "Locations.worlds.spawn" ) );
	}




	public static World getWorld( final String name ) {
		return Bukkit.getServer().getWorld( name );
	}




	public static boolean isCloseToCenter( final Player player ) {
		return player.getWorld().equals( getOverWorld() )
				&& player
						.getLocation()
						.toVector()
						.distance(
								new Vector( BSPSolutions.config.getInt( "Locations.center.x" ), player.getLocation().getY(),
										BSPSolutions.config.getInt( "Locations.center.z" ) ) ) <= BSPSolutions.config
						.getInt( "Locations.center.radius" );

	}




	public static boolean isInSafeZone( final Location location ) {
		return FactionsUtil.isSafeZone( BoardColls.get().getFactionAt( PS.valueOf( location ) ) );
	}




	public static boolean isLocationSafe( final Location location ) {
		final Material block = location.getBlock().getType();
		final Material above = location.getBlock().getRelative( BlockFace.UP ).getType();

		if ( block.isSolid() || above.isSolid() || ItemUtil.isLava( block ) || ItemUtil.isLava( above ) || block == Material.FIRE
				|| above == Material.FIRE )
			return false;

		final Material below = location.getBlock().getRelative( BlockFace.DOWN ).getType();

		if ( ItemUtil.isLava( below ) || below == Material.FIRE || below == Material.CACTUS || below == Material.ENDER_PORTAL )
			return false;

		if ( !below.isSolid() )
		{
			int fallHeight = 1;
			Block belowBlock = location.getBlock().getRelative( BlockFace.DOWN );

			while ( fallHeight < 4 )
			{
				belowBlock = belowBlock.getRelative( BlockFace.DOWN );
				if ( belowBlock.getType().isSolid() || ItemUtil.isWater( belowBlock ) )
					return true;

				fallHeight++;
			}

			return false;
		}

		return true;

	}




	public static void setUpConfig( final Configuration config ) {
		config.set( "Locations.worlds.spawn", config.get( "Locations.worlds.spawn", "world_creative" ) );
		config.set( "Locations.worlds.purgatory", config.get( "Locations.worlds.purgatory", "world_purgatory" ) );
		config.set( "Locations.worlds.world", config.get( "Locations.worlds.world", "world" ) );
		config.set( "Locations.worlds.nether", config.get( "Locations.worlds.nether", "world_nether" ) );
		config.set( "Locations.worlds.end", config.get( "Locations.worlds.end", "world_the_end" ) );
		config.set( "Locations.center.x", config.get( "Locations.center.x", 500 ) );
		config.set( "Locations.center.z", config.get( "Locations.center.z", -340 ) );
		config.set( "Locations.center.radius", config.get( "Locations.center.radius", 5 ) );
		config.set( "Locations.spawn.radiusMin", config.get( "Locations.spawn.radius", 1200 ) );
		config.set( "Locations.spawn.radiusMax", config.get( "Locations.spawn.radius", 3500 ) );
	}

}
