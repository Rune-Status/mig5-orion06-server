package com.rs2.launcher;


import java.io.PrintStream;
import javax.swing.*;

public class Console extends PrintStream
{

    private JTextArea jt;
    private JScrollPane scroller;

    public Console(JTextArea jt, JScrollPane scroller)
    {
        super(System.out);
        this.jt = jt;
        this.scroller = scroller;
    }

    public void println(Object o)
    {
        printlnObject(o);
    }

    public void printlnObject(Object o)
    {
        jt.append((new StringBuilder()).append(o.toString()).append("\n").toString());
        //adjustScroller(); //What this does is, everytime it prints something out it moves down. I don't like it so i disabled it.
    }

    public void println(String s)
    {
        printlnObject(s);
    }

    public void println()
    {
        println("println\n");
    }

    public void print(Object o)
    {
        printObject(o);
    }

    public void printObject(Object o)
    {
        jt.append(o.toString());
    }

    public void print(String s)
    {
        printObject(s);
    }

    public void adjustScroller()
    {
        scroller.getVerticalScrollBar().setValue(scroller.getVerticalScrollBar().getMaximum());
    }
}