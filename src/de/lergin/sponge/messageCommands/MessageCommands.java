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

package de.lergin.sponge.messageCommands;

import com.google.inject.Inject;
import de.lergin.sponge.messageCommands.commands.AddCommand;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.Game;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandMapping;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandSpec;
import org.mcstats.Metrics;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * main class
 */

@Plugin(
        id = "confCmd",
        name = "ConfigurationCommands",
        version = "1.0"
)
public class MessageCommands {
    @Inject
    @DefaultConfig(sharedRoot = false)
    public ConfigurationLoader<CommentedConfigurationNode> configManager;

    @Inject
    public Logger logger;

    @Inject
    public Game game;

    @Inject
    public PluginContainer container;

    public ConfigurationNode rootNode;
    public ResourceBundle resourceBundle;
    public ResourceBundle fallBackResourceBundle;
    public HashMap<String, CommandMapping> confCommands = new HashMap<>();
    public HashMap<String, ConfigurationNode> commandMap = new HashMap<>();
    public HashMap<String, CommandSetting> commandSettings = new HashMap<>();
    public CommandMapping editCommand;
    public CommandMapping deleteCommand;


    @Listener
    public void onServerStart(GameStartedServerEvent event){
        util.setPlugin(this);

        fallBackResourceBundle = ResourceBundle.getBundle("resources/translation");

        //load translation
        try {
            File file = new File("config" + File.separator + "confCmd");
            URL[] urls = {file.toURI().toURL()};
            ClassLoader loader = new URLClassLoader(urls);
            resourceBundle = ResourceBundle.getBundle("translation", Locale.getDefault(), loader);
        }catch(Exception ex) {
            resourceBundle = fallBackResourceBundle;
            logger.info(util.getStringFromKey("error.no.custom.translation"));
        }

        //load config
        try {
            rootNode = configManager.load();
        } catch(IOException e) {
            rootNode = configManager.createEmptyNode(ConfigurationOptions.defaults());

            util.saveConfig();
        }



        try {
            Metrics metrics = new Metrics(game, container);
            metrics.start();
        } catch (IOException e) {
            logger.info(util.getStringFromKey("error.no.connection.mcStats"));
        }


        for(CommandSetting commandSetting : CommandSetting.values()){
            commandSettings.put(
                    commandSetting.toString(),
                    commandSetting);
        }


        for ( Map.Entry<Object, ? extends ConfigurationNode> entry :
                rootNode.getNode("commands").getChildrenMap().entrySet()) {
            commandMap.put(entry.getKey().toString(), entry.getValue());
        }


        CommandSpec addCmd = CommandSpec.builder()
                .permission("confCmd.add")
                .description(util.getTextFromJsonByKey("command.add.description"))
                .extendedDescription(util.getTextFromJsonByKey("command.add.extendedDescription"))
                .executor(new AddCommand(this))
                .arguments(
                        GenericArguments.string(Texts.of(
                                util.getStringFromKey("command.param.name")
                        )),
                        GenericArguments.string(Texts.of(
                                util.getStringFromKey("command.param.command")
                        )),
                        GenericArguments.remainingJoinedStrings(Texts.of(
                                util.getStringFromKey("command.param.message")
                        ))
                )
                .build();

        game.getCommandDispatcher().register(this,
                addCmd,
                util.getStringFromKey("command.add.command")
        ).get();


        util.createDeleteCmd();


        util.createEditCmd();

        logger.info(util.getStringFromKey("plugin.initialized"));



        for ( Map.Entry<Object, ? extends ConfigurationNode> entry :
                rootNode.getNode("commands").getChildrenMap().entrySet())
        {
            util.registerCommand(entry.getValue());

            logger.info(
                    String.format(util.getStringFromKey("command.initialized"), entry.getKey())
            );
        }
    }

    @Listener
    public void onServerStop(GameStoppingServerEvent event){
        logger.info(util.getStringFromKey("plugin.stopped"));
    }
}
