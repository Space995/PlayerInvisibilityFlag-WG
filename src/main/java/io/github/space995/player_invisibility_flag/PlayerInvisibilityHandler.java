package io.github.space995.player_invisibility_flag;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.FlagValueChangeHandler;
import com.sk89q.worldguard.session.handler.Handler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class PlayerInvisibilityHandler extends FlagValueChangeHandler<Boolean>
{
    static final Factory FACTORY = new Factory();

    static final class Factory extends Handler.Factory<PlayerInvisibilityHandler>
    {
        @Override
        public final PlayerInvisibilityHandler create(@NotNull final Session session)
        {
            return new PlayerInvisibilityHandler(session);
        }
    }

    private static final boolean DEFAULT_VISIBILITY = true;

    PlayerInvisibilityHandler(@NotNull final Session session)
    {
        super(session, PlayerInvisibilityFlag_Plugin.PLAYER_INVISIBILITY_FLAG);
    }

    private static void changeVisibility(@NotNull final Player targetPlayer, boolean setVisible)
    {
        for (final Player player : Bukkit.getOnlinePlayers())
        {
            if (setVisible)
            {
                player.showPlayer(targetPlayer);
            }
            else
            {
                player.hidePlayer(targetPlayer);
            }
        }
    }

    private void updateVisibility(@NotNull final Player targetPlayer, @Nullable final Boolean newValue, @Nullable final Boolean lastvalue, @NotNull final World world)
    {
        if (!getSession().getManager().hasBypass(targetPlayer, world) && newValue != null)
        {
            changeVisibility(targetPlayer, !newValue);
        }
        else if (lastvalue != null && lastvalue)
        {
            changeVisibility(targetPlayer, DEFAULT_VISIBILITY);
        }
    }

    @Override
    protected final void onInitialValue(@NotNull final Player targetPlayer, @NotNull final ApplicableRegionSet set, @Nullable final Boolean value)
    {
        updateVisibility(targetPlayer, value, null, targetPlayer.getWorld());
    }

    @Override
    protected boolean onSetValue(@NotNull final Player targetPlayer, @NotNull final Location from, @NotNull final Location to,
                                 @NotNull final ApplicableRegionSet toSet,
                                 @NotNull final Boolean currentValue, @Nullable final Boolean lastValue,
                                 @NotNull final MoveType moveType)
    {
        updateVisibility(targetPlayer, currentValue, lastValue, to.getWorld());
        return true;
    }

    @Override
    protected boolean onAbsentValue(@NotNull final Player targetPlayer, @NotNull final Location from, @NotNull final Location to,
                                    @NotNull final ApplicableRegionSet toSet, @NotNull final Boolean lastValue, @NotNull final MoveType moveType)
    {
        updateVisibility(targetPlayer, null, lastValue, targetPlayer.getWorld());
        return true;
    }

    /*
    private static void changeVisibility(@NotNull final Player targetPlayer, boolean setVisible)
    {
        final Method setVisibility;

        try
        {
            if (setVisible)
            {
                setVisibility = Player.class.getDeclaredMethod("showPlayer", Player.class);
            }
            else
            {
                setVisibility = Player.class.getDeclaredMethod("hidePlayer", Player.class);
            }

            try
            {
                for (final Player player : Bukkit.getOnlinePlayers())
                {
                    setVisibility.invoke(player, targetPlayer);
                }
            }
            catch (IllegalAccessException | InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
    }
    */
}
