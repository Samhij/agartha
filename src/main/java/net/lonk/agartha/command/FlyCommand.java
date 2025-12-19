package net.lonk.agartha.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class FlyCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("fly")
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    return toggleFlight(player, ctx.getSource());
                })
                .then(Commands.argument("target", EntityArgument.player())
                        .requires(src -> src.hasPermission(2))
                        .executes(ctx -> {
                            ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
                            return toggleFlight(target, ctx.getSource());
                        })
                )
        );
    }

    private static int toggleFlight(ServerPlayer player, CommandSourceStack source) {
        // Toggle mayfly; if disabling, also stop flying
        boolean newMayfly = !player.getAbilities().mayfly;
        player.getAbilities().mayfly = newMayfly;
        if (!newMayfly) {
            player.getAbilities().flying = false;
        }
        // Sync abilities to client
        player.onUpdateAbilities();

        String msg = (newMayfly ? "enabled" : "disabled") + " for " + player.getName().getString();
        source.sendSuccess(() -> Component.literal("Flight " + msg), true);
        return 1;
    }
}
