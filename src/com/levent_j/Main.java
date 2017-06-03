package com.levent_j;

import java.util.Scanner;

public class Main {

    public static final String DEFAULT_INPUT = ">";

    public static void main(String[] args) {
	// write your code here
        ProcessManager processManager = new ProcessManager();
        Scanner in = new Scanner(System.in);
        while (true){
            System.out.print(DEFAULT_INPUT);
            String command = in.nextLine();
            CommandParser.Params params = CommandParser.getInstance().parserCommand(command);
            switch (params.commandType){
                case CommandParser.COMMAND_NULL:
                    System.out.println("command error");
                    break;
                case CommandParser.COMMAND_CREATE:
                    processManager.createProcess(params.commandP1,Integer.parseInt(params.commandP2));
                    break;
                case CommandParser.COMMAND_DESTROY:
                    processManager.destroyProcess(params.commandP1);
                    break;
                case CommandParser.COMMAND_TIME_OUT:
                    processManager.timeOut();
                    break;
                case CommandParser.COMMAND_REQUEST_RESOURCE:
                    processManager.requestResource(params.commandP1,Integer.parseInt(params.commandP2));
                    break;
                case CommandParser.COMMAND_RELEASE_RESOURCE:
                    processManager.releaseResource(params.commandP1,Integer.parseInt(params.commandP2));
                    break;
                case CommandParser.COMMAND_LIST_ALL_PROCESS:
                    processManager.listAllPrcess();
                    break;
            }
        }
    }
}
