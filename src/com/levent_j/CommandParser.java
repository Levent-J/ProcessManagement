package com.levent_j;

/**
 * Created by levent_j on 17-5-31.
 */
public class CommandParser {
    public static final int COMMAND_NULL = 0;
    public static final int COMMAND_CREATE = 1;
    public static final int COMMAND_DESTROY = 2;
    public static final int COMMAND_TIME_OUT = 3;
    public static final int COMMAND_REQUEST_RESOURCE = 4;
    public static final int COMMAND_RELEASE_RESOURCE = 5;

    public int commandType;
    public String param1 = "";
    public String param2 = "";

    private static CommandParser instance;

    private CommandParser() {
    }

    public static CommandParser getInstance(){
        if (instance == null) {
            instance = new CommandParser();
        }
        return instance;
    }

    public Params parserCommand(String command) {
        int index = 0;
        //得到type
        commandType = 0;
        String type = "";
        while (index < command.length()) {
            if (command.charAt(index) != ' ') {
                type += command.charAt(index);
                index++;
            }else {
                break;
            }
        }

        param1 = "";
        index++;
        while (index < command.length()) {
            if (command.charAt(index) != ' ') {
                param1 += command.charAt(index);
                index++;
            }else {
                break;
            }
        }

        param2 = "";
        index++;
        while (index < command.length()) {
            if (command.charAt(index) != ' ') {
                param2 += command.charAt(index);
                index++;
            }else {
                break;
            }
        }

        if (type.equals(Command.COMMAND_CREATE)) {
            commandType = COMMAND_CREATE;
        }else if (type.equals(Command.COMMAND_DESTROY)) {
            commandType = COMMAND_DESTROY;
        }else if (type.equals(Command.COMMAND_TIME_OUT)) {
            commandType = COMMAND_TIME_OUT;
        }else if (type.equals(Command.COMMAND_REQUEST_RESOURCE)) {
            commandType = COMMAND_REQUEST_RESOURCE;
        }else if (type.equals(Command.COMMAND_RELEASE_RESOURCE)) {
            commandType = COMMAND_RELEASE_RESOURCE;
        }else {
            commandType = COMMAND_NULL;
        }

        return new Params(commandType,param1,param2);
    }

    class Params{
        public int commandType;
        public String commandP1;
        public String commandP2;

        public Params(int commandType, String commandP1, String commandP2) {
            this.commandType = commandType;
            this.commandP1 = commandP1;
            this.commandP2 = commandP2;
        }
    }
}
