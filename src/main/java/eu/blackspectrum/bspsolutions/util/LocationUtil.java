package eu.blackspectrum.bspsolutions.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.massivecore.ps.PS;

import eu.blackspectrum.bspsolutions.BSPSolutions;
import eu.blackspectrum.spawnbed.entities.BedHead;
import eu.blackspectrum.spawnbed.util.Util;

public class LocationUtil
{


	public static World getPurgatoryWorld() {
		return getWorld( BSPSolutions.config.getString( "Locations.worldPurgatory" ) );
	}




	public static Location getRespawnLocation( final Player player ) {
		final BedHead bedHead = Util.getBed( player, false );

		if ( bedHead != null && bedHead.isSpawnable( player, true ) )
			return bedHead.getSpawnLocation();

		return getSpawnWorld().getSpawnLocation();
	}




	public static World getSpawnWorld() {
		return getWorld( BSPSolutions.config.getString( "Locations.worldSpawn" ) );
	}




	public static World getWorld( final String name ) {
		return Bukkit.getServer().getWorld( name );
	}




	public static boolean isInSafeZone( final Location location ) {
		return FactionsUtil.isSafeZone( BoardColls.get().getFactionAt( PS.valueOf( location ) ) );
	}




	public static void setUpConfig( final Configuration config ) {
		config.set( "Locations.worldSpawn", config.get( "Locations.worldSpawn", "world_creative" ) );
		config.set( "Locations.worldPurgatory", config.get( "Locations.worldPurgatory", "world_purgatory" ) );
	}

}
