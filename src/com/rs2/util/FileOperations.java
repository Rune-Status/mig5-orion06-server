package com.rs2.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;

/**
 *
 */
public class FileOperations {


    public FileOperations()
    {
    }

    public static final byte[] ReadFile(String s)
    {
        try
        {
            File file = new File(s);
            int i = (int)file.length();
            byte abyte0[] = new byte[i];
            DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new FileInputStream(s)));
            datainputstream.readFully(abyte0, 0, i);
            datainputstream.close();
            TotalRead++;
            return abyte0;
        }
        catch(Exception exception)
        {
            System.out.println((new StringBuilder()).append("Read Error: ").append(s).toString());
        }
        return null;
    }

    public static final void WriteFile(String s, byte abyte0[])
    {
        try
        {
            (new File((new File(s)).getParent())).mkdirs();
            FileOutputStream fileoutputstream = new FileOutputStream(s);
            fileoutputstream.write(abyte0, 0, abyte0.length);
            fileoutputstream.close();
            TotalWrite++;
            CompleteWrite++;
        }
        catch(Throwable throwable)
        {
            System.out.println((new StringBuilder()).append("Write Error: ").append(s).toString());
        }
    }
	
	public static boolean FileExists(String file) {
	File f = new File(file);
		if(f.exists())
			return true;
		else
			return false;
	}
	

    public static int TotalRead = 0;
    public static int TotalWrite = 0;
    public static int CompleteWrite = 0;
}