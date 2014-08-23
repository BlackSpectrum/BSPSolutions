package eu.blackspectrum.bspsolutions.util;

import java.util.HashMap;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.ps.PS;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class FactionsUtil
{


	private static HashMap<String, Long>	offlineFactions	= null;




	public static void addFaction( final Faction faction ) {

		if ( faction == null )
			return;

		if ( offlineFactions == null )
			offlineFactions = new HashMap<String, Long>();

		// When last player logs off he still gets counted as offline, so <= 1
		if ( faction.getOnlinePlayers().size() <= 1 )
			offlineFactions.put( faction.getId(), System.currentTimeMillis() + BSPSolutions.config.getLong( "Factions.offlineDelay" )
					* 1000 );
	}




	public static void addFaction( final Player player ) {
		addFaction( UPlayer.get( player ).getFaction() );
	}




	public static void addFaction( final String id ) {
		addFaction( Faction.get( id ) );
	}




	public static boolean isFactionOffline( final Faction faction ) {
		if ( offlineFactions.containsKey( faction.getId() ) && System.currentTimeMillis() > offlineFactions.get( faction.getId() ) )
		{
			removeFaction( faction );
			return true;
		}

		// If not in the map use player count. This is
		// for situations after server restart/reload
		return !faction.isNone() && !isSafeZone( faction ) && !isWarZone( faction ) && faction.isAllUPlayersOffline();
	}




	public static boolean isFactionOffline( final Player player ) {
		return isFactionOffline( UPlayer.get( player ).getFaction() );
	}




	public static boolean isFactionOffline( final PS ps ) {
		return isFactionOffline( BoardColls.get().getFactionAt( ps ) );
	}




	public static boolean isFactionOffline( final String id ) {
		return isFactionOffline( Faction.get( id ) );
	}




	public static boolean isFactionOffline( final UPlayer uPlayer ) {
		return isFactionOffline( uPlayer.getFaction() );
	}




	public static boolean isSafeZone( final Faction faction ) {
		return faction.equals( FactionColls.get().getForUniverse( faction.getUniverse() ).getSafezone() );
	}




	public static boolean isWarZone( final Faction faction ) {
		return faction.equals( FactionColls.get().getForUniverse( faction.getUniverse() ).getWarzone() );
	}




	public static void removeFaction( final Faction faction ) {
		removeFaction( faction.getId() );
	}




	public static void removeFaction( final Player player ) {
		removeFaction( UPlayer.get( player ).getFactionId() );
	}




	public static void removeFaction( final String id ) {
		if ( offlineFactions == null )
			return;

		offlineFactions.remove( id );

		// Emtpy map -> null
		if ( offlineFactions.size() == 0 )
			offlineFactions = null;
	}




	public static void setUpConfig( final Configuration config ) {
		config.set( "Factions.offlineDelay", config.getLong( "Factions.offlineDelay", 300 ) );
	}

}
