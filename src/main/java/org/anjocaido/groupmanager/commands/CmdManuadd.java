package org.anjocaido.groupmanager.commands;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.anjocaido.groupmanager.dataholder.worlds.WorldsHolder;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author sarhatabaot
 */
public class CmdManuadd extends GMCommand {
    private OverloadedWorldHolder dataHolder;
    private WorldsHolder worldsHolder;

    public CmdManuadd(GroupManager plugin) {
        this.worldsHolder = plugin.getWorldsHolder();
    }

    @Override
    boolean validateArguments(CommandSender sender, String[] args) {
        if (args.length != 2 && args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Review your arguments count! (/manuadd <player> <group> | optional [world])");
            return true;
        }
        return false;
    }

    @ACommand(
            name = "manuadd",
            usage = "/manuadd <player> <group> | optional [world]",
            permissions = "groupmanager.manuadd",
            min = 2,
            max = 3
    )
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Validating arguments
        if ((args.length != 2) && (args.length != 3)) {
            sender.sendMessage(ChatColor.RED + "Review your arguments count! (/manuadd <player> <group> | optional [world])");
            return true;
        }

        // Select the relevant world (if specified)
        if (args.length == 3) {
            dataHolder = worldsHolder.getWorldData(args[2]);
            permissionHandler = dataHolder.getPermissionsHandler();
        }

        // Validating state of sender
        if (dataHolder == null || permissionHandler == null) {
            if (!setDefaultWorldHandler(sender))
                return true;
        }

        if ((validateOnlinePlayer) && ((match = validatePlayer(args[0], sender)) == null)) {
            return false;
        }

        if (match != null) {
            auxUser = dataHolder.getUser(match.get(0));
        } else {
            auxUser = dataHolder.getUser(args[0]);
        }
        auxGroup = dataHolder.getGroup(args[1]);
        if (auxGroup == null) {
            sender.sendMessage(ChatColor.RED + "'" + args[1] + "' Group doesnt exist!");
            return false;
        }
        if (auxGroup.isGlobal()) {
            sender.sendMessage(ChatColor.RED + "Players may not be members of GlobalGroups directly.");
            return false;
        }

        // Validating permissions
        if (!isConsole && !isOpOverride && (senderGroup != null ? permissionHandler.inGroup(auxUser.getUUID(), senderGroup.getName()) : false)) {
            sender.sendMessage(ChatColor.RED + "Can't modify a player with the same permissions as you, or higher.");
            return true;
        }
        if (!isConsole && !isOpOverride && (permissionHandler.hasGroupInInheritance(auxGroup, senderGroup.getName()))) {
            sender.sendMessage(ChatColor.RED + "The destination group can't be the same as yours, or higher.");
            return true;
        }
        if (!isConsole && !isOpOverride && (!permissionHandler.inGroup(senderUser.getUUID(), auxUser.getGroupName()) || !permissionHandler.inGroup(senderUser.getUUID(), auxGroup.getName()))) {
            sender.sendMessage(ChatColor.RED + "You can't modify a player involving a group that you don't inherit.");
            return true;
        }

        // Seems OK
        auxUser.setGroup(auxGroup);
        if (!sender.hasPermission("groupmanager.notify.other") || (isConsole))
            sender.sendMessage(ChatColor.YELLOW + "You changed player '" + auxUser.getLastName() + "' group to '" + auxGroup.getName() + "' in world '" + dataHolder.getName() + "'.");

        return true;
    }
}
