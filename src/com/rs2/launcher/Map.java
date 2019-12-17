package com.rs2.launcher;

import java.awt.*;

import javax.swing.JPanel;

import com.rs2.model.World;
import com.rs2.model.players.Player;
import com.rs2.util.FileOperations;

import java.awt.image.BufferedImage;
import java.io.*;

import java.util.Scanner;
import java.util.Vector;
  
public class Map extends JPanel {  
   
   	public int scale = 1;
	public int sizeX = 2368*scale;
	public int sizeY = 8000*scale;
	
	MapIndex mapIndex = new MapIndex();
	//Item item = new Item();
	//Npc npc = new Npc();
	
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	static Image[][] regionImage;
	//static Image[] mapDot = new Image[5];
	
	static int[] regionX;
	static int[] regionY;
	//static int[] regionType;
	//static int[] regionHasKeys;
	
	public int height = 0;
	
	public int mode = 0;
	
	public int currentX;
	public int currentY;
	public int currentZ;
	
	public int selectionStage = 0;
	  
	int xA[] = new int[2];
	int yA[] = new int[2];
	
	/*int areaId[] = new int[2000];
	//int areaType[] = new int[2000];
	int areaPriority[] = new int[2000];
	String areaName[] = new String[2000];
	int areaMusic[] = new int[2000];
	int areaX1[] = new int[2000];
	int areaY1[] = new int[2000];
	int areaX2[] = new int[2000];
	int areaY2[] = new int[2000];*/
	
	private boolean runnedOnce = false;
	
	/*int maxNpcs = 2000;
   
	int npcId[] = new int[maxNpcs];
	int npcType[] = new int[maxNpcs];
	int npcX[] = new int[maxNpcs];
	int npcY[] = new int[maxNpcs];
	int npcZ[] = new int[maxNpcs];
	int npcFace[] = new int[maxNpcs];
	int npcX1[] = new int[maxNpcs];
	int npcY1[] = new int[maxNpcs];
	int npcX2[] = new int[maxNpcs];
	int npcY2[] = new int[maxNpcs];
	String npcDesc[] = new String[maxNpcs];
	int npcArea[] = new int[maxNpcs];
	
	int maxItems = 2000;
   
	int itemId[] = new int[maxItems];
	int itemAmount[] = new int[maxItems];
	int itemType[] = new int[maxItems];
	int itemX[] = new int[maxItems];
	int itemY[] = new int[maxItems];
	int itemZ[] = new int[maxItems];
	String itemDesc[] = new String[maxItems];
	int itemArea[] = new int[maxItems];
	
	int maxInfoAreas = 200;
	int areaIdInfo[] = new int[maxInfoAreas];
	String areaNameInfo[] = new String[maxInfoAreas];*/
	
