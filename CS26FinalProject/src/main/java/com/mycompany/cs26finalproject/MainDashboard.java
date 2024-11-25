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
        dashboard.setTitle("Kanban Dashboard");
        dashboard.setLayout(new GridBagLayout());
        dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboard.setSize(1600, 1000);

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
        mainContent.setLayout(new BorderLayout());
        mainContent.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.8;
        gbc.weighty = 1;
        dashboard.add(mainContent, gbc);

        // Scrollable Kanban board
        JPanel kanbanContent = new JPanel();
        kanbanContent.setLayout(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(kanbanContent);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        mainContent.add(scrollPane, BorderLayout.CENTER);

        // Button to add new columns
        JPanel buttonPanel = new JPanel();
        JButton addColumnButton = new JButton("Add new list");
        buttonPanel.add(addColumnButton);
        mainContent.add(buttonPanel, BorderLayout.SOUTH);

        // Add column action
        addColumnButton.addActionListener(e -> {
            String columnName = JOptionPane.showInputDialog(dashboard, "Enter column name:");
            if (columnName != null && !columnName.trim().isEmpty()) {
                addKanbanColumn(kanbanContent, columnName);
                kanbanContent.revalidate();
                kanbanContent.repaint();
            }
        });

        dashboard.setVisible(true);
    }

    // Method to add a Kanban column dynamically
    private void addKanbanColumn(JPanel kanbanContent, String columnName) {
        GridBagConstraints columnGbc = new GridBagConstraints();
        columnGbc.fill = GridBagConstraints.BOTH;
        columnGbc.gridy = 0;
        columnGbc.gridx = kanbanContent.getComponentCount(); // Add next to existing columns
        columnGbc.weightx = 1; // Equal width for all columns
        columnGbc.weighty = 1;

        JPanel columnPanel = new JPanel();
        columnPanel.setLayout(new BorderLayout());
        columnPanel.setBackground(Color.LIGHT_GRAY);

        // Column title
        JLabel columnTitle = new JLabel(columnName, SwingConstants.CENTER);
        columnTitle.setOpaque(true);
        columnTitle.setBackground(Color.DARK_GRAY);
        columnTitle.setForeground(Color.WHITE);
        columnTitle.setFont(new Font("Arial", Font.BOLD, 16));
        columnPanel.add(columnTitle, BorderLayout.NORTH);

        // Task panel
        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
        taskPanel.setBackground(Color.LIGHT_GRAY);

        // Wrap task panel in a scroll pane
        JScrollPane taskScrollPane = new JScrollPane(taskPanel);
        taskScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        taskScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        columnPanel.add(taskScrollPane, BorderLayout.CENTER);

        // Button to add tasks
        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.addActionListener(e -> {
            String taskName = JOptionPane.showInputDialog(kanbanContent, "Enter task name:");
            if (taskName != null && !taskName.trim().isEmpty()) {
                addTaskToColumn(taskPanel, taskName);
                taskPanel.revalidate();
                taskPanel.repaint();
            }
        });
        columnPanel.add(addTaskButton, BorderLayout.SOUTH);

        kanbanContent.add(columnPanel, columnGbc);
    }

    // Method to add a task to a specific column
    private void addTaskToColumn(JPanel taskPanel, String taskName) {
        JLabel taskCard = new JLabel(taskName);
        taskCard.setOpaque(true);
        taskCard.setBackground(Color.WHITE);
        taskCard.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        taskCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        taskCard.setMaximumSize(new Dimension(200, 50));
        taskCard.setPreferredSize(new Dimension(200, 50));
        taskCard.setHorizontalAlignment(SwingConstants.CENTER);

        taskPanel.add(taskCard);
        taskPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between tasks
    }

    public static void main(String[] args) {
        MainDashboard dash = new MainDashboard();
    }
}
