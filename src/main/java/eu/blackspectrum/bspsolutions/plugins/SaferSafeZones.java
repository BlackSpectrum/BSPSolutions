package eu.blackspectrum.bspsolutions.plugins;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class SaferSafeZones
{


	public static void onPlayerChangedWorld( final PlayerChangedWorldEvent event ) {
		final Player player = event.getPlayer();

		if ( BSPSolutions.isInSafeZone( player.getLocation() ) )
			onEnterSafeZone( player );
	}




	public static void onPlayerChunkMove( final PlayerMoveEvent event ) {
		if ( event.isCancelled() )
			return;

		final Location from = event.getFrom();
		final Location to = event.getTo();
		final Player player = event.getPlayer();

		if ( from.getChunk().equals( to.getChunk() ) )
			return;

		if ( BSPSolutions.isInSafeZone( to ) )
			onEnterSafeZone( player );
	}




	public static void onPlayerDamaged( final EntityDamageEvent event ) {
		if ( event.getEntityType().equals( EntityType.PLAYER ) )
			if ( BSPSolutions.isInSafeZone( event.getEntity().getLocation() ) )
				event.setCancelled( true );
	}




	public static void onPlayerJoin( final PlayerJoinEvent event ) {
		final Player player = event.getPlayer();

		if ( BSPSolutions.isInSafeZone( player.getLocation() ) )
			onEnterSafeZone( player );
	}




	public static void onPlayerUseItem( final PlayerInteractEvent event ) {
		if ( BSPSolutions.isInSafeZone( event.getPlayer().getLocation() ) )
		{
			if ( event.getItem() == null || event.getAction().equals( Action.LEFT_CLICK_AIR )
					|| event.getAction().equals( Action.LEFT_CLICK_BLOCK ) )
				return;

			final Material item = event.getItem().getType();

			if ( item.equals( Material.EGG ) || item.equals( Material.ENDER_PEARL ) || item.equals( Material.POTION ) )
			{
				event.setCancelled( true );
				final ItemStack toDrop = event.getItem().clone();
				final Player player = event.getPlayer();
				toDrop.setAmount( 1 );
				if ( event.getItem().getAmount() == 1 )
					player.getInventory().remove( player.getItemInHand() );
				else
					event.getItem().setAmount( event.getItem().getAmount() - 1 );

				BSPSolutions.dropItemNaturally( player, toDrop );
			}
		}
	}




	private static void onEnterSafeZone( final Player player ) {
		for ( final PotionEffect pot : player.getActivePotionEffects() )
			player.removePotionEffect( pot.getType() );
	}
}
