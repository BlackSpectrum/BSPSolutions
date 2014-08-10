package eu.blackspectrum.bspsolutions;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.ps.PS;

public class OfflineFactions
{


	private static OfflineFactions		instance		= null;

	// Faction ID: OfflineSince
	private final HashMap<String, Long>	offlineFactions	= new HashMap<String, Long>();




	public static OfflineFactions Instance() {
		if ( instance == null )
			instance = new OfflineFactions();

		return instance;
	}




	private OfflineFactions() {

	}




	public void addFaction( final Faction faction ) {

		if ( faction == null )
			return;

		// When last player logs off he still gets counted as offline, so <= 1
		if ( faction.getOnlinePlayers().size() <= 1 )
			this.offlineFactions.put( faction.getId(), System.currentTimeMillis() + BSPSolutions.config.getLong( "Factions.offlineDelay" )
					* 1000 );
	}




	public void addFaction( final Player player ) {
		this.addFaction( UPlayer.get( player ).getFaction() );
	}




	public void addFaction( final String id ) {
		this.addFaction( Faction.get( id ) );
	}




	public boolean isFactionOffline( final Faction faction ) {
		if ( this.offlineFactions.containsKey( faction.getId() ) )
			return System.currentTimeMillis() > this.offlineFactions.get( faction.getId() );

		// If not in the map use player count. This is
		// for situations after server restart/reload
		return !faction.isNone() && !BSPSolutions.isSafeZone( faction ) && !BSPSolutions.isWarZone( faction )
				&& faction.isAllUPlayersOffline();
	}




	public boolean isFactionOffline( final Player player ) {
		return this.isFactionOffline( UPlayer.get( player ).getFaction() );
	}




	public boolean isFactionOffline( final PS ps ) {
		return this.isFactionOffline( BoardColls.get().getFactionAt( ps ) );
	}




	public boolean isFactionOffline( final String id ) {
		return this.isFactionOffline( Faction.get( id ) );
	}




	public boolean isFactionOffline( final UPlayer uPlayer ) {
		return this.isFactionOffline( uPlayer.getFaction() );
	}




	public void removeFaction( final Faction faction ) {
		this.offlineFactions.remove( faction.getId() );
	}




	public void removeFaction( final Player player ) {
		this.offlineFactions.remove( UPlayer.get( player ).getFactionId() );
	}




	public void removeFaction( final String id ) {
		this.offlineFactions.remove( id );
	}

}
