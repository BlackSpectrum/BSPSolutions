package eu.blackspectrum.bspsolutions.util;

public class MiscUtil
{


	public static int roundToClosest16( final int i ) {
		return (int) ( Math.round( i / 16d ) * 16 );
	}
}
