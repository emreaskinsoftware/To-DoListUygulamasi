/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.todolist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDocumentDAO {
    public void addDocumentToTask(int taskId, String documentPath) {
        String query = "INSERT INTO task_documents (task_id, document_path) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, taskId);
            stmt.setString(2, documentPath);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<TaskDocument> getDocumentsForTask(int taskId) {
        List<TaskDocument> documents = new ArrayList<>();
        String query = "SELECT * FROM task_documents WHERE task_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, taskId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TaskDocument document = new TaskDocument();
                    document.setId(rs.getInt("id"));
                    document.setTaskId(rs.getInt("task_id"));
                    document.setDocumentPath(rs.getString("document_path"));
                    document.setUploadedAt(rs.getTimestamp("uploaded_at"));

                    documents.add(document);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return documents;
    }

    public void deleteDocumentFromTask(int documentId) {
        String query = "DELETE FROM task_documents WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, documentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

