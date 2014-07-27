package eu.blackspectrum.bspsolutions.plugins;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDeathEvent;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class EndReset
{


	private static final long	MS_IN_DAY	= 86400000;




	public static void onEnable() {

		// Only when there are no players left
		if ( Bukkit.getServer().getOnlinePlayers().length > 0 )
			return;

		if ( BSPSolutions.config.getLong( "EndReset.nextReset" ) == 0 )
			if ( BSPSolutions.getWorld( "world_the_end" ).getEntitiesByClasses( EnderDragon.class ).size() == 0 )
				scheduleReset();

		if ( BSPSolutions.config.getLong( "EndReset.nextReset" ) != 0
				&& System.currentTimeMillis() >= BSPSolutions.config.getLong( "EndReset.nextReset" ) )
			resetEnd();
	}




	public static void onEnderdragonDeath( final EntityDeathEvent event ) {
		final Entity e = event.getEntity();

		if ( e.getType().equals( EntityType.ENDER_DRAGON ) )
			scheduleReset();
	}




	public static void setUpConfig( final Configuration config ) {

		config.set( "EndReset.nextReset", config.getLong( "EndReset.nextReset", 0 ) );
		config.set( "EndReset.minDays", config.getInt( "EndReset.minDays", 5 ) );
		config.set( "EndReset.maxDays", config.getInt( "EndReset.maxDays", 15 ) );

	}




	private static void resetEnd() {
		final World end = BSPSolutions.getWorld( "world_the_end" );
		for ( int a = -32; a <= 31; a++ )
			for ( int i = -32; i <= 31; i++ )
				if ( end.loadChunk( a, i, false ) )
					if ( end.regenerateChunk( a, i ) )
					{
						end.refreshChunk( a, i );
						end.unloadChunkRequest( a, i );
					}

		BSPSolutions.config.set( "EndReset.nextReset", 0 );
		BSPSolutions.instance.saveConfig();
	}




	private static void scheduleReset() {
		final Random rand = new Random();

		final int maxDays = BSPSolutions.config.getInt( "EndReset.maxDays" ) - BSPSolutions.config.getInt( "EndReset.minDays" );
		final int minDays = BSPSolutions.config.getInt( "EndReset.minDays" );

		long nextReset = System.currentTimeMillis() + minDays * MS_IN_DAY;

		if ( maxDays > 0 )
			nextReset += rand.nextInt( maxDays + 1 ) * MS_IN_DAY;

		BSPSolutions.config.set( "EndReset.nextReset", nextReset );
		BSPSolutions.instance.saveConfig();
	}
}
