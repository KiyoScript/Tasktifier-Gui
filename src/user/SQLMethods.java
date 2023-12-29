/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package user;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class SQLMethods {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/tasktifier_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection;
    
    public SQLMethods(){}
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        }
        return connection;
    }
    
    public static void testConnection() throws SQLException {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                System.out.println("Connected to the database!");
            }
        } catch (SQLException e) {
            System.out.println("Not connected to the database! Make sure that MySQL server is open and `tasktifier_db` database exists.");
            System.exit(0);
        }
    }

    public static HashMap<String, String> createUser(HashMap<String, String> userData) {
        HashMap<String, String> resultData = new HashMap<>();
        try {
            Connection connection = getConnection();
            String sql = "INSERT INTO users (email, password) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, userData.get("email"));
                preparedStatement.setString(2, userData.get("password"));
                preparedStatement.executeUpdate();

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        resultData.put("generatedKey", String.valueOf(generatedKeys.getLong(1)));
                    }
                }
            }
        } catch (SQLException ex) {
            resultData.put("error", ex.getMessage());
        }
        return resultData;
    }
    
    public static HashMap<String, String> findUser(String email) {
        HashMap<String, String> resultData = new HashMap<>();

        try {
            Connection connection = getConnection();
            String sql = "SELECT * FROM users WHERE email = ?";
        
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, email);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        resultData.put("id", String.valueOf(resultSet.getLong("id")));
                        resultData.put("email", resultSet.getString("email"));
                        resultData.put("password", resultSet.getString("password"));
                    }
                }
            }
        } catch (SQLException ex) {
            resultData.put("error", ex.getMessage());
        }

        return resultData.isEmpty() ? null : resultData;
    }
    // Methods below will be implemented soon while implementing tasks GUI
    /*
        public static HashMap<String, String> find(HashMap<String, String> userData)
        public static HashMap<String, String> update(HashMap<String, String> userData)
        public static HashMap<String, String> delete(HashMap<String, String> userData)
    */
    
    public void createTask(String taskName, LocalDate dueDate, LocalTime startTime,
                                        LocalTime reminder, String notes, int userId, int snoozeMinutes) {
        String query = "INSERT INTO tasks(task_name, due_date, start_time, reminder, notes, user_id, snooze_reminder, snooze_minutes) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, taskName);
            statement.setDate(2, java.sql.Date.valueOf(dueDate));
            statement.setTime(3, java.sql.Time.valueOf(startTime));
            statement.setTime(4, java.sql.Time.valueOf(reminder));
            statement.setString(5, notes);
            statement.setInt(6, userId);
            statement.setTime(7, java.sql.Time.valueOf(reminder));
            statement.setInt(8, snoozeMinutes);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Task added successfully.");
            } else {
                System.out.println("Failed to add task.");
            }
        } catch (SQLException e) {}
    }
    
    public List<Object[]> readUsersList() throws SQLException {
        List<Object[]> userList = new ArrayList<>();
        String query = "SELECT id, email, status FROM users WHERE email != 'admin@tasktifier.com'";

        try (Connection connection = SQLMethods.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String status = resultSet.getString("status");

                Object[] user = {id, email, status};
                userList.add(user);
            }
        }

        return userList;
    }
    public List<Object[]> readUndoneTasks(int userID) throws SQLException {
        List<Object[]> taskList = new ArrayList<>();
        
        String sql = "SELECT task_name, due_date, start_time, reminder, snooze_minutes FROM tasks WHERE user_id = ? AND is_done = 0";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userID);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int snoozeMinutes = resultSet.getInt("snooze_minutes");
                    String snoozeLabel = snoozeMinutes == 1 ? " minute" : " minutes";

                    Object[] row = new Object[]{
                        resultSet.getString("task_name"),
                        resultSet.getString("due_date"),
                        convertTo12HourFormat(resultSet.getString("start_time")),
                        convertTo12HourFormat(resultSet.getString("reminder")),
                        snoozeMinutes + snoozeLabel,
                        null
                    };
                    taskList.add(row);
                }
            }
        }
        return taskList;
    }
    public List<Object[]> readTodayTasks(int userId) throws SQLException {
        List<Object[]> tasksData = new ArrayList<>();
        
        String sql = "SELECT task_name, due_date, start_time, reminder, snooze_minutes FROM tasks WHERE due_date = CURDATE() AND is_done = 0 AND user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int snoozeMinutes = resultSet.getInt("snooze_minutes");
                    String snoozeLabel = snoozeMinutes == 1 ? " minute" : " minutes";

                    Object[] row = new Object[]{
                        resultSet.getString("task_name"),
                        resultSet.getString("due_date"),
                        convertTo12HourFormat(resultSet.getString("start_time")),
                        convertTo12HourFormat(resultSet.getString("reminder")),
                        snoozeMinutes + snoozeLabel,
                        null
                    };
                    tasksData.add(row);
                }
            }
        }
        return tasksData;
    }
    public int readCountTodayTasks(int userId) throws SQLException {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM tasks WHERE due_date = CURDATE() AND is_done = 0 AND user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
        }
        return count;
    }
    public List<Object[]> readDetailsTasks(int userID) throws SQLException {
        List<Object[]> result = new ArrayList<>();

        String sql = "SELECT task_name, due_date, start_time, reminder, snooze_minutes, status FROM tasks JOIN details ON tasks.id = details.tasks_id WHERE user_id = ? ORDER BY tasks.due_date ASC, tasks.start_time ASC";

        try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userID);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int snoozeMinutes = resultSet.getInt("snooze_minutes");
                    String snoozeLabel = snoozeMinutes == 1 ? " minute" : " minutes";
                    
                    Object[] row = new Object[]{
                        resultSet.getString("task_name"),
                        resultSet.getDate("due_date"),
                        resultSet.getTime("start_time"),
                        resultSet.getString("reminder"),
                        snoozeMinutes + snoozeLabel,
                        resultSet.getString("status")
                    };
                result.add(row);
                }
            }
        }
        return result;
    }
    public List<LocalDate> readDueDates(int userId) throws SQLException {
        List<LocalDate> result = new ArrayList<>();

        String sql = "SELECT due_date FROM tasks WHERE user_id = ? AND is_done = 0;";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String dueDateString = resultSet.getString("due_date");
                    LocalDate dueDate = LocalDate.parse(dueDateString);
                    result.add(dueDate);
                }
            }
        }

        return result;
    }
    public List<LocalDateTime> readDueDateWithStartTime(int userId) throws SQLException {
        List<LocalDateTime> result = new ArrayList<>();

        String sql = "SELECT CONCAT(due_date, 'T', LEFT(start_time, LENGTH(start_time) - 3)) AS due_date_with_start_time " +
                     "FROM tasks " +
                     "WHERE user_id = ? AND is_done = 0";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String dueDateWithStartTimeString = resultSet.getString("due_date_with_start_time");
                    LocalDateTime dueDateWithStartTime = LocalDateTime.parse(dueDateWithStartTimeString);
                    result.add(dueDateWithStartTime);
                }
            }
        }

        return result;
    }
    public List<LocalTime> readSnoozeReminder(int userId) throws SQLException {
        List<LocalTime> result = new ArrayList<>();

        String sql = "SELECT LEFT(snooze_reminder, LENGTH(snooze_reminder) - 3) AS reminder FROM tasks WHERE user_id = ? AND is_done = 0";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String reminderString = resultSet.getString("reminder");
                    LocalTime reminder = LocalTime.parse(reminderString);
                    result.add(reminder);
                }
            }
        }

        return result;
    }
    public int[] readUndoneTaskIDs(int userID) {
        String sql = "SELECT id FROM tasks WHERE user_id = ? AND is_done = 0;";
        List<Integer> idList = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userID);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int taskId = resultSet.getInt("id");
                    idList.add(taskId);
                }
            }

        } catch (SQLException e) {}

        return idList.stream().mapToInt(Integer::intValue).toArray();
    }
    public int[] readUserIDs() {
        String sql = "SELECT id FROM users WHERE id != 7";
        List<Integer> idList = new ArrayList<>();

        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int userId = resultSet.getInt("id");
                idList.add(userId);
            }

        } catch (SQLException e) {}

        return idList.stream().mapToInt(Integer::intValue).toArray();
    }
    public List<Object[]> readUserToBeUpdated(int userId) {
        String query = "SELECT id, email, password FROM users WHERE id = ?";
        List<Object[]> userData = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");

                    userData.add(new Object[]{id, email, password});
                }
            }

        } catch (SQLException e) { System.out.println(e); }

        return userData;
    }
    private String convertTo12HourFormat(String time24Hour) {
        if (time24Hour != null && !time24Hour.isEmpty()) {
            LocalTime localTime = LocalTime.parse(time24Hour, DateTimeFormatter.ofPattern("HH:mm:ss"));

            return localTime.format(DateTimeFormatter.ofPattern("hh:mm a"));
        } else {
            return time24Hour;
        }
    }
    public List<Object[]> readTaskNameAndNotesFromDueDatesWithStartTime(int userId, LocalDate dueDate, LocalTime startTime) throws SQLException {
        List<Object[]> result = new ArrayList<>();

        String sql = "SELECT task_name, notes FROM tasks WHERE user_id = ? AND is_done = 0 AND start_time = ? AND due_date = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setTime(2, java.sql.Time.valueOf(startTime));
            statement.setDate(3, java.sql.Date.valueOf(dueDate));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Object[] row = new Object[]{
                            resultSet.getString("task_name"),
                            resultSet.getString("notes")
                    };
                    result.add(row);
                }
            }
        } catch (SQLException e) {}

        return result;
    }
    public List<Object[]> readTaskNameAndNotesFromReminder(int userId, LocalTime reminder) throws SQLException {
        List<Object[]> result = new ArrayList<>();

        String sql = "SELECT task_name, notes FROM tasks WHERE user_id = ? AND is_done = 0 AND reminder = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setTime(2, java.sql.Time.valueOf(reminder));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Object[] row = new Object[]{
                            resultSet.getString("task_name"),
                            resultSet.getString("notes")
                    };
                    result.add(row);
                }
            }
        } catch (SQLException e) {}

        return result;
    }
    
    public void updateTaskToDoneByID(int taskId) {
        try {
            String insertQuery = "INSERT INTO details(tasks_id, status) VALUES(?, 'COMPLETED')";
            try (Connection connection = getConnection();
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setInt(1, taskId);
                insertStatement.executeUpdate();
            }

            String updateQuery = "UPDATE tasks SET is_done = 1 WHERE id = ?";
            try (Connection connection = getConnection();
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setInt(1, taskId);
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {}
    }
    public void updateUserStatus(int userId) {
        try {
            String updateStatusQuery = "UPDATE users SET status = CASE WHEN status = 'DISABLED' THEN 'ENABLED' ELSE 'DISABLED' END WHERE id = ?";

            try (Connection connection = getConnection();
                PreparedStatement updateStatusStatement = connection.prepareStatement(updateStatusQuery)) {

                updateStatusStatement.setInt(1, userId);
                updateStatusStatement.executeUpdate();
            }
        } catch (SQLException e) {}
    }
    public void updateTasksToNotDone() {
        try {
            String selectQuery = "SELECT id FROM tasks WHERE due_date < CURDATE() AND is_done = 0";
            String insertQuery = "INSERT INTO details(tasks_id, status) VALUES(?, 'FAILED')";
            String updateQuery = "UPDATE tasks SET is_done = 1 WHERE id = ?";

            try (Connection connection = getConnection();
                PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                ResultSet resultSet = selectStatement.executeQuery()) {

                while (resultSet.next()) {
                    int taskId = resultSet.getInt("id");

                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                        PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

                        insertStatement.setInt(1, taskId);
                        insertStatement.executeUpdate();

                        updateStatement.setInt(1, taskId);
                        updateStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {}
    }
    public void updateSnoozeReminder(LocalTime snoozeReminder, int userID) {
        String snoozeMinutesQuery = "SELECT snooze_minutes FROM tasks WHERE snooze_reminder = ? AND user_id = ?;";

        String updateQuery = "UPDATE tasks SET snooze_reminder = SEC_TO_TIME(TIME_TO_SEC(?) + (? * 60)) " +
                             "WHERE snooze_reminder = ? AND user_id = ?;";

        try (Connection connection = getConnection();
            PreparedStatement snoozeMinutesStatement = connection.prepareStatement(snoozeMinutesQuery);
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            Time sqlSnoozeReminder = Time.valueOf(snoozeReminder);

            snoozeMinutesStatement.setTime(1, sqlSnoozeReminder);
            snoozeMinutesStatement.setInt(2, userID);

            ResultSet resultSet = snoozeMinutesStatement.executeQuery();

            int snooze_minutes = 0;
            if (resultSet.next()) {
                snooze_minutes = resultSet.getInt("snooze_minutes");
            }

            updateStatement.setTime(1, sqlSnoozeReminder);
            updateStatement.setInt(2, snooze_minutes);
            updateStatement.setTime(3, sqlSnoozeReminder);
            updateStatement.setInt(4, userID);

            int rowsUpdated = updateStatement.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);

        } catch (SQLException e) { System.out.println(e); }
    }
    public void updateUser(int oldId, int newId, String email, String password) {
        String query = "UPDATE users SET id = ?, email = ?, password = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, newId);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setInt(4, oldId);

            statement.executeUpdate();
        } catch (SQLException e) { System.out.println(e); }
    }
    
    public void deleteTaskByID(int taskId) {
        String sql = "DELETE FROM tasks WHERE id = ?;";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, taskId);
            statement.executeUpdate();

        } catch (SQLException e) {}
    }
    public void deleteUserByID(int userId) {
        String deleteDetailsSql = "DELETE FROM details WHERE tasks_id = (SELECT id FROM tasks WHERE user_id = ?);";

        String deleteTaskSql = "DELETE FROM tasks WHERE user_id = ?;";

        String deleteUserSql = "DELETE FROM users WHERE id = ?;";

        try (Connection connection = getConnection();
            PreparedStatement deleteDetailsStatement = connection.prepareStatement(deleteDetailsSql);
            PreparedStatement deleteTaskStatement = connection.prepareStatement(deleteTaskSql);
            PreparedStatement deleteUserStatement = connection.prepareStatement(deleteUserSql)) {

            deleteDetailsStatement.setInt(1, userId);
            deleteDetailsStatement.executeUpdate();

            deleteTaskStatement.setInt(1, userId);
            deleteTaskStatement.executeUpdate();

            deleteUserStatement.setInt(1, userId);
            deleteUserStatement.executeUpdate();

        } catch (SQLException e) { System.out.println(e); }
    }
}
