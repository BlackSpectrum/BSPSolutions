package eu.blackspectrum.bspsolutions.plugins;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import eu.blackspectrum.bspsolutions.renderers.PictureRenderer;
import eu.blackspectrum.bspsolutions.util.LocationUtil;
import eu.blackspectrum.bspsolutions.util.PicIO;

public class Pic2Map
{


	@SuppressWarnings("deprecation")
	public static void paintMap( final String url, final Player player ) {
		try
		{

			final MapView map = Bukkit.createMap( LocationUtil.getOverWorld() );

			PicIO.loadImgageFromURL( url, map.getId() );

			// Clear all other renderers
			for ( final MapRenderer renderer : map.getRenderers() )
				map.removeRenderer( renderer );

			map.addRenderer( new PictureRenderer() );

			final ItemStack item = new ItemStack( Material.MAP, 1, map.getId() );
			final ItemStack newItemInHand = player.getItemInHand();

			// Reduce amount of item in hand
			newItemInHand.setAmount( newItemInHand.getAmount() - 1 );

			if ( newItemInHand.getAmount() == 0 )
				player.setItemInHand( null );
			else
				player.setItemInHand( newItemInHand );

			// Find place to add the new map
			final Inventory inv = player.getInventory();
			if ( inv.firstEmpty() > -1 )
				inv.addItem( new ItemStack[] { item } );
			else
				player.getLocation().getWorld().dropItemNaturally( player.getLocation(), item );

		}
		catch ( final Exception e )
		{
			player.sendMessage( e.getMessage() );
		}
	}
}
