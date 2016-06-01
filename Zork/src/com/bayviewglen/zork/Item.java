package com.bayviewglen.zork;

public class Item {
	private String name;
	private double weight;
	private String description;

	public Item(String n, String desc, double w) {
		this.name = n;
		this.description = desc;
		this.weight = w;
	}
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public double getWeight() {
		return weight;
	}
}
