package com.mycompany.cs26finalproject;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MainDashboard {
    private GridBagConstraints gbc = new GridBagConstraints();

    private JPanel projectListPanel; // Panel for the list of projects
    private ButtonGroup projectButtonGroup; // Button group for project radio buttons
    private JPanel mainContent; // Kanban board area
    private int userID;
    private int currentUserID; // The logged-in user's ID

    public MainDashboard(int userID) {
        this.currentUserID = userID;

        JFrame dashboard = new JFrame();
        dashboard.setTitle("Kanban Dashboard");
        dashboard.setLayout(new GridBagLayout());
        dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboard.setSize(1600, 1000);

        gbc.fill = GridBagConstraints.BOTH;

        JPanel topNavBar = new JPanel();
        topNavBar.setBackground(Color.BLUE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 0.1;
        dashboard.add(topNavBar, gbc);

        JPanel sideNavBar = new JPanel();
        sideNavBar.setLayout(new BorderLayout());
        sideNavBar.setBackground(Color.GREEN);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.2;
        gbc.weighty = 1;
        dashboard.add(sideNavBar, gbc);

        projectListPanel = new JPanel();
        projectListPanel.setLayout(new BoxLayout(projectListPanel, BoxLayout.Y_AXIS));
        projectListPanel.setBackground(Color.GREEN);

        JScrollPane projectListScrollPane = new JScrollPane(projectListPanel);
        projectListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sideNavBar.add(projectListScrollPane, BorderLayout.CENTER);

        JButton addProjectButton = new JButton("Add Project");
        sideNavBar.add(addProjectButton, BorderLayout.SOUTH);

        projectButtonGroup = new ButtonGroup();

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

        loadProjectsFromDatabase();

        addProjectButton.addActionListener(e -> {
            String projectName = JOptionPane.showInputDialog(dashboard, "Enter project name:");
            if (projectName != null && !projectName.trim().isEmpty()) {
                addProjectToDatabase(projectName);
                addProjectToUI(projectName);
            }
        });

        dashboard.setVisible(true);
    }

    private void loadProjectsFromDatabase() {
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT projectName FROM projects WHERE userID = ?")) {

            stmt.setInt(1, currentUserID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String projectName = rs.getString("projectName");
                addProjectToUI(projectName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load projects from database.");
        }
    }

    private void addProjectToDatabase(String projectName) {
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement("INSERT INTO projects (projectName, userID) VALUES (?, ?)");) {

            stmt.setString(1, projectName);
            stmt.setInt(2, currentUserID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add project to database.");
        }
    }

    private void addProjectToUI(String projectName) {
        JRadioButton projectButton = new JRadioButton(projectName);
        projectButtonGroup.add(projectButton);
        projectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        projectButton.addActionListener(e -> setActiveProject(projectName));
        projectListPanel.add(projectButton);

        projectListPanel.revalidate();
        projectListPanel.repaint();
    }

    private void setActiveProject(String projectName) {
        mainContent.removeAll();
        mainContent.setLayout(new BorderLayout());

        JPanel kanbanBoard = loadKanbanBoardFromDatabase(projectName);
        mainContent.add(kanbanBoard, BorderLayout.CENTER);

        JButton addListButton = new JButton("Add List");
        addListButton.addActionListener(e -> {
            String listName = JOptionPane.showInputDialog(mainContent, "Enter list name:");
            if (listName != null && !listName.trim().isEmpty()) {
                addListToDatabase(projectName, listName);
                addKanbanColumn(kanbanBoard, listName);
            }
        });
        mainContent.add(addListButton, BorderLayout.SOUTH);

        mainContent.revalidate();
        mainContent.repaint();
    }

    private JPanel loadKanbanBoardFromDatabase(String projectName) {
        JPanel kanbanBoard = new JPanel(new GridBagLayout());

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT listName FROM lists WHERE projectID = (SELECT id FROM projects WHERE projectName = ? AND userID = ?)");) {

            stmt.setString(1, projectName);
            stmt.setInt(2, currentUserID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String listName = rs.getString("listName");
                int listID = rs.getInt("id");
                addKanbanColumn(kanbanBoard, listName, listID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load Kanban board from database.");
        }

        return kanbanBoard;
    }

    private void addListToDatabase(String projectName, String listName) {
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement("INSERT INTO lists (listName, projectID) VALUES (?, (SELECT id FROM projects WHERE projectName = ? AND userID = ?))")) {

            stmt.setString(1, listName);
            stmt.setString(2, projectName);
            stmt.setInt(3, currentUserID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add list to database.");
        }
    }

    private void addKanbanColumn(JPanel kanbanContent, String columnName, int listID) {
        GridBagConstraints columnGbc = new GridBagConstraints();
        columnGbc.fill = GridBagConstraints.BOTH;
        columnGbc.gridy = 0;
        columnGbc.gridx = kanbanContent.getComponentCount();
        columnGbc.weightx = 1;
        columnGbc.weighty = 1;

        JPanel columnPanel = new JPanel();
        columnPanel.setLayout(new BorderLayout());
        columnPanel.setBackground(Color.LIGHT_GRAY);

        JLabel columnTitle = new JLabel(columnName, SwingConstants.CENTER);
        columnTitle.setOpaque(true);
        columnTitle.setBackground(Color.DARK_GRAY);
        columnTitle.setForeground(Color.WHITE);
        columnTitle.setFont(new Font("Arial", Font.BOLD, 16));
        columnPanel.add(columnTitle, BorderLayout.NORTH);

        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
        taskPanel.setBackground(Color.LIGHT_GRAY);

        JScrollPane taskScrollPane = new JScrollPane(taskPanel);
        taskScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        taskScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        columnPanel.add(taskScrollPane, BorderLayout.CENTER);
        
        loadTasksFromDatabase(taskPanel, listID);
        
        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.addActionListener(e -> {
            String taskName = JOptionPane.showInputDialog(kanbanContent, "Enter task name:");
            if (taskName != null && !taskName.trim().isEmpty()) {
                addTaskToDatabase(columnName, taskName);
                addTaskToColumn(taskPanel, taskName);
            }
        });
        columnPanel.add(addTaskButton, BorderLayout.SOUTH);

        kanbanContent.add(columnPanel, columnGbc);
        kanbanContent.revalidate();
        kanbanContent.repaint();
    }
    
    private void loadTasksFromDatabase(JPanel taskPanel, int listID) {
        try (Connection connection = DatabaseConnector.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT taskName FROM tasks WHERE listID = ?")) {

            stmt.setInt(1, listID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String taskName = rs.getString("taskName");
                addTaskToColumn(taskPanel, taskName); // Add task to the column UI
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load tasks from database.");
        }
    }
    
    private void addTaskToDatabase(String listName, String taskName) {
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement("INSERT INTO tasks (taskName, listID) VALUES (?, (SELECT id FROM lists WHERE listName = ? AND projectID IN (SELECT id FROM projects WHERE userID = ?)))")) {

            stmt.setString(1, taskName);
            stmt.setString(2, listName);
            stmt.setInt(3, currentUserID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add task to database.");
        }
    }

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
        taskPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        taskPanel.revalidate();
        taskPanel.repaint();
    }

    public static void main(String[] args) {
        // Assuming the userID is passed as a command-line argument
        if (args.length > 0) {
            int userID = Integer.parseInt(args[0]);
            new MainDashboard(userID);  // Pass userID to the constructor
        }
    }
}