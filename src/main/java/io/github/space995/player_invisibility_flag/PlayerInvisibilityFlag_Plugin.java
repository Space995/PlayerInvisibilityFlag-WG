package io.github.space995.player_invisibility_flag;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.session.SessionManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerInvisibilityFlag_Plugin extends JavaPlugin
{
    // BooleanFlag with the name "player-invisibility"
    static final Flag<Boolean> PLAYER_INVISIBILITY_FLAG = new BooleanFlag("player-invisibility");

    @Override
    public void onLoad()
    {
        getLogger().info("INFO: Hooking with WorldGuard.");

        final WorldGuardPlugin worldGuardPlugin = WGBukkit.getPlugin();
        final FlagRegistry registry = worldGuardPlugin.getFlagRegistry();

        getLogger().info("OK: Hooked with WorldGuard.");

        try
        {
            // register our flag with the registry
            registry.register(PLAYER_INVISIBILITY_FLAG);
            getLogger().info("OK: Registered 'player-invisibility' flag.");
        }
        catch (FlagConflictException e)
        {
            // print a message to let the server admin know of the conflict
            getLogger().info("FATAL-ERROR: Some other plugin registered a flag by the name 'player-invisibility'.");
            getLogger().info("WARNING: This could cause issues with saved flags in region files!");
        }
    }

    @Override
    public void onEnable()
    {
        final WorldGuardPlugin worldGuardPlugin = WGBukkit.getPlugin();
        final SessionManager sessionManager = worldGuardPlugin.getSessionManager();

        sessionManager.registerHandler(PlayerInvisibilityHandler.FACTORY, null);

        getLogger().info("OK: Registered 'player-invisibility' handler.");
        getLogger().info("FINE: All should work well.");
        getLogger().info("TIP: Please report issues on github.com/Space995/PlayerInvisibilityFlag-WG/issues");
    }
}
