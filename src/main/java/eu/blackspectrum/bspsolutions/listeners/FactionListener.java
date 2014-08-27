package eu.blackspectrum.bspsolutions.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.EventFactionsChunkChange;

public class FactionListener extends BSPListener
{


	@EventHandler(priority = EventPriority.NORMAL)
	public void onChunkClaim( final EventFactionsChunkChange event ) {
		if ( event.isCancelled() )
			return;

		final Faction newFaction = event.getNewFaction();
		final Faction oldFaction = BoardColls.get().getFactionAt( event.getChunk() );

		event.setCancelled( true );

		BoardColls.get().setFactionAt( event.getChunk(), newFaction );

		final UPlayer player = event.getUSender();

		if ( player == null )
			return;

		switch ( event.getType() ) {
		case CONQUER:
			player.sendMessage( "You " + ChatColor.YELLOW + ChatColor.ITALIC + "conquered" + ChatColor.GREEN + " some land from "
					+ ChatColor.RED + oldFaction.getName() + "!" );
			break;
		case BUY:
			player.sendMessage( "You " + ChatColor.YELLOW + ChatColor.ITALIC + "claimed" + ChatColor.GREEN + " some land for "
					+ ChatColor.YELLOW + newFaction.getName() + "!" );
			break;
		case SELL:
			player.sendMessage( "You " + ChatColor.RED + ChatColor.ITALIC + "unclaimed" + ChatColor.GREEN + " some land from "
					+ ChatColor.YELLOW + oldFaction.getName() + "!" );
			break;
		case PILLAGE:
			player.sendMessage( "You " + ChatColor.YELLOW + ChatColor.ITALIC + "pillaged" + ChatColor.GREEN + " some land of "
					+ ChatColor.RED + oldFaction.getName() + "!" );
			break;
		default:
			break;
		}
	}
}
