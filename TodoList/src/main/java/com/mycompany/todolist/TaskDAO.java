package com.mycompany.todolist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class TaskDAO {

    public void addTask(Task task) {
        String query = "INSERT INTO tasks (title, description, due_date, completed) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());

            // Due date'nin null olup olmadığını kontrol ediyoruz
            if (task.getDueDate() == null) {
                // Eğer null ise, SQL'de NULL olarak ekliyoruz
                stmt.setNull(3, Types.DATE);
            } else {
                // Eğer null değilse, normal bir şekilde ekliyoruz
                stmt.setDate(3, new java.sql.Date(task.getDueDate().getTime()));
            }

            stmt.setBoolean(4, task.isCompleted());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM tasks";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setDueDate(rs.getDate("due_date"));
                task.setCompleted(rs.getBoolean("completed"));
                task.setCreatedAt(rs.getTimestamp("created_at"));

                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public void deleteTask(int taskId) {
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this task?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM tasks WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, taskId);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void restoreTask(String taskTitle) {
        String query = "UPDATE tasks SET completed = FALSE WHERE title = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, taskTitle);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void completeTask(String taskTitle) {
        String query = "UPDATE tasks SET completed = NOT completed, completed_at = CURRENT_TIMESTAMP WHERE title = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, taskTitle);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Task> getCompletedTasks() {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM tasks WHERE completed = TRUE AND completed_at >= NOW() - INTERVAL 1 MONTH";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setDueDate(rs.getDate("due_date"));
                task.setCompleted(rs.getBoolean("completed"));
                task.setCreatedAt(rs.getTimestamp("created_at"));
                task.setCompletedAt(rs.getTimestamp("completed_at"));

                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

}
