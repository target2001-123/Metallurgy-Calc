import java.sql.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Класс для реализации базы данных проекта
 * @author Топалов Максим
 * @version 1.0
 * */

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:users.db";

    /**
     * Инициализация базы данных
     * */
    // Инициализация базы данных с двумя таблицами
    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Таблица пользователей
            String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            stmt.execute(usersTable);

            // Таблица результатов фурмы расчетов
            String resultsTable1 = "CREATE TABLE IF NOT EXISTS OxygenTuyere (" +
                    "record_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id DECIMAL(10,2) NOT NULL, " +
                    "nozzle_angle INTEGER NOT NULL, " +
                    "nozzle_count TEXT NOT NULL, " +
                    "calculation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)";
            stmt.execute(resultsTable1);

            // Таблица результатов сопла Лаваля расчетов
            String resultsTable2 = "CREATE TABLE IF NOT EXISTS Laval_nozzle (" +
                    "record_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL, " +
                    "P1 INTEGER NOT NULL, " +
                    "Wcr INTEGER NOT NULL, " +
                    "Pcr INTEGER NOT NULL, " +
                    "Tcr INTEGER NOT NULL, " +
                    "p_cr INTEGER NOT NULL, " +
                    "Fcr INTEGER NOT NULL, " +
                    "Dcr INTEGER NOT NULL, " +
                    "P2 INTEGER NOT NULL, " +
                    "T2 INTEGER NOT NULL, " +
                    "RO2 INTEGER NOT NULL, " +
                    "W2 INTEGER NOT NULL, " +
                    "F2 INTEGER NOT NULL, " +
                    "D2 INTEGER NOT NULL, " +
                    "l2 INTEGER NOT NULL, " +
                    "l1 INTEGER NOT NULL, " +
                    "D1 INTEGER NOT NULL, " +
                    "Fkt INTEGER NOT NULL, " +
                    "Dkt INTEGER NOT NULL, " +
                    "Dc INTEGER NOT NULL, " +
                    "calculation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)";
            stmt.execute(resultsTable2);

            System.out.println("База данных и таблицы инициализированы");

        } catch (SQLException e) {
            System.err.println("Ошибка инициализации базы данных: " + e.getMessage());
        }

        addUser("admin", "password");
    }

    // Методы для работы с пользователями (остаются без изменений)
    /**
     * Регистрация пользователя в базе данных
     * @param username - имя пользователя
     * @param password - пароль пользователя
     * */
    public static boolean addUser(String username, String password) {
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int hashed = password.hashCode();

            pstmt.setString(1, username);
            pstmt.setString(2, String.valueOf(hashed));
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Ошибка добавления пользователя: " + e.getMessage());
            return false;
        }
    }
    /**
     * Отображение таблицы расчета Фурмы
     * */
    // Отображение таблицы расчета Фурмы
    private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // Получаем названия колонок
        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }

        // Получаем данные
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getObject(i));
            }
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Запрещаем редактирование ячеек
            }
        };
    }

    /**
     * Метод для отображения записей OxygenTuyere для конкретного пользователя
     * @param userId - идентификатор пользователя
     */
    public static void showOxygenTuyereTableForUser(int userId) {
        String sql = "SELECT * FROM OxygenTuyere WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            // Создаем и настраиваем окно
            JFrame frame = new JFrame("Данные OxygenTuyere для пользователя ID: " + userId);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);

            // Создаем модель таблицы из ResultSet
            DefaultTableModel tableModel = buildTableModel(rs);
            JTable table = new JTable(tableModel);
            table.setAutoCreateRowSorter(true);

            // Настраиваем внешний вид таблицы
            table.setFont(new Font("Arial", Font.PLAIN, 12));
            table.setRowHeight(25);
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

            // Добавляем прокрутку
            JScrollPane scrollPane = new JScrollPane(table);

            // Панель с информацией и кнопками
            JPanel infoPanel = new JPanel(new BorderLayout());
            JLabel infoLabel = new JLabel("Записи пользователя ID: " + userId + " | Найдено записей: " + tableModel.getRowCount());
            infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
            infoPanel.add(infoLabel, BorderLayout.WEST);

            JPanel buttonPanel = new JPanel();
            JButton closeButton = new JButton("Закрыть");
            JButton Sort = new JButton("Сортировка по...");
            JTextField sortEl = new JTextField(15);

            // Сортировка по P1
            Sort.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String el = sortEl.getText();
                    String sql = "SELECT * FROM OxygenTuyere WHERE user_id = ? ORDER BY CAST(" + el + " AS REAL)";
                    showSortedOxygenTuyereTableForUser(userId, sql);
                    frame.dispose();
                }
            });


            buttonPanel.add(Sort);
            buttonPanel.add(sortEl);

            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Main.HistoryWindow(userId);
                    frame.dispose(); // Закрываем окно выбора
                }
            });

            buttonPanel.add(closeButton);
            infoPanel.add(buttonPanel, BorderLayout.EAST);

            // Компоновка окна
            frame.setLayout(new BorderLayout());
            frame.add(infoPanel, BorderLayout.NORTH);
            frame.add(scrollPane, BorderLayout.CENTER);

            frame.setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка при загрузке данных для пользователя " + userId + ": " + e.getMessage(),
                    "Ошибка базы данных", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Отображение таблицы расчета Фурмы в сортированном виде
     * @param userId - идентификатор пользователя
     * @param sql - критерий сортировки (имя столбца, по которому выполнится сортировка)
     * */
    // Отсортированная история расчетов для Фурмы
    public static void showSortedOxygenTuyereTableForUser(int userId, String sql) {

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            // Создаем и настраиваем окно
            JFrame frame = new JFrame("Данные OxygenTuyere для пользователя ID: " + userId);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);

            // Создаем модель таблицы из ResultSet
            DefaultTableModel tableModel = buildTableModel(rs);
            JTable table = new JTable(tableModel);
            table.setAutoCreateRowSorter(true);

            // Настраиваем внешний вид таблицы
            table.setFont(new Font("Arial", Font.PLAIN, 12));
            table.setRowHeight(25);
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

            // Добавляем прокрутку
            JScrollPane scrollPane = new JScrollPane(table);

            // Панель с информацией и кнопками
            JPanel infoPanel = new JPanel(new BorderLayout());
            JLabel infoLabel = new JLabel("Записи пользователя ID: " + userId + " | Найдено записей: " + tableModel.getRowCount());
            infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
            infoPanel.add(infoLabel, BorderLayout.WEST);

            JPanel buttonPanel = new JPanel();
            JButton closeButton = new JButton("Закрыть");

            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showOxygenTuyereTableForUser(userId);
                    frame.dispose(); // Закрываем окно выбора
                }
            });

            buttonPanel.add(closeButton);
            infoPanel.add(buttonPanel, BorderLayout.EAST);

            // Компоновка окна
            frame.setLayout(new BorderLayout());
            frame.add(infoPanel, BorderLayout.NORTH);
            frame.add(scrollPane, BorderLayout.CENTER);

            frame.setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка при загрузке данных для пользователя " + userId + ": " + e.getMessage(),
                    "Ошибка базы данных", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Общая история расчетов для сопла Лаваля
     * @param userId - идентификатор пользователя
     * */
    // Общая история расчетов для сопла Лаваля
    public static void showLavalNozzleTableForUser(int userId) {
        String sql = "SELECT * FROM Laval_nozzle WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            // Создаем и настраиваем окно
            JFrame frame = new JFrame("Данные Laval_nozzle для пользователя ID: " + userId);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);

            // Создаем модель таблицы из ResultSet
            DefaultTableModel tableModel = buildTableModel(rs);
            JTable table = new JTable(tableModel);
            table.setAutoCreateRowSorter(true);

            // Настраиваем внешний вид таблицы
            table.setFont(new Font("Arial", Font.PLAIN, 12));
            table.setRowHeight(25);
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

            // Добавляем прокрутку
            JScrollPane scrollPane = new JScrollPane(table);

            // Панель с информацией и кнопками
            JPanel infoPanel = new JPanel(new BorderLayout());
            JLabel infoLabel = new JLabel("Записи пользователя ID: " + userId + " | Найдено записей: " + tableModel.getRowCount());
            infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
            infoPanel.add(infoLabel, BorderLayout.WEST);

            JPanel buttonPanel = new JPanel();
            JButton closeButton = new JButton("Закрыть");
            JButton Pmax = new JButton("Сортировка по...");
            JTextField sortEl = new JTextField(5);

            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Main.HistoryWindow(userId);
                    frame.dispose(); // Закрываем окно выбора
                }
            });
            // Сортировка по P1
            Pmax.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String el = sortEl.getText();
                    String sql = "SELECT * FROM Laval_nozzle WHERE user_id = ? ORDER BY " + el;
                    showSortedLavalNozzleTableForUser(userId, sql);
                    frame.dispose();
                }
            });


            buttonPanel.add(Pmax);
            buttonPanel.add(sortEl);
            buttonPanel.add(closeButton);
            infoPanel.add(buttonPanel, BorderLayout.EAST);

            // Компоновка окна
            frame.setLayout(new BorderLayout());
            frame.add(infoPanel, BorderLayout.NORTH);
            frame.add(scrollPane, BorderLayout.CENTER);

            frame.setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка при загрузке данных для пользователя " + userId + ": " + e.getMessage(),
                    "Ошибка базы данных", JOptionPane.ERROR_MESSAGE);
        }
    }
    // Общая отсортированная история расчетов для сопла Лаваля
    /**
     * Таблица результатов вычислений параметров сопла Лаваля в сортированном виде
     * @param userId - идентификатор пользователя
     * @param sql - критерий сортировки (название столбца, по значениям которого выполнится сортировка)
     * */
    public static void showSortedLavalNozzleTableForUser(int userId, String sql) {

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            // Создаем и настраиваем окно
            JFrame frame = new JFrame("Данные Laval_nozzle для пользователя ID: " + userId);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);

            // Создаем модель таблицы из ResultSet
            DefaultTableModel tableModel = buildTableModel(rs);
            JTable table = new JTable(tableModel);
            table.setAutoCreateRowSorter(true);

            // Настраиваем внешний вид таблицы
            table.setFont(new Font("Arial", Font.PLAIN, 12));
            table.setRowHeight(25);
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

            // Добавляем прокрутку
            JScrollPane scrollPane = new JScrollPane(table);

            // Панель с информацией и кнопками
            JPanel infoPanel = new JPanel(new BorderLayout());
            JLabel infoLabel = new JLabel("Записи пользователя ID: " + userId + " | Найдено записей: " + tableModel.getRowCount());
            infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
            infoPanel.add(infoLabel, BorderLayout.WEST);

            JPanel buttonPanel = new JPanel();
            JButton closeButton = new JButton("Закрыть");

            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showLavalNozzleTableForUser(userId);
                    frame.dispose(); // Закрываем окно выбора
                }
            });

            buttonPanel.add(closeButton);
            infoPanel.add(buttonPanel, BorderLayout.EAST);

            // Компоновка окна
            frame.setLayout(new BorderLayout());
            frame.add(infoPanel, BorderLayout.NORTH);
            frame.add(scrollPane, BorderLayout.CENTER);

            frame.setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка при загрузке данных для пользователя " + userId + ": " + e.getMessage(),
                    "Ошибка базы данных", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Метод для работы с расчетами Фурмы (остаются без изменений)
    /**
     * Добавление результатов расчета кислородной фурмы в базу данных
     * */
    public static boolean addNozzle(Integer us_id, double angle, int count) {
        String sql = "INSERT INTO OxygenTuyere(user_id, nozzle_angle, nozzle_count) VALUES(?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, us_id);
            pstmt.setDouble(2, angle);
            pstmt.setInt(3, count);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Ошибка добавления пользователя: " + e.getMessage());
            return false;
        }
    }
    /**
     * Добавление результатов расчета параметров сопла Лаваля в базу данных
     * */
    // Метод для работы с расчетами Фурмы (остаются без изменений)
    public static boolean LavalNozzle(Integer us_id, double n1, double n2, double n3, double n4, double n5, double n6, double n7, double n8, double n9, double n10, double n11, double n12, double n13, double n14, double n15, double n16, double n17, double n18, double n19) {
        String sql = "INSERT INTO Laval_nozzle(P1, Wcr, Pcr, Tcr, p_cr, Fcr, Dcr, P2, T2, RO2, W2, F2, D2, l2, l1, D1, Fkt, Dkt, Dc, user_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, n1);
            pstmt.setDouble(2, n2);
            pstmt.setDouble(3, n3);
            pstmt.setDouble(4, n4);
            pstmt.setDouble(5, n5);
            pstmt.setDouble(6, n6);
            pstmt.setDouble(7, n7);
            pstmt.setDouble(8, n8);
            pstmt.setDouble(9, n9);
            pstmt.setDouble(10, n10);
            pstmt.setDouble(11, n11);
            pstmt.setDouble(12, n12);
            pstmt.setDouble(13, n13);
            pstmt.setDouble(14, n14);
            pstmt.setDouble(15, n15);
            pstmt.setDouble(16, n16);
            pstmt.setDouble(17, n17);
            pstmt.setDouble(18, n18);
            pstmt.setDouble(19, n19);
            pstmt.setDouble(20, us_id);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Ошибка добавления пользователя: " + e.getMessage());
            return false;
        }
    }
    /**
     * Аутентификация пользователя
     * @param username - имя пользователя
     * @param password - пароль пользователя
     * */
    public static boolean authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int hashed = password.hashCode();
            pstmt.setString(1, username);
            pstmt.setString(2, String.valueOf(hashed));
            ResultSet rs = pstmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            System.err.println("Ошибка аутентификации: " + e.getMessage());
            return false;
        }
    }


    /**
     * Получение ID пользователя по имени
     * @param username - имя пользователя
     * */
    // Получение ID пользователя по имени
    public static Integer getUserId(String username) {
        String sql = "SELECT user_id FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }
            return null;

        } catch (SQLException e) {
            System.err.println("Ошибка получения ID пользователя: " + e.getMessage());
            return null;
        }
    }


    /**
     * Вывод таблицы базы данных
     * */
    public static void showSimpleTableData(String tableName) {
        String sql = "SELECT * FROM " + tableName;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            // Просто выводим все строки
            while (rs.next()) {
                for (int i = 1; i <= colCount; i++) {
                    System.out.print(rs.getString(i) + " ");
                }
                System.out.println();
            }

        } catch (SQLException e) {
            System.err.println("  Пусто или ошибка: " + e.getMessage());
        }
    }



}