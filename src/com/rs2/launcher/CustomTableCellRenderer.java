package com.rs2.launcher;


import java.awt.Component;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
public class CustomTableCellRenderer extends DefaultTableCellRenderer 
{
    public Component getTableCellRendererComponent
       (JTable table, Object value, boolean isSelected,
       boolean hasFocus, int row, int column) 
    {
        Component cell = super.getTableCellRendererComponent
           (table, value, isSelected, hasFocus, row, column);
        
        if( value instanceof String ){
        	if(value.equals("Mod"))
        		cell.setBackground(new Color(175, 238, 238));
        	if(value.equals("Admin"))
        		cell.setBackground(new Color(152, 251, 152));
        	if(value.equals("Yes"))
        		cell.setBackground(new Color(34, 177, 76));
        	if(value.equals("Player") || value.equals("No"))
        		cell.setBackground( Color.white );
        }
        //g.setColor(new Color(Red2, Green2, Blue2));
        /*if( value instanceof Integer )
        {
            Integer amount = (Integer) value;
            if( amount.intValue() < 0 )
            {
                cell.setBackground( Color.red );
                // You can also customize the Font and Foreground this way
                // cell.setForeground();
                // cell.setFont();
            }
            else
            {
                cell.setBackground( Color.white );
            }
        }*/
        //cell.setBackground( Color.red );
        setHorizontalAlignment( JLabel.CENTER );
        return cell;
    }
}