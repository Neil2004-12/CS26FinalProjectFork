package com.mycompany.cs26finalproject;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
                if (isProjectNameUnique(projectName, currentUserID)) {
                    addProjectToDatabase(projectName);
                    addProjectToUI(projectName);
                } else {
                    JOptionPane.showMessageDialog(dashboard, "Project name must be unique. Please choose a different name.");
                }
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
                
                // Call the showTaskDetails method to display the task details dialog
                showTaskDetails(taskID, taskName, description, dueDate, priority);
            }
        });

        taskPanel.add(taskCard);
        taskPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        taskPanel.revalidate();
        taskPanel.repaint();
    }
    private void showTaskDetails(int taskID, String taskName, String description, String dueDate, String priority) {
        JDialog taskDialog = new JDialog((Frame) null, "Task Details", true);
        taskDialog.setLayout(new GridLayout(5, 2, 10, 10));

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

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // Save the updated task information to the database
            updateTaskInDatabase(taskID, nameField.getText(), descriptionField.getText(),
                    dueDateField.getText(), (String) priorityBox.getSelectedItem());
            
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
    
    private void updateTaskInDatabase(int taskID, String taskName, String description, String dueDate, String priority) {
        // SQL query to update the task in the database
        String updateQuery = "UPDATE tasks SET taskName = ?, description = ?, due_date = ?, priority = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConnector.getConnection();
                PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
            
            // Set the parameters for the query
            stmt.setString(1, taskName);
            stmt.setString(2, description);
            stmt.setString(3, dueDate);
            stmt.setString(4, priority);
            stmt.setInt(5, taskID);
            
            // Execute the update
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Task updated successfully");
                System.out.println("test");                      
                String projectName = getProjectNameByTaskID(taskID);
                setActiveProject(projectName);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update task. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating task in the database.");
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
                String projectName = getProjectNameByTaskID(taskID);
                setActiveProject(projectName);
                JOptionPane.showMessageDialog(null, "Task deleted successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to delete task from database.");
            }
        }
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


    public static void main(String[] args) {
        // Assuming the userID is passed as a command-line argument
        if (args.length > 0) {
            int userID = Integer.parseInt(args[0]);
            new MainDashboard(userID);  // Pass userID to the constructor
        }
    }
}