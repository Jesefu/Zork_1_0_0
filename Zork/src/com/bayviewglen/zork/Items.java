
package com.bayviewglen.zork;


import java.util.ArrayList;

public class Items {
	
	private double MAX_WEIGHT = 100;
	private double currentWeight;
	
	private ArrayList<Item> itemList;
	
	public Items() {
		itemList = new ArrayList<Item>();
		currentWeight = 0;
	}
	
    public Item get(String itemName) {
    	Item item;
    	String lowerCaseItem = itemName.toLowerCase();
    	for (int count = 0;count < itemList.size();count ++) {
    		item = (Item) itemList.get(count);
    		if (item.getName().toLowerCase().equals(lowerCaseItem)) {
    			return (item);
    		}
    	}
    	return null;
    }
    public void put(Item item) {
    	if (hasBackpack(itemList)==true){
    		MAX_WEIGHT = 400;
    	}
    	if (item.getWeight() + currentWeight < MAX_WEIGHT){
    		itemList.add(item);
    		currentWeight = (currentWeight + item.getWeight()) ;
    	}else{
    		System.out.println("You are too weak to carry this with you...");
    	}
    }
    public void remove(Item item){
    	itemList.remove(item);
    }
    public String getAsString() {
    	String itemString = "";
    	Item item;
    	for (int count = 0;count < itemList.size();count ++) {
    		item = (Item) itemList.get(count);
    		if (count != 0) {
    			itemString = itemString + ", ";
    		}
    		itemString = itemString + item.getName();
    	}
    	return itemString;
    }

}

//public class extends getItems()