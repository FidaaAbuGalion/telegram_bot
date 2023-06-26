package org.example;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Window extends JFrame {

    public Window() {

        SwingUtilities.invokeLater(() -> {
            OurBot bot = new OurBot();


            JFrame frame = new JFrame("Telegram Bot Manager");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            JPanel activityPanel = new JPanel(new FlowLayout());
            activityPanel.setBorder(BorderFactory.createTitledBorder("Activity Management"));

            JCheckBox activity1 = new JCheckBox("Activity 1");
            JCheckBox activity2 = new JCheckBox("Activity 2");
            JCheckBox activity3 = new JCheckBox("Activity 3");
            JCheckBox activity4 = new JCheckBox("Activity 4");
            JCheckBox activity5 = new JCheckBox("Activity 5");

            activityPanel.add(activity1);
            activityPanel.add(activity2);
            activityPanel.add(activity3);
            activityPanel.add(activity4);
            activityPanel.add(activity5);

            JPanel statisticsPanel = new JPanel(new FlowLayout());
            statisticsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));

            JLabel totalRequestsLabel = new JLabel("Total Requests: 0");
            JLabel uniqueUsersLabel = new JLabel("Unique Users: 0");
            if (bot.mostActiveUser == null){
                bot.mostActiveUser = "None";
            }
            JLabel mostActiveUserLabel = new JLabel("Most Active User: " + bot.mostActiveUser);
            JLabel mostPopularActivityLabel = new JLabel("Most Popular Activity: " + bot.findMostPopularActivity());

            statisticsPanel.add(totalRequestsLabel);
            statisticsPanel.add(uniqueUsersLabel);
            statisticsPanel.add(mostActiveUserLabel);
            statisticsPanel.add(mostPopularActivityLabel);

            JPanel historyPanel = new JPanel(new BorderLayout());
            historyPanel.setBorder(BorderFactory.createTitledBorder("Activity History"));

            JTextArea historyTextArea = new JTextArea(10, 30);
            historyTextArea.setEditable(false);


            JScrollPane historyScrollPane = new JScrollPane(historyTextArea);

            historyPanel.add(historyScrollPane, BorderLayout.CENTER);

            JPanel graphPanel = new JPanel(new BorderLayout());
            graphPanel.setBorder(BorderFactory.createTitledBorder("Request Growth Over Time"));

            JLabel graphLabel = new JLabel("Graph Placeholder");
            graphLabel.setHorizontalAlignment(JLabel.CENTER);

            graphPanel.add(graphLabel, BorderLayout.CENTER);

            JPanel controlPanel = new JPanel(new FlowLayout());

            JButton startButton = new JButton("Start Bot");

            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                        OurBot ourBot = new OurBot();

                        List<String> selectedActivities = new ArrayList<>();

                        if (activity1.isSelected()) {
                            selectedActivities.add(activity1.getText());
                        }
                        if (activity2.isSelected()) {
                            selectedActivities.add(activity2.getText());
                        }
                        if (activity3.isSelected()) {
                            selectedActivities.add(activity3.getText());
                        }
                        if (activity4.isSelected()) {
                            selectedActivities.add(activity4.getText());
                        }
                        if (activity5.isSelected()) {
                            selectedActivities.add(activity5.getText());
                        }

                        if (selectedActivities.size() != 3) {
                            JOptionPane.showMessageDialog(frame, "Please select exactly three activities.", "Invalid Selection", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        telegramBotsApi.registerBot(ourBot);

                        System.out.println("Selected activities: " + selectedActivities.toString());
                    } catch (TelegramApiException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            controlPanel.add(startButton);

            frame.add(activityPanel, BorderLayout.NORTH);
            frame.add(statisticsPanel, BorderLayout.CENTER);
            frame.add(historyPanel, BorderLayout.WEST);
            frame.add(graphPanel, BorderLayout.EAST);
            frame.add(controlPanel, BorderLayout.SOUTH);

            frame.setSize(800, 600);
            frame.setVisible(true);
        });

    }
}
