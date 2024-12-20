package com.mycompany.cs26finalproject;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class MainDashboard {
    private GridBagConstraints gbc = new GridBagConstraints();
    
    private JPanel projectListPanel; // Panel for the list of projects
    private ButtonGroup projectButtonGroup; // Button group for project radio buttons
    private JPanel mainContent; // Kanban board area
    private int userID;
    private int currentUserID; // The logged-in user's ID
    private String currentUserName;
    private JButton selectedButton = null; // Track the selected button
    
    public MainDashboard(int userID) {
        this.currentUserID = userID;
        currentUserName = getUserNameById(currentUserID);
        // Main Frame
        JFrame dashboard = new JFrame();
        dashboard.setTitle("Flowzone");
        dashboard.setLayout(new GridBagLayout());
        dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboard.setSize(1600, 1000);
        dashboard.getContentPane().setBackground(new Color(0, 102, 153)); // Dark blue background
        
        gbc.fill = GridBagConstraints.BOTH;
        
        // Top Navigation Bar
        JPanel topNavBar = new JPanel();
        topNavBar.setBackground(new Color(0, 102, 153)); // Dark blue background
        topNavBar.setPreferredSize(new Dimension(dashboard.getWidth(), 60));
        topNavBar.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        
        JLabel titleLabel = new JLabel("Flowzone");
        titleLabel.setForeground(new Color(255, 223, 0)); // Yellow text for contrast
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        topNavBar.add(titleLabel);
        
        // Add Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUserName);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        topNavBar.add(welcomeLabel);
        
        // Add Logout Button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(255, 77, 77)); // Red for logout
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dashboard, "Are you sure you want to log out?",
                    "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true);
                dashboard.dispose(); // Close the current dashboard window
            }
        });
        topNavBar.add(logoutButton);
        
        // Create the date/time label
        JLabel dateTimeLabel = new JLabel();
        dateTimeLabel.setForeground(Color.WHITE); // White text for contrast
        dateTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        topNavBar.add(dateTimeLabel);
        
        // Update the date/time label every second
        Timer timer = new Timer(1000, e -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            String currentTime = dateFormat.format(new Date());
            dateTimeLabel.setText(currentTime);
        });
        timer.start();
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 0.1;
        dashboard.add(topNavBar, gbc);
        
        // Side Navigation Bar
        JPanel sideNavBar = new JPanel();
        sideNavBar.setLayout(new BorderLayout());
        sideNavBar.setBackground(new Color(249, 239, 196)); // Beige background
        
        JLabel sideBarLabel = new JLabel("Projects", SwingConstants.CENTER);
        sideBarLabel.setForeground(new Color(0, 102, 153)); // Dark blue text
        sideBarLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sideBarLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        sideNavBar.add(sideBarLabel, BorderLayout.NORTH);
        
        projectListPanel = new JPanel();
        projectListPanel.setLayout(new BoxLayout(projectListPanel, BoxLayout.Y_AXIS));
        projectListPanel.setBackground(new Color(249, 239, 196)); // Match the beige color
        
        JScrollPane projectListScrollPane = new JScrollPane(projectListPanel);
        projectListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        projectListScrollPane.setBorder(null);
        sideNavBar.add(projectListScrollPane, BorderLayout.CENTER);
        
        
        
        // Add Edit and Delete Buttons
        JPanel projectActionPanel = new JPanel();
        projectActionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        projectActionPanel.setBackground(new Color(249, 239, 196)); // Match the side bar color
        
        JButton editProjectButton = new JButton("Edit Project");
        editProjectButton.setBackground(new Color(0, 102, 153)); // Dark blue
        editProjectButton.setForeground(Color.WHITE);
        editProjectButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        editProjectButton.setFocusPainted(false);
        editProjectButton.addActionListener(e -> {
            String selectedProjectName = getSelectedProjectName();
            if (selectedProjectName != null) {
                String newProjectName = JOptionPane.showInputDialog(dashboard, "Enter new project name:", selectedProjectName);
                if (newProjectName != null && !newProjectName.trim().isEmpty()) {
                    if (isProjectNameUnique(newProjectName, currentUserID)) {
                        updateProjectNameInDatabase(selectedProjectName, newProjectName);
                        refreshProjectList();
                    } else {
                        JOptionPane.showMessageDialog(dashboard, "Project name must be unique. Please choose a different name.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(dashboard, "Please select a project to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        projectActionPanel.add(editProjectButton);
        
        JButton addProjectButton = new JButton("Add Project");
        addProjectButton.setBackground(new Color(0, 102, 153)); // Dark blue
        addProjectButton.setForeground(Color.WHITE);
        addProjectButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addProjectButton.setFocusPainted(false);
        addProjectButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addProjectButton.addActionListener(e -> {
            String projectName = JOptionPane.showInputDialog(dashboard, "Enter project name:");
            
            if (projectName != null && !projectName.trim().isEmpty()) {
                if (isProjectNameUnique(projectName, currentUserID)) {
                    addProjectToDatabase(projectName);
                    addProjectToUI(projectName);
                } else {
                    JOptionPane.showMessageDialog(dashboard, "Project name must be unique. Please choose a different name.");
                }
            }
        });
        projectActionPanel.add(addProjectButton);
        
        JButton deleteProjectButton = new JButton("Delete Project");
        deleteProjectButton.setBackground(new Color(204, 0, 0)); // Red for delete
        deleteProjectButton.setForeground(Color.WHITE);
        deleteProjectButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deleteProjectButton.setFocusPainted(false);
        deleteProjectButton.addActionListener(e -> {
            String selectedProjectName = getSelectedProjectName();
            if (selectedProjectName != null) {
                int confirm = JOptionPane.showConfirmDialog(dashboard, "Are you sure you want to delete this project?",
                        "Delete Project", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteProjectFromDatabase(selectedProjectName);
                    refreshProjectList();
                    reloadUI();
                }
            } else {
                JOptionPane.showMessageDialog(dashboard, "Please select a project to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        projectActionPanel.add(deleteProjectButton);
        
        sideNavBar.add(projectActionPanel, BorderLayout.SOUTH);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.2;
        gbc.weighty = 1;
        dashboard.add(sideNavBar, gbc);
        
        // Main Content Area
        mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.8;
        gbc.weighty = 1;
        dashboard.add(mainContent, gbc);
        
        // Load Projects from Database
        loadProjectsFromDatabase();
        
        dashboard.setVisible(true);
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
        JButton projectButton = new JButton(projectName);
        projectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        projectButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        projectButton.setForeground(Color.BLACK); // gray text for contrast
        projectButton.setBackground(new Color(249, 239, 196)); // Beige background
        
        // Remove borders and focus
        projectButton.setBorderPainted(false);
        projectButton.setFocusPainted(false);
        projectButton.setContentAreaFilled(false);
        
        // Add hover effect (optional)
        projectButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (projectButton != selectedButton) {
                    projectButton.setBackground(new Color(230, 230, 230)); // Light gray hover
                    projectButton.setOpaque(true);
                }
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (projectButton != selectedButton) {
                    projectButton.setBackground(new Color(249, 239, 196)); // Restore beige
                    projectButton.setOpaque(true);
                }
            }
        });
        
        projectButton.addActionListener(e -> {
            // If there is a selected button, reset its background
            if (selectedButton != null) {
                selectedButton.setBackground(new Color(249, 239, 196)); // Beige background
                selectedButton.setOpaque(true);
            }
            
            // Set the new selected button's background to dark blue
            projectButton.setBackground(new Color(0, 102, 153)); // Dark blue background
            projectButton.setOpaque(true);
            
            // Update the selected button reference
            selectedButton = projectButton;
            
            // Call method to handle the active project change
            setActiveProject(projectName);
        });
        
        projectListPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between buttons
        projectListPanel.add(projectButton);
        projectListPanel.revalidate();
        projectListPanel.repaint();
    }
    
    private int addListToDatabase(String projectName, String listName) {
        int listID = -1; // Default value if insertion fails
        try (Connection connection = DatabaseConnector.getConnection();
                PreparedStatement stmt = connection.prepareStatement(
                        "INSERT INTO lists (listName, projectID) VALUES (?, (SELECT id FROM projects WHERE projectName = ? AND userID = ?))",
                        Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, listName);
            stmt.setString(2, projectName);
            stmt.setInt(3, currentUserID);
            stmt.executeUpdate();
            // Get the generated list ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    listID = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add list to database.");
        }
        return listID;
    }
    
    private void addKanbanColumn(JPanel kanbanContent, String columnName, int listID) {
        System.out.println("test");
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
        
        taskPanel.setFocusable(true);
        taskPanel.requestFocus();
        
        JScrollPane taskScrollPane = new JScrollPane(taskPanel);
        taskScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        taskScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        columnPanel.add(taskScrollPane, BorderLayout.CENTER);
        
        loadTasksFromDatabase(taskPanel, listID);
        
        // Create a panel to hold the buttons in a FlowLayout with CENTER alignment
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // FlowLayout.CENTER for centering buttons, 10px spacing
        
        // Add the "View List Details" button to the button panel
        JButton viewListButton = new JButton("View List Details");
        viewListButton.addActionListener(e -> {
            String listName = getListNameFromDatabase(listID);
            showListDetails(listName, listID);
        });
        buttonPanel.add(viewListButton); // Add to button panel
        
        // Add the "Add Task" button to the button panel
        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.addActionListener(e -> {
            // Display a dialog to input task details (name, description, due date, and priority)
            JPanel taskInputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            
            taskInputPanel.add(new JLabel("Task Name:"));
            JTextField taskNameField = new JTextField();
            taskInputPanel.add(taskNameField);
            
            taskInputPanel.add(new JLabel("Description:"));
            JTextArea descriptionArea = new JTextArea(3, 20);
            JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
            taskInputPanel.add(descriptionScrollPane);
            
            taskInputPanel.add(new JLabel("Due Date (YYYY-MM-DD):"));
            JTextField dueDateField = new JTextField();
            taskInputPanel.add(dueDateField);
            
            taskInputPanel.add(new JLabel("Priority:"));
            JComboBox<String> priorityComboBox = new JComboBox<>(new String[]{"Low", "Medium", "High"});
            taskInputPanel.add(priorityComboBox);
            
            taskPanel.revalidate();
            taskPanel.repaint();
            
            // Show input dialog with task details form
            int option = JOptionPane.showConfirmDialog(kanbanContent, taskInputPanel, "Enter Task Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (option == JOptionPane.OK_OPTION) {
                String taskName = taskNameField.getText().trim();
                String description = descriptionArea.getText().trim();
                String dueDate = dueDateField.getText().trim();
                String priority = (String) priorityComboBox.getSelectedItem();
                
                // Validate input before proceeding
                if (!taskName.isEmpty() && !description.isEmpty() && !dueDate.isEmpty()) {
                    // Add task to database
                    addTaskToDatabase(listID, taskName, description, dueDate, priority);
                    addTaskToColumn(taskPanel, taskName);
                } else {
                    JOptionPane.showMessageDialog(kanbanContent, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(addTaskButton); // Add to button panel
        
        // Add the button panel to the column panel (at the bottom)
        columnPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        kanbanContent.add(columnPanel, columnGbc);
        kanbanContent.revalidate();
        kanbanContent.repaint();
    }
    
    private void addTaskToDatabase(int listID, String taskName, String description, String dueDate, String priority) {
        try (Connection connection = DatabaseConnector.getConnection();
                PreparedStatement stmt = connection.prepareStatement(
                        "INSERT INTO tasks (taskName, listID, description, due_date, priority) VALUES (?, ?, ?, ?, ?)")) {
            
            stmt.setString(1, taskName);
            stmt.setInt(2, listID);
            stmt.setString(3, description);
            stmt.setString(4, dueDate);
            stmt.setString(5, priority);
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
        
        
        taskCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Assuming taskID and other details are stored with the task card
                // You would fetch the task details from the database or maintain them in a map
                // For simplicity, let's assume you can get them from the database based on taskName
                int taskID = getTaskIDFromName(taskName); // You should implement this method to fetch taskID
                String description = getTaskDescriptionFromDatabase(taskID); // Fetch description
                String dueDate = getTaskDueDateFromDatabase(taskID); // Fetch due date
                String priority = getTaskPriorityFromDatabase(taskID); // Fetch priority
                int projectID = getProjectIDFromTask(taskID);
                // Call the showTaskDetails method to display the task details dialog
                showTaskDetails(taskID, taskName, description, dueDate, priority, projectID);
            }
        });
        
        taskPanel.add(taskCard);
        taskPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        taskPanel.revalidate();
        taskPanel.repaint();
    }
    
    private void updateProjectNameInDatabase(String oldName, String newName) {
        try (Connection connection = DatabaseConnector.getConnection();
                PreparedStatement stmt = connection.prepareStatement("UPDATE projects SET projectName = ? WHERE projectName = ?")) {
            stmt.setString(1, newName);
            stmt.setString(2, oldName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update project name in the database.");
        }
    }
    
    private void updateListInDatabase(int listID, String newListName) {
        try (Connection connection = DatabaseConnector.getConnection();
                PreparedStatement stmt = connection.prepareStatement("UPDATE lists SET listName = ? WHERE id = ?")) {
            
            stmt.setString(1, newListName);
            stmt.setInt(2, listID);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update list in the database.");
        }
    }
    
    private void updateTaskInDatabase(int taskID, String taskName, String description, String dueDate, String priority, String listName) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            // Get listID from listName
            String listQuery = "SELECT id FROM lists WHERE listName = ?";
            int listID = -1;
            try (PreparedStatement stmt = connection.prepareStatement(listQuery)) {
                stmt.setString(1, listName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    listID = rs.getInt("id");
                }
            }
            
            if (listID != -1) {
                // Update the task with the new listID
                String updateQuery = "UPDATE tasks SET taskName = ?, description = ?, due_date = ?, priority = ?, listID = ? WHERE id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
                    stmt.setString(1, taskName);
                    stmt.setString(2, description);
                    stmt.setString(3, dueDate);
                    stmt.setString(4, priority);
                    stmt.setInt(5, listID);
                    stmt.setInt(6, taskID);
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    
    private JPanel loadKanbanBoardFromDatabase(String projectName) {
        JPanel kanbanBoard = new JPanel(new GridBagLayout());
        
        try (Connection connection = DatabaseConnector.getConnection();
                PreparedStatement stmt = connection.prepareStatement("SELECT id, listName FROM lists WHERE projectID = (SELECT id FROM projects WHERE projectName = ? AND userID = ?)");) {
            
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
    
    private void deleteTaskFromDatabase(int taskID) {
        int confirmation = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete this task?",
                "Delete Task",
                JOptionPane.YES_NO_OPTION
        );
        
        if (confirmation == JOptionPane.YES_OPTION) {
            try (Connection connection = DatabaseConnector.getConnection();
                    PreparedStatement stmt = connection.prepareStatement("DELETE FROM tasks WHERE id = ?")) {
                
                stmt.setInt(1, taskID);
                stmt.executeUpdate();
                reloadUI();
                JOptionPane.showMessageDialog(null, "Task deleted successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to delete task from database.");
            }
        }
    }   
    
    private void deleteListFromDatabase(int listID) {
        try (Connection connection = DatabaseConnector.getConnection();
                PreparedStatement stmt = connection.prepareStatement("DELETE FROM lists WHERE id = ?")) {
            
            stmt.setInt(1, listID);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete list from the database.");
        }
    }
    
    private void deleteProjectFromDatabase(String projectName) {
        try (Connection connection = DatabaseConnector.getConnection();
                PreparedStatement stmt = connection.prepareStatement("DELETE FROM projects WHERE projectName = ?")) {
            stmt.setString(1, projectName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete project from the database.");
        }
    }
    
    private String getProjectNameByTaskID(int taskID) {
        String projectName = null;
        
        try (Connection connection = DatabaseConnector.getConnection();
                PreparedStatement stmt = connection.prepareStatement(
                        "SELECT p.projectName " +
                                "FROM projects p " +
                                "JOIN lists l ON l.projectID = p.id " +
                                "JOIN tasks t ON t.listID = l.id " +
                                "WHERE t.id = ?")) {
            
            stmt.setInt(1, taskID);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    projectName = rs.getString("projectName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to fetch project name for the given task ID.");
        }
        
        return projectName;
    }
    
    private String getSelectedProjectName() {
        for (Component comp : projectListPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getBackground().equals(new Color(0, 102, 153))) { // Assuming selected buttons are highlighted
                    return button.getText();
                }
            }
        }
        return null; // No project selected
    }
    
    public String getUserNameById(int userID) {
        String username = null;
        
        try (Connection connection = DatabaseConnector.getConnection();
                PreparedStatement stmt = connection.prepareStatement("SELECT userName FROM users WHERE id = ?")) {
            
            stmt.setInt(1, userID); // Set the userID in the query
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                username = rs.getString("userName"); // Retrieve the username
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
        
        return username;
    }
    
    private String getListNameFromDatabase(int listID) {
        String listName = null;
        
        try (Connection connection = DatabaseConnector.getConnection();
                PreparedStatement stmt = connection.prepareStatement("SELECT listName FROM lists WHERE id = ?")) {
            
            stmt.setInt(1, listID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                listName = rs.getString("listName");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to fetch the list name from the database.");
        }
        
        return listName;
    }
    
     private int getTaskIDFromName(String taskName) {
        int taskID = -1; // Default value if task not found
        String query = "SELECT id FROM tasks WHERE taskName = ?";
        
        try (Connection connection = DatabaseConnector.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setString(1, taskName); // Set the task name parameter
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                taskID = rs.getInt("id"); // Get the task ID from the result set
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to retrieve task ID.");
        }
        
        return taskID;
    }
    
    private String getTaskDescriptionFromDatabase(int taskID) {
        String description = null;
        String query = "SELECT description FROM tasks WHERE id = ?";
        
        try (Connection connection = DatabaseConnector.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setInt(1, taskID); // Set the task ID parameter
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                description = rs.getString("description"); // Get the description from the result set
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to retrieve task description.");
        }
        
        return description;
    }
    
    private String getTaskDueDateFromDatabase(int taskID) {
        String dueDate = null;
        String query = "SELECT due_date FROM tasks WHERE id = ?";
        
        try (Connection connection = DatabaseConnector.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setInt(1, taskID); // Set the task ID parameter
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                dueDate = rs.getString("due_date"); // Get the due date from the result set
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to retrieve task due date.");
        }
        
        return dueDate;
    }
    
    private String getTaskPriorityFromDatabase(int taskID) {
        String priority = null;
        String query = "SELECT priority FROM tasks WHERE id = ?";
        
        try (Connection connection = DatabaseConnector.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setInt(1, taskID); // Set the task ID parameter
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                priority = rs.getString("priority"); // Get the priority from the result set
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to retrieve task priority.");
        }
        
        return priority;
    }
    
    private int getProjectIDFromTask(int taskID) {
        int projectID = -1; // Default value if no project is found
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "SELECT l.projectID FROM tasks t " +
                    "JOIN lists l ON t.listID = l.id " +
                    "WHERE t.id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, taskID);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    projectID = rs.getInt("projectID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectID;
    }
    
    private List<String> getProjectLists(int projectID) {
        List<String> lists = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "SELECT listName FROM lists WHERE projectID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, projectID);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    lists.add(rs.getString("listName"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lists;
    }
    
    private void showTaskDetails(int taskID, String taskName, String description, String dueDate, String priority, int currentProjectID) {
        JDialog taskDialog = new JDialog((Frame) null, "Task Details", true);
        taskDialog.setLayout(new GridLayout(6, 2, 10, 10)); // Increase row count to accommodate new field
        
        taskDialog.add(new JLabel("Task Name:"));
        JTextField nameField = new JTextField(taskName);
        taskDialog.add(nameField);
        
        taskDialog.add(new JLabel("Description:"));
        JTextArea descriptionField = new JTextArea(description, 3, 20);
        taskDialog.add(new JScrollPane(descriptionField));
        
        taskDialog.add(new JLabel("Due Date:"));
        JTextField dueDateField = new JTextField(dueDate);
        taskDialog.add(dueDateField);
        
        taskDialog.add(new JLabel("Priority:"));
        JComboBox<String> priorityBox = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        priorityBox.setSelectedItem(priority);
        taskDialog.add(priorityBox);
        
        // Add JComboBox to select a new list
        taskDialog.add(new JLabel("Transfer to List:"));
        JComboBox<String> listBox = new JComboBox<>();
        // Get available lists for the current project
        List<String> lists = getProjectLists(currentProjectID); // Method to fetch lists for the project
        for (String listName : lists) {
            listBox.addItem(listName);
        }
        taskDialog.add(listBox);
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // Save the updated task information to the database
            updateTaskInDatabase(taskID, nameField.getText(), descriptionField.getText(),
                    dueDateField.getText(), (String) priorityBox.getSelectedItem(), (String) listBox.getSelectedItem());
            
            String projectName = getProjectNameByTaskID(taskID);
            setActiveProject(projectName);
            // Close the task detail dialog
            taskDialog.dispose();
        });
        taskDialog.add(saveButton);
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            // Delete the task from the database
            deleteTaskFromDatabase(taskID);
            // Close the task detail dialog
            taskDialog.dispose();
        });
        taskDialog.add(deleteButton);
        
        taskDialog.setSize(400, 300);
        taskDialog.setVisible(true);
    }
     
    private void refreshProjectList() {
        projectListPanel.removeAll();
        loadProjectsFromDatabase();
        projectListPanel.revalidate();
        projectListPanel.repaint();
    }
    
    private void showListDetails(String listName, int listID) {
        JDialog taskDialog = new JDialog((Frame) null, "List Details", true);
        taskDialog.setLayout(new GridLayout(5, 2, 10, 10));
        
        taskDialog.add(new JLabel("List Name:"));
        JTextField nameField = new JTextField(listName);
        taskDialog.add(nameField);
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // Save the updated task information to the database
            updateListInDatabase(listID, nameField.getText());
            reloadUI();
            
            // Close the task detail dialog
            taskDialog.dispose();
        });
        taskDialog.add(saveButton);
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            // Delete the task from the database
            deleteListFromDatabase(listID);
            reloadUI();
            // Close the task detail dialog
            taskDialog.dispose();
        });
        taskDialog.add(deleteButton);
        
        taskDialog.setSize(400, 300);
        taskDialog.setVisible(true);
    }
    
    private boolean isProjectNameUnique(String projectName, int userID) {
        boolean isUnique = false;
        
        try (Connection connection = DatabaseConnector.getConnection();
                PreparedStatement stmt = connection.prepareStatement(
                        "SELECT COUNT(*) FROM projects WHERE projectName = ? AND userID = ?")) {
            
            stmt.setString(1, projectName);
            stmt.setInt(2, userID);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    isUnique = rs.getInt(1) == 0; // True if no matching project name is found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to check project name uniqueness.");
        }
        
        return isUnique;
    }
    
    private void reloadUI() {
        projectListPanel.removeAll();
        mainContent.removeAll();
        
        loadProjectsFromDatabase(); // Reload projects and associated data
        projectListPanel.revalidate();
        projectListPanel.repaint();
        mainContent.revalidate();
        mainContent.repaint();
    }
    
    private void setActiveProject(String projectName) {
        System.out.println("test123");
        mainContent.removeAll();
        mainContent.setLayout(new BorderLayout());
        
        // Load the Kanban board and pass the project name
        JPanel kanbanBoard = loadKanbanBoardFromDatabase(projectName);
        mainContent.add(kanbanBoard, BorderLayout.CENTER);
        
        JButton addListButton = new JButton("Add List");
        addListButton.addActionListener(e -> {
            String listName = JOptionPane.showInputDialog(mainContent, "Enter list name:");
            if (listName != null && !listName.trim().isEmpty()) {
                // Add list to database
                int listID = addListToDatabase(projectName, listName);
                // Add the Kanban column (task list) to the UI
                addKanbanColumn(kanbanBoard, listName, listID);
            }
        });
        mainContent.add(addListButton, BorderLayout.SOUTH);
        
        mainContent.revalidate();
        mainContent.repaint();
    }
    
    public static void main(String[] args) {
        // Assuming the userID is passed as a command-line argument
        if (args.length > 0) {
            int userID = Integer.parseInt(args[0]);
            new MainDashboard(userID);  // Pass userID to the constructor
        }
    }
}