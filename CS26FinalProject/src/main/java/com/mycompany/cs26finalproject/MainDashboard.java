/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cs26finalproject;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author dionardvale
 */
public class MainDashboard {
    JButton b1, b2, b3, b4, b5;
    JPanel panel1, panel2, panel3;
    GridBagConstraints gbc = new GridBagConstraints();
    
    
    public MainDashboard() {
        // Create the main frame (JFrame)
        JFrame dashboard = new JFrame();
        //todo: Change title
        dashboard.setTitle("Test");
        dashboard.setLayout(new GridBagLayout());
        dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboard.setSize(1600, 1000);
        dashboard.setVisible(true);
        
        gbc.fill = GridBagConstraints.BOTH;
        
          // Horizontal navigation bar
        JPanel topNavBar = new JPanel();
        topNavBar.setBackground(Color.BLUE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 0.1; // Small height for the nav bar
        dashboard.add(topNavBar, gbc);
        
        // Vertical navigation bar
        JPanel sideNavBar = new JPanel();
        sideNavBar.setBackground(Color.GREEN);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.2; // Small width for the nav bar
        gbc.weighty = 1;
        dashboard.add(sideNavBar, gbc);
        
        // Main content area
        JPanel mainContent = new JPanel();
        mainContent.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.8;
        gbc.weighty = 1;
        dashboard.add(mainContent, gbc);
    }
    public static void main (String[] args) {
           MainDashboard dash = new MainDashboard();

        
    }
}