	/*public void saveAreaInfo(){
		BufferedWriter bw = null;
		File f = new File("./Data/Areas.dat");
		f.delete();
		int j1 = maxInfoAreas;	
		for (int i2 = 0; i2 < j1; i2++) {
			if(areaIdInfo[i2] == 1){
				try {
						bw = new BufferedWriter(new FileWriter("./Data/Areas.dat", true));
						String name = areaNameInfo[i2].replaceAll(" ", "_");
						bw.write(i2+" "+name);
						bw.newLine();
						bw.flush();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					} finally {
						if (bw != null) try {
							bw.close();
					} catch (IOException ioe2) {
					}
				}
			}
		}
		System.out.println("saved");
	}
	
	public void loadAreaInfo(){
		if(FileOperations.FileExists("./Data/Areas.dat")){
			File file = new File("./Data/Areas.dat");	
			int id = 0;
			String name = "";
			try {
				Scanner scanner = new Scanner(file);
				while (scanner.hasNextLine()) {
					if(runnedOnce){
						String line = scanner.nextLine();
					}
					if(scanner.hasNext()){
						id = scanner.nextInt();
						name = scanner.next().replaceAll("_", " ");
						runnedOnce = true;
						//save(id, type, priority, name, music, x1, y1, x2, y2);
						addAreaInfo(id, name);
					}else{
						runnedOnce = false;
					}
				try{
				}catch(Exception ex){}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("File not found!");
			}
		}
	}
	
	public void removeAreaInfo(int index){
		areaIdInfo[index] = 0;
		areaNameInfo[index] = "";
	}
	
	public void addAreaInfo(int index, String name){
		areaIdInfo[index] = 1;
		areaNameInfo[index] = name;
	}
	
	public int checkNextAreaInfo(){
		int j = 0;
		boolean areaFound = false;
		while(!areaFound){
			if(areaIdInfo[j] == 1){
				j++;
			}
			if(areaIdInfo[j] == 0){
				areaFound = true;
			}
		}
		return j;
	}
	
	public void setItemDefs(){
		int j1 = maxItems;	
		for (int i2 = 0; i2 < j1; i2++) {
			itemId[i2] = -1;
		}
	}
	
	public void removeItem(int index){
		itemId[index] = -1;
		itemAmount[index] = 0;
		itemType[index] = 0;
		itemX[index] = 0;
		itemY[index] = 0;
		itemZ[index] = 0;
		itemDesc[index] = "";
		itemArea[index] = 0;
		repaint();
	}
	
	public void addItem(int index, int id, int amount, int type, int x, int y, int z, String description, int area){
		String desc = description.replaceAll("_", " ");
		desc = desc.replace(getItemName(id)+" ", "");
		desc = desc.replace("(", "");
		desc = desc.replace(")", "");
		itemId[index] = id;
		itemAmount[index] = amount;
		itemType[index] = type;
		itemX[index] = x;
		itemY[index] = y;
		itemZ[index] = z;
		itemDesc[index] = desc;
		itemArea[index] = area;
		repaint();
	}
	
	public int checkNextItem(){
		int j = 0;
		boolean itemFound = false;
		while(!itemFound){
			if(itemId[j] >= 0){
				j++;
			}
			if(itemId[j] == -1){
				itemFound = true;
			}
		}
		return j;
	}
	
	public void setNpcDefs(){
		int j1 = maxNpcs;	
		for (int i2 = 0; i2 < j1; i2++) {
			npcId[i2] = -1;
		}
	}
	
	public void removeNpc(int index){
		npcId[index] = -1;
		npcType[index] = 0;
		npcX[index] = 0;
		npcY[index] = 0;
		npcZ[index] = 0;
		npcFace[index] = 0;
		npcX1[index] = 0;
		npcY1[index] = 0;
		npcX2[index] = 0;
		npcY2[index] = 0;
		npcDesc[index] = "";
		npcArea[index] = 0;
		repaint();
	}
	
	public void addNpc(int index, int id, int type, int x, int y, int z, int face, int x1, int y1, int x2, int y2, String description, int area){
		String desc = description.replaceAll("_", " ");
		desc = desc.replace(getNpcName(id)+" ", "");
		desc = desc.replace("(", "");
		desc = desc.replace(")", "");
		npcId[index] = id;
		npcType[index] = type;
		npcX[index] = x;
		npcY[index] = y;
		npcZ[index] = z;
		npcFace[index] = face;
		npcX1[index] = x1;
		npcY1[index] = y1;
		npcX2[index] = x2;
		npcY2[index] = y2;
		npcDesc[index] = desc;
		npcArea[index] = area;
		repaint();
	}
	
	public void saveNpcs(){
		BufferedWriter bw = null;
		File f = new File("./Data/Npc spawn edit.cfg");
		f.delete();
		int j1 = maxNpcs;
		int area = -1;
		try {
			bw = new BufferedWriter(new FileWriter("./Data/Npc spawn edit.cfg", true));
			bw.write("//-----npcID--type---face--spawnX--spawnY--spawnZ--minX----minY----maxX----maxY----description");
			bw.newLine();
			for (int i2 = 0; i2 < j1; i2++) {
				if(npcId[i2] != -1) {
					String desc = getNpcName(npcId[i2]).replaceAll(" ", "_");
					String desc2 = npcDesc[i2].replaceAll(" ", "_");
					if(desc2.equals("")){
					}else{
						desc += "_("+desc2+")";
					}
					String areaName = areaNameInfo[npcArea[i2]];
					if(area != npcArea[i2]){
						bw.newLine();
						bw.write("["+areaName+"]");
						bw.newLine();
						area = npcArea[i2];
					}
					bw.write("spawn = "+npcId[i2]+"\t"+npcType[i2]+"\t"+npcFace[i2]+"\t"+npcX[i2]+"\t"+npcY[i2]+"\t"+npcZ[i2]+"\t"+npcX1[i2]+"\t"+npcY1[i2]+"\t"+npcX2[i2]+"\t"+npcY2[i2]+"\t"+desc);
					bw.newLine();
				}
			}
			bw.write("[ENDOFSPAWNLIST]");
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null) try {
				bw.close();
			} catch (IOException ioe2) {
			}
		}	
		System.out.println("saved");
	}
	
	public boolean loadNpcSpawn() {
		String FileName = "Data/Npc spawn edit.cfg";
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[11];
		boolean EndOfFile = false;
		int ReadMode = 0;
		int count = 0;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./"+FileName));
		} catch(FileNotFoundException fileex) {
			System.out.println(FileName+": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch(IOException ioexception) {
			System.out.println(FileName+": error loading file.");
			return false;
		}
		int areaId = 0;
		while(EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			for (int id = 0; id < areaNameInfo.length; id++) {
				if(areaNameInfo[id] != null){
					if(areaNameInfo[id] != ""){
						String areaName = areaNameInfo[id];
						if (line.equals("["+areaName+"]")) {
							areaId = id;
						}
					}
				}
			}
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");	
				if (token.equals("spawn")) {
					addNpc(count, Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[3]), Integer.parseInt(token3[4]), Integer.parseInt(token3[5]), Integer.parseInt(token3[2]), Integer.parseInt(token3[6]), Integer.parseInt(token3[7]), Integer.parseInt(token3[8]), Integer.parseInt(token3[9]), token3[10], areaId);
					count++;
				}
			} else {
				if (line.equals("[ENDOFSPAWNLIST]")) {
					try { characterfile.close(); } catch(IOException ioexception) { }
					return true;
				}
			}
			try {
				line = characterfile.readLine();
			} catch(IOException ioexception1) { EndOfFile = true; }
		}
		try { characterfile.close(); } catch(IOException ioexception) { }
		return false;
	}
	
	public void packNpcs(){
		File f = new File("./Data/Npc spawn.dat");
		f.delete();
		int j1 = 0;
		for (int i2 = 0; i2 < maxNpcs; i2++) {
			if(npcId[i2] != -1)
				j1++;
		}		
        try {
			Packer stream2 = new Packer(new FileOutputStream("./Data/Npc-spawn.dat"));
			stream2.writeShort(j1);
			for (int i2 = 0; i2 < maxNpcs; i2++) {
				if(npcId[i2] != -1) {
					stream2.writeShort(npcId[i2]);
					stream2.writeUnsignedByte(npcType[i2]);
					stream2.writeUnsignedByte(npcFace[i2]);
					stream2.writeShort(npcX[i2]);
					stream2.writeShort(npcY[i2]);
					stream2.writeUnsignedByte(npcZ[i2]);
					stream2.writeShort(npcX1[i2]);
					stream2.writeShort(npcY1[i2]);
					stream2.writeShort(npcX2[i2]);
					stream2.writeShort(npcY2[i2]);
				}
			}
			stream2.close();
        } catch (Exception e) {

        }
		System.out.println("Npcs packed.");
	}
   
    public void saveItems(){
		BufferedWriter bw = null;
		File f = new File("./Data/Item spawn edit.cfg");
		f.delete();
		int j1 = maxItems;
		int area = -1;
		try {
			bw = new BufferedWriter(new FileWriter("./Data/Item spawn edit.cfg", true));
			bw.write("//-----itemID--amount---type---spawnX--spawnY--spawnZ---description");
			bw.newLine();
			for (int i2 = 0; i2 < j1; i2++) {
				if(itemId[i2] != -1) {
					String desc = getItemName(itemId[i2]).replaceAll(" ", "_");
					String desc2 = itemDesc[i2].replaceAll(" ", "_");
					if(desc2.equals("")){
					}else{
						desc += "_("+desc2+")";
					}
					String areaName = areaNameInfo[itemArea[i2]];
					if(area != itemArea[i2]){
						bw.newLine();
						bw.write("["+areaName+"]");
						bw.newLine();
						area = itemArea[i2];
					}
					bw.write("spawn = "+itemId[i2]+"\t"+itemAmount[i2]+"\t"+itemType[i2]+"\t"+itemX[i2]+"\t"+itemY[i2]+"\t"+itemZ[i2]+"\t"+desc);
					bw.newLine();
				}
			}
			bw.write("[ENDOFSPAWNLIST]");
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null) try {
				bw.close();
			} catch (IOException ioe2) {
			}
		}	
		System.out.println("saved");
	}
	
	public boolean loadItemSpawn() {
		String FileName = "Data/Item spawn edit.cfg";
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[7];
		boolean EndOfFile = false;
		int ReadMode = 0;
		int count = 0;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./"+FileName));
		} catch(FileNotFoundException fileex) {
			System.out.println(FileName+": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch(IOException ioexception) {
			System.out.println(FileName+": error loading file.");
			return false;
		}
		int areaId = 0;
		while(EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			for (int id = 0; id < areaNameInfo.length; id++) {
				if(areaNameInfo[id] != null){
					if(areaNameInfo[id] != ""){
						String areaName = areaNameInfo[id];
						if (line.equals("["+areaName+"]")) {
							areaId = id;
						}
					}
				}
			}
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("spawn")) {
					addItem(count, Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2]), Integer.parseInt(token3[3]), Integer.parseInt(token3[4]), Integer.parseInt(token3[5]), token3[6], areaId);
					count++;
				}
			} else {
				if (line.equals("[ENDOFSPAWNLIST]")) {
					try { characterfile.close(); } catch(IOException ioexception) { }
					return true;
				}
			}
			try {
				line = characterfile.readLine();
			} catch(IOException ioexception1) { EndOfFile = true; }
		}
		try { characterfile.close(); } catch(IOException ioexception) { }
		return false;
	}
	
	public void packItems(){
		File f = new File("./Data/Item spawn.dat");
		f.delete();
		int j1 = 0;
		for (int i2 = 0; i2 < maxItems; i2++) {
			if(itemId[i2] != -1)
				j1++;
		}
		try {
			Packer stream2 = new Packer(new FileOutputStream("./Data/Item-spawn.dat"));
			stream2.writeShort(j1);
			for (int i2 = 0; i2 < maxItems; i2++) {
				if(itemId[i2] != -1) {
					stream2.writeShort(itemId[i2]);
					stream2.writeShort(itemAmount[i2]);
					stream2.writeUnsignedByte(itemType[i2]);
					stream2.writeShort(itemX[i2]);
					stream2.writeShort(itemY[i2]);
					stream2.writeUnsignedByte(itemZ[i2]);
				}
			}	
			stream2.close();
        } catch (Exception e) {

        }
		System.out.println("Items packed.");
	}
   
	public int checkNextNpc(){
		int j = 0;
		boolean npcFound = false;
		while(!npcFound){
			if(npcId[j] >= 0){
				j++;
			}
			if(npcId[j] == -1){
				npcFound = true;
			}
		}
		return j;
	}*/
   
