package com.bayviewglen.zork;

public class CommandWords {
    public boolean isCommand(String command)
    {
    	if (command == null) {
    		return false;
    	}
    	if ((command.equals("go")) || (command.equals("help")) || (command.equals("quit")) ||
    			(command.equals("take")) || (command.equals("add")) || (command.equals("get")) || 
    			(command.equals("drop")) || (command.equals("remove")) || (command.equals("leave")) ||
    			(command.equals("inventory") || command.equals("i")) ||
    			(command.equals("hit")) || (command.equals("open")) || (command.equals("8549"))) {
    		return true;
    	}
        return false;
    }
	   public void showAll() {
    	System.out.println("go, help, quit");
    }
}
