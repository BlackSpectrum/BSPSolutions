package eu.blackspectrum.bspsolutions.renderers;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapFont;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;

public class ErrorRenderer extends MapRenderer
{


	private final String	error;
	private boolean			rendered	= false;




	public ErrorRenderer(final String error) {
		this.error = error;
	}




	public String getError() {
		return this.error;
	}




	public boolean isRendered() {
		return this.rendered;
	}




	@Override
	public void render( final MapView view, final MapCanvas canvas, final Player player ) {
		if ( this.rendered || this.error == null || this.error.trim().isEmpty() )
			return;

		final MapFont font = new MinecraftFont();
		String text = "";
		final ArrayList<String> textAL = new ArrayList<String>();
		char[] arrayOfChar;
		final int j = ( arrayOfChar = this.error.toCharArray() ).length;
		for ( int i = 0; i < j; i++ )
		{
			final Character c = Character.valueOf( arrayOfChar[i] );

			text = text + c;
			if ( font.getWidth( text ) > 100 )
			{
				text = text.substring( 0, text.length() - 1 );
				textAL.add( text );
				text = "";
			}
		}
		textAL.add( text );

		text = "";
		for ( final String s : textAL )
			text = text + "\n" + s;
		canvas.drawText( 0, 0, font, text );

		this.rendered = true;
	}
}
