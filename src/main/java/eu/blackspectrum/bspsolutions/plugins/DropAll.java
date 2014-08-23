package eu.blackspectrum.bspsolutions.plugins;

import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.blackspectrum.bspsolutions.util.ItemUtil;

public class DropAll
{


	public static void onEnderManDeath( final EntityDeathEvent event ) {
		if ( !( event.getEntity() instanceof Enderman ) )
			return;

		final Enderman e = (Enderman) event.getEntity();

		ItemUtil.dropItemNaturally( e, e.getCarriedMaterial(), 1 );
	}




	public static void onPlayerDeath( final PlayerDeathEvent event ) {
		final Player player = event.getEntity();
		final Inventory enderChest = player.getEnderChest();

		for ( final ItemStack item : enderChest.getContents() )
			ItemUtil.dropItemNaturally( player, item );

		enderChest.clear();
	}

}
