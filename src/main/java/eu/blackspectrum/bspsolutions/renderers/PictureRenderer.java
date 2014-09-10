package eu.blackspectrum.bspsolutions.renderers;

import java.awt.Image;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import eu.blackspectrum.bspsolutions.util.PicIO;

public class PictureRenderer extends MapRenderer
{


	private boolean	rendered	= false;




	public boolean isRendered() {
		return this.rendered;
	}




	@Override
	@SuppressWarnings("deprecation")
	public void render( final MapView view, final MapCanvas canvas, final Player player ) {
		if ( this.rendered )
			return;

		Image image;
		try
		{
			image = PicIO.getImage( view.getId() );
			canvas.drawImage( 0, 0, image );
		}
		catch ( final Exception e )
		{
			view.addRenderer( new ErrorRenderer( e.getMessage() ) );
			view.removeRenderer( this );
		}

		this.rendered = true;
	}
}