   	public Map() {
		setFocusable(true);
		setBackground(Color.black);
		setDoubleBuffered(true);
		setSize(sizeX, sizeY);
		init();
    }
   
   	public void init(){
		try{
			mapIndex.unpack();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		regionImage = new Image[mapIndex.regionCount][4];
		regionX = new int[mapIndex.regionCount];
		regionY = new int[mapIndex.regionCount];
		//regionType = new int[mapIndex.regionCount];
		//regionHasKeys = new int[mapIndex.regionCount];
		loadImages();
	}
	
	/*public void loadItems(){
		item.unpack();
	}
	
	public void loadNpcs(){
		npc.unpack();
	}
	
	public String getNpcName(int id){
		return npc.dNam[id];
	}
	
	public int getNpclvl(int id){
		return npc.dCbLvl[id];
	}
	
	public String getItemName(int id){
		return item.dNam[id];
	}
	
	public int grabInfo(int x, int y, int z){
		if(mode == 4){
			for(int index = 0; index < maxNpcs; index++){
				if(npcX[index] == x && npcY[index] == y && npcZ[index] == z){
					return index;
				}
			}
			return -1;
		}
		if(mode == 5){
			for(int index = 0; index < maxItems; index++){
				if(itemX[index] == x && itemY[index] == y && itemZ[index] == z){
					return index;
				}
			}
			return -1;
		}
		return -1;
	}*/
	
	//public int xteaCount = 0;
	
	public void loadImages(){
		//for(int z = 0; z < 4; z++)
		int z = 0;
		for(int id = 0; id < mapIndex.regionCount; id++){
			if(FileOperations.FileExists("./data/launcher/Sprites/"+z+"/"+id+" "+mapIndex.regionId[id]+".png")){
				//System.out.println("Loading image: "+id);
				Image image = toolkit.getImage("./data/launcher/Sprites/"+z+"/"+id+" "+mapIndex.regionId[id]+".png");
				image = ImageHandler.makeColorTransparent(image, new Color(0xff00ff));
				//regionImage[id] = ImageHandler.toBufferedImage(image);
				regionImage[id][z] = image;
				region2Coords(mapIndex.regionId[id], id);
			}
			/*if(z == 0)
			if(FileOperations.FileExists("./Xtea/"+mapIndex.regionId[id]+".txt")){
				regionHasKeys[id] = 1;
				xteaCount++;
			}*/
		}
		/*for(int id = 0; id < 5; id++){
			if(FileOperations.FileExists("./Sprites/Dots/"+id+".png")){
				Image image = toolkit.getImage("./Sprites/Dots/"+id+".png");
				image = ImageHandler.makeColorTransparent(image, new Color(0xff00ff));
				mapDot[id] = image;
			}
		}*/		
	}
	
	/*public void checkArea(){
		int x1 = xA[0];
		int y1 = yA[0];
		int x2 = xA[1];
		int y2 = yA[1];
		if(x1 > x2){
			xA[0] = x2;
			xA[1] = x1;
		}
		if(y1 > y2){
			yA[0] = y2;
			yA[1] = y1;
		}
	}
	
	public int checkNextArea(){
		int j = 0;
		boolean areaFound = false;
		while(!areaFound){
			if(areaId[j] != 0){
				j++;
			}
			if(areaId[j] == 0){
				areaFound = true;
			}
		}
		return j;
	}
	
	public void removeArea(int id){
	  areaId[id] = 0;
	  //areaType[id] = 0;
	  areaPriority[id] = 0;
	  areaName[id] = "";
	  areaMusic[id] = 0;
	  areaX1[id] = 0;
	  areaY1[id] = 0;
	  areaX2[id] = 0;
	  areaY2[id] = 0;
	  repaint();
	}
	
	public void clearAreas(){
		for(int id = 0; id < 2000; id++){
			areaId[id] = 0;
			areaPriority[id] = 0;
			areaName[id] = "";
			areaMusic[id] = 0;
			areaX1[id] = 0;
			areaY1[id] = 0;
			areaX2[id] = 0;
			areaY2[id] = 0;
		}	
		repaint();
	}
	
	public void saveArea(int id, int priority, String name, int music, int x1, int y1, int x2, int y2){
		if(mode == 2){
			areaId[id] = 1;
			//areaType[id] = type;
			areaPriority[id] = priority;
			areaName[id] = name;
			areaMusic[id] = music;
			areaX1[id] = x1;
			areaY1[id] = y1;
			areaX2[id] = x2;
			areaY2[id] = y2;
		}
		if(mode == 3){
			areaId[id] = 1;
			areaName[id] = name;
			areaX1[id] = x1;
			areaY1[id] = y1;
			areaX2[id] = x2;
			areaY2[id] = y2;
		}
		repaint();
	}
	
	public void loadAreas(){	
		if(mode == 2 && FileOperations.FileExists("./Data/Music-edit.dat")){
			File file = new File("./Data/Music-edit.dat");	
			int id = 0;
			//int type = 0;
			int priority = 0;
			String name = "";
			int music = 0;
			int x1 = 0;
			int y1 = 0;
			int x2 = 0;
			int y2 = 0;
			try {
				Scanner scanner = new Scanner(file);
				while (scanner.hasNextLine()) {
					if(runnedOnce){
						String line = scanner.nextLine();
					}
					if(scanner.hasNext()){
						id = scanner.nextInt();
						//type = scanner.nextInt();
						priority = scanner.nextInt();
						name = scanner.next().replaceAll("_", " ");
						music = scanner.nextInt();
						x1 = scanner.nextInt();
						y1 = scanner.nextInt();
						x2 = scanner.nextInt();
						y2 = scanner.nextInt();
						runnedOnce = true;
						//save(id, type, priority, name, music, x1, y1, x2, y2);
						saveArea(id, priority, name, music, x1, y1, x2, y2);
					}else{
						runnedOnce = false;
					}
				try{
				}catch(Exception ex){}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("File not found!");
			}
		}
		if(mode == 3 && FileOperations.FileExists("./Data/Multiway-edit.dat")){
			File file = new File("./Data/Multiway-edit.dat");	
			int id = 0;
			String name = "";
			int x1 = 0;
			int y1 = 0;
			int x2 = 0;
			int y2 = 0;
			try {
				Scanner scanner = new Scanner(file);
				while (scanner.hasNextLine()) {
					if(runnedOnce){
						String line = scanner.nextLine();
					}
					if(scanner.hasNext()){
						id = scanner.nextInt();
						name = scanner.next().replaceAll("_", " ");
						x1 = scanner.nextInt();
						y1 = scanner.nextInt();
						x2 = scanner.nextInt();
						y2 = scanner.nextInt();
						runnedOnce = true;
						saveArea(id, 0, name, 0, x1, y1, x2, y2);
					}else{
						runnedOnce = false;
					}
				try{
				}catch(Exception ex){}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("File not found!");
			}
		}
    }
	
	public void saveArea(){
		if(mode == 2){
			BufferedWriter bw = null;
			File f = new File("./Data/Music edit.dat");
			f.delete();
			for(int i = 0; i < 2000; i++){
				if(areaId[i] != 0){
					try {
						bw = new BufferedWriter(new FileWriter("./Data/Music edit.dat", true));
						String name = areaName[i].replaceAll(" ", "_");
						bw.write(i+" "+areaPriority[i]+" "+name+" "+areaMusic[i]+" "+areaX1[i]+" "+areaY1[i]+" "+areaX2[i]+" "+areaY2[i]);
						bw.newLine();
						bw.flush();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					} finally {
						if (bw != null) try {
							bw.close();
					} catch (IOException ioe2) {
					}
					}
				}	
			}	
		}
		if(mode == 3){
			BufferedWriter bw = null;
			File f = new File("./Data/Multiway edit.dat");
			f.delete();
			for(int i = 0; i < 2000; i++){
				if(areaId[i] != 0){
					try {
						bw = new BufferedWriter(new FileWriter("./Data/Multiway edit.dat", true));
						String name = areaName[i].replaceAll(" ", "_");
						bw.write(i+" "+name+" "+areaX1[i]+" "+areaY1[i]+" "+areaX2[i]+" "+areaY2[i]);
						bw.newLine();
						bw.flush();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					} finally {
						if (bw != null) try {
							bw.close();
					} catch (IOException ioe2) {
					}
					}
				}	
			}	
		}
	}
	
	public void packArea() {
		if(mode == 2){
			File f = new File("./Data/Music.dat");
			f.delete();
			try {
			int j1 = checkAreaCount();
			Packer stream2 = new Packer(new FileOutputStream("./Data/Music.dat"));
			stream2.writeShort(j1);
			for(int i = 0; i < j1; i++)
			{	
				stream2.writeUnsignedByte(areaPriority[i]);
				stream2.writeShort(areaMusic[i]);
				stream2.writeShort(areaX1[i]);
				stream2.writeShort(areaY1[i]);
				stream2.writeShort(areaX2[i]);
				stream2.writeShort(areaY2[i]);
			}
			stream2.close();
			} catch (Exception e) {

			}
		}
		if(mode == 3){
			File f = new File("./Data/Multiway.dat");
			f.delete();
			try {
			int j1 = checkAreaCount();
			Packer stream2 = new Packer(new FileOutputStream("./Data/Multiway.dat"));
			stream2.writeShort(j1);
			for(int i = 0; i < j1; i++)
			{	
				stream2.writeShort(areaX1[i]);
				stream2.writeShort(areaY1[i]);
				stream2.writeShort(areaX2[i]);
				stream2.writeShort(areaY2[i]);
			}
			stream2.close();
			} catch (Exception e) {

			}
		}
    }
	
	public int checkAreaCount(){
		int count = 0;
		for(int i = 0; i < 2000; i++){
			if(areaId[i] != 0){
				count++;
			}	
		}
		return count;
	}*/
	
	/*
	int maxX;//3840 + 64 = 3904
	int maxY;//10368 + 64 = 10432
	int minX;//1600 - 64 = 1536
	int minY;//2496 - 64 = 2432
	-----------------
	width = 2368
	height = 8000
	*/
	
	public int coords2Region(int x, int y){
		int rx = x >> 6;
		int ry = y >> 6;
		int region = ry + (rx * 256);
		return region;
	}
	
	public void region2Coords(int region, int id){
		int x, y;
		x = (region >> 8) * 64;
		y = (region & 0xff) * 64;
		regionX[id] = x;
		regionY[id] = y;
		/*if(maxX < x)
			maxX = x;
		if(maxY < y)
			maxY = y;
		if(minX > x)
			minX = x;
		if(minY > y)
			minY = y;	*/
	}
	
	public int getRegionId(int id){
		for(int idz = 0; idz < mapIndex.regionCount; idz++)
			if(mapIndex.regionId[idz] == id)
				return idz;
		return 0;		
	}
	
	int x = 3217;
	int y = 3218;
	
	int xOff = 1536;
	int yOff = 2432;
	
	public int selectedRegion;
	
	/*public void loadF2Pregions(){
		if(FileOperations.FileExists("./Data/F2P_regions.dat")){
			byte abyte2[] = FileOperations.ReadFile("./Data/F2P_regions.dat");
			Stream stream2 = new Stream(abyte2);
			int j1 = stream2.readUnsignedWord();
			for(int i2 = 0; i2 < j1; i2++){
				int id = stream2.readUnsignedWord();
				regionType[getRegionId(id)] = 1;
			}
		}	
	}
	
	public void save(){
		try {
			int count = 0;
			for(int id = 0; id < mapIndex.regionCount; id++){
				if(regionType[id] == 1)
					count++;
			}
			int j1 = mapIndex.regionCount;
			Packer stream2 = new Packer(new FileOutputStream("./Data/F2P_regions.dat"));
			stream2.writeShort(count);
			for(int i2 = 0; i2 < j1; i2++)
			{	
				if(regionType[i2] == 1)
					stream2.writeShort(mapIndex.regionId[i2]);
			}
			stream2.close();
		} catch (Exception e) {

		}
	}*/
	
	public int regCount(){
		return mapIndex.regionCount;
	}
	
	public boolean showNames = true;
	public boolean showP2P = true;
	
	public boolean showDetails = true;
	
	/*
	public static void updatePlayers(){
		int id = 0;
		for (Player player : World.getPlayers()) {
			id++;
			if(playerAlreadyOnTable(id) && player == null){
				if(getPlayerRow(id) != -1)
					model.removeRow(getPlayerRow(id));
				if(id == selectedPlayer)
					clearInfo();
			}
			if (player == null) {
				continue;
			}
			if(id == selectedPlayer && selectedPlayer != -1){
				updatePlayerInfo();
			}
			if(!playerAlreadyOnTable(id)){
				String name = player.getUsername();
				String rights = "";
				if(player.getStaffRights() == 0)
					rights = "Player";
				if(player.getStaffRights() == 1)
					rights = "Mod";
				if(player.getStaffRights() >= 2)
					rights = "Admin";
				boolean member = player.isMember();
				Vector newRow = new Vector();
				newRow.add(id);
				newRow.add(name);
				newRow.add(rights);
				newRow.add((member ? "Yes" : "No"));
				model.addRow(newRow);
			}
		}
	}
	 */
	
	public void paint (Graphics g) {
		//System.out.println("x "+x+" y "+y);
		for(int id = 0; id < mapIndex.regionCount; id++){
			//if(regionX[id] >= (x-64*3) && regionY[id] >= (y-64*3) && regionX[id] <= (x+64*3) && regionY[id] <= (y+64*3)){
				g.drawImage(regionImage[id][height], (regionX[id]-xOff)*scale-1, (10432-regionY[id])*scale-(64*scale)+scale, null);
				//System.out.println("Drawing region: "+id);
				/*if(regionHasKeys[id] == 0){
						Color color = new Color(1, 0, 0, 0.5f);
						g.setColor(color);
						g.fillRect((regionX[id]-xOff)*scale-1,(10432-regionY[id])*scale-(64*scale)+scale,64*scale,64*scale);
				}
				if(showDetails){
				g.setColor(Color.white);
				g.drawRect((regionX[id]-xOff)*scale-1,(10432-regionY[id])*scale-(64*scale)+scale,64*scale,64*scale);
				Font normal = new Font("Arial", Font.PLAIN, 17);
					FontMetrics metr = this.getFontMetrics(normal);
					g.setFont(normal);
					String s = ""+mapIndex.regionId[id];
					int x1 = (regionX[id]-xOff)*scale-1;
					int x2 = x1+(64*scale);
					int y1 = (10432-regionY[id])*scale+scale;
					int y2 = y1-(64*scale);
					g.drawString(s,x1+((x2-x1)/2) - (metr.stringWidth(s)) / 2,y1+((y2-y1)/2));
				}*/
			//}	
		}
		for (Player player : World.getPlayers()) {
			if (player == null) {
				continue;
			}
			int x = player.getPosition().getX();
			int y = player.getPosition().getY();
			int z = player.getPosition().getZ();
			if(height == z){
				g.setColor(Color.white);
				g.fillRect((x-xOff)*scale,(10432-y)*scale,scale,scale);
				if(showNames){
					String name = player.getUsername();
					Font normal = new Font("Calibri", Font.PLAIN, 12);
					FontMetrics metr = this.getFontMetrics(normal);
					g.setFont(normal);
					g.drawString(name,(x-xOff)*scale - (metr.stringWidth(name)) / 2,(10432-y)*scale);
				}
			}
		}
		/*if(mode == 0 && currentZ == height){//coord grab
			g.setColor(Color.black);
			g.fillRect((currentX-xOff)*scale,(10432-currentY)*scale,scale,scale);
		}
		if(mode == 1){//region types
			for(int id = 0; id < mapIndex.regionCount; id++){
				//if(regionX[id] >= (x-64*3) && regionY[id] >= (y-64*3) && regionX[id] <= (x+64*3) && regionY[id] <= (y+64*3)){
					if(regionType[id] == 1){//f2p region
						Color color = new Color(0, 1, 0, 0.5f);
						g.setColor(color);
						g.fillRect((regionX[id]-xOff)*scale-1,(10432-regionY[id])*scale-(64*scale)+scale,64*scale,64*scale);
					}
					if(mapIndex.regionId[id] == selectedRegion){
						Color color = new Color(1, 1, 1, 0.5f);
						g.setColor(color);
						g.fillRect((regionX[id]-xOff)*scale-1,(10432-regionY[id])*scale-(64*scale)+scale,64*scale,64*scale);
					}
				//}	
			}
		}
		if((mode == 2 || mode == 3) && currentZ == height){//area editing
			for(int i = 0; i < 2000; i++){
				if(areaId[i] != 0){
					String s = "";
					if(mode == 2){
						g.setColor(Color.blue);
						s = "["+i+"] "+areaName[i]+" ("+areaMusic[i]+")";
					}
					if(mode == 3){
						g.setColor(Color.red);
						s = "["+i+"] "+areaName[i];
					}
					int x1 = (areaX1[i]-xOff)*scale-1;
					int x2 = (areaX2[i]-xOff)*scale+scale;
					int y1 = (10432-areaY1[i])*scale+scale;
					int y2 = (10432-areaY2[i])*scale+scale-5;
					g.drawLine(x1,y1,x2,y1);
					g.drawLine(x1,y1,x1,y2);
					g.drawLine(x1,y2,x2,y2);
					g.drawLine(x2,y1,x2,y2);
					Font normal = new Font("Arial", Font.PLAIN, 17);
					FontMetrics metr = this.getFontMetrics(normal);
					g.setFont(normal);
					g.drawString(s,x1+((x2-x1)/2) - (metr.stringWidth(s)) / 2,y1+((y2-y1)/2));
				}
			}
			g.setColor(Color.black);
			g.fillRect((currentX-xOff)*scale,(10432-currentY)*scale,scale,scale);
		}
		if(mode == 4){//npc edit
			for(int index = 0; index < maxNpcs; index++){
				if(npcId[index] != -1 && npcZ[index] == height && ((npcType[index] == 1 && showP2P) || npcType[index] != 1)){
					g.drawImage(mapDot[1], (npcX[index]-xOff)*scale, (10432-npcY[index])*scale, null);
					if(showNames){
						g.setColor(Color.yellow);
						String s = "["+index+"] "+getNpcName(npcId[index]);
						if(getNpclvl(npcId[index]) > 0)
							s += " (lvl-"+getNpclvl(npcId[index])+")";
						Font normal = new Font("Arial", Font.PLAIN, 12);
						FontMetrics metr = this.getFontMetrics(normal);
						g.setFont(normal);
						g.drawString(s,(npcX[index]-xOff)*scale - (metr.stringWidth(s)) / 2,(10432-npcY[index])*scale);
					}
					if(npcX1[index] > 0 && npcX2[index] > 0 && npcY1[index] > 0 && npcY2[index] > 0){
						int x1 = (npcX1[index]-xOff)*scale-1;
						int x2 = (npcX2[index]-xOff)*scale+scale;
						int y1 = (10432-npcY1[index])*scale+scale;
						int y2 = (10432-npcY2[index])*scale+scale-5;
						g.setColor(Color.yellow);
						g.drawLine(x1,y1,x2,y1);
						g.drawLine(x1,y1,x1,y2);
						g.drawLine(x1,y2,x2,y2);
						g.drawLine(x2,y1,x2,y2);
					}	
				}
			}
		}
		if(mode == 5){//item edit
			for(int index = 0; index < maxItems; index++){
				if(itemId[index] != -1 && itemZ[index] == height && ((itemType[index] == 1 && showP2P) || itemType[index] != 1)){
					g.drawImage(mapDot[0], (itemX[index]-xOff)*scale, (10432-itemY[index])*scale, null);
					if(showNames){
						g.setColor(Color.red);
						String s = "["+index+"] "+getItemName(itemId[index]);
						if(itemAmount[index] > 1)
							s += " ("+itemAmount[index]+")";
						Font normal = new Font("Arial", Font.PLAIN, 12);
						FontMetrics metr = this.getFontMetrics(normal);
						g.setFont(normal);
						g.drawString(s,(itemX[index]-xOff)*scale - (metr.stringWidth(s)) / 2,(10432-itemY[index])*scale);
					}
				}
			}
		}
		if((mode == 4 || mode == 5) && currentZ == height){//npc/item editing
			g.setColor(Color.black);
			g.fillRect((currentX-xOff)*scale,(10432-currentY)*scale,scale,scale);
		}*/
	}
   
}
