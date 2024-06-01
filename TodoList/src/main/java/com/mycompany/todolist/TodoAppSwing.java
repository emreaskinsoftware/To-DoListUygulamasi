package com.mycompany.todolist;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoAppSwing {

    private TaskDAO taskDAO = new TaskDAO();
    private JList<String> taskList;
    private Map<Integer, JPanel> taskPanels = new HashMap<>();

    public TodoAppSwing() {
        JFrame frame = new JFrame("To-Do List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        taskList = new JList<>();
        updateTaskList(taskList);

        JPanel inputPanel = new JPanel(new GridLayout(5, 1));
        JTextField titleField = new JTextField();
        JTextArea descriptionArea = new JTextArea();
        JButton addButton = new JButton("Add Task");
        JButton completeButton = new JButton("Complete Task");
        JButton showTasksButton = new JButton("Show Completed Tasks");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Task task = new Task();
                task.setTitle(titleField.getText());
                task.setDescription(descriptionArea.getText());
                task.setCompleted(false);
                taskDAO.addTask(task);
                updateTaskList(taskList);
                titleField.setText("");
                descriptionArea.setText("");
            }
        });

        completeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTaskTitle = taskList.getSelectedValue();
                if (selectedTaskTitle != null) {
                    taskDAO.completeTask(selectedTaskTitle);
                    updateTaskList(taskList);
                }
            }
        });

        showTasksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCompletedTasks();
            }
        });

        inputPanel.add(new JLabel("Task Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Task Description:"));
        inputPanel.add(descriptionArea);
        inputPanel.add(addButton);
        inputPanel.add(completeButton);
        inputPanel.add(showTasksButton);

        frame.add(new JScrollPane(taskList), BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void updateTaskList(JList<String> taskList) {
        List<Task> tasks = taskDAO.getTasks();
        List<String> taskTitles = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                taskTitles.add(task.getTitle());
            }
        }
        taskList.setListData(taskTitles.toArray(new String[0]));
    }

    private void showCompletedTasks() {
    JFrame completedTasksFrame = new JFrame("Completed Tasks");
    completedTasksFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    completedTasksFrame.setSize(500, 500);

    List<Task> completedTasks = taskDAO.getCompletedTasks();
    completedTasks.sort((t1, t2) -> t2.getCompletedAt().compareTo(t1.getCompletedAt())); // Zamana göre sırala

    JPanel completedTasksPanel = new JPanel(new GridLayout(completedTasks.size(), 3));
    taskPanels.clear(); // Temizleme

    for (Task task : completedTasks) {
        JPanel taskPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel(task.getTitle() + " (Completed on " + task.getCompletedAt() + ")");
        JButton deleteButton = new JButton("Delete");
        JButton restoreButton = new JButton("Restore");

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taskDAO.deleteTask(task.getId());
                completedTasksPanel.remove(taskPanel);
                completedTasksFrame.revalidate();
                completedTasksFrame.repaint();
            }
        });

        restoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taskDAO.restoreTask(task.getTitle());
                updateTaskList(taskList);
                completedTasksPanel.remove(taskPanel);
                completedTasksPanel.revalidate(); // Paneli yeniden doğrula
                completedTasksPanel.repaint(); // Paneli yeniden boyamak için
            }
        });

        taskPanel.add(titleLabel);
        taskPanel.add(deleteButton);
        taskPanel.add(restoreButton);

        taskPanels.put(task.getId(), taskPanel);
        completedTasksPanel.add(taskPanel);
    }

    completedTasksFrame.add(new JScrollPane(completedTasksPanel), BorderLayout.CENTER);
    completedTasksFrame.setVisible(true);
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TodoAppSwing();
            }
        });
    }
}
