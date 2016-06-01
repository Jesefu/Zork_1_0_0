
package com.bayviewglen.zork;


import java.util.ArrayList;

public class Items {
	ArrayList<Item> itemList;
	
	public Items() {
		itemList = new ArrayList<Item>();
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
    	itemList.add(item);
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