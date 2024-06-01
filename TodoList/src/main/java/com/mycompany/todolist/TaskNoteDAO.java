/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.todolist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskNoteDAO {
    public void addNoteToTask(int taskId, String note) {
        String query = "INSERT INTO task_notes (task_id, note) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, taskId);
            stmt.setString(2, note);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<TaskNote> getNotesForTask(int taskId) {
        List<TaskNote> notes = new ArrayList<>();
        String query = "SELECT * FROM task_notes WHERE task_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, taskId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TaskNote note = new TaskNote();
                    note.setId(rs.getInt("id"));
                    note.setTaskId(rs.getInt("task_id"));
                    note.setNote(rs.getString("note"));
                    note.setCreatedAt(rs.getTimestamp("created_at"));

                    notes.add(note);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notes;
    }

    public void deleteNoteFromTask(int noteId) {
        String query = "DELETE FROM task_notes WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, noteId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

