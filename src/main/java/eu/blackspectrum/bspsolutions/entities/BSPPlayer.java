package eu.blackspectrum.bspsolutions.entities;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.massivecraft.massivecore.store.SenderEntity;

import eu.blackspectrum.bspsolutions.BSPSolutions;
import eu.blackspectrum.bspsolutions.tasks.TeleportTask;
import eu.blackspectrum.bspsolutions.util.LocationUtil;

public class BSPPlayer extends SenderEntity<BSPPlayer>
{


	// Store generic booleans in here
	// Bits (right to left):
	// 1: isTeleporting
	private byte				booleans;

	private long				timeInPurgatory;

	// MapIds that are faction mode for this player
	private ArrayList<Short>	fMaps	= null;




	public static BSPPlayer get( final Object o ) {
		return BSPPlayerColls.get().get2( o );
	}




	public void abortTeleport() {
		if ( this.isTeleporting() )
		{
			this.getPlayer().sendMessage( BSPSolutions.Config().getString( "CompassTP.failMessage" ) );
			this.setTeleporting( false );
		}
	}




	public boolean canLeavePurgatory() {
		return this.timeInPurgatory <= System.currentTimeMillis();
	}




	public void freeFromPurgatory() {
		final Player player = this.getPlayer();
		this.setTimeInPurgatory( 0 );
		player.teleport( LocationUtil.getRespawnLocation( player ), TeleportCause.PLUGIN );

		player.sendMessage( "You got freed from the Purgatory!" );
	}




	public void freeFromPurgatory( final PlayerRespawnEvent event ) {
		final Player player = this.getPlayer();
		this.setTimeInPurgatory( 0 );
		event.setRespawnLocation( LocationUtil.getRespawnLocation( player ) );

		player.sendMessage( "You got freed from the Purgatory!" );
	}




	@Override
	public boolean isDefault() {
		return this.canLeavePurgatory() && ( this.fMaps == null || this.fMaps.size() == 0 );
	}




	public boolean isFMap( final short id ) {
		if ( this.fMaps == null )
			return false;

		return this.fMaps.contains( id );
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

		return this;
	}




	public void setTeleporting( final boolean status ) {
		if ( status )
			this.booleans |= 0b00000001;
		else
			this.booleans &= 0b11111110;
	}




	public void setTimeInPurgatory( final long arg ) {
		this.timeInPurgatory = arg;

	}




	public void startTeleport() {
		this.setTeleporting( true );
		new TeleportTask( this ).runTaskTimer( BSPSolutions.Instance(), 0, 20 );
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
			this.fMaps = new ArrayList<Short>();

		this.fMaps.add( id );
		return true;
	}

}
