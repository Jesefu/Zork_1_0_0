package com.bayviewglen.zork;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class Game - the main class of the "Zork" game.
 *
 * Author:  Michael Kolling
 * Version: 1.1
 * Date:    March 2000
 * 
 *  This class is the main class of the "Zork" application. Zork is a very
 *  simple, text based adventure game.  Users can walk around some scenery.
 *  That's all. It should really be extended to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  routine.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates the
 *  commands that the parser returns.
 */

class Game 
{
    private Parser parser;
    private Room currentRoom;
    // This is a MASTER object that contains all of the rooms and is easily accessible.
    // The  will be the name of the room -> no spaces (Use all caps and underscore -> Great Room would have a  of GREAT_ROOM
    // In a hashmap s are case sensitive.
    // masterRoomMap.get("GREAT_ROOM") will return the Room Object that is the Great Room (assuming you have one).
    private HashMap<String, Room> masterRoomMap;
    /* temp test by Ethan */
    private Inventory inventory;
    
    private void initRooms(String fileName) throws Exception{
    	masterRoomMap = new HashMap<String, Room>();
    	Scanner roomScanner;
		try {
			HashMap<String, HashMap<String, String>> exits = new HashMap<String, HashMap<String, String>>();    
			roomScanner = new Scanner(new File(fileName));
			while(roomScanner.hasNext()){
				Room room = new Room();
				// Read the Name
				String roomName = roomScanner.nextLine();
				room.setRoomName(roomName.split(":")[1].trim());
				// Read the Description
				String roomDescription = roomScanner.nextLine();
				room.setDescription(roomDescription.split(":")[1].replaceAll("<br>", "\n").trim());
				// Read the Exits
				String roomExits = roomScanner.nextLine();
				// An array of strings in the format E-RoomName
				String[] rooms = roomExits.split(":")[1].split(",");
				HashMap<String, String> temp = new HashMap<String, String>(); 
				for (String s : rooms){
					temp.put(s.split("-")[0].trim(), s.split("-")[1]);
				}
				
				exits.put(roomName.substring(10).trim().toUpperCase().replaceAll(" ",  "_"), temp);
				// Added by Ethan - loads in the items into the room
				Items items = new Items();
			
				String roomItemsText = roomScanner.nextLine();
				String[] itemSplit = roomItemsText.split(":");
				String itemParts[];
				if (itemSplit.length > 1) {
					itemParts = itemSplit[1].split(",");
				} else {
					itemParts = new String[0];
				}
				if (itemParts.length > 1) {
					int count=0;
					while (count < itemParts.length) {
						items.put(new Item(itemParts[count].trim(), itemParts[count+1].trim(), Double.parseDouble(itemParts[count+2].trim())));
						count = count + 3;
					}
				}
				room.setItems(items);
				// end add by Ethan
				// This puts the room we created (Without the exits in the masterMap)
				masterRoomMap.put(roomName.toUpperCase().substring(10).trim().replaceAll(" ",  "_"), room);
				
				
				
				// Now we better set the exits.
			}
			
			for (String key : masterRoomMap.keySet()){
				Room roomTemp = masterRoomMap.get(key);
				HashMap<String, String> tempExits = exits.get(key);
				for (String s : tempExits.keySet()){
					// s = direction
					// value is the room.
					
					String roomName2 = tempExits.get(s.trim());
					Room exitRoom = masterRoomMap.get(roomName2.toUpperCase().replaceAll(" ", "_"));
					roomTemp.setExit(s.trim().charAt(0), exitRoom);
					
				}
				
				
			}
    	
			roomScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }    

    /**
     * Create the game and initialise its internal map.
     */
    public Game() {
        try {
			initRooms("data/rooms2.dat");
			currentRoom = masterRoomMap.get("COMPUTER_SCIENCE_ROOM");
			/* temp test for Ethan */
			/*Items newItems = new Items();
			newItems.put(new Item("Sword", "It is a large broadsword", 10.0));
			newItems.put(new Item("Wat", "It is a gold ", 1.0));
			newItems.put(new Item("Magic Wand", "It has writing on it that says Yoho", 3.0));
			newItems.put(new Item("Troll","An angry old troll", 1000.0));			
			currentRoom.setItems(newItems);*/
			inventory = new Inventory();


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        parser = new Parser();
    }

  

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished)
        {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to Zork!");
        System.out.println("Zork is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.longDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * If this command ends the game, true is returned, otherwise false is
     * returned.
     */
    private boolean processCommand(Command command) 
    {
        if(command.isUnknown())
        {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help"))
            printHelp();
        else if (commandWord.equals("go"))
            goRoom(command);
        else if (commandWord.equals("quit"))
        {
            if(command.hasSecondWord())
                System.out.println("Quit what?");
            else
                return true;  // signal that we want to quit
        }else if (commandWord.equals("eat")){
        	System.out.println("Do you really think you should be eating at a time like this?");
        }
        else if ((commandWord.equals("take")) || (commandWord.equals("add")) || (commandWord.equals("get"))){
        	addItem(command);
        }
        else if ((commandWord.equals("drop")) || (commandWord.equals("remove")) || (commandWord.equals("leave"))){
        	dropItem(command);
        }
        else if (commandWord.equals("inventory") || commandWord.equals("i")){
        	showInventory();
        }
        else if (commandWord.equals("hit")) {
        	hit(command);
        }  
        else if (commandWord.equals("open")){
        	open(command);
        }
        return false;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at Monash Uni, Peninsula Campus.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord())
        {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.nextRoom(direction);

        if (nextRoom == null)
            System.out.println("You can't go that way");
        else 
        {
            currentRoom = nextRoom;
            System.out.println(currentRoom.longDescription());
        }
    }
    
    private void look(){
    	System.out.println(currentRoom.longDescription());
    }
    
    private void addItem(Command command){
    	String secondWord = command.getSecondWord();
    	Items items = currentRoom.getRoomItems();
    	Item item = items.get(secondWord);
    	if (item == null)
    		System.out.println("That item isnt in this room");
    	else{
    		System.out.println("Item has been added to the inventory");
    		inventory.put(item);
    		items.remove(item);
    	}
    	
    }
    private void dropItem(Command command){
    	String secondWord = command.getSecondWord();
    	Item inventoryItem = inventory.get(secondWord);
    	Items roomItems = currentRoom.getRoomItems();
    	if (inventoryItem == null)
    		System.out.println("You can't drop an item that you don't have");
    	else{
    		System.out.println("The item has been dropped");
    		inventory.remove(inventoryItem);
    		roomItems.put(inventoryItem);
    	}
    }
    private void showInventory(){
    	String i = inventory.getAsString();
    	System.out.println("inventory: " + i);
    }
    private void open(Command command){
    	Items roomItems = currentRoom.getRoomItems();
    	String secondWord = command.getSecondWord();
    	String thirdWord = command.getThirdWord();

    	if (currentRoom.getRoomName().toLowerCase().equals("stairs")) {
    		Item item = null;
    		Item item2 = null;
    		if ((secondWord.toLowerCase().equals("upstairs")) && (thirdWord.toLowerCase().equals("door"))) {
    			item = roomItems.get("Upstairs Door");
    			item2 = inventory.get("Key");
    		} else if ((secondWord.toLowerCase().equals("basement")) && (thirdWord.toLowerCase().equals("door"))) {
    			item = roomItems.get("Basement Door");
    			System.out.println("What is the password?"); 
				Command command2 = parser.getCommand();
				String password = "8549"; 
				String firstWord = command2.getCommandWord();
				if (firstWord != null) {
					if (firstWord.equals(password)){
						roomItems.remove(item);
						try {
							currentRoom.setExit('D', masterRoomMap.get("STAIRS_2"));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("The door was opened!");
						look();
						return;
					}else{
					}
				} else {
				}
    		}
	    	if (item == null){
	    		System.out.println("You can't go that way");
	    		return;
	    	}
	    	if (item2 == null){
	    		System.out.println("The door is locked, you can't get through");
	    		return;
	    	}
	    	else{
	    		System.out.println("Used key to open the door");
				roomItems.remove(item);
				try {
					currentRoom.setExit('U', masterRoomMap.get("STAIRS_3"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				look();
			
	    	}
    	}
    }
    	
    private void hit(Command command) {
    	String secondWord = command.getSecondWord();
    	String fourthWord = command.getFourthWord();
    	Items roomItems = currentRoom.getRoomItems();
    	Item inventorySword = inventory.get("Sword");
    	Item troll = roomItems.get("Troll");
    	if (secondWord == null){
    		System.out.println("Hit what?");
    		return;
    	}
    	if (troll == null){
    		System.out.println("There are no trolls here");
    		return;
    	}
    	if(fourthWord == null){
    		System.out.println("With what?");
    		return;
    	}
    	if(!secondWord.equals("troll")){
    		System.out.println("You can't hit that!");
    		return;
    	}
    	if(!fourthWord.equals("sword")){
    		System.out.println("You can't hit him with that!");
    		return;
    	}
    	if(inventorySword == null){
    		System.out.println("You don't have a sword");
    		return;
    	}
    	else {
    		System.out.println("the troll is dead");
    		roomItems.remove(troll);
    	}
    }
  
}