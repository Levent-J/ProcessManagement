package com.levent_j;

import java.util.Scanner;

public class Main {

    public static final String DEFAULT_INPUT = ">";

    public static void main(String[] args) {
	// write your code here
        Scanner in = new Scanner(System.in);
        while (true){
            System.out.print(DEFAULT_INPUT);
            String command = in.nextLine();
            CommandParser.Params params = CommandParser.getInstance().parserCommand(command);
            switch (params.commandType){
                case CommandParser.COMMAND_NULL:
                    System.out.println("command null");
                    break;
                case CommandParser.COMMAND_CREATE:
                    System.out.println("command create p1 = " + params.commandP1 + " p2 = " + params.commandP2);
                    break;
                case CommandParser.COMMAND_DESTROY:
                    System.out.println("command destroy p1 = " + params.commandP1);
                    break;
                case CommandParser.COMMAND_TIME_OUT:
                    System.out.println("command to");
                    break;
                case CommandParser.COMMAND_REQUEST_RESOURCE:
                    System.out.println("command create p1 = " + params.commandP1 + " p2 = " + params.commandP2);
                    break;
                case CommandParser.COMMAND_RELEASE_RESOURCE:
                    System.out.println("command rel p1 = ");
                    break;
            }
        }
    }
}
