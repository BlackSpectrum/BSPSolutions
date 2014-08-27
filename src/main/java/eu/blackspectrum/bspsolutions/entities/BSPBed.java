package eu.blackspectrum.bspsolutions.entities;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Entity;

import eu.blackspectrum.bspsolutions.util.LocationUtil;

public class BSPBed extends Entity<BSPBed>
{


	private String		ownerId;
	private PS location;


	public static BSPBed get(String id, String world)
	{
		return BSPBedColls.get().getForWorld( world ).get( id );
	}


	@Override
	public BSPBed load( final BSPBed that ) {

		this.ownerId = that.ownerId;
		setLocation( that.location );
		
		return this;
	}
	
	public void setOwner(BSPPlayer owner)
	{
		ownerId = owner.getId();
	}
	
	public BSPPlayer getOwner()
	{
		return BSPPlayerColls.get().get( location.getWorld() ).get( ownerId );
	}
	
	public String getOwnerName()
	{
		return getOwner().getName();
	}


	public PS getLocation() {
		return location;
	}


	public void setLocation( PS location ) {
		this.location = location;
	}
	
	public Location getSpawnLocation()
	{
		Block bed = location.asBukkitBlock().getRelative( BlockFace.UP );
		
		for ( final BlockFace face : new BlockFace[] { BlockFace.EAST, BlockFace.NORTH_EAST, BlockFace.NORTH, BlockFace.NORTH_WEST,
				BlockFace.WEST, BlockFace.SOUTH_WEST, BlockFace.SOUTH, BlockFace.SOUTH_EAST } )
		{
			final Block block = bed.getRelative( face );
			if ( !LocationUtil.isLocationSafe( block.getLocation() ) )
				continue;

			if ( LocationUtil.isLocationSafe( block.getRelative( BlockFace.DOWN ).getLocation() )  )
				return block.getLocation().add( 0.5d, -0.5d, 0.5d );

			if ( LocationUtil.isLocationSafe( block.getRelative( BlockFace.UP ).getLocation() )  )
				return block.getLocation().add( 0.5d, 0, 0.5d );
		}
		
		return null;
	}

}
