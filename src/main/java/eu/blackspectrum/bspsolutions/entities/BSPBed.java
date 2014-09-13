package eu.blackspectrum.bspsolutions.entities;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Entity;

import eu.blackspectrum.bspsolutions.util.LocationUtil;

public class BSPBed extends Entity<BSPBed>
{


	private String	ownerId		= null;
	private PS		location	= null;




	public static BSPBed get( final String id ) {
		return BSPBedColl.get().get( id );
	}




	public PS getLocation() {
		return this.location;
	}




	public BSPPlayer getOwner() {
		return BSPPlayerColl.get().get( this.ownerId );
	}




	public String getOwnerName() {
		return this.getOwner().getName();
	}




	public Location getSpawnLocation() {

		final Block bed = this.location.asBukkitBlock();

		// Check if can spawn on bed
		if ( LocationUtil.isLocationSafe( bed.getLocation().add( 0, 1, 0 ) ) )
			return bed.getLocation().add( 0.5f, 0.5f, 0.5f );

		for ( final BlockFace face : new BlockFace[] { BlockFace.EAST, BlockFace.NORTH_EAST, BlockFace.NORTH, BlockFace.NORTH_WEST,
				BlockFace.WEST, BlockFace.SOUTH_WEST, BlockFace.SOUTH, BlockFace.SOUTH_EAST } )
		{
			final Block block = bed.getRelative( face );

			if ( LocationUtil.isLocationSafe( block.getLocation() ) )
				return block.getLocation().add( 0.5d, 0, 0.5d );

			if ( LocationUtil.isLocationSafe( block.getRelative( BlockFace.DOWN ).getLocation() ) )
				return block.getLocation().add( 0.5d, -0.5d, 0.5d );

			if ( LocationUtil.isLocationSafe( block.getRelative( BlockFace.UP ).getLocation() ) )
				return block.getLocation().add( 0.5d, 1, 0.5d );
		}

		return null;
	}




	@Override
	public boolean isDefault() {
		return this.ownerId == null || this.location == null;
	}




	@Override
	public BSPBed load( final BSPBed that ) {

		this.ownerId = that.ownerId;
		this.setLocation( that.location );

		return this;
	}




	public void setLocation( final PS location ) {
		this.location = location;
	}




	public void setOwner( final BSPPlayer owner ) {
		this.ownerId = owner.getId();
	}

}
