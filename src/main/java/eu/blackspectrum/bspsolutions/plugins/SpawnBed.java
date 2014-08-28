package eu.blackspectrum.bspsolutions.plugins;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Bed;

import com.massivecraft.factions.FPerm;
import com.massivecraft.massivecore.ps.PS;

import eu.blackspectrum.bspsolutions.entities.BSPBed;
import eu.blackspectrum.bspsolutions.entities.BSPBedColls;
import eu.blackspectrum.bspsolutions.entities.BSPPlayer;
import eu.blackspectrum.bspsolutions.entities.BedBoard;
import eu.blackspectrum.bspsolutions.entities.BedBoardColls;
import eu.blackspectrum.bspsolutions.util.LocationUtil;

public class SpawnBed
{


	public static void onPlayerClickedBed( PlayerInteractEvent event ) {
		Block clickedBlock = event.getClickedBlock();

		if ( clickedBlock == null || clickedBlock.getType() != Material.BED_BLOCK
				|| !clickedBlock.getWorld().equals( LocationUtil.getOverWorld() ) )
			return;

		BSPPlayer player = BSPPlayer.get( event.getPlayer() );

		// Prevent sleeping
		event.setCancelled( event.getAction() == Action.RIGHT_CLICK_BLOCK );

		final Bed bedBlock = (Bed) clickedBlock.getState().getData();
		if ( bedBlock.isHeadOfBed() )
			clickedBlock = clickedBlock.getRelative( bedBlock.getFacing().getOppositeFace() );

		PS ps = PS.valueOf( clickedBlock );

		BedBoard board = BedBoardColls.get().getForWorld( ps.getWorld() ).get( ps.getWorld() );
		BSPBed bed = board.getBedAt( ps );

		// No bed found, player can set here
		if ( bed == null )
		{
			// Leftclicking does nothing
			if(event.getAction() == Action.LEFT_CLICK_BLOCK)
				return;
			
			if ( !FPerm.BUILD.has( player.getUPlayer(), ps, false ) )
			{
				player.sendMessage( "It is too dangerous to sleep in non friendly territory." );
				return;
			}

			BSPBed oldBed = player.getBed();
			if ( oldBed != null )
				board.removeBed( oldBed );

			bed = BSPBedColls.get().get( player ).create();
			bed.setOwner( player );
			bed.setLocation( ps );
			player.setBed( bed );
			board.setBedAt( ps, bed );
			player.sendMessage( "This is your bed now. Keep it clean." );
		}

		// Bed found, is this the players bed?
		else if ( bed.getOwner().equals( player ) )
		{
			if ( event.getAction() == Action.LEFT_CLICK_BLOCK )
			{
				board.removeBedAt( ps );
				player.sendMessage( "No longer your bed." );
			}
			else
			{
				if ( bed.getSpawnLocation() != null )
					player.sendMessage( "This is your bed, it looks tidy." );
				else
					player.sendMessage( "This is your bed, seems to dangerous to sleep in." );
			}
		}
		else
			// send owner name
			player.sendMessage( "This is the bed of " + bed.getOwnerName() + "." );

	}




	public static void onBlockBreak( final BlockBreakEvent event ) {
		final Block block = event.getBlock();
		if ( !block.getWorld().equals( LocationUtil.getOverWorld() ) )
			return;
		onBlockBreak( block );
	}




	public static void onLiquidFromTo( final BlockFromToEvent event ) {
		if ( !event.getBlock().isLiquid() )
			return;

		final Block to = event.getToBlock();

		// /If bed destroy and check if used by anyone
		if ( to.getType().equals( Material.BED_BLOCK ) )
		{
			to.breakNaturally();

			onBlockBreak( to );
		}

	}




	public static void onEntityExplode( final EntityExplodeEvent event ) {
		if ( event.isCancelled() )
			return;

		if ( !event.getLocation().getWorld().equals( LocationUtil.getOverWorld() ) )
			return;

		// Iterate all destroyed blocks
		for ( final Block block : event.blockList() )
		{
			onBlockBreak( block );
		}
	}




	private static void onBlockBreak( Block block ) {
		// Only consider BED_BLOCK in overworld
		if ( block.getType() != Material.BED_BLOCK )
			return;

		PS ps = PS.valueOf( block );

		BedBoard board = BedBoardColls.get().getForWorld( ps.getWorld() ).get( ps.getWorld() );
		BSPBed bed = board.getBedAt( ps );

		if ( bed != null )
		{
			board.removeBedAt( ps );
		}
	}
}
