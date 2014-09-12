package eu.blackspectrum.bspsolutions.commands;

import java.util.Arrays;

import com.massivecraft.factions.FPerm;
import com.massivecraft.factions.Perm;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.cmd.arg.ARFPerm;
import com.massivecraft.factions.cmd.arg.ARFaction;
import com.massivecraft.factions.cmd.arg.ARRel;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.cmd.arg.ARBoolean;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

public class FactionsPermCommand extends FCommand
{


	private static final Rel[]		RELS				= { Rel.ENEMY, Rel.NEUTRAL, Rel.ALLY, Rel.TRUCE };
	private static final FPerm[]	REL_DEPENDENT_PERMS	= { FPerm.BUTTON, FPerm.LEVER, FPerm.PAINBUILD, FPerm.CONTAINER };
	private static final FPerm[]	UNUSED_PERMS		= { FPerm.SETHOME, FPerm.WITHDRAW };




	public FactionsPermCommand() {
		// Aliases
		this.addAliases( "perm" );

		// Args
		this.addOptionalArg( "faction", "you" );
		this.addOptionalArg( "perm", "all" );
		this.addOptionalArg( "relation", "read" );
		this.addOptionalArg( "yes/no", "read" );
		this.setErrorOnToManyArgs( false );

		// Requirements
		this.addRequirements( ReqFactionsEnabled.get() );
		this.addRequirements( ReqHasPerm.get( Perm.PERM.node ) );
	}




	@Override
	public void perform() {
		final Faction faction = this.arg( 0, ARFaction.get( this.usenderFaction ), this.usenderFaction );
		if ( faction == null )
			return;

		if ( !this.argIsSet( 1 ) )
		{
			this.msg( Txt.titleize( "Perms for " + faction.describeTo( this.usender, true ) ) );
			this.msg( FPerm.getStateHeaders() );
			for ( final FPerm perm : FPerm.values() )
			{
				// Dont show unused Perms
				if ( Arrays.asList( UNUSED_PERMS ).contains( perm ) )
					continue;

				this.msg( perm.getStateInfo( faction.getPermittedRelations( perm ), true ) );
			}
			return;
		}

		final FPerm perm = this.arg( 1, ARFPerm.get() );
		if ( perm == null || Arrays.asList( UNUSED_PERMS ).contains( perm ) )
			return;

		if ( !this.argIsSet( 2 ) )
		{
			this.msg( Txt.titleize( "Perm for " + faction.describeTo( this.usender, true ) ) );
			this.msg( FPerm.getStateHeaders() );
			this.msg( perm.getStateInfo( faction.getPermittedRelations( perm ), true ) );
			return;
		}

		// Do the sender have the right to change perms for this faction?
		if ( !FPerm.PERMS.has( this.usender, faction, true ) )
			return;

		final Rel rel = this.arg( 2, ARRel.get() );
		if ( rel == null )
			return;

		if ( !this.canEditPermForRel( perm, rel ) && !( this.usender.isConsole() || this.usender.isUsingAdminMode() ) )
		{
			this.msg( "<b>You cant edit <h>%s <b>permission for <h>%s<b>.", Txt.getNicedEnum( perm ), Txt.getNicedEnum( rel ) );
			return;
		}

		if ( !this.argIsSet( 3 ) )
		{
			this.msg( "<b>Should <h>%s <b>have the <h>%s <b>permission or not?\nYou must <h>add \"yes\" or \"no\" <b>at the end.",
					Txt.getNicedEnum( rel ), Txt.getNicedEnum( perm ) );
			return;
		}

		final Boolean val = this.arg( 3, ARBoolean.get(), null );
		if ( val == null )
			return;

		// Do the change
		// System.out.println("setRelationPermitted perm "+perm+", rel "+rel+", val "+val);
		faction.setRelationPermitted( perm, rel, val );

		// The following is to make sure the leader always has the right to
		// change perms if that is our goal.
		if ( perm == FPerm.PERMS && FPerm.PERMS.getDefault( faction ).contains( Rel.LEADER ) )
			faction.setRelationPermitted( FPerm.PERMS, Rel.LEADER, true );

		this.msg( Txt.titleize( "Perm for " + faction.describeTo( this.usender, true ) ) );
		this.msg( FPerm.getStateHeaders() );
		this.msg( perm.getStateInfo( faction.getPermittedRelations( perm ), true ) );
	}




	private boolean canEditPermForRel( final FPerm perm, final Rel rel ) {
		return !( Arrays.asList( REL_DEPENDENT_PERMS ).contains( perm ) && Arrays.asList( RELS ).contains( rel ) );
	}
}
