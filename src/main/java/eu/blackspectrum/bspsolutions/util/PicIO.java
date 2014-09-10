package eu.blackspectrum.bspsolutions.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class PicIO
{


	public static Image getImage( final short id ) throws Exception {
		final File file = new File( BSPSolutions.get().getDataFolder(), "pics" + File.separator + "mapPic_" + id + ".png" );

		if ( !file.exists() )
			throw new Exception( "ERROR: No picture saved for this map" );

		Image ret;
		try
		{
			ret = ImageIO.read( file );
		}
		catch ( final Exception e )
		{
			throw new Exception( "ERROR: Could not read picture" );
		}

		return ret;
	}




	public static boolean isPicture( final short id ) {
		final File file = new File( BSPSolutions.get().getDataFolder(), "pics" + File.separator + "mapPic_" + id + ".png" );
		return file.exists();
	}




	public static void loadImgageFromURL( final String s, final short id ) throws Exception {
		URL url;
		Image img;
		try
		{
			url = new URL( s );
		}
		catch ( final Exception e )
		{
			throw new Exception( "ERROR: " + s + " is not valid URL!" );
		}
		try
		{
			img = ImageIO.read( url );
			img = img.getScaledInstance( 128, 128, 4 );

			saveImageToDisc( img, id );
		}
		catch ( final Exception e )
		{
			// Only conceal IOExceptions
			if ( e instanceof IOException )
				throw new Exception( "ERROR: Could not read picture from " + url );
			else
				throw e;
		}

	}




	public static void saveImageToDisc( final Image image, final short id ) throws Exception {

		// Convert Image to renderedImage
		final BufferedImage bufferedImage = new BufferedImage( image.getWidth( null ), image.getHeight( null ), BufferedImage.TYPE_INT_RGB );
		final Graphics2D bImgGraphics = bufferedImage.createGraphics();
		bImgGraphics.drawImage( image, null, null );
		bImgGraphics.dispose();

		final RenderedImage renderedImage = bufferedImage;

		final File output = new File( BSPSolutions.get().getDataFolder(), "pics" + File.separator + "mapPic_" + id + ".png" );

		try
		{
			if ( !output.exists() )
				output.createNewFile();

			ImageIO.write( renderedImage, "png", output );
		}
		catch ( final IOException e )
		{
			throw new Exception( "ERROR: Could not save picture for map: " + id + " to the disc." );
		}

	}

}
