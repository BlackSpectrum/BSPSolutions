package eu.blackspectrum.bspsolutions.commands;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import com.massivecraft.massivecore.cmd.MassiveCommand;

import eu.blackspectrum.bspsolutions.BSPSolutions;

public class InfoCommand extends MassiveCommand
{


	public InfoCommand(final String alias) {
		this.addAliases( alias );
	}




	@Override
	public void perform() {
		for ( final String s : this.loadResponse( this.getAliases().get( 0 ) ) )
			this.sendMessage( ChatColor.translateAlternateColorCodes( '&', s.trim() ) );
	}




	private List<String> loadResponse( final String cmd ) {

		final File textYml = new File( BSPSolutions.get().getDataFolder(), "infos" + File.separator + cmd + ".yml" );
		if ( !textYml.exists() )
		{
			this.unregister();
			return Collections.emptyList();
		}

		final List<String> lines = YamlConfiguration.loadConfiguration( textYml ).getStringList( "Lines" );
		if ( lines != null )
			return lines;
		else
			return Collections.emptyList();
	}

}
