package eu.blackspectrum.bspsolutions.entities;

import com.massivecraft.massivecore.ps.PS;

public interface IBedBoard
{


	public BSPBed getBedAt( PS ps );




	public void removeBed( BSPBed bed );




	public void removeBedAt( PS ps );




	public void setBedAt( PS ps, BSPBed bed );
}
