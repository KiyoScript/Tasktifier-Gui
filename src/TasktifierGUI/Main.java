/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package TasktifierGUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.*;
import java.util.List;
import javax.sound.sampled.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
/**
 *
 * @author Daniel
 */
public class Main extends javax.swing.JFrame {
    private static Main instance;
    private TrayIcon trayIcon;
    public static File reminderMusicPath = new File("src/TasktifierGUI/assets/audios/ReminderAudio.wav");
    public static popupFrame.StartTimeReminderFrame startTimeReminderFrame = new popupFrame.StartTimeReminderFrame("DO IT NOW!", "DO IT NOW!");
    public static popupFrame.ReminderFrame reminderFrame = new popupFrame.ReminderFrame("Just A Reminder", "Just A Reminder");
    /**
     * Creates new form Main
     */
    public Main() {
        user.Authenticator user = new user.Authenticator();
        user.SQLMethods method = new user.SQLMethods();
        int userID = user.getLoggedInUserID();
        customComponents.LiveLocalDateAndTime liveDateTime = new customComponents.LiveLocalDateAndTime();
        final String[] taskNameDueDate = {""};
        final String[] notesDueDate = {""};
        final String[] taskNameReminder = {""};
        final String[] notesReminder = {""};
        
        
        this.setUndecorated(true);
        
        try {
            List<LocalDate> dueDates = method.readDueDates(userID);
            LocalDate currentDate = liveDateTime.getCurrentDate();

            for (LocalDate dueDate : dueDates) {
                if (currentDate.isAfter(dueDate)) {
                    method.updateTasksToNotDone();
                    break;
                }
            }
        } catch (SQLException ex) {}
    
        initComponents();
        
        SelectMusicButton.addActionListener((ActionEvent e) -> {
            showFileChooser();
        });
        
        try {
            int countTodayTasks = method.readCountTodayTasks(userID);
    
            if (countTodayTasks == 0) {
                TodayButton.setText("Today");
            } else {
                TodayButton.setText("Today (" + countTodayTasks + ")");
            }
        } catch (SQLException e) {}
        
        int[] undoneTaskIDs = method.readUndoneTaskIDs(userID);
        
        
        try {
            List<Object[]> tasksData = method.readUndoneTasks(userID);

            Object[][] tasksArray = new Object[tasksData.size()][];
            tasksData.toArray(tasksArray);

            DefaultTableModel model = new javax.swing.table.DefaultTableModel(
                    tasksArray,
                    new String[]{"Task Name", "Due Date", "Start Time", "Reminder", "Snooze", "Action"}
            );

            TasksTable.setModel(model);

        } catch (SQLException e) {}
        
        try {
            List<Object[]> todayTasksData = method.readTodayTasks(userID);
    
            Object[][] todayTasksArray = new Object[todayTasksData.size()][];
            todayTasksData.toArray(todayTasksArray);
    
            DefaultTableModel model = new javax.swing.table.DefaultTableModel(
            todayTasksArray,
                new String[]{"Task Name", "Due Date", "Start Time", "Reminder", "Snooze", "Action"}
            );
    
            TodayTable.setModel(model);
    
        } catch (SQLException e) {}
        
        try {
            List<Object[]> data = method.readDetailsTasks(userID);

            DefaultTableModel model = (DefaultTableModel) DetailsTable.getModel();
            model.setRowCount(0);

            for (Object[] row : data) {
                model.addRow(row);
            }

        } catch (SQLException e) {}
        
        Timer[] timerHolder = new Timer[1];
        timerHolder[0] = new Timer(1000, (ActionEvent e) -> {
        try {
            List<LocalDateTime> dueDatesWithStartTime = method.readDueDateWithStartTime(userID);
            List<LocalTime> snoozeReminder = method.readSnoozeReminder(userID);
            
            
            for (LocalDateTime dateTime : dueDatesWithStartTime) {
                if (dateTime.equals(liveDateTime.getCurrentDateTime())) {
                    timerHolder[0].stop();
                    LocalDate datePart = dateTime.toLocalDate();
                    LocalTime timePart = dateTime.toLocalTime();

                    try {
                        List<Object[]> result = method.readTaskNameAndNotesFromDueDatesWithStartTime(userID, datePart, timePart);

                        for (Object[] row : result) {
                            taskNameDueDate[0] = (String) row[0];
                            notesDueDate[0] = (String) row[1];

                            startTimeReminderFrame = new popupFrame.StartTimeReminderFrame(taskNameDueDate[0], notesDueDate[0]);
                        }
                    } catch (SQLException f) {}
                    try {
                        File musicPath = new File("src/TasktifierGUI/assets/audios/StartTaskAudio.wav");
                        if(musicPath.exists()){
                            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                            Clip clip = AudioSystem.getClip();
                            clip.open(audioInput);
                            clip.start();
                        } else{
                            System.out.println("Not OK.");
                        }
                    } catch (IOException | LineUnavailableException | UnsupportedAudioFileException g){}
                    startTimeReminderFrame.setVisible(true);
                    Timer restartTimer = new Timer(60000, (ActionEvent restartEvent) -> {
                        timerHolder[0].setInitialDelay((int) (1000 - System.currentTimeMillis() % 1000));
                        timerHolder[0].start();
                    });
                    restartTimer.setRepeats(false);
                    restartTimer.start();
                    break;
                }
            }

            for (LocalTime time : snoozeReminder) {
                if (time.equals(liveDateTime.getCurrentTime())) {
                    timerHolder[0].stop();
                    method.updateSnoozeReminder(time, userID);
                    LocalTime timePart = liveDateTime.getCurrentTime();

                    try {
                        List<Object[]> result = method.readTaskNameAndNotesFromReminder(userID, timePart);

                        for (Object[] row : result) {
                            taskNameReminder[0] = (String) row[0];
                            notesReminder[0] = (String) row[1];

                            reminderFrame = new popupFrame.ReminderFrame(taskNameReminder[0], notesReminder[0]);   
                        }

                    } catch (SQLException f) {}  
                    if(reminderMusicPath.exists()){
                        if(isMp3File(reminderMusicPath)){
                            try {
                                FileInputStream fileInputStream = new FileInputStream(reminderMusicPath);
                                Player player = new Player(fileInputStream);
                                Thread playerThread = new Thread(() -> {
                                    try {
                                        System.out.println("Song is playing...");
                                        player.play();
                                    } catch (JavaLayerException i) {
                                        System.out.println(i);
                                    }
                                });
                                playerThread.start();
                            } catch (FileNotFoundException | JavaLayerException g) {
                                System.out.println(g);
                            }
                        } else if(isWavFile(reminderMusicPath)){
                            AudioInputStream audioInput = AudioSystem.getAudioInputStream(reminderMusicPath);
                            Clip clip = AudioSystem.getClip();
                            clip.open(audioInput);
                            clip.start();
                        }
                    } else{
                        System.out.println("Not OK.");
                    }
                    reminderFrame.setVisible(true);
                    Timer restartTimer = new Timer(60000, (ActionEvent restartEvent) -> {
                        timerHolder[0].setInitialDelay((int) (1000 - System.currentTimeMillis() % 1000));
                        timerHolder[0].start();
                    });
                    restartTimer.setRepeats(false);
                    restartTimer.start();
                    break;
                }
            }
        } catch (SQLException ex) {} catch (LineUnavailableException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedAudioFileException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        timerHolder[0].setInitialDelay((int) (1000 - System.currentTimeMillis() % 1000));
        timerHolder[0].start();

        customComponents.TableActionEvent event = new customComponents.TableActionEvent(){
            @Override
            public void deleteTask(int row){
                method.deleteTaskByID(undoneTaskIDs[row]);
            }
            
            @Override
            public void doneTask(int row){
                method.updateTaskToDoneByID(undoneTaskIDs[row]);
            }
        };
        TasksTable.getColumnModel().getColumn(5).setCellRenderer(new customComponents.TableActionCellRender());
        TasksTable.getColumnModel().getColumn(5).setCellEditor(new customComponents.TableActionCellEditor(event));
        TodayTable.getColumnModel().getColumn(5).setCellRenderer(new customComponents.TableActionCellRender());
        TodayTable.getColumnModel().getColumn(5).setCellEditor(new customComponents.TableActionCellEditor(event));
        TasksPanelContainer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
            }
        });
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/TasktifierGUI/assets/images/logo.png")); // Replace with the path to your icon

            PopupMenu popup = new PopupMenu();
            MenuItem showItem = new MenuItem("Show");
            showItem.addActionListener((ActionEvent e) -> {
                showApplication();
            });
            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener((ActionEvent e) -> {
                exitApplication();
            });
            popup.add(showItem);
            popup.addSeparator();
            popup.add(exitItem);

            trayIcon = new TrayIcon(image, "TasktifierGUI", popup);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {}
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        customComponents.RoundedPanel roundedPanel = new customComponents.RoundedPanel(10);
        TasksPanelContainer = new javax.swing.JPanel();
        TasksInterface = new javax.swing.JScrollPane();
        TasksList = new javax.swing.JPanel();
        AddTaskButton = new javax.swing.JButton();
        TasksScrollPane = new javax.swing.JScrollPane();
        TasksTable = new customComponents.TableDark();
        TasksListLabel = new javax.swing.JLabel();
        SelectMusicPanel = new javax.swing.JPanel();
        SelectMusicLabel = new javax.swing.JLabel();
        SelectMusicButton = new javax.swing.JButton();
        SelectedMusicLabel = new javax.swing.JLabel();
        SelectedMusicName = new javax.swing.JTextField();
        TasksPanelHeader = new javax.swing.JPanel();
        TasksPanelHeaderButtons = roundedPanel;
        TasksButton = new javax.swing.JButton();
        TodayButton = new javax.swing.JButton();
        DetailsButton = new javax.swing.JButton();
        FirstCircle = new customComponents.Circles(255, 95, 87);
        SecondCircle = new customComponents.Circles(255, 189, 41);
        ThirdCircle = new customComponents.Circles(38, 202, 63);
        LogoutButton = new javax.swing.JButton();
        ExitButton = new javax.swing.JButton();
        MinimizeButton = new javax.swing.JButton();
        ThemeChangeButton = new javax.swing.JButton();
        TodayPanelContainer = new javax.swing.JPanel();
        TodayInterface = new javax.swing.JScrollPane();
        TodayList = new javax.swing.JPanel();
        TodayScrollPane = new javax.swing.JScrollPane();
        TodayTable = new customComponents.TableDark();
        TodayListLabel = new javax.swing.JLabel();
        DetailsPanelContainer = new javax.swing.JPanel();
        DetailsInterface = new javax.swing.JScrollPane();
        DetailsList = new javax.swing.JPanel();
        DetailsListLabel = new javax.swing.JLabel();
        DetailsScrollPane = new javax.swing.JScrollPane();
        DetailsTable = new customComponents.TableDarkStatus();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TasksPanelContainer.setBackground(new java.awt.Color(21, 28, 26));
        TasksPanelContainer.setVisible(true);

        TasksInterface.setBackground(new java.awt.Color(21, 28, 26));
        TasksInterface.setBorder(null);
        TasksInterface.setOpaque(false);
        JViewport Viewport = TasksInterface.getViewport();

        Viewport.setBackground(new Color(21, 28, 26));

        TasksInterface.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        TasksList.setBackground(new java.awt.Color(21, 28, 26));

        AddTaskButton.setBackground(new java.awt.Color(23, 162, 184));
        AddTaskButton.setForeground(new java.awt.Color(255, 255, 255));
        AddTaskButton.setText("Add Task");
        AddTaskButton.setFocusPainted(false);
        AddTaskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddTaskButtonActionPerformed(evt);
            }
        });

        TasksTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Eating Bee Lots", "12-25-2023", "08:30 P.M.", "07:00 P.M.", "Work", null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Task Name", "Due Date", "Start Time", "Reminder", "Snooze", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TasksTable.setSelectionBackground(Color.BLUE);
        TasksTable.setSelectionForeground(Color.WHITE);
        TasksTable.setFocusable(false);
        TasksTable.setRowSelectionAllowed(false);
        TasksTable.setSelectionBackground(new java.awt.Color(40, 40, 40));
        TasksScrollPane.setViewportView(TasksTable);

        TasksListLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        TasksListLabel.setForeground(new java.awt.Color(51, 153, 255));
        TasksListLabel.setText("Tasks List");

        javax.swing.GroupLayout TasksListLayout = new javax.swing.GroupLayout(TasksList);
        TasksList.setLayout(TasksListLayout);
        TasksListLayout.setHorizontalGroup(
            TasksListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TasksListLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TasksListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(TasksListLayout.createSequentialGroup()
                        .addComponent(TasksListLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(AddTaskButton))
                    .addComponent(TasksScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 804, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        TasksListLayout.setVerticalGroup(
            TasksListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TasksListLayout.createSequentialGroup()
                .addGroup(TasksListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TasksListLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(AddTaskButton))
                    .addComponent(TasksListLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TasksScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE))
        );

        TasksInterface.setViewportView(TasksList);

        SelectMusicPanel.setBackground(new java.awt.Color(21, 28, 26));

        SelectMusicLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        SelectMusicLabel.setForeground(new java.awt.Color(102, 255, 0));
        SelectMusicLabel.setText("SELECT MUSIC");

        SelectMusicButton.setText("Select");

        SelectedMusicLabel.setBackground(new java.awt.Color(21, 28, 26));
        SelectedMusicLabel.setForeground(new java.awt.Color(166, 166, 166));
        SelectedMusicLabel.setText("Selected Music:");

        SelectedMusicName.setEditable(false);
        SelectedMusicName.setBackground(new java.awt.Color(21, 28, 26));
        SelectedMusicName.setForeground(new java.awt.Color(166, 166, 166));
        SelectedMusicName.setText("ReminderAudio.wav");
        SelectedMusicName.setFocusable(false);

        javax.swing.GroupLayout SelectMusicPanelLayout = new javax.swing.GroupLayout(SelectMusicPanel);
        SelectMusicPanel.setLayout(SelectMusicPanelLayout);
        SelectMusicPanelLayout.setHorizontalGroup(
            SelectMusicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SelectMusicPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SelectMusicLabel)
                .addGap(18, 18, 18)
                .addComponent(SelectMusicButton)
                .addGap(18, 18, 18)
                .addComponent(SelectedMusicLabel)
                .addGap(18, 18, 18)
                .addComponent(SelectedMusicName, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        SelectMusicPanelLayout.setVerticalGroup(
            SelectMusicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SelectMusicPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SelectMusicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SelectMusicLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(SelectMusicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(SelectMusicButton, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(SelectedMusicLabel)
                        .addComponent(SelectedMusicName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout TasksPanelContainerLayout = new javax.swing.GroupLayout(TasksPanelContainer);
        TasksPanelContainer.setLayout(TasksPanelContainerLayout);
        TasksPanelContainerLayout.setHorizontalGroup(
            TasksPanelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TasksPanelContainerLayout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addGroup(TasksPanelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SelectMusicPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(TasksPanelContainerLayout.createSequentialGroup()
                        .addComponent(TasksInterface, javax.swing.GroupLayout.PREFERRED_SIZE, 816, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 118, Short.MAX_VALUE)))
                .addContainerGap())
        );
        TasksPanelContainerLayout.setVerticalGroup(
            TasksPanelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TasksPanelContainerLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(TasksInterface, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(SelectMusicPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(128, Short.MAX_VALUE))
        );

        TasksPanelHeader.setBackground(new java.awt.Color(26, 36, 33));

        TasksPanelHeaderButtons.setBackground(new java.awt.Color(21, 28, 26));

        TasksButton.setBackground(new java.awt.Color(51, 67, 62));
        TasksButton.setForeground(new java.awt.Color(217, 217, 217));
        TasksButton.setText("Tasks");
        TasksButton.setBorderPainted(false);
        TasksButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        TasksButton.setFocusPainted(false);
        TasksButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TasksButtonActionPerformed(evt);
            }
        });

        TodayButton.setBackground(new java.awt.Color(21, 28, 26));
        TodayButton.setForeground(new java.awt.Color(191, 191, 191));
        TodayButton.setText("Today");
        TodayButton.setBorderPainted(false);
        TodayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TodayButtonActionPerformed(evt);
            }
        });

        DetailsButton.setBackground(new java.awt.Color(21, 28, 26));
        DetailsButton.setForeground(new java.awt.Color(191, 191, 191));
        DetailsButton.setText("Details");
        DetailsButton.setBorderPainted(false);
        DetailsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DetailsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout TasksPanelHeaderButtonsLayout = new javax.swing.GroupLayout(TasksPanelHeaderButtons);
        TasksPanelHeaderButtons.setLayout(TasksPanelHeaderButtonsLayout);
        TasksPanelHeaderButtonsLayout.setHorizontalGroup(
            TasksPanelHeaderButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TasksPanelHeaderButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TasksButton, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TodayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DetailsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        TasksPanelHeaderButtonsLayout.setVerticalGroup(
            TasksPanelHeaderButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TasksPanelHeaderButtonsLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(TasksPanelHeaderButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TodayButton)
                    .addComponent(TasksButton)
                    .addComponent(DetailsButton))
                .addGap(6, 6, 6))
        );

        FirstCircle.setBackground(new java.awt.Color(26, 36, 33));

        javax.swing.GroupLayout FirstCircleLayout = new javax.swing.GroupLayout(FirstCircle);
        FirstCircle.setLayout(FirstCircleLayout);
        FirstCircleLayout.setHorizontalGroup(
            FirstCircleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );
        FirstCircleLayout.setVerticalGroup(
            FirstCircleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        SecondCircle.setBackground(new java.awt.Color(26, 36, 33));

        javax.swing.GroupLayout SecondCircleLayout = new javax.swing.GroupLayout(SecondCircle);
        SecondCircle.setLayout(SecondCircleLayout);
        SecondCircleLayout.setHorizontalGroup(
            SecondCircleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );
        SecondCircleLayout.setVerticalGroup(
            SecondCircleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        ThirdCircle.setBackground(new java.awt.Color(26, 36, 33));

        javax.swing.GroupLayout ThirdCircleLayout = new javax.swing.GroupLayout(ThirdCircle);
        ThirdCircle.setLayout(ThirdCircleLayout);
        ThirdCircleLayout.setHorizontalGroup(
            ThirdCircleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );
        ThirdCircleLayout.setVerticalGroup(
            ThirdCircleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        LogoutButton.setBackground(new java.awt.Color(26, 36, 33));
        LogoutButton.setForeground(new java.awt.Color(217, 217, 217));
        LogoutButton.setText("↪");
        LogoutButton.setBorderPainted(false);
        LogoutButton.setFocusPainted(false);
        LogoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutButtonActionPerformed(evt);
            }
        });

        ExitButton.setBackground(new java.awt.Color(26, 36, 33));
        ExitButton.setForeground(new java.awt.Color(217, 217, 217));
        ExitButton.setText("✖");
        ExitButton.setBorderPainted(false);
        ExitButton.setFocusPainted(false);
        ExitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitButtonActionPerformed(evt);
            }
        });

        MinimizeButton.setBackground(new java.awt.Color(26, 36, 33));
        MinimizeButton.setForeground(new java.awt.Color(217, 217, 217));
        MinimizeButton.setText("➖");
        MinimizeButton.setBorderPainted(false);
        MinimizeButton.setFocusPainted(false);
        MinimizeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MinimizeButtonActionPerformed(evt);
            }
        });

        ThemeChangeButton.setBackground(new java.awt.Color(51, 67, 62));
        ThemeChangeButton.setForeground(new java.awt.Color(217, 217, 217));
        ThemeChangeButton.setBorderPainted(false);
        ThemeChangeButton.setFocusPainted(false);

        javax.swing.GroupLayout TasksPanelHeaderLayout = new javax.swing.GroupLayout(TasksPanelHeader);
        TasksPanelHeader.setLayout(TasksPanelHeaderLayout);
        TasksPanelHeaderLayout.setHorizontalGroup(
            TasksPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TasksPanelHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(FirstCircle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(SecondCircle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(ThirdCircle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 256, Short.MAX_VALUE)
                .addComponent(TasksPanelHeaderButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(197, 197, 197)
                .addComponent(ThemeChangeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(MinimizeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ExitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LogoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        TasksPanelHeaderLayout.setVerticalGroup(
            TasksPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TasksPanelHeaderLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(TasksPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TasksPanelHeaderLayout.createSequentialGroup()
                        .addGroup(TasksPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TasksPanelHeaderButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(FirstCircle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(SecondCircle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ThirdCircle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LogoutButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(MinimizeButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ExitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(6, 6, 6))
                    .addGroup(TasksPanelHeaderLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(ThemeChangeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))))
        );

        TodayPanelContainer.setBackground(new java.awt.Color(21, 28, 26));
        TodayPanelContainer.setVisible(false);

        TodayInterface.setBackground(new java.awt.Color(21, 28, 26));
        TodayInterface.setBorder(null);
        TodayInterface.setOpaque(false);
        TodayInterface.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        TodayList.setBackground(new java.awt.Color(21, 28, 26));

        TodayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "Task Name", "Due Date", "Start Time", "Reminder", "Snooze", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TodayTable.setFocusable(false);
        TodayTable.setRowSelectionAllowed(false);
        TodayTable.setSelectionBackground(new java.awt.Color(40, 40, 40));
        TodayScrollPane.setViewportView(TodayTable);

        TodayListLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        TodayListLabel.setForeground(new java.awt.Color(23, 162, 184));
        TodayListLabel.setText("Today's Tasks");

        javax.swing.GroupLayout TodayListLayout = new javax.swing.GroupLayout(TodayList);
        TodayList.setLayout(TodayListLayout);
        TodayListLayout.setHorizontalGroup(
            TodayListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TodayListLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TodayListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TodayListLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TodayScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 804, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        TodayListLayout.setVerticalGroup(
            TodayListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TodayListLayout.createSequentialGroup()
                .addComponent(TodayListLabel)
                .addGap(29, 29, 29)
                .addComponent(TodayScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
                .addContainerGap())
        );

        TodayInterface.setViewportView(TodayList);

        javax.swing.GroupLayout TodayPanelContainerLayout = new javax.swing.GroupLayout(TodayPanelContainer);
        TodayPanelContainer.setLayout(TodayPanelContainerLayout);
        TodayPanelContainerLayout.setHorizontalGroup(
            TodayPanelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TodayPanelContainerLayout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addComponent(TodayInterface, javax.swing.GroupLayout.PREFERRED_SIZE, 816, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(124, Short.MAX_VALUE))
        );
        TodayPanelContainerLayout.setVerticalGroup(
            TodayPanelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TodayPanelContainerLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(TodayInterface, javax.swing.GroupLayout.PREFERRED_SIZE, 636, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );

        DetailsPanelContainer.setBackground(new java.awt.Color(21, 28, 26));
        TodayPanelContainer.setVisible(false);

        DetailsInterface.setBackground(new java.awt.Color(21, 28, 26));
        DetailsInterface.setBorder(null);
        DetailsInterface.setOpaque(false);
        DetailsInterface.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        DetailsList.setBackground(new java.awt.Color(21, 28, 26));

        DetailsListLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        DetailsListLabel.setForeground(new java.awt.Color(102, 255, 0));
        DetailsListLabel.setText("Tasks Details");

        DetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "Task Name", "Due Date", "Start Time", "Reminder", "Snooze", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        DetailsTable.setFocusable(false);
        DetailsScrollPane.setViewportView(DetailsTable);

        javax.swing.GroupLayout DetailsListLayout = new javax.swing.GroupLayout(DetailsList);
        DetailsList.setLayout(DetailsListLayout);
        DetailsListLayout.setHorizontalGroup(
            DetailsListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DetailsListLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DetailsListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(DetailsListLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DetailsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 804, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        DetailsListLayout.setVerticalGroup(
            DetailsListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DetailsListLayout.createSequentialGroup()
                .addComponent(DetailsListLabel)
                .addGap(29, 29, 29)
                .addComponent(DetailsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 576, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        DetailsInterface.setViewportView(DetailsList);

        javax.swing.GroupLayout DetailsPanelContainerLayout = new javax.swing.GroupLayout(DetailsPanelContainer);
        DetailsPanelContainer.setLayout(DetailsPanelContainerLayout);
        DetailsPanelContainerLayout.setHorizontalGroup(
            DetailsPanelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DetailsPanelContainerLayout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addComponent(DetailsInterface, javax.swing.GroupLayout.PREFERRED_SIZE, 816, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(124, Short.MAX_VALUE))
        );
        DetailsPanelContainerLayout.setVerticalGroup(
            DetailsPanelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DetailsPanelContainerLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(DetailsInterface, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TasksPanelContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(TasksPanelHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(TodayPanelContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(DetailsPanelContainer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(TasksPanelContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(TasksPanelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 735, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0, 47, Short.MAX_VALUE)
                    .addComponent(TodayPanelContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0, 47, Short.MAX_VALUE)
                    .addComponent(DetailsPanelContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    private void showFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Audio files", "mp3", "wav");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            SelectedMusicName.setText(selectedFile.getName());
            SelectedMusicLabel.setText("Selected Music:");
            reminderMusicPath = selectedFile;
        } else {
            
        }
    }
    
    private static boolean isWavFile(File file) {
        return file.getName().toLowerCase().endsWith(".wav");
    }

    private static boolean isMp3File(File file) {
        return file.getName().toLowerCase().endsWith(".mp3");
    }
    
    private void exitApplication() {
        SystemTray.getSystemTray().remove(trayIcon);
        System.exit(0);
    }

    private void showApplication() {
        this.setVisible(true);
        this.setState(JFrame.NORMAL);
    }
    
    public static Main getInstance() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }
    
    public static void disposeFrame() {
        if (instance != null) {
            instance.dispose();
        }
    }
    
    private void ExitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitButtonActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_ExitButtonActionPerformed

    private void TodayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TodayButtonActionPerformed
        TodayButton.setBackground(new Color(51,67,62));
        TodayButton.setForeground(new Color(217, 217, 217));
        TodayPanelContainer.setVisible(true);
        TasksButton.setBackground(new Color(21,28,26));
        TasksButton.setForeground(new Color(191, 191, 191));
        TasksPanelContainer.setVisible(false);
        DetailsButton.setBackground(new Color(21,28,26));
        DetailsButton.setForeground(new Color(191, 191, 191));
        DetailsPanelContainer.setVisible(false);
    }//GEN-LAST:event_TodayButtonActionPerformed

    private void TasksButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TasksButtonActionPerformed
        TasksButton.setBackground(new Color(51,67,62));
        TasksButton.setForeground(new Color(217, 217, 217));
        TasksPanelContainer.setVisible(true);
        TodayButton.setBackground(new Color(21,28,26));
        TodayButton.setForeground(new Color(191, 191, 191));
        TodayPanelContainer.setVisible(false);
        DetailsButton.setBackground(new Color(21,28,26));
        DetailsButton.setForeground(new Color(191, 191, 191));
        DetailsPanelContainer.setVisible(false);
    }//GEN-LAST:event_TasksButtonActionPerformed

    private void DetailsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DetailsButtonActionPerformed
        DetailsButton.setBackground(new Color(51,67,62));
        DetailsButton.setForeground(new Color(217, 217, 217));
        DetailsPanelContainer.setVisible(true);
        TodayButton.setBackground(new Color(21,28,26));
        TodayButton.setForeground(new Color(191, 191, 191));
        TodayPanelContainer.setVisible(false);
        TasksButton.setBackground(new Color(21,28,26));
        TasksButton.setForeground(new Color(191, 191, 191));
        TasksPanelContainer.setVisible(false);
    }//GEN-LAST:event_DetailsButtonActionPerformed

    private void LogoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutButtonActionPerformed
        int option = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Exit Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            user.Authenticator authenticator = new user.Authenticator();
            int loggedInID = authenticator.getLoggedInUserID();
            authenticator.updateLoginStatus(loggedInID, false);
            setVisible(false);
            dispose();
            Login loginFrame = Login.getInstance();
                SwingUtilities.invokeLater(() -> {
                loginFrame.setVisible(true);
            });
        }
    }//GEN-LAST:event_LogoutButtonActionPerformed

    private void AddTaskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddTaskButtonActionPerformed
        new popupFrame.AddTask().setVisible(true);
    }//GEN-LAST:event_AddTaskButtonActionPerformed

    private void MinimizeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MinimizeButtonActionPerformed
        this.setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_MinimizeButtonActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        try{
//            UIManager.setLookAndFeel(new FlatDarkLaf());
//        } cadtch (UnsupportedLookAndFeelException e){}
//        
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                Main mainFrame = new Main(this);
//                mainFrame.setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddTaskButton;
    private javax.swing.JButton DetailsButton;
    private javax.swing.JScrollPane DetailsInterface;
    private javax.swing.JPanel DetailsList;
    private javax.swing.JLabel DetailsListLabel;
    private javax.swing.JPanel DetailsPanelContainer;
    private javax.swing.JScrollPane DetailsScrollPane;
    private customComponents.TableDarkStatus DetailsTable;
    private javax.swing.JButton ExitButton;
    private javax.swing.JPanel FirstCircle;
    private javax.swing.JButton LogoutButton;
    private javax.swing.JButton MinimizeButton;
    private javax.swing.JPanel SecondCircle;
    private javax.swing.JButton SelectMusicButton;
    private javax.swing.JLabel SelectMusicLabel;
    private javax.swing.JPanel SelectMusicPanel;
    private javax.swing.JLabel SelectedMusicLabel;
    private javax.swing.JTextField SelectedMusicName;
    private javax.swing.JButton TasksButton;
    private javax.swing.JScrollPane TasksInterface;
    private javax.swing.JPanel TasksList;
    private javax.swing.JLabel TasksListLabel;
    private javax.swing.JPanel TasksPanelContainer;
    private javax.swing.JPanel TasksPanelHeader;
    private javax.swing.JPanel TasksPanelHeaderButtons;
    private javax.swing.JScrollPane TasksScrollPane;
    private customComponents.TableDark TasksTable;
    private javax.swing.JButton ThemeChangeButton;
    private javax.swing.JPanel ThirdCircle;
    private javax.swing.JButton TodayButton;
    private javax.swing.JScrollPane TodayInterface;
    private javax.swing.JPanel TodayList;
    private javax.swing.JLabel TodayListLabel;
    private javax.swing.JPanel TodayPanelContainer;
    private javax.swing.JScrollPane TodayScrollPane;
    private customComponents.TableDark TodayTable;
    // End of variables declaration//GEN-END:variables
}
