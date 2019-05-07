/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2019. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.computercraft.shared.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class CommandCopy
{
    private static final String PREFIX = "/computercraft copy ";

    private CommandCopy()
    {
    }

    public static void register( CommandDispatcher<ServerCommandSource> registry )
    {
        registry.register( literal( "computercraft" )
            .then( literal( "copy" ) )
            .then( argument( "message", StringArgumentType.greedyString() ) )
            .executes( context -> {
                MinecraftClient.getInstance().keyboard.setClipboard( context.getArgument( "message", String.class ) );
                return 1;
            } )
        );
    }

    public static boolean onClientSendMessage( String message )
    {
        // Emulate the command on the client side
        if( message.startsWith( PREFIX ) )
        {
            MinecraftClient.getInstance().keyboard.setClipboard( message.substring( PREFIX.length() ) );
            return true;
        }

        return false;
    }

    public static TextComponent createCopyText( String text )
    {
        TextComponent name = new TextComponent( text );
        name.getStyle()
            .setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, PREFIX + text ) )
            .setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new TranslatableComponent( "gui.computercraft.tooltip.copy" ) ) );
        return name;
    }
}
