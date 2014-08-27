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

		if ( event.getAction() == Action.RIGHT_CLICK_BLOCK )
		{
			final Block block = event.getClickedBlock();
			final Player player = event.getPlayer();

			if ( ( block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST ) && !player.isSneaking() )
			{
				event.setCancelled( true );
				final Chest chest = (Chest) block.getState();
				player.openInventory( chest.getInventory() );

			}
			else if ( block.getType() == Material.ENDER_CHEST && !player.isSneaking() )
			{
				event.setCancelled( true );
				player.openInventory( player.getEnderChest() );
			}
		}

	}
}
