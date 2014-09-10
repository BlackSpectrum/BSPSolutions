package eu.blackspectrum.bspsolutions.plugins;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.util.InventoryUtil;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class Hats
{


	public static int	RAW_HAT_SLOT_ID	= 5;




	public static void doDenyingHardSwap( final InventoryClickEvent event ) {
		// We deny the normal result
		// NOTE: There is no need to cancel the event since that is just a proxy
		// method for the line below.
		event.setResult( Result.DENY );

		// Schedule swap
		Bukkit.getScheduler().scheduleSyncDelayedTask( BSPSolutions.get(), new Runnable() {


			@Override
			public void run() {
				final ItemStack current = event.getCurrentItem();
				final ItemStack cursor = event.getCursor();
				event.setCurrentItem( cursor );
				event.getView().setCursor( current );
				InventoryUtil.update( event.getWhoClicked() );
			}
		} );
	}




	public static void hatSwitch( final InventoryClickEvent event ) {
		// If a player ...
		if ( !( event.getWhoClicked() instanceof Player ) )
			return;
		final Player me = (Player) event.getWhoClicked();

		// ... is clicking around in their own/armor/crafting view ...
		if ( event.getView().getType() != InventoryType.CRAFTING )
			return;

		// ... and they are clicking their hat slot ...
		if ( event.getRawSlot() != RAW_HAT_SLOT_ID )
			return;

		// ... and the cursor isn't a pumpkin ..
		if ( event.getCursor().getType() == Material.PUMPKIN )
			return;

		// ... and the cursor is a hat ...
		if ( !isHat( event.getCursor() ) )
			return;

		// ... and hatting is allowed ...
		if ( !me.hasPermission( "BSP.hatUse" ) )
			return;

		// ... then perform the switch.
		doDenyingHardSwap( event );
	}




	public static boolean isHat( final ItemStack itemStack ) {
		if ( itemStack == null )
			return false;
		if ( itemStack.getAmount() == 0 )
			return false;
		if ( itemStack.getType() == Material.AIR )
			return false;
		if ( itemStack.getType().isBlock() == false )
			return false;
		return true;
	}
}
