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

/**
 * settings of commands that can be edit with the edit command
 */
public enum CommandSetting {
    MESSAGE("message"),
    COMMAND("command", true),
    DESCRIPTION("description"),
    EXTENDED_DESCRIPTION("extendedDescription"),
    PERMISSION("permission"),
    OTHER_PLAYER_PERMISSION("otherPlayerPermission"),
    COMMANDS_PLAYER("playerCommands", true);

    private final String name;
    private final Boolean isList;

    /**
     * create a CommandSetting
     * @param name name of the setting
     * @param isList is the setting a list/array setting
     */
    CommandSetting(String name, Boolean isList){
        this.name = name;
        this.isList = isList;
    }

    /**
     * create a CommandSetting
     * @param name name of the setting
     */
    CommandSetting(String name){
        this.name = name;
        this.isList = false;
    }

    /**
     * get if the setting is a list/array
     * @return if the setting is a list/array
     */
    public Boolean isList(){
        return this.isList;
    }

    /**
     * get the name of the setting
     * @return the name of the setting
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return util.getStringFromKey("command.param." + name);
    }
}
