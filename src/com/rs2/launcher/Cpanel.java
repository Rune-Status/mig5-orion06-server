package com.rs2.launcher;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.rs2.Constants;
import com.rs2.Server;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.hiscore.Hplayer;
import com.rs2.model.players.Player;
import com.rs2.util.FileOperations;
import com.rs2.util.PlayerSave;

public class Cpanel
		extends 	JFrame implements ActionListener
{
	private		JTabbedPane tabbedPane;
	private		JPanel		panel1;
	private		JPanel		panel2;
	private		JPanel		panel3;
	private		JPanel		panel4;
	private		JPanel		panel5;
	private		JPanel		panel6;
	private		JPanel		panel7;
	
	private static JTable table;
	
	private static JTable table7;
	
	static JButton btnStartServer;
	static JButton btnRestartServer;
	static JButton btnShutdownServer;
	static JButton btnSend;
	static JButton btnDefault;
	static JLabel label11;
	static JLabel label12;
	static JLabel label13;
	static JLabel label16;
	static JTextField field11;
	
	static JTextField field21;
	static JTextField field22;
	static JTextField field23;
	static JTextField field24;
	static JTextField field25;
	static JCheckBox dueleButton;
	static JCheckBox adminieButton;
	static JCheckBox f2pceButton;
	static JCheckBox itemspeButton;
	static JCheckBox funpkeButton;
	static JCheckBox pkworldeButton;
	static JRadioButton rbMenuItem1;
	static JRadioButton rbMenuItem2;
	static JRadioButton rbMenuItem3;
	static JRadioButton rbMenuItem4;
	static JButton btnSet;
	
	static JTextField field31;
	static JTextField field32;
	static JCheckBox mysqleButton;
	static JTextField field35;
	static JTextField field36;
	static JTextField field37;
	static JTextField field38;
	static JCheckBox rsaeButton;
	static JTextField field33;
	static JTextField field34;
	static JCheckBox debugeButton;
	static JCheckBox hiscoreseButton;
	static JCheckBox developeButton;
	
	static JCheckBox wceButton;
	static JCheckBox thiefeButton;
	static JCheckBox smitheButton;
	static JCheckBox slayereButton;
	static JCheckBox rceButton;
	static JCheckBox prayeButton;
	static JCheckBox mineeButton;
	static JCheckBox herbeButton;
	static JCheckBox fletcheButton;
	static JCheckBox fisheButton;
	static JCheckBox fmeButton;
	static JCheckBox farmeButton;
	static JCheckBox crafteButton;
	static JCheckBox cookeButton;
	static JCheckBox agilityeButton;
	
	static JLabel label52;
	static JLabel label53;
	static JLabel label54;
	static JLabel label5Mac;
	static JLabel label55;
	static JLabel label56;
	static JLabel label57;
	static JLabel label59;
	static JTextField field51;
	static JRadioButton rbMenuItem51;
	static JRadioButton rbMenuItem52;
	static JRadioButton rbMenuItem53;
	static JRadioButton rbMenuItem54;
	static JRadioButton rbMenuItem55;
	static JCheckBox followeButton;
	
	static JTextField textField_Plist;
	static JTextField textField_Paypal;
	static JTextField textField_Amount;
	static JTextField textField_Linked;
	static JTextField textField_Ppos;
	
	JCheckBox showNamesButton;
	
	Font normal = new Font("Calibri", Font.PLAIN, 12);
	
	static Map m = new Map();
	 
	int scale = m.scale;

	JFrame worldMap = new JFrame();
	
	private JPanel gamePanel;
	private JScrollPane jsp;
	
	int curX;
	int curY;
	
	public Cpanel()
	{	
		setTitle( "World 1 Control Panel" );
		if(Constants.EASY_AEON)
			setTitle( "World 2 Control Panel" );
		setServerSettings();
		setSize( 440, 370 );
		setResizable(false);
		
		worldMap.setTitle("LIVE World Map");
		worldMap.setBounds(100, 100, 415, 400);
		worldMap.setResizable(true);
		
		gamePanel = new JPanel();
		gamePanel.setLayout(new BorderLayout());
		gamePanel.add(m);
		m.setOpaque(false);
		gamePanel.setBackground(Color.black);
		gamePanel.setPreferredSize(new Dimension(m.sizeX, m.sizeY));
		
		int v=ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
		int h=ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS; 
		jsp=new JScrollPane(gamePanel,v,h);
		jsp.setPreferredSize(new Dimension(512,512));
		
		JMenuBar menuBar = new JMenuBar();
		
		jsp.getHorizontalScrollBar().addAdjustmentListener(new MyActionH());
		jsp.getVerticalScrollBar().addAdjustmentListener(new MyActionV());
		worldMap.getContentPane().add(jsp, BorderLayout.CENTER);
		worldMap.getContentPane().add(menuBar, BorderLayout.NORTH);
		
		showNamesButton = new JCheckBox("Show names");
		showNamesButton.setFont(normal);
		showNamesButton.addActionListener(this);
		menuBar.add(showNamesButton);
		
		//start pos.
		curX = 3217;
		curY = 3218;
		jsp.getViewport().setViewPosition(new java.awt.Point((curX-1536-64)*scale, (10432-curY-64)*scale));
		
		showNamesButton.setSelected(true);
		m.showNames = showNamesButton.isSelected();
		m.repaint();
		
		setBackground( Color.gray );
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		getContentPane().add( topPanel );

		// Create the tab pages
		createPage1();
		createPage2();
		createPage3();
		createPage4();
		createPage5();
		createPage6();
		createPage7();//donation tab

		// Create a tabbed pane
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab( "Main", panel1 );
		tabbedPane.addTab( "Settings", panel2 );
		tabbedPane.addTab( "Advanced", panel3 );
		tabbedPane.addTab( "Skills", panel4 );
		tabbedPane.addTab( "World", panel5 );
		tabbedPane.addTab( "Donate", panel7 );
		tabbedPane.addTab( "Credits", panel6 );
		topPanel.add( tabbedPane, BorderLayout.CENTER );
		setLocationRelativeTo(null);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	if(btnShutdownServer.isEnabled())
            		JOptionPane.showMessageDialog(tabbedPane, "Please use the shutdown button for closing!");
            	else
            		System.exit(0);
            }
		});
		
		worldMap.setLocation((this.getX() + this.getWidth()) + 5, this.getY());
		
		if(FileOperations.FileExists("data/settings.dat")){
			try{
				readSettings();
			}catch(Exception ex){}
		} else {
			restoreDefaults();
		}
		
	}

	public void setServerSettings(){
		Constants.EMULATOR = "EasyRS06 v"+version;
	}
	
	static String status = "Offline";
	static int maxPlayers = 2000;
	static int onlinePlayers = 0;
	static int onlineMods = 0;
	static int onlineAdmins = 0;
	static String name = "OSRSPK";
	static int runTime = 0;
	
	double version = 1.0;
	
	public void createPage1()
	{
		panel1 = new JPanel();
		panel1.setLayout( null );
		
		String color = (status.equals("Online") ? "<font color=green>" : "<font color=red>");
		label11 = new JLabel( "<html>Server Status: <b>"+color+status);
		label11.setFont(normal);
		
		label11.setBounds( 10, 15, 250, 20 );
		panel1.add( label11 );

		label12 = new JLabel( "<html>Users Online: "+onlinePlayers+"/"+maxPlayers+" <font color=blue>("+onlineMods+" Mods, <font color=orange>"+onlineAdmins+" Admins)" );
		label12.setFont(normal);
		label12.setBounds( 10, 40, 250, 20 );
		panel1.add( label12 );
		
		JLabel label8 = new JLabel( "<html>Server Emulator: <font color=blue>EasyRS06 v"+version );
		label8.setFont(normal);
		label8.setBounds( 10, 65, 250, 20 );
		panel1.add( label8 );
		
		label13 = new JLabel( "<html>Server Name: <font color=#1589FF>"+name );
		label13.setFont(normal);
		label13.setBounds( 10, 90, 250, 20 );
		panel1.add( label13 );

		JLabel label4 = new JLabel( "Server Message:" );
		label4.setFont(normal);
		label4.setBounds( 10, 115, 250, 20 );
		panel1.add( label4 );
		
		field11 = new JTextField();
		field11.setBounds( 10, 140, 150, 20 );
		panel1.add( field11 );
		
		btnStartServer = new JButton("Start Server");
		btnStartServer.setFont(normal);
		btnStartServer.setBounds(280, 15, 125, 21);
		panel1.add(btnStartServer);
		btnStartServer.addActionListener(this);
		
		btnRestartServer = new JButton("Restart Server");
		btnRestartServer.setFont(normal);
		btnRestartServer.setBounds(280, 40, 125, 21);
		panel1.add(btnRestartServer);
		btnRestartServer.addActionListener(this);
		btnRestartServer.setEnabled(false);
		
		String runningTime = (runTime/60 > 0 ? (runTime/60)+" hours "+(runTime%60)+" mins" : runTime+" mins" );
		//label16 = new JLabel( "<html>Runtime: <font color=#1589FF>"+runTime+" mins" );
		label16 = new JLabel( "<html>Runtime: <font color=#1589FF>"+runningTime );
		label16.setFont(normal);
		label16.setBounds( 280, 65, 250, 20 );
		panel1.add( label16 );
		
		JCheckBox restartMinButton = new JCheckBox("Restart every");
		restartMinButton.setBounds(259, 90, 95, 21);
		restartMinButton.setFont(normal);
		restartMinButton.addActionListener(this);
		panel1.add(restartMinButton);
		
		JTextField field2 = new JTextField();
		field2.setBounds( 355, 90, 30, 20 );
		panel1.add( field2 );
		
		JLabel label7 = new JLabel( "min" );
		label7.setFont(normal);
		label7.setBounds( 386, 90, 30, 20 );
		panel1.add( label7 );
		
		btnShutdownServer = new JButton("Shutdown Server");
		btnShutdownServer.setFont(normal);
		btnShutdownServer.setBounds(280, 140, 125, 21);
		panel1.add(btnShutdownServer);
		btnShutdownServer.addActionListener(this);
		btnShutdownServer.setEnabled(false);
		
		btnSend = new JButton("Send");
		btnSend.setFont(normal);
		btnSend.setBounds(170, 140, 65, 21);
		panel1.add(btnSend);
		btnSend.addActionListener(this);
		btnSend.setEnabled(false);
		
		JLabel label5 = new JLabel( "Console:" );
		label5.setFont(normal);
		label5.setBounds( 10, 165, 250, 20 );
		panel1.add( label5 );
		
		//for console
		JTextArea consoleText = new JTextArea(20, 30);
		JScrollPane scroll = new JScrollPane(consoleText, 22, 31);
		consoleText.setForeground(new Color(0, 0, 0)); //console text color
		consoleText.setBackground(new Color(255, 255, 255)); //console background color
		System.setOut(new Console(consoleText, scroll));
		//scroll.setPreferredSize(new Dimension(330, 170));
		scroll.setBounds(10, 190, 400, 110);
		consoleText.setEnabled(false);
		//
		panel1.add(scroll);
		
	}

	public void createPage2()
	{	
		panel2 = new JPanel();
		panel2.setLayout( null );
		
		JLabel label1 = new JLabel( "Server Name:" );
		label1.setFont(normal);
		label1.setBounds( 10, 15, 80, 20 );
		panel2.add( label1 );
		
		field21 = new JTextField();
		field21.setBounds( 95, 15, 155, 20 );
		panel2.add( field21 );
		
		JLabel label2 = new JLabel( "Max Players:" );
		label2.setFont(normal);
		label2.setBounds( 10, 40, 80, 20 );
		panel2.add( label2 );
		
		field22 = new JTextField();
		field22.setBounds( 95, 40, 155, 20 );
		panel2.add( field22 );
		
		JLabel label3 = new JLabel( "XP Rate:" );
		label3.setFont(normal);
		label3.setBounds( 10, 65, 80, 20 );
		panel2.add( label3 );
		
		field23 = new JTextField();
		field23.setBounds( 95, 65, 155, 20 );
		panel2.add( field23 );
		
		JLabel label4 = new JLabel( "Starting Pos:" );
		label4.setFont(normal);
		label4.setBounds( 10, 90, 80, 20 );
		panel2.add( label4 );
		
		field24 = new JTextField();
		field24.setBounds( 95, 90, 155, 20 );
		panel2.add( field24 );
		
		JLabel label5 = new JLabel( "Respawn Pos:" );
		label5.setFont(normal);
		label5.setBounds( 10, 115, 80, 20 );
		panel2.add( label5 );
		
		field25 = new JTextField();
		field25.setBounds( 95, 115, 155, 20 );
		panel2.add( field25 );
		
		f2pceButton = new JCheckBox("F2P only content");
		f2pceButton.setBounds(250, 15, 170, 21);
		f2pceButton.setFont(normal);
		f2pceButton.addActionListener(this);
		panel2.add(f2pceButton);
		
		dueleButton = new JCheckBox("Enable Dueling");
		dueleButton.setBounds(250, 40, 170, 21);
		dueleButton.setFont(normal);
		dueleButton.addActionListener(this);
		panel2.add(dueleButton);
		
		adminieButton = new JCheckBox("Allow admin interactions");
		adminieButton.setBounds(250, 65, 170, 21);
		adminieButton.setFont(normal);
		adminieButton.addActionListener(this);
		panel2.add(adminieButton);
		
		itemspeButton = new JCheckBox("Item spawning");
		itemspeButton.setBounds(250, 90, 170, 21);
		itemspeButton.setFont(normal);
		itemspeButton.addActionListener(this);
		panel2.add(itemspeButton);
		
		funpkeButton = new JCheckBox("Fun PK");
		funpkeButton.setBounds(250, 115, 170, 21);
		funpkeButton.setFont(normal);
		funpkeButton.addActionListener(this);
		panel2.add(funpkeButton);
		
		pkworldeButton = new JCheckBox("PK World");
		pkworldeButton.setBounds(250, 140, 170, 21);
		pkworldeButton.setFont(normal);
		pkworldeButton.addActionListener(this);
		panel2.add(pkworldeButton);
		
		JLabel label6 = new JLabel( "<html><b>Login Restrictions" );
		label6.setFont(normal);
		label6.setBounds( 80, 140, 150, 20 );
		panel2.add( label6 );
				
		rbMenuItem1 = new JRadioButton("None");
		rbMenuItem1.setFont(normal);
		rbMenuItem1.setSelected(true);
		rbMenuItem1.setBounds( 10, 165, 60, 20 );
		panel2.add(rbMenuItem1);
		rbMenuItem1.addActionListener(this);
		
		rbMenuItem2 = new JRadioButton("P2P");
		rbMenuItem2.setFont(normal);
		rbMenuItem2.setBounds( 70, 165, 50, 20 );
		panel2.add(rbMenuItem2);
		rbMenuItem2.addActionListener(this);
		
		rbMenuItem3 = new JRadioButton("Mod");
		rbMenuItem3.setFont(normal);
		rbMenuItem3.setBounds( 120, 165, 50, 20 );
		panel2.add(rbMenuItem3);
		rbMenuItem3.addActionListener(this);
		
		rbMenuItem4 = new JRadioButton("Admin");
		rbMenuItem4.setFont(normal);
		rbMenuItem4.setBounds( 175, 165, 60, 20 );
		panel2.add(rbMenuItem4);
		rbMenuItem4.addActionListener(this);
		
		//Group the radio buttons.
	    ButtonGroup group = new ButtonGroup();
	    group.add(rbMenuItem1);
	    group.add(rbMenuItem2);
	    group.add(rbMenuItem3);
	    group.add(rbMenuItem4);
	    
	    btnSet = new JButton("Set");
	    btnSet.setBounds(145, 280, 60, 20);
		panel2.add(btnSet);
		btnSet.addActionListener(this);
		
		btnDefault = new JButton("Defaults");
		btnDefault.setBounds(210, 280, 85, 20);
		panel2.add(btnDefault);
		btnDefault.addActionListener(this);
		
	}

	public void createPage3()
	{	
		panel3 = new JPanel();
		panel3.setLayout( null );
		
		JLabel label1 = new JLabel( "Port Number:" );
		label1.setFont(normal);
		label1.setBounds( 10, 15, 80, 20 );
		panel3.add( label1 );
		
		field31 = new JTextField();
		field31.setBounds( 95, 15, 155, 20 );
		panel3.add( field31 );
		
		JLabel label2 = new JLabel( "Client Version:" );
		label2.setFont(normal);
		label2.setBounds( 10, 40, 80, 20 );
		panel3.add( label2 );
		
		field32 = new JTextField();
		field32.setBounds( 95, 40, 155, 20 );
		panel3.add( field32 );
		
		mysqleButton = new JCheckBox("MySQL Enabled");
		mysqleButton.setBounds(10, 65, 155, 21);
		mysqleButton.setFont(normal);
		mysqleButton.addActionListener(this);
		panel3.add(mysqleButton);
		
		JLabel label5 = new JLabel( "DB Driver:" );
		label5.setFont(normal);
		label5.setBounds( 10, 90, 80, 20 );
		panel3.add( label5 );
		
		field35 = new JTextField();
		field35.setBounds( 95, 90, 155, 20 );
		panel3.add( field35 );
		
		JLabel label6 = new JLabel( "DB URL:" );
		label6.setFont(normal);
		label6.setBounds( 10, 115, 80, 20 );
		panel3.add( label6 );
		
		field36 = new JTextField();
		field36.setBounds( 95, 115, 155, 20 );
		panel3.add( field36 );
		
		JLabel label7 = new JLabel( "DB User:" );
		label7.setFont(normal);
		label7.setBounds( 10, 140, 80, 20 );
		panel3.add( label7 );
		
		field37 = new JTextField();
		field37.setBounds( 95, 140, 155, 20 );
		panel3.add( field37 );
		
		JLabel label8 = new JLabel( "DB Pass:" );
		label8.setFont(normal);
		label8.setBounds( 10, 165, 80, 20 );
		panel3.add( label8 );
		
		field38 = new JTextField();
		field38.setBounds( 95, 165, 155, 20 );
		panel3.add( field38 );
		
		hiscoreseButton = new JCheckBox("Hiscores Enabled");
		hiscoreseButton.setBounds(260, 15, 155, 21);
		hiscoreseButton.setFont(normal);
		hiscoreseButton.addActionListener(this);
		panel3.add(hiscoreseButton);
		
		rsaeButton = new JCheckBox("RSA Enabled");
		rsaeButton.setBounds(10, 190, 155, 21);
		rsaeButton.setFont(normal);
		rsaeButton.addActionListener(this);
		panel3.add(rsaeButton);
		
		JLabel label3 = new JLabel( "RSA Key1:" );
		label3.setFont(normal);
		label3.setBounds( 10, 215, 80, 20 );
		panel3.add( label3 );
		
		field33 = new JTextField();
		field33.setBounds( 95, 215, 155, 20 );
		panel3.add( field33 );
		
		JLabel label4 = new JLabel( "RSA Key2:" );
		label4.setFont(normal);
		label4.setBounds( 10, 240, 80, 20 );
		panel3.add( label4 );
		
		field34 = new JTextField();
		field34.setBounds( 95, 240, 155, 20 );
		panel3.add( field34 );
		
		debugeButton = new JCheckBox("Debug Mode");
		debugeButton.setBounds(260, 40, 155, 21);
		debugeButton.setFont(normal);
		debugeButton.addActionListener(this);
		panel3.add(debugeButton);
		
		developeButton = new JCheckBox("Develop Mode");
		developeButton.setBounds(260, 65, 155, 21);
		developeButton.setFont(normal);
		developeButton.addActionListener(this);
		panel3.add(developeButton);
		
	}
	
	public void createPage4()
	{	
		panel4 = new JPanel();
		panel4.setLayout( null );
		
		JLabel label1 = new JLabel( "<html><b>Enabled" );
		label1.setFont(normal);
		label1.setBounds( 10, 15, 80, 20 );
		panel4.add( label1 );
		
		wceButton = new JCheckBox("Woodcutting");
		wceButton.setBounds(20, 40, 95, 21);
		wceButton.setFont(normal);
		wceButton.addActionListener(this);
		panel4.add(wceButton);
		
		thiefeButton = new JCheckBox("Thieving");
		thiefeButton.setBounds(20, 65, 95, 21);
		thiefeButton.setFont(normal);
		thiefeButton.addActionListener(this);
		panel4.add(thiefeButton);
		
		smitheButton = new JCheckBox("Smithing");
		smitheButton.setBounds(20, 90, 95, 21);
		smitheButton.setFont(normal);
		smitheButton.addActionListener(this);
		panel4.add(smitheButton);
		
		slayereButton = new JCheckBox("Slayer");
		slayereButton.setBounds(20, 115, 95, 21);
		slayereButton.setFont(normal);
		slayereButton.addActionListener(this);
		panel4.add(slayereButton);
		
		rceButton = new JCheckBox("Runecrafting");
		rceButton.setBounds(20, 140, 95, 21);
		rceButton.setFont(normal);
		rceButton.addActionListener(this);
		panel4.add(rceButton);
		
		prayeButton = new JCheckBox("Prayer");
		prayeButton.setBounds(20, 165, 95, 21);
		prayeButton.setFont(normal);
		prayeButton.addActionListener(this);
		panel4.add(prayeButton);
		
		mineeButton = new JCheckBox("Mining");
		mineeButton.setBounds(20, 190, 95, 21);
		mineeButton.setFont(normal);
		mineeButton.addActionListener(this);
		panel4.add(mineeButton);
		
		herbeButton = new JCheckBox("Herblore");
		herbeButton.setBounds(20, 215, 95, 21);
		herbeButton.setFont(normal);
		herbeButton.addActionListener(this);
		panel4.add(herbeButton);
		
		fletcheButton = new JCheckBox("Fletching");
		fletcheButton.setBounds(20, 240, 95, 21);
		fletcheButton.setFont(normal);
		fletcheButton.addActionListener(this);
		panel4.add(fletcheButton);
		
		fisheButton = new JCheckBox("Fishing");
		fisheButton.setBounds(20, 265, 95, 21);
		fisheButton.setFont(normal);
		fisheButton.addActionListener(this);
		panel4.add(fisheButton);
		
		JLabel label2 = new JLabel( "<html><b>Enabled" );
		label2.setFont(normal);
		label2.setBounds( 110, 15, 80, 20 );
		panel4.add( label2 );
		
		fmeButton = new JCheckBox("Firemaking");
		fmeButton.setBounds(120, 40, 95, 21);
		fmeButton.setFont(normal);
		fmeButton.addActionListener(this);
		panel4.add(fmeButton);
		
		farmeButton = new JCheckBox("Farming");
		farmeButton.setBounds(120, 65, 95, 21);
		farmeButton.setFont(normal);
		farmeButton.addActionListener(this);
		panel4.add(farmeButton);
		
		crafteButton = new JCheckBox("Crafting");
		crafteButton.setBounds(120, 90, 95, 21);
		crafteButton.setFont(normal);
		crafteButton.addActionListener(this);
		panel4.add(crafteButton);
		
		cookeButton = new JCheckBox("Cooking");
		cookeButton.setBounds(120, 115, 95, 21);
		cookeButton.setFont(normal);
		cookeButton.addActionListener(this);
		panel4.add(cookeButton);
		
		agilityeButton = new JCheckBox("Agility");
		agilityeButton.setBounds(120, 140, 95, 21);
		agilityeButton.setFont(normal);
		agilityeButton.addActionListener(this);
		panel4.add(agilityeButton);
		
	}

	private static final Object[][] rowData = {{}};
    private static final Object[] columnNames = {"ID", "Name", "Rank", "Donator"};
	private static DefaultTableModel model;
	
	public void createPage5()
	{	
		panel5 = new JPanel();
		panel5.setLayout( null );
		
		JLabel label1 = new JLabel( "<html><b>Accounts" );
		label1.setFont(normal);
		label1.setBounds( 110, 0, 80, 20 );
		panel5.add( label1 );
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 25, 230, 240);
		panel5.add(scrollPane);
		
		table = new JTable(){
			 public boolean isCellEditable(int row, int column) {                
	                return false;               
	        };
		};
		scrollPane.setViewportView(table);
		
		model = new DefaultTableModel(rowData, columnNames);
		table.setModel(model);

		model.removeRow(0);
		
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getColumnModel().getColumn(2).setPreferredWidth(70);
		table.getColumnModel().getColumn(3).setResizable(false);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.setRowHeight(20);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(new CustomTableCellRenderer());
		table.getColumnModel().getColumn(3).setCellRenderer(new CustomTableCellRenderer());
		
		label52 = new JLabel( "<html><b>Name:</b>" );
		label52.setFont(normal);
		label52.setBounds( 245, 0, 150, 20 );
		panel5.add( label52 );
		
		label53 = new JLabel( "<html><b>Rank:</b>" );
		label53.setFont(normal);
		label53.setBounds( 245, 20, 150, 20 );
		panel5.add( label53 );
		
		label54 = new JLabel( "<html><b>IP:</b>" );
		label54.setFont(normal);
		label54.setBounds( 245, 40, 150, 20 );
		panel5.add( label54 );
		
		label5Mac = new JLabel( "<html><b>MAC:</b>" );
		label5Mac.setFont(normal);
		label5Mac.setBounds( 245, 60, 150, 20 );
		panel5.add( label5Mac );
		
		label59 = new JLabel( "<html><b>Donator:</b>" );
		label59.setFont(normal);
		label59.setBounds( 245, 80, 80, 20 );
		panel5.add( label59 );
		
		label55 = new JLabel( "<html><b>Muted:</b>" );
		label55.setFont(normal);
		label55.setBounds( 245, 100, 80, 20 );
		panel5.add( label55 );
		
		label56 = new JLabel( "<html><b>Level:</b>" );
		label56.setFont(normal);
		label56.setBounds( 245, 120, 80, 20 );
		panel5.add( label56 );
		
		label57 = new JLabel( "<html><b>Coords:</b>" );
		label57.setFont(normal);
		label57.setBounds( 245, 140, 150, 20 );
		panel5.add( label57 );
		
		JButton btnKick = new JButton("Kick");
		btnKick.setFont(normal);
		btnKick.setBounds(245, 165, 70, 21);
		panel5.add(btnKick);
		btnKick.addActionListener(this);
		
		JButton btnMute = new JButton("Mute");
		btnMute.setFont(normal);
		btnMute.setBounds(245, 190, 70, 21);
		panel5.add(btnMute);
		btnMute.addActionListener(this);
		
		JButton btnBan = new JButton("Ban");
		btnBan.setFont(normal);
		btnBan.setBounds(320, 165, 70, 21);
		panel5.add(btnBan);
		btnBan.addActionListener(this);
		
		JButton btnIpBan = new JButton("IP Ban");
		btnIpBan.setFont(normal);
		btnIpBan.setBounds(320, 190, 70, 21);
		panel5.add(btnIpBan);
		btnIpBan.addActionListener(this);
		
		JLabel label8 = new JLabel( "<html><b>Teleport To:" );
		label8.setFont(normal);
		label8.setBounds( 250, 260, 150, 20 );
		panel5.add( label8 );
		
		field51 = new JTextField();
		field51.setBounds( 250, 285, 100, 20 );
		panel5.add( field51 );
		
		JButton btnTele = new JButton("Tele");
		btnTele.setFont(normal);
		btnTele.setBounds(355, 285, 60, 21);
		panel5.add(btnTele);
		btnTele.addActionListener(this);
		
		JButton btnGetData = new JButton("Get Data");
		btnGetData.setFont(normal);
		btnGetData.setBounds(10, 270, 85, 21);
		panel5.add(btnGetData);
		btnGetData.addActionListener(this);
		
		JButton btnGetPos = new JButton("Get Pos");
		btnGetPos.setFont(normal);
		btnGetPos.setBounds(100, 270, 75, 21);
		panel5.add(btnGetPos);
		btnGetPos.addActionListener(this);
		
		JButton btnMap = new JButton("Map");
		btnMap.setFont(normal);
		btnMap.setBounds(180, 270, 60, 21);
		panel5.add(btnMap);
		btnMap.addActionListener(this);
		
		followeButton = new JCheckBox("Follow");
		followeButton.setBounds(180, 292, 65, 21);
		followeButton.setFont(normal);
		followeButton.addActionListener(this);
		panel5.add(followeButton);
		
		rbMenuItem51 = new JRadioButton("Player");
		rbMenuItem51.setFont(normal);
		rbMenuItem51.setSelected(true);
		rbMenuItem51.setBounds( 240, 220, 60, 20 );
		panel5.add(rbMenuItem51);
		
		rbMenuItem52 = new JRadioButton("Mod");
		rbMenuItem52.setFont(normal);
		rbMenuItem52.setSelected(true);
		rbMenuItem52.setBounds( 305, 220, 50, 20 );
		panel5.add(rbMenuItem52);
		
		rbMenuItem53 = new JRadioButton("Admin");
		rbMenuItem53.setFont(normal);
		rbMenuItem53.setSelected(true);
		rbMenuItem53.setBounds( 360, 220, 60, 20 );
		panel5.add(rbMenuItem53);
		
		//Group the radio buttons.
	    ButtonGroup group1 = new ButtonGroup();
	    group1.add(rbMenuItem51);
	    group1.add(rbMenuItem52);
	    group1.add(rbMenuItem53);
	    
	    rbMenuItem54 = new JRadioButton("F2P");
		rbMenuItem54.setFont(normal);
		rbMenuItem54.setSelected(true);
		rbMenuItem54.setBounds( 240, 240, 45, 20 );
		panel5.add(rbMenuItem54);
		
		rbMenuItem55 = new JRadioButton("P2P");
		rbMenuItem55.setFont(normal);
		rbMenuItem55.setSelected(true);
		rbMenuItem55.setBounds( 290, 240, 45, 20 );
		panel5.add(rbMenuItem55);
		
	    //Group the radio buttons.
	    ButtonGroup group2 = new ButtonGroup();
	    group2.add(rbMenuItem54);
	    group2.add(rbMenuItem55);
	    
	    JButton btnSetRank = new JButton("Set");
	    btnSetRank.setFont(normal);
	    btnSetRank.setBounds(360, 240, 60, 20);
		panel5.add(btnSetRank);
		btnSetRank.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	setPlayerRank();
            }
        });
	    
	}
	
	public void createPage6()
	{	
		panel6 = new JPanel();
		panel6.setLayout( null );
		
		JLabel label1 = new JLabel( "<html><b>EasyRS06</b> was created by <b><font color=blue>Mige</b>" );
		label1.setFont(normal);
		label1.setBounds( 110, 15, 200, 20 );
		panel6.add( label1 );
		
	}
	
	private static final Object[][] rowData7 = {{}};
    private static final Object[] columnNames7 = {"Name", "Rank"};
	private static DefaultTableModel model7;
	
	Hplayer currentDPlayer;
	JLabel currentDPlayerName;
	JLabel currentDPlayerIP;
	JLabel currentDPlayerMAC;
	JLabel DMess;
	
	public void createPage7()
	{	
		panel7 = new JPanel();
		panel7.setLayout( null );
		
		JLabel label1 = new JLabel( "<html><b>Accounts" );
		label1.setFont(normal);
		label1.setBounds( 110, 0, 80, 20 );
		panel7.add( label1 );
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 25, 230, 240);
		panel7.add(scrollPane);
		
		table7 = new JTable(){
			 public boolean isCellEditable(int row, int column) {                
	                return false;               
	        };
		};
		scrollPane.setViewportView(table7);
		
		model7 = new DefaultTableModel(rowData7, columnNames7);
		table7.setModel(model7);

		model7.removeRow(0);
		
		table7.getColumnModel().getColumn(0).setResizable(false);
		table7.getColumnModel().getColumn(0).setPreferredWidth(180);
		table7.getColumnModel().getColumn(1).setResizable(false);
		table7.getColumnModel().getColumn(1).setPreferredWidth(70);
		table7.setRowHeight(20);
		
		table7.getSelectionModel().addListSelectionListener(
		        new ListSelectionListener() {
		            public void valueChanged(ListSelectionEvent event) {
		                int viewRow = table7.getSelectedRow();
		                if (viewRow < 0) {
		                } else {
		                	String name = (String) table7.getModel().getValueAt(viewRow, 0);
		                	currentDPlayer = findPlayer(name);
		                	if(currentDPlayer != null)
		                		setDinfo();
		                }
		            }
		        });
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		
		table7.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table7.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		
		currentDPlayerName = new JLabel( "<html><b>NAME" );
		currentDPlayerName.setFont(normal);
		currentDPlayerName.setBounds( 296, 0, 80, 20 );
		panel7.add( currentDPlayerName);
		currentDPlayerName.setHorizontalAlignment( JLabel.CENTER );
		
		currentDPlayerIP = new JLabel( "<html><b>IP" );
		currentDPlayerIP.setFont(normal);
		currentDPlayerIP.setBounds( 296, 10, 80, 20 );
		panel7.add( currentDPlayerIP);
		currentDPlayerIP.setHorizontalAlignment( JLabel.CENTER );
		
		currentDPlayerMAC = new JLabel( "<html><b>MAC" );
		currentDPlayerMAC.setFont(normal);
		currentDPlayerMAC.setBounds( 276, 20, 120, 20 );
		panel7.add( currentDPlayerMAC);
		currentDPlayerMAC.setHorizontalAlignment( JLabel.CENTER );
		
		DMess = new JLabel( "" );
		DMess.setFont(normal);
		DMess.setBounds( 240, 150, 200, 20 );
		panel7.add( DMess);
		DMess.setHorizontalAlignment( JLabel.CENTER );
		
		JLabel label3 = new JLabel( "<html><b>Paypal:" );
		label3.setFont(normal);
		label3.setBounds( 320, 30, 80, 20 );
		panel7.add( label3 );
		
		textField_Paypal = new JTextField();
		textField_Paypal.setBounds(250, 55, 170, 20);
		panel7.add(textField_Paypal);
		textField_Paypal.setColumns(10);
		
		JLabel label4 = new JLabel( "<html><b>Amount to add:" );
		label4.setFont(normal);
		label4.setBounds( 300, 70, 80, 20 );
		panel7.add( label4 );
		
		textField_Amount = new JTextField();
		textField_Amount.setBounds(250, 95, 170, 20);
		panel7.add(textField_Amount);
		textField_Amount.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.setBounds(280, 130, 65, 20);
		panel7.add(btnSend);
		btnSend.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	if(currentDPlayer != null)
            		sendDonatorPoints(true);
            }
        });
		
		JButton btnBan = new JButton("Ban");
		btnBan.setBounds(355, 130, 65, 20);
		panel7.add(btnBan);
		btnBan.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	if(currentDPlayer != null)
            		sendDonatorPoints(false);
            }
        });
		
		JLabel label5 = new JLabel( "<html><b>Forum account:" );
		label5.setFont(normal);
		label5.setBounds( 300, 160, 80, 20 );
		panel7.add( label5 );
		
		textField_Linked = new JTextField();
		textField_Linked.setBounds(250, 185, 170, 20);
		panel7.add(textField_Linked);
		textField_Linked.setColumns(10);
		
		JLabel label6 = new JLabel( "<html><b>Position:" );
		label6.setFont(normal);
		label6.setBounds( 310, 210, 80, 20 );
		panel7.add( label6 );
		
		textField_Ppos = new JTextField();
		textField_Ppos.setBounds(250, 235, 170, 20);
		panel7.add(textField_Ppos);
		textField_Ppos.setColumns(10);
		
		JLabel label2 = new JLabel( "<html><b>Search" );
		label2.setFont(normal);
		label2.setBounds( 110, 265, 80, 20 );
		panel7.add( label2 );
		
		textField_Plist = new JTextField();
		textField_Plist.setBounds(60, 285, 140, 20);
		panel7.add(textField_Plist);
		textField_Plist.setColumns(10);
	    
		JButton btnPreviousName = new JButton("<");
		btnPreviousName.setBounds(5, 285, 50, 20);
		panel7.add(btnPreviousName);
		btnPreviousName.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	prevName();
            }
        });
		
		JButton btnNextName = new JButton(">");
		btnNextName.setBounds(205, 285, 50, 20);
		panel7.add(btnNextName);
		btnNextName.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	nextName();
            }
        });
	}
	
	void prevName(){
		String value = textField_Plist.getText().toLowerCase();
		int rowi = 0;
		if(table7.getSelectedRow() != -1)
			rowi = table7.getSelectedRow()-1;
		for (int row = rowi; row >= 0; row--) {
			Object obj1 = GetData(table7, row, 0);
			if(obj1 != null){
				String tVal = ((String) obj1).toLowerCase();
				if (tVal.contains(value)) {
					// this will automatically set the view of the scroll in the location of the value
                    table7.scrollRectToVisible(table7.getCellRect(row, 0, true));
                    // this will automatically set the focus of the searched/selected row/value
                    table7.setRowSelectionInterval(row, row);
                    break;
				}
			}
		}
	}
	
	void nextName(){
		String value = textField_Plist.getText().toLowerCase();
		int rowi = 0;
		if(table7.getSelectedRow() != -1)
			rowi = table7.getSelectedRow()+1;
		for (int row = rowi; row <= table7.getRowCount() - 1; row++) {
			Object obj1 = GetData(table7, row, 0);
			if(obj1 != null){
				String tVal = ((String) obj1).toLowerCase();
				if (tVal.contains(value)) {
					// this will automatically set the view of the scroll in the location of the value
                    table7.scrollRectToVisible(table7.getCellRect(row, 0, true));
                    // this will automatically set the focus of the searched/selected row/value
                    table7.setRowSelectionInterval(row, row);
                    break;
				}
			}
		}
	}
	
	void setDinfo(){
		textField_Paypal.setText(currentDPlayer.getPaypal());
		textField_Amount.setText("");
		textField_Linked.setText(currentDPlayer.getLinkedAcc());
		textField_Ppos.setText(currentDPlayer.getX()+","+currentDPlayer.getY()+","+currentDPlayer.getZ());
		DMess.setText("");
		currentDPlayerName.setText(currentDPlayer.getUsername());
		currentDPlayerIP.setText(currentDPlayer.getLastIp());
		currentDPlayerMAC.setText(currentDPlayer.getMACaddress());
	}
	
	boolean sendDonatorPoints(boolean donate){
		for (Player player : World.getPlayers()) {
			if(player == null)
				continue;
			if(player.getUsername().equals(currentDPlayer.getUsername())){
				if(donate){
				if(textField_Paypal.getText() != null)
					if(!textField_Paypal.getText().equals("")){
						player.setPaypal(textField_Paypal.getText());
						currentDPlayer.setPaypal(textField_Paypal.getText());
					}
				if(textField_Linked.getText() != null)
					if(!textField_Linked.getText().equals("")){
						player.setLinkedAcc(textField_Linked.getText());
						currentDPlayer.setLinkedAcc(textField_Linked.getText());
					}
				if(textField_Amount.getText() != null)
					if(!textField_Amount.getText().equals("")){
						int a = Integer.parseInt(textField_Amount.getText());
						if(a > 0)
							player.addDonatorPoints(a);
						DMess.setText(a+" Donator Points sent!");
					}
				}else{
					if(textField_Amount.getText() != null)
						if(!textField_Amount.getText().equals("")){
							int hours = Integer.parseInt(textField_Amount.getText());
							if(hours > 0){
								player.setBanExpire(System.currentTimeMillis() + (hours * 60 * 60 * 1000));
								player.disconnect();
							}
							DMess.setText("Banned for "+hours+" hours.");
						}
				}
				textField_Amount.setText("");
				return true;
			}
		}
		if(donate){
		if(textField_Paypal.getText() != null)
			if(!textField_Paypal.getText().equals(""))
				currentDPlayer.setPaypal(textField_Paypal.getText());
		if(textField_Linked.getText() != null)
			if(!textField_Linked.getText().equals(""))
				currentDPlayer.setLinkedAcc(textField_Linked.getText());
		if(textField_Ppos.getText() != null)
			if(!textField_Ppos.getText().equals("")){
				String[] s4 = textField_Ppos.getText().split(",");
				if(s4.length == 3){
					int x = Integer.parseInt(s4[0]);
					int y = Integer.parseInt(s4[1]);
					int z = Integer.parseInt(s4[2]);
					currentDPlayer.setX(x);
					currentDPlayer.setY(y);
					currentDPlayer.setZ(z);
				}
			}
		if(textField_Amount.getText() != null)
			if(!textField_Amount.getText().equals("")){
				int a = Integer.parseInt(textField_Amount.getText());
				if(a > 0)
					currentDPlayer.addDonatorPoints(a);
				DMess.setText(a+" Donator Points sent!");
			}
		}else{
			if(textField_Amount.getText() != null)
				if(!textField_Amount.getText().equals("")){
					int hours = Integer.parseInt(textField_Amount.getText());
					if(hours > 0){
						currentDPlayer.setBanExpire(System.currentTimeMillis() + (hours * 60 * 60 * 1000));
					}
					DMess.setText("Banned for "+hours+" hours.");
				}
		}
		PlayerSave.savePlayerFile(currentDPlayer);
		textField_Amount.setText("");
		return true;
	}
	
    // Main method to get things started
	public static void main( String args[] )
	{
		// Create an instance of the test application
		Cpanel mainFrame	= new Cpanel();
		mainFrame.setVisible( true );
	}
	
	public static void refreshCpanel(){
		updatePage1();
		updatePlayers();
		m.repaint();
	}

	static int rLogin = 0;
	
	public static void updateSettings(){
		String s = field21.getText();
		Constants.SERVER_NAME = s;
		String s2 = field22.getText();
		Constants.MAX_PLAYERS_AMOUNT = Integer.parseInt(s2);
		String s3 = field23.getText();
		//Constants.EXP_RATE = Double.parseDouble(s3); //the xp rate is set in the cpanel
		String[] s4 = field24.getText().split(",");
		Constants.START_X = Integer.parseInt(s4[0]);
		Constants.START_Y = Integer.parseInt(s4[1]);
		Constants.START_Z = Integer.parseInt(s4[2]);
		String[] s5 = field25.getText().split(",");
		Constants.RESPAWN_X = Integer.parseInt(s5[0]);
		Constants.RESPAWN_Y = Integer.parseInt(s5[1]);
		Constants.RESPAWN_Z = Integer.parseInt(s5[2]);
		Constants.F2P_CONTENT_ONLY = f2pceButton.isSelected();
		Constants.DUELING_DISABLED = !(dueleButton.isSelected());
		Constants.ADMINS_CAN_INTERACT = adminieButton.isSelected();
		Constants.ITEM_SPAWNING = itemspeButton.isSelected();
		Constants.FUN_PK = funpkeButton.isSelected();
		Constants.PK_WORLD = pkworldeButton.isSelected();
		Constants.RESTRICT_LOGIN = rLogin;
		refreshCpanel();
	}
	
	public static void updateAdvancedSettings(){
		String s = field31.getText();
		Constants.PORT_NUMBER = Integer.parseInt(s);
		String s2 = field32.getText();
		Constants.CLIENT_VERSION = Integer.parseInt(s2);
		Constants.MYSQL_ENABLED = mysqleButton.isSelected();
		if(mysqleButton.isSelected()){
		String s3 = field35.getText();
		Constants.DB_DRIVER = s3;
		String s4 = field36.getText();
		Constants.GAME_DB_URL = s4;
		String s5 = field37.getText();
		Constants.GAME_DB_USER = s5;
		String s6 = field38.getText();
		Constants.GAME_DB_PASS = s6;
		}
		Constants.RSA_CHECK = rsaeButton.isSelected();
		if(rsaeButton.isSelected()){
		String s7 = field33.getText();
		Constants.key1 = s7;
		String s8 = field34.getText();
		Constants.key2 = s8;
		}
		Constants.HIGHSCORES_ENABLED = hiscoreseButton.isSelected();
		Constants.SERVER_DEBUG = debugeButton.isSelected();
		Constants.DEVELOPER_MODE = developeButton.isSelected();
		refreshCpanel();
	}
	
	public static void setSkills(){
		Constants.WOODCUTTING_ENABLED = wceButton.isSelected();
		Constants.THIEVING_ENABLED  = thiefeButton.isSelected();
		Constants.SMITHING_ENABLED = smitheButton.isSelected();
		Constants.SLAYER_ENABLED = slayereButton.isSelected();
		Constants.RUNECRAFTING_ENABLED = rceButton.isSelected();
		Constants.PRAYER_ENABLED = prayeButton.isSelected();
		Constants.MINING_ENABLED = mineeButton.isSelected();
		Constants.HERBLORE_ENABLED = herbeButton.isSelected();
		Constants.FLETCHING_ENABLED = fletcheButton.isSelected();
		Constants.FISHING_ENABLED = fisheButton.isSelected();
		Constants.FIREMAKING_ENABLED = fmeButton.isSelected();
		Constants.FARMING_ENABLED = farmeButton.isSelected();
		Constants.CRAFTING_ENABLED = crafteButton.isSelected();
		Constants.COOKING_ENABLED = cookeButton.isSelected();
		Constants.AGILITY_ENABLED = agilityeButton.isSelected();
		refreshCpanel();
	}
	
	public static void updatePage1(){
		if(Server.serverStatus == 2){//Server.runner.isAlive()
			status = "<font color=green>Online";
			btnStartServer.setEnabled(false);
			btnRestartServer.setEnabled(true);
			btnShutdownServer.setEnabled(true);
			btnSend.setEnabled(true);
		}
		if(Server.serverStatus == 1){
			status = "<font color=black>Starting up...";
			btnStartServer.setEnabled(false);
			btnRestartServer.setEnabled(true);
			btnShutdownServer.setEnabled(true);
			btnSend.setEnabled(false);
		}
		if(Server.serverStatus == 3){
			status = "<font color=black>Shutting down...";
			btnStartServer.setEnabled(false);
			btnRestartServer.setEnabled(false);
			btnShutdownServer.setEnabled(false);
			btnSend.setEnabled(true);
		}
		if(Server.serverStatus == 0){
			status = "<font color=red>Offline";
			btnStartServer.setEnabled(true);
			btnRestartServer.setEnabled(false);
			btnShutdownServer.setEnabled(false);
			btnSend.setEnabled(false);
		}
		label11.setText("<html>Server Status: <b>"+status);
		runTime = Server.runTimeMin;
		name = Constants.SERVER_NAME;
		label13.setText("<html>Server Name: <font color=#1589FF>"+name);
		String runningTime = (runTime/60 > 0 ? (runTime/60)+" hours "+(runTime%60)+" mins" : runTime+" mins" );
		//label16.setText("<html>Runtime: <font color=#1589FF>"+runTime+" mins");
		label16.setText("<html>Runtime: <font color=#1589FF>"+runningTime);
		onlinePlayers = Server.cPAmount;
		maxPlayers = Constants.MAX_PLAYERS_AMOUNT;
		onlineMods = Server.cMAmount;
		onlineAdmins = Server.cAAmount;
		label12.setText("<html>Users Online: "+onlinePlayers+"/"+maxPlayers+" <font color=blue>("+onlineMods+" Mods, <font color=orange>"+onlineAdmins+" Admins)");
		
	}
	
	public void restoreDefaults(){
		name = "OSRSPK";
		maxPlayers = 2000;
		Constants.SERVER_NAME = name;
		Constants.MAX_PLAYERS_AMOUNT = maxPlayers;
		field21.setText("OSRSPK");
		field22.setText("2000");
		field23.setText("1.5");
		field24.setText("3233,3230,0");
		field25.setText("3222,3218,0");
		f2pceButton.setSelected(false);
		dueleButton.setSelected(true);
		adminieButton.setSelected(true);
		itemspeButton.setSelected(false);
		funpkeButton.setSelected(false);
		pkworldeButton.setSelected(false);
		rLogin = 0;
		rbMenuItem1.setSelected(true);
		
		field31.setText("43594");
		field32.setText("317");
		mysqleButton.setSelected(false);
		field35.setText("");
		field36.setText("");
		field37.setText("");
		field38.setText("");
		rsaeButton.setSelected(false);
		field33.setText("");
		field34.setText("");
		hiscoreseButton.setSelected(false);
		debugeButton.setSelected(false);
		developeButton.setSelected(false);
		
		wceButton.setSelected(true);
		thiefeButton.setSelected(true);
		smitheButton.setSelected(true);
		slayereButton.setSelected(true);
		rceButton.setSelected(true);
		prayeButton.setSelected(true);
		mineeButton.setSelected(true);
		herbeButton.setSelected(true);
		fletcheButton.setSelected(true);
		fisheButton.setSelected(true);
		fmeButton.setSelected(true);
		farmeButton.setSelected(false);
		crafteButton.setSelected(true);
		cookeButton.setSelected(true);
		agilityeButton.setSelected(false);
	}
	
	public static void writeSettings() throws IOException {
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("data/settings.dat")));
		String s = field21.getText();
		out.writeUTF(s);
		String s2 = field22.getText();
		out.writeShort(Integer.parseInt(s2));
		String s3 = field23.getText();
		out.writeDouble(Double.parseDouble(s3));
		String s4 = field24.getText();
		out.writeUTF(s4);
		String s5 = field25.getText();
		out.writeUTF(s5);
		out.writeBoolean(f2pceButton.isSelected());
		out.writeBoolean(dueleButton.isSelected());
		out.writeBoolean(adminieButton.isSelected());
		out.writeBoolean(itemspeButton.isSelected());
		out.writeBoolean(funpkeButton.isSelected());
		out.writeBoolean(pkworldeButton.isSelected());
		out.writeByte(rLogin);
		
		String s6 = field31.getText();
		out.writeInt(Integer.parseInt(s6));
		String s7 = field32.getText();
		out.writeShort(Integer.parseInt(s7));
		out.writeBoolean(mysqleButton.isSelected());
		String s8 = field35.getText();
		out.writeUTF(s8);
		String s9 = field36.getText();
		out.writeUTF(s9);
		String s10 = field37.getText();
		out.writeUTF(s10);
		String s11 = field38.getText();
		out.writeUTF(s11);
		out.writeBoolean(rsaeButton.isSelected());
		String s12 = field33.getText();
		out.writeUTF(s12);
		String s13 = field34.getText();
		out.writeUTF(s13);
		out.writeBoolean(hiscoreseButton.isSelected());
		out.writeBoolean(debugeButton.isSelected());
		out.writeBoolean(developeButton.isSelected());
		
		out.writeBoolean(wceButton.isSelected());
		out.writeBoolean(thiefeButton.isSelected());
		out.writeBoolean(smitheButton.isSelected());
		out.writeBoolean(slayereButton.isSelected());
		out.writeBoolean(rceButton.isSelected());
		out.writeBoolean(prayeButton.isSelected());
		out.writeBoolean(mineeButton.isSelected());
		out.writeBoolean(herbeButton.isSelected());
		out.writeBoolean(fletcheButton.isSelected());
		out.writeBoolean(fisheButton.isSelected());
		out.writeBoolean(fmeButton.isSelected());
		out.writeBoolean(farmeButton.isSelected());
		out.writeBoolean(crafteButton.isSelected());
		out.writeBoolean(cookeButton.isSelected());
		out.writeBoolean(agilityeButton.isSelected());
		
		out.close();
	}
	
	public static void readSettings() throws IOException {
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream("data/settings.dat")));
		name = in.readUTF();
		maxPlayers = in.readUnsignedShort();
		Constants.SERVER_NAME = name;
		Constants.MAX_PLAYERS_AMOUNT = maxPlayers;
		field21.setText(name);
		field22.setText(""+maxPlayers);
		field23.setText(""+in.readDouble());
		field24.setText(in.readUTF());
		field25.setText(in.readUTF());
		f2pceButton.setSelected(in.readBoolean());
		dueleButton.setSelected(in.readBoolean());
		adminieButton.setSelected(in.readBoolean());
		itemspeButton.setSelected(in.readBoolean());
		funpkeButton.setSelected(in.readBoolean());
		pkworldeButton.setSelected(in.readBoolean());
		rLogin = in.readByte();
		if(rLogin == 0)
			rbMenuItem1.setSelected(true);
		if(rLogin == 1)
			rbMenuItem2.setSelected(true);
		if(rLogin == 2)
			rbMenuItem3.setSelected(true);
		if(rLogin == 3)
			rbMenuItem4.setSelected(true);	
		field31.setText(""+in.readInt());
		field32.setText(""+in.readShort());
		mysqleButton.setSelected(in.readBoolean());
		field35.setText(in.readUTF());
		field36.setText(in.readUTF());
		field37.setText(in.readUTF());
		field38.setText(in.readUTF());
		rsaeButton.setSelected(in.readBoolean());
		field33.setText(in.readUTF());
		field34.setText(in.readUTF());
		hiscoreseButton.setSelected(in.readBoolean());
		debugeButton.setSelected(in.readBoolean());
		developeButton.setSelected(in.readBoolean());
		
		wceButton.setSelected(in.readBoolean());
		thiefeButton.setSelected(in.readBoolean());
		smitheButton.setSelected(in.readBoolean());
		slayereButton.setSelected(in.readBoolean());
		rceButton.setSelected(in.readBoolean());
		prayeButton.setSelected(in.readBoolean());
		mineeButton.setSelected(in.readBoolean());
		herbeButton.setSelected(in.readBoolean());
		fletcheButton.setSelected(in.readBoolean());
		fisheButton.setSelected(in.readBoolean());
		fmeButton.setSelected(in.readBoolean());
		farmeButton.setSelected(in.readBoolean());
		crafteButton.setSelected(in.readBoolean());
		cookeButton.setSelected(in.readBoolean());
		agilityeButton.setSelected(in.readBoolean());
		in.close();
		updateSettings();
		updateAdvancedSettings();
		setSkills();
		refreshCpanel();
	}
	
	Hplayer findPlayer(String name){
		for (Hplayer player : PlayerSave.listOfPlayersAll) {
			if(player.getUsername().equals(name))
				return player;
		}
		return null;
	}
	
	public static void loadAllPlayers(){
		for (Hplayer player : PlayerSave.listOfPlayersAll) {
			if(player == null)
				continue;
			if(playerAlreadyOnTable(player.getUsername()))
				continue;
			Vector newRow = new Vector();
			newRow.add(player.getUsername());
			String rights = "";
			if(player.getRights() == 0)
				rights = "Player";
			if(player.getRights() == 1)
				rights = "Mod";
			if(player.getRights() >= 2)
				rights = "Admin";
			newRow.add(rights);
			model7.addRow(newRow);
		}
	}
	
	public static boolean playerAlreadyOnTable(String name){
		for(int row = 0; row < table7.getRowCount(); row++){
			Object t_playerName = GetData(table7,row, 0);
			String tz_playerName = (String)""+t_playerName;
			if(tz_playerName.equals(name))
				return true;
		}
		return false;
	}
	
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
				boolean donator = player.isDonator();
				Vector newRow = new Vector();
				newRow.add(id);
				newRow.add(name);
				newRow.add(rights);
				newRow.add((donator ? "Yes" : "No"));
				model.addRow(newRow);
			}
		}
	}
	
	public static void clearInfo(){
		label52.setText("<html><b>Name:</b>");
		label53.setText("<html><b>Rank:</b>");
		label54.setText("<html><b>IP:</b>");
		label5Mac.setText("<html><b>MAC:</b>");
		label59.setText("<html><b>Donator:</b>");
		label55.setText("<html><b>Muted:</b>");
		label56.setText("<html><b>Level:</b>");
		label57.setText("<html><b>Coords:</b>");
		rbMenuItem51.setSelected(true);
		rbMenuItem54.setSelected(true);
		selectedPlayer = -1;
	}
	
	static int selectedPlayer = -1;
	
	public void showInfo(int id){
		Player player = World.players[id-1];
		String name = player.getUsername();
		String rights = "";
		if(player.getStaffRights() == 0){
			rights = "Player";
			rbMenuItem51.setSelected(true);
		}
		if(player.getStaffRights() == 1){
			rights = "Mod";
			rbMenuItem52.setSelected(true);
		}
		if(player.getStaffRights() >= 2){
			rights = "Admin";
			rbMenuItem53.setSelected(true);
		}
		String ip = player.getHost();
		String MAC = player.getMACaddress();
		boolean donator = player.isDonator();
		if(donator == false)
		rbMenuItem54.setSelected(true);
		if(donator == true)
		rbMenuItem55.setSelected(true);
		boolean muted = player.isMuted();
		int cb = player.getCombatLevel();
		String location = player.getPosition().getX()+","+player.getPosition().getY()+","+player.getPosition().getZ();
		label52.setText("<html><b>Name:</b> <font color=blue>"+name);
		label53.setText("<html><b>Rank:</b> <font color=blue>"+rights);
		label54.setText("<html><b>IP:</b> <font color=blue>"+ip);
		label5Mac.setText("<html><b>MAC:</b> <font color=blue>"+MAC);
		label59.setText("<html><b>Donator:</b> <font color=blue>"+(donator ? "Yes" : "No"));
		label55.setText("<html><b>Muted:</b> <font color=blue>"+(muted ? "Yes" : "No"));
		label56.setText("<html><b>Level:</b> <font color=blue>"+cb);
		label57.setText("<html><b>Coords:</b> <font color=blue>"+location);
	}
	
	public static void updatePlayerInfo(){
		Player player = World.players[selectedPlayer-1];
		String name = player.getUsername();
		String rights = "";
		if(player.getStaffRights() == 0){
			rights = "Player";
		}
		if(player.getStaffRights() == 1){
			rights = "Mod";
		}
		if(player.getStaffRights() >= 2){
			rights = "Admin";
		}
		String ip = player.getHost();
		String MAC = player.getMACaddress();
		boolean donator = player.isDonator();
		boolean muted = player.isMuted();
		int cb = player.getCombatLevel();
		String location = player.getPosition().getX()+","+player.getPosition().getY()+","+player.getPosition().getZ();
		label52.setText("<html><b>Name:</b> <font color=blue>"+name);
		label53.setText("<html><b>Rank:</b> <font color=blue>"+rights);
		label54.setText("<html><b>IP:</b> <font color=blue>"+ip);
		label5Mac.setText("<html><b>MAC:</b> <font color=blue>"+MAC);
		label59.setText("<html><b>Donator:</b> <font color=blue>"+(donator ? "Yes" : "No"));
		label55.setText("<html><b>Muted:</b> <font color=blue>"+(muted ? "Yes" : "No"));
		label56.setText("<html><b>Level:</b> <font color=blue>"+cb);
		label57.setText("<html><b>Coords:</b> <font color=blue>"+location);
	}
	
	public void setPlayerRank(){
		if(selectedPlayer != -1){
			Player player = World.players[selectedPlayer-1];
			int rights = 0;
			if(rbMenuItem51.isSelected())
				rights = 0;
			if(rbMenuItem52.isSelected())
				rights = 1;
			if(rbMenuItem53.isSelected())
				rights = 2;
			player.setStaffRights(rights);
			player.setDonator(rbMenuItem55.isSelected());
		}
	}
	
	public static int getPlayerRow(int playerId){
		for(int row = 0; row < table.getRowCount(); row++){
			Object t_playerId = GetData(table,row, 0);
			String tz_playerId = (String)""+t_playerId;
			int i = (Integer.parseInt(tz_playerId));
			if(i == playerId)
				return row;
		}
		return -1;
	}
	
	public static boolean playerAlreadyOnTable(int playerId){
		for(int row = 0; row < table.getRowCount(); row++){
			Object t_playerId = GetData(table,row, 0);
			String tz_playerId = (String)""+t_playerId;
			int i = (Integer.parseInt(tz_playerId));
			if(i == playerId)
				return true;
		}
		return false;
	}
	
	public static Object GetData(JTable table, int row_index, int col_index){
		  return table.getModel().getValueAt(row_index, col_index);
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand()=="Start Server"){
			/*try{
			System.out.println(Server.runner.isAlive()+ " "+Server.runner2.isAlive());
			}catch(Exception ex){}*/
			String[] arguments = new String[] {""};
			Server.main(arguments);
			try{
				writeSettings();
			}catch(Exception ex){}
			refreshCpanel();
		}
		if (e.getActionCommand()=="Shutdown Server"){
			//String[] arguments = new String[] {""};
			Server.initiateRestart();
			refreshCpanel();
		}
		if (e.getActionCommand()=="Send"){
			//System.out.println("Test");
			String s = field11.getText();
			Server.sendCPanelMessage(s);
		}
		if (e.getActionCommand()=="None"){
			rLogin = 0;
			refreshCpanel();
		}
		if (e.getActionCommand()=="P2P"){
			rLogin = 1;
			refreshCpanel();
		}
		if (e.getActionCommand()=="Mod"){
			rLogin = 2;
			refreshCpanel();
		}
		if (e.getActionCommand()=="Admin"){
			rLogin = 3;
			refreshCpanel();
		}
		if (e.getActionCommand()=="Set"){
			updateSettings();
			updateAdvancedSettings();
			setSkills();
			refreshCpanel();
		}
		if (e.getActionCommand()=="Defaults"){
			int yes = -1;
			yes = JOptionPane.showConfirmDialog(this, "Are you sure you want to restore all default values?", "Restore default values", JOptionPane.YES_NO_OPTION);
			if(yes == 0){
				restoreDefaults();
			}
			refreshCpanel();
		}
		if (e.getActionCommand()=="Get Data"){
			if(table.getSelectedRow() != -1){
				Object t_playerId = GetData(table, table.getSelectedRow(), 0);
				String tz_playerId = (String)""+t_playerId;
				int id = (Integer.parseInt(tz_playerId));
				selectedPlayer = id;
				showInfo(id);
				refreshCpanel();
			}
		}
		if (e.getActionCommand()=="Get Pos"){
			if(table.getSelectedRow() != -1){
				Object t_playerId = GetData(table, table.getSelectedRow(), 0);
				String tz_playerId = (String)""+t_playerId;
				int id = (Integer.parseInt(tz_playerId));
				Player player = World.players[id-1];
				String location = player.getPosition().getX()+","+player.getPosition().getY()+","+player.getPosition().getZ();
				field51.setText(location);
				refreshCpanel();
			}
		}
		if (e.getActionCommand()=="Tele"){
			if(selectedPlayer != -1){
				Player player = World.players[selectedPlayer-1];
				String[] s5 = field51.getText().split(",");
				player.teleport(new Position(Integer.parseInt(s5[0]), Integer.parseInt(s5[1]), Integer.parseInt(s5[2])));
				refreshCpanel();
			}
		}
		if (e.getActionCommand()=="Kick"){
			if(selectedPlayer != -1){
				Player player = World.players[selectedPlayer-1];
				player.disconnect();
				refreshCpanel();
			}
		}
		if (e.getActionCommand()=="Mute"){
			if(selectedPlayer != -1){
				Player player = World.players[selectedPlayer-1];
				int hours = 48;
				player.actionSender.sendMessage("You have been muted for "+hours+" hours");
				player.setMuteExpire(System.currentTimeMillis()+(hours*60*60*1000));
				refreshCpanel();
			}
		}
		if (e.getActionCommand()=="Ban"){
			if(selectedPlayer != -1){
				Player player = World.players[selectedPlayer-1];
				int hours = 100;
				player.setBanExpire(System.currentTimeMillis() + (hours * 60 * 60 * 1000));
				player.disconnect();
				refreshCpanel();
			}
		}
		if (e.getActionCommand()=="Map"){
			worldMap.setVisible(true);
		}
		if (e.getActionCommand()=="Show names"){
			m.showNames = showNamesButton.isSelected();
			m.repaint();
		}
	}
	
	public class MyActionH implements AdjustmentListener{
		public void adjustmentValueChanged(AdjustmentEvent ae){
			int value = ae.getValue();
			curX = value;
			String st = Integer.toString(value);
			//System.out.println("H "+st);
			m.x = (value/scale)+1536;
			//frame.setTitle(title+" - "+(m.x+64)+","+(m.y-64)+","+m.height+" "+getModeName(m.mode));
			m.repaint();
		}
	}
	
	public class MyActionV implements AdjustmentListener{
		public void adjustmentValueChanged(AdjustmentEvent ae){
			int value = ae.getValue();
			curY = value;
			String st = Integer.toString(value);
			//System.out.println("V "+st);
			m.y = 10432-(value/scale);
			//frame.setTitle(title+" - "+(m.x+64)+","+(m.y-64)+","+m.height+" "+getModeName(m.mode));
			m.repaint();
		}
	}
	
}