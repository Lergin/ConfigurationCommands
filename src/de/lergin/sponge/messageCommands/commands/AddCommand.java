/*
 * Copyright (c) 2015. Malte 'Lergin' Laukötter
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package de.lergin.sponge.messageCommands.commands;

import de.lergin.sponge.messageCommands.CommandSetting;
import de.lergin.sponge.messageCommands.MessageCommands;
import de.lergin.sponge.messageCommands.util;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import java.util.Arrays;

/**
 * command for adding new commands
 */
public class AddCommand implements CommandExecutor {
    private final MessageCommands plugin;

    public AddCommand(MessageCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        ConfigurationNode node = plugin.rootNode.getNode(
                "commands",
                args.getOne(
                        util.getStringFromKey("command.param.name")
                ).get().toString()
        );


        node.getNode(CommandSetting.MESSAGE.getName()).setValue(
                args.getOne(
                        util.getStringFromKey("command.param.message")
                ).get().toString()
        );

        node.getNode(CommandSetting.COMMAND.getName()).setValue(
                Arrays.asList(
                        args.getOne(
                                util.getStringFromKey("command.param.command")
                        ).get().toString().split(" ")
                )
        );



        util.saveConfig();
        util.registerCommand(node);


        plugin.commandMap.put(node.getKey().toString(), node);

        util.updateEditCmd();
        util.updateDeleteCmd();

        src.sendMessage(util.getTextFromJsonByKey("command.add.success", node.getKey()));


        return CommandResult.success();
    }
}
