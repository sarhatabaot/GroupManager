package org.anjocaido.groupmanager.commands;

import org.anjocaido.groupmanager.data.Group;
import org.anjocaido.groupmanager.data.User;
import org.anjocaido.groupmanager.utils.GroupManagerPermissions;
import org.anjocaido.groupmanager.utils.PermissionCheckResult;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sarhatabaot
 */
public abstract class GMCommand implements CommandExecutor {
    int count;
    PermissionCheckResult permissionCheckResult;
    ArrayList<User> removeList;
    List<String> match;
    User auxUser;
    Group auxGroup;
    Group auxGroup2;
    GroupManagerPermissions execCmd;

    abstract boolean validateArguments(CommandSender sender,String[] args);

}
