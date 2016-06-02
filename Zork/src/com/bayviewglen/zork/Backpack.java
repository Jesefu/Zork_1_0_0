package com.bayviewglen.zork;

import java.util.ArrayList;

public class Backpack {
	
	public boolean hasBackpack(ArrayList<Item> itemList){
		for(int i=0;i<=itemList.size(); i++){
			if(itemList.contains("Backpack")){
				return true;
				
			}
		}
		return false;
	}
	
	
	
}
