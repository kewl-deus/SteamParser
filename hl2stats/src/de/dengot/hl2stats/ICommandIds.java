package de.dengot.hl2stats;

/**
 * Interface defining the application's command IDs.
 * Key bindings can be defined for specific commands.
 * To associate an action with a command, use IAction.setActionDefinitionId(commandId).
 *
 * @see org.eclipse.jface.action.IAction#setActionDefinitionId(String)
 */
public interface ICommandIds {

    public static final String CMD_PARSE = "de.dengot.hl2stats.actions.ParseLogfile";
    public static final String CMD_OPEN = "de.dengot.hl2stats.open";
    
}
