
package com.lagopusempire.playerlookups.commands;

import com.lagopusempire.playerlookups.Permissions;
import com.lagopusempire.playerlookups.PlayerLookups;
import com.lagopusempire.playerlookups.utils.Formatter;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author MrZoraman
 */
public class HelpCommand extends PlCommandBase
{
    public HelpCommand(PlayerLookups plugin)
    {
        super(plugin);
    }
    
    private final List<PlCommandBase> commands = new ArrayList<PlCommandBase>();
    
    @Override
    public boolean execute(CommandSender sender, Player player, String cmdName, String[] preArgs, String[] args)
    {
        sender.sendMessage(new Formatter(plugin.getConfig().getString("strings.help-header"))
                .colorize()
                .toString());
        
        if(Permissions.VIEW_HELP.verify(sender))
        {
            for(int ii = 0; ii < commands.size(); ii++)
            {
                commands.get(ii).printUsage(sender);
            }
        }
        
        return true;
    }
    
    public void addCommand(PlCommandBase command)
    {
        commands.add(command);
    }

    @Override
    public void printUsage(CommandSender sender)
    {
        //do nothing
    }
}
