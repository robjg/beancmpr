package org.oddjob.beancmpr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SharedTestData {

	private final List<Fruit> fruitX = new ArrayList<>();
	private final List<Fruit> fruitY = new ArrayList<>();
		
	private final Map<Long, Fruit> fruitMapX = new HashMap<>();
	private final Map<Long, Fruit> fruitMapY = new HashMap<>();
	
	
	public static class Fruit {
		
		private long id;
		private String type;
		private int quantity;
		private String colour;
		private double price;
		
		public long getId() {
			return id;
		}
		
		public void setId(long id) {
			this.id = id;
		}
		
		public String getType() {
			return type;
		}
		
		public void setType(String name) {
			this.type = name;
		}
		
		public int getQuantity() {
			return quantity;
		}
		
		public void setQuantity(int age) {
			this.quantity = age;
		}
		
		public String getColour() {
			return colour;
		}
		
		public void setColour(String snack) {
			this.colour = snack;
		}
		
		public double getPrice() {
			return price;
		}
		
		public void setPrice(double price) {
			this.price = price;
		}
	}
	

	{		
		Fruit fruit = new Fruit();		
		fruit.setId(1);
		fruit.setType("Apple");
		fruit.setQuantity(4);
		fruit.setColour("green");
		fruit.setPrice(54.56);
		
		fruitX.add(fruit);
		fruitMapX.put(fruit.getId(), fruit);
	}
	
	{
		Fruit fruit = new Fruit();		
		fruit.setId(2);
		fruit.setType("Banana");
		fruit.setQuantity(3);
		fruit.setColour("yellow");
		fruit.setPrice(23.24);
		
		fruitX.add(fruit);
		fruitMapX.put(fruit.getId(), fruit);
	}
	
	{
		Fruit fruit = new Fruit();		
		fruit.setId(5);
		fruit.setType("Orange");
		fruit.setQuantity(2);
		fruit.setColour("orange");
		fruit.setPrice(70.95);
		
		fruitX.add(fruit);
		fruitMapX.put(fruit.getId(), fruit);
	}
	
	{
		Fruit fruit = new Fruit();		
		fruit.setId(1);
		fruit.setType("Apple");
		fruit.setQuantity(4);
		fruit.setColour("red");
		fruit.setPrice(54.57);
		
		fruitY.add(fruit);
		fruitMapY.put(fruit.getId(), fruit);
	}
	
	{
		Fruit fruit = new Fruit();		
		fruit.setId(2);
		fruit.setType("Banana");
		fruit.setQuantity(4);
		fruit.setColour("yellow");
		fruit.setPrice(23.25);
		
		fruitY.add(fruit);
		fruitMapY.put(fruit.getId(), fruit);
	}
	
	{
		Fruit fruit = new Fruit();		
		fruit.setId(3);
		fruit.setType("Orange");
		fruit.setQuantity(2);
		fruit.setColour("orange");		
		fruit.setPrice(80.05);
		
		fruitY.add(fruit);
		fruitMapY.put(fruit.getId(), fruit);
	}	
	
	public Fruit[] getArrayFruitX() {
		return fruitX.toArray(new Fruit[0]);
	}
	
	public Fruit[] getArrayFruitY() {
		return fruitY.toArray(new Fruit[0]);
	}

	public List<Fruit> getListFruitX() {
		return fruitX;
	}
	
	public List<Fruit> getListFruitY() {
		return fruitY;
	}
	
	public Map<Long, Fruit> getFruitMapX() {
		return fruitMapX;
	}
	
	public Map<Long, Fruit> getFruitMapY() {
		return fruitMapY;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
