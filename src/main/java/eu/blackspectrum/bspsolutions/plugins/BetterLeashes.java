package eu.blackspectrum.bspsolutions.plugins;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.inventory.ItemStack;

public class BetterLeashes
{


	public static void onUnleash( final PlayerUnleashEntityEvent event ) {
		if ( event.isCancelled() )
			return;

		event.setCancelled( true );

		if ( !( event.getEntity() instanceof LivingEntity ) )
			return;

		final LivingEntity entity = (LivingEntity) event.getEntity();

		final Entity leashHolder = entity.getLeashHolder();

		( (LivingEntity) event.getEntity() ).setLeashHolder( null );

		if ( leashHolder.getType().equals( EntityType.LEASH_HITCH ) )
			leashHolder.remove();

		event.getPlayer().getWorld().dropItemNaturally( event.getPlayer().getLocation(), new ItemStack( Material.LEASH, 1 ) );
	}
}
