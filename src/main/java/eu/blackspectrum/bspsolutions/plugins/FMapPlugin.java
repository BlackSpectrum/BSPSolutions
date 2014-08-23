package eu.blackspectrum.bspsolutions.plugins;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.map.CraftMapRenderer;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import eu.blackspectrum.bspsolutions.FMaps;
import eu.blackspectrum.bspsolutions.renderers.FMapRenderer;

public class FMapPlugin
{


	public static void onMapInitialize( final MapInitializeEvent event ) {
		final MapView newMap = event.getMap();

		if ( newMap != null )
		{
			// Throw away all default map renderer
			for ( final MapRenderer rend : newMap.getRenderers() )
				if ( rend instanceof CraftMapRenderer )
					newMap.removeRenderer( rend );

			// If no renderer left add FMapRenderer
			if ( newMap.getRenderers().size() == 0 )
				newMap.addRenderer( new FMapRenderer() );

		}
	}




	public static void onPlayerRightClickMap( final PlayerInteractEvent event ) {

		if ( event.getAction() != Action.RIGHT_CLICK_AIR )
			return;

		final ItemStack item = event.getPlayer().getItemInHand();

		if ( item != null && item.getType() == Material.MAP )
			FMaps.Instance().getFMap( item.getDurability() ).togglePlayer( event.getPlayer().getUniqueId() );
	}
}
