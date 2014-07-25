package eu.blackspectrum.bspsolutions.plugins;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ForceOpen
{


	public static void onOpenChest( final PlayerInteractEvent event ) {
		if ( event.isCancelled() )
			return;

		if ( event.getAction().equals( Action.RIGHT_CLICK_BLOCK ) )
		{
			final Block block = event.getClickedBlock();
			final Player player = event.getPlayer();

			if ( block.getType().equals( Material.CHEST ) )
			{
				event.setCancelled( true );
				final Chest chest = (Chest) block.getState();
				player.openInventory( chest.getInventory() );

			}
		}

	}
}
