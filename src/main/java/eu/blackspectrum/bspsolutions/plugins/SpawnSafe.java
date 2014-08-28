package eu.blackspectrum.bspsolutions.plugins;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import eu.blackspectrum.bspsolutions.BSPSolutions;
import eu.blackspectrum.bspsolutions.entities.BSPPlayer;

/*
 * We dont use actual respawn to trigger protection as that is in Purgatory anyway
 * Use world changed instead
 */
public class SpawnSafe
{


	public static void onPlayerChangedWorld( final PlayerChangedWorldEvent event, BSPPlayer bspPlayer ) {
		bspPlayer.setLastRespawn( System.currentTimeMillis() );
	}




	public static void onEntityDamage( final EntityDamageEvent event, BSPPlayer bspPlayer ) {
		final Entity victim = event.getEntity();

		if ( victim.getType() != EntityType.PLAYER )
			return;

		bspPlayer = BSPPlayer.get( victim );

		if ( bspPlayer.getLastRespawn() + BSPSolutions.getConfig2().getDouble( "SpawnSafe.protetionTime" ) * 1000 >= System
				.currentTimeMillis() )
			event.setDamage( event.getDamage() * BSPSolutions.getConfig2().getDouble( "SpawnSafe.damageToProtected" ) );
		else
			bspPlayer.setLastRespawn( null );
	}




	public static void onEntityDamageByEntity( final EntityDamageByEntityEvent event, BSPPlayer bspPlayer ) {
		final Entity attacker = event.getDamager();

		if ( event.getEntity().getType() != EntityType.PLAYER || attacker.getType() != EntityType.PLAYER )
			return;

		bspPlayer = BSPPlayer.get( attacker );

		if ( bspPlayer.getLastRespawn() + BSPSolutions.getConfig2().getDouble( "SpawnSafe.protetionTime" ) * 1000 >= System
				.currentTimeMillis() )
			event.setDamage( event.getDamage() * BSPSolutions.getConfig2().getDouble( "SpawnSafe.damageByProtected" ) );
		else
			bspPlayer.setLastRespawn( null );
	}




	public static void setUpConfig( Configuration config ) {
		config.set( "SpawnSafe.protectionTime", config.getDouble( "SpawnSafe.protectionTime", 3.0d ) );
		config.set( "SpawnSafe.damageDealtToProtected", config.getDouble( "SpawnSafe.damageDealtToProtected", 0.0d ) );
		config.set( "SpawnSafe.damageDealtByProtected", config.getDouble( "SpawnSafe.damageDealtByProtected", 1.0d ) );
	}
}
