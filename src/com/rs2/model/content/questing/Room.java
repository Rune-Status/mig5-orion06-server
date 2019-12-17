package com.rs2.model.content.questing;

import com.rs2.model.Position;
import com.rs2.util.Area;

public class Room{
	
	int id;
	Position middle;
	Area area;
	
	public Room(int id, Position middle, Area area){
		this.id = id;
		this.middle = middle;
		this.area = area;
	}

	public int getId() {
		return id;
	}
	
	public Position getMiddle(){
		return middle;
	}

	public Area getArea() {
		return area;
	}
	
}
