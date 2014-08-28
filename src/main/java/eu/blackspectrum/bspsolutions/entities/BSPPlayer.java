package eu.blackspectrum.bspsolutions.entities;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.store.SenderEntity;

import eu.blackspectrum.bspsolutions.BSPSolutions;
import eu.blackspectrum.bspsolutions.tasks.TeleportTask;
import eu.blackspectrum.bspsolutions.util.LocationUtil;

public class BSPPlayer extends SenderEntity<BSPPlayer>
{


	// Store generic booleans in here
	// Bits (right to left):
	// 1: isTeleporting
	private byte				booleans			= 0;

	private Long				timeInPurgatory		= null;

	// MapIds that are faction mode for this player
	private ArrayList<Short>	fMaps				= null;

	private String				bedId				= null;

	private transient Long		lastRespawn			= null;
	private transient Long		lastCancelledEvent	= null;




	public static BSPPlayer get( final Object o ) {
		return BSPPlayerColl.get().get( o );
	}




	public void abortTeleport() {
		if ( this.isTeleporting() )
		{
			this.getPlayer().sendMessage( BSPSolutions.getConfig2().getString( "CompassTP.failMessage" ) );
			this.setTeleporting( false );
		}
	}




	public boolean canLeavePurgatory() {
		if ( this.timeInPurgatory == null )
			return true;

		return this.timeInPurgatory == null ? true : this.timeInPurgatory <= System.currentTimeMillis();
	}




	public void freeFromPurgatory() {
		final Player player = this.getPlayer();
		this.setTimeInPurgatory( null );
		player.teleport( this.getSpawnLocation(), TeleportCause.PLUGIN );

		player.sendMessage( "You got freed from the Purgatory!" );
	}




	public void freeFromPurgatory( final PlayerRespawnEvent event ) {
		final Player player = this.getPlayer();
		this.setTimeInPurgatory( null );
		event.setRespawnLocation( this.getSpawnLocation() );

		player.sendMessage( "You got freed from the Purgatory!" );
	}




	public BSPBed getBed() {
		return BSPBed.get( this.bedId );
	}




	public Long getLastCancelledEvent() {
		return this.lastCancelledEvent == null ? 0 : this.lastCancelledEvent;
	}




	public Long getLastRespawn() {
		return this.lastRespawn == null ? 0 : this.lastRespawn;
	}




	public Location getSpawnLocation() {
		Location ret = this.getBed() == null ? null : this.getBed().getSpawnLocation();

		if ( ret == null )
			ret = LocationUtil.getSpawnWorld().getSpawnLocation();

		return ret;
	}




	public UPlayer getUPlayer() {
		return UPlayer.get( this.getPlayer() );
	}




	@Override
	public boolean isDefault() {
		return this.canLeavePurgatory() && ( this.fMaps == null || this.fMaps.size() == 0 ) && this.bedId == null;
	}




	public boolean isFMap( final short id ) {
		return this.fMaps == null ? false : this.fMaps.contains( id );
	}




	public boolean isInPurgatory() {
		return this.getPlayer().getWorld().equals( LocationUtil.getPurgatoryWorld() );
	}




	public boolean isTeleporting() {
		return ( this.booleans & 0b00000001 ) == 1;
	}




	@Override
	public BSPPlayer load( final BSPPlayer that ) {
		this.setTimeInPurgatory( that.timeInPurgatory );
		this.fMaps = that.fMaps;
		this.bedId = that.bedId;

		return this;
	}




	public void setBed( final BSPBed bed ) {
		if ( bed == null )
			this.bedId = null;
		else
			this.bedId = bed.getId();
	}




	public void setLastCancelledEvent( final Long lastCancelledEvent ) {
		this.lastCancelledEvent = lastCancelledEvent;
	}




	public void setLastRespawn( final Long lastRespawn ) {
		this.lastRespawn = lastRespawn;
	}




	public void setTeleporting( final boolean status ) {
		if ( status )
			this.booleans |= 0b00000001;
		else
			this.booleans &= 0b11111110;
	}




	public void setTimeInPurgatory( final Long arg ) {
		this.timeInPurgatory = arg;
	}




	public void startTeleport() {
		this.setTeleporting( true );
		new TeleportTask( this ).runTaskTimer( BSPSolutions.get(), 0, 20 );
	}




	public boolean toggleFMap( final short id ) {

		if ( this.fMaps != null && this.isFMap( id ) )
		{
			this.fMaps.remove( id );

			if ( this.fMaps.size() == 0 )
				this.fMaps = null;

			return false;
		}

		if ( this.fMaps == null )
			this.fMaps = new ArrayList<Short>( 2 );

		this.fMaps.add( id );
		return true;
	}

}
