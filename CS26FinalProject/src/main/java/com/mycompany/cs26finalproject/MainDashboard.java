package com.mycompany.cs26finalproject;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class MainDashboard {
    GridBagConstraints gbc = new GridBagConstraints();

    // Store projects: project name -> Kanban content panel
    private HashMap<String, JPanel> projects = new HashMap<>();
    private JPanel kanbanContent; // Active Kanban content area
    private JPanel projectListPanel; // Panel for the list of projects
    private ButtonGroup projectButtonGroup; // Button group for radio buttons
    private JPanel mainContent; // Kanban board area

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

        // Vertical navigation bar (left pane)
        JPanel sideNavBar = new JPanel();
        sideNavBar.setLayout(new BorderLayout());
        sideNavBar.setBackground(Color.GREEN);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.2; // Small width for the nav bar
        gbc.weighty = 1;
        dashboard.add(sideNavBar, gbc);

        // Project list and "Add Project" button
        projectListPanel = new JPanel();
        projectListPanel.setLayout(new BoxLayout(projectListPanel, BoxLayout.Y_AXIS));
        projectListPanel.setBackground(Color.GREEN);

        JScrollPane projectListScrollPane = new JScrollPane(projectListPanel);
        projectListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sideNavBar.add(projectListScrollPane, BorderLayout.CENTER);

        JButton addProjectButton = new JButton("Add Project");
        sideNavBar.add(addProjectButton, BorderLayout.SOUTH);

        // Button group for projects
        projectButtonGroup = new ButtonGroup();

        // Main content area for the active Kanban board
        mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());
        mainContent.setBackground(Color.LIGHT_GRAY);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.8;
        gbc.weighty = 1;
        dashboard.add(mainContent, gbc);

        // Add project action
        addProjectButton.addActionListener(e -> {
            String projectName = JOptionPane.showInputDialog(dashboard, "Enter project name:");
            if (projectName != null && !projectName.trim().isEmpty() && !projects.containsKey(projectName)) {
                addProject(projectName);
                projectListPanel.revalidate();
                projectListPanel.repaint();
            }
        });

        dashboard.setVisible(true);
    }

    // Method to add a new project
    private void addProject(String projectName) {
        // Create new Kanban panel for the project
        JPanel projectKanban = new JPanel();
        projectKanban.setLayout(new GridBagLayout());
        JScrollPane projectScrollPane = new JScrollPane(projectKanban);
        projectScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        projectScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        // Add Kanban panel to the map
        projects.put(projectName, projectKanban);

        // Add project to the list in the left nav bar
        JRadioButton projectButton = new JRadioButton(projectName);
        projectButtonGroup.add(projectButton);
        projectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        projectButton.addActionListener(e -> setActiveProject(projectName));
        projectListPanel.add(projectButton);

        // If it's the first project, select it by default
        if (projects.size() == 1) {
            projectButton.setSelected(true);
            setActiveProject(projectName);
        }
    }

    // Set the active project and display its Kanban board
    private void setActiveProject(String projectName) {
        JPanel selectedKanban = projects.get(projectName);
        mainContent.removeAll();
        mainContent.add(selectedKanban, BorderLayout.CENTER);

        // Add "Add List" button for the active project
        JButton addListButton = new JButton("Add List");
        addListButton.addActionListener(e -> {
            String listName = JOptionPane.showInputDialog(mainContent, "Enter list name:");
            if (listName != null && !listName.trim().isEmpty()) {
                addKanbanColumn(selectedKanban, listName);
                selectedKanban.revalidate();
                selectedKanban.repaint();
            }
        });
        mainContent.add(addListButton, BorderLayout.SOUTH);

        mainContent.revalidate();
        mainContent.repaint();
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
