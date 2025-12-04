import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Класс реализации графического интерфейса.
 * @author Топалов Максим
 * @version 1.0
 *
 * */

public class Main {

    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("✓ Драйвер SQLite найден!");

            // Проверяем подключение к БД
            java.sql.DriverManager.getConnection("jdbc:sqlite:test.db").close();
            System.out.println("✓ Подключение к БД успешно!");

        } catch (Exception e) {
            System.err.println("✗ Ошибка: " + e.getMessage());
        }
        DatabaseManager.initializeDatabase();
        DatabaseManager.addUser("admin", "password");
        DatabaseManager.showSimpleTableData("users");
        DatabaseManager.showSimpleTableData("OxygenTuyere");
        DatabaseManager.showSimpleTableData("Laval_nozzle");
        Authorization();
    }
    /**
     * Главное окно приложения. Предоставляется возможность перейти к расчету кислородной фурмы, параметров сопла Лаваля, к просмотру истории расчетов
     * @param id_user - идентификатор пользователя.
     * */
    public static void mainWindow(Integer id_user) {
        // Создаем стартовое окно выбора
        JFrame choiceFrame = new JFrame("Расчет кислородной фурмы");
        choiceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Устанавливаем BorderLayout для лучшего контроля
        choiceFrame.setLayout(new BorderLayout());
        choiceFrame.setSize(600, 150);
        choiceFrame.setLocationRelativeTo(null);

        // Создаем панель для горизонтального расположения кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Выберите калькулятор для расчета.", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        JButton window1Button = new JButton("Расчет Фурмы");
        JButton window2Button = new JButton("Сопло Лаваля");
        JButton historyButton = new JButton("История расчетов");

        // Устанавливаем предпочтительные размеры кнопок
        window1Button.setPreferredSize(new Dimension(120, 35));
        window2Button.setPreferredSize(new Dimension(150, 35));
        historyButton.setPreferredSize(new Dimension(150, 35));

        window1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theoryNozzle(id_user);
                choiceFrame.dispose(); // Закрываем окно выбора
            }
        });

        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HistoryWindow(id_user);
                choiceFrame.dispose(); // Закрываем окно выбора
            }
        });

        window2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theoryLaval(id_user);
                choiceFrame.dispose(); // Закрываем окно выбора
            }
        });

        // Добавляем кнопки в горизонтальную панель
        buttonPanel.add(window1Button);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Горизонтальный отступ
        buttonPanel.add(window2Button);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Горизонтальный отступ
        buttonPanel.add(historyButton);

        // Добавляем компоненты в основное окно
        choiceFrame.add(label, BorderLayout.NORTH);
        choiceFrame.add(buttonPanel, BorderLayout.CENTER);
        choiceFrame.setVisible(true);





        Laval_nozzle b = new Laval_nozzle(200, 3.2, 1.43, 20, 16, 4, 4.9
        );
        System.out.println( b.maxPressureOne()+ " P1");
        System.out.println( b.criticalSpeed()+ " Wcr");
        System.out.println( b.criticalPressure()+ " Pcr");
        System.out.println( b.criticalTemperature()+ " Tcr");
        System.out.println( b.criticalDensity()+ " pcr");
        System.out.println( b.criticalArea() + " Fcr");
        System.out.println( b.criticalDiameter()+ "Dcr");

        System.out.println( b.pressureInTheOut()+ " P2");
        System.out.println( b.temperatureInTheEnd()+ " T2");
        System.out.println( b.densityInTheEnd()+ " RO2");
        System.out.println( b.speedInTheOut()+ " W2");
        System.out.println( b.areaInTheOut()+ " F2");
        System.out.println( b.deameterInTheOut()+ " D2");
        System.out.println( b.lengthInTheOut()+ " l2");

        System.out.println( b.lenDoctPartNozzle()+ " l1");
        System.out.println( b.inputDiameter()+ " D1");
        System.out.println( b.areaOxygenFlow()+ " Fkt");
        System.out.println( b.maxPressureOne()+ " P1");
        System.out.println( b.interiorDiameter()+ " Dkt");
        System.out.println( b.circleDiameter() + " Dc");
    }
    // История расчетов
    /**
     * Окно истории расчетов. Предоставляется возможность перейти к истории расчетов кислородной фурмы, параметров сопла Лаваля, возвращению в главное окно приложения.
     * @param id_user - идентификатор пользователя.
     * */
    public static void HistoryWindow(Integer id_user) {
        // Создаем стартовое окно выбора
        JFrame choiceFrame = new JFrame("Расчет кислородной фурмы");
        choiceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Устанавливаем BorderLayout для лучшего контроля
        choiceFrame.setLayout(new BorderLayout());
        choiceFrame.setSize(600, 150);
        choiceFrame.setLocationRelativeTo(null);

        // Создаем панель для горизонтального расположения кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Выберите калькулятор для расчета.", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        JButton window1Button = new JButton("Расчет Фурмы");
        JButton window2Button = new JButton("Сопло Лаваля");
        JButton returnButton = new JButton("Назад");

        // Устанавливаем предпочтительные размеры кнопок
        window1Button.setPreferredSize(new Dimension(120, 35));
        window2Button.setPreferredSize(new Dimension(120, 35));
        returnButton.setPreferredSize(new Dimension(120, 35));

        window1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatabaseManager.showOxygenTuyereTableForUser(id_user);
                choiceFrame.dispose(); // Закрываем окно выбора

            }
        });

        window2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatabaseManager.showLavalNozzleTableForUser(id_user);
                choiceFrame.dispose(); // Закрываем окно выбора
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choiceFrame.dispose();
                mainWindow(id_user);
            }
        });

        // Добавляем кнопки в горизонтальную панель
        buttonPanel.add(window1Button);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Горизонтальный отступ
        buttonPanel.add(window2Button);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Горизонтальный отступ
        buttonPanel.add(returnButton);

        // Добавляем компоненты в основное окно
        choiceFrame.add(label, BorderLayout.NORTH);
        choiceFrame.add(buttonPanel, BorderLayout.CENTER);
        choiceFrame.setVisible(true);
    }

    // Расчет количества сопел
    /**
     * Окно расчета кислородной фурмы
     * @param id_user - идентификатор пользователя
     * */
    private static void Nozzle(Integer id_user) {
        String data_nozzle[] = new String[2];
        JFrame frame = new JFrame("Число сопел и угол наклона их к вертикали");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        // Создаем основную панель с отступами
        JPanel mainPanel = new JPanel(new GridLayout(7, 2, 10, 10));

        // Добавляем отступы: верх, лево, низ, право (в пикселях)
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JTextField num1Field = new JTextField();
        JTextField num2Field = new JTextField();
        JTextField num3Field = new JTextField();
        JLabel resultLabel1 = new JLabel("Результат появится здесь");
        JLabel resultLabel2 = new JLabel("Результат появится здесь");
        JButton calculateButton = new JButton("Вычислить");
        JButton returnButton = new JButton("Назад");
        JButton export = new JButton("Экспорт");
        JTextField name = new JTextField();

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double num1 = Double.parseDouble(num1Field.getText());
                    double num2 = Double.parseDouble(num2Field.getText());
                    double num3 = Double.parseDouble(num3Field.getText());

                    oxygenTuyere a = new oxygenTuyere(num1, num2, num3);

                    data_nozzle[0] = Double.toString(a.nozzleAngle());
                    data_nozzle[1] = Double.toString(a.nozzleCount());
                    System.out.println(data_nozzle[0]);
                    System.out.println(data_nozzle[1]);

                    DatabaseManager.addNozzle(id_user, a.nozzleAngle(), a.nozzleCount());

                    resultLabel1.setText(String.format("" + a.nozzleCount()));
                    resultLabel2.setText(String.format("" + a.nozzleAngle()));
                } catch (Exception ex) {
                    resultLabel1.setText("Ошибка ввода!");
                    resultLabel2.setText("Ошибка ввода!");
                }

            }
        });

        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String file_name = name.getText();
                exportNozzle(data_nozzle, file_name);
                name.setText("Данные экспортированы в " + file_name + ".csv.");
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                theoryNozzle(id_user);
            }
        });



        // Добавляем компоненты
        mainPanel.add(new JLabel("Интенсивность продувки: "));
        mainPanel.add(num1Field);
        mainPanel.add(new JLabel("Садка конвертера: "));
        mainPanel.add(num2Field);
        mainPanel.add(new JLabel("Угол раскрытия струи: "));
        mainPanel.add(num3Field);
        mainPanel.add(returnButton);
        mainPanel.add(calculateButton);
        mainPanel.add(new JLabel("Количество сопел:"));
        mainPanel.add(resultLabel1);
        mainPanel.add(new JLabel("Угол наклона оси сопел к вертикали:"));
        mainPanel.add(resultLabel2);
        mainPanel.add(export);
        mainPanel.add(name);

        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }
    //  Экспорт данных расчета кислородной фурмы
    /**
     * Метод для экспорта результатов расчета кислородной фурмы в csv файл.
     * @param data - массив экспортируемых данных;
     * @param filename - имя файла экспорта.
     * */
    private static void exportNozzle(String[] data, String filename){
        filename += ".csv";
        try (FileWriter writer = new FileWriter(filename)) {
            StringBuilder sb = new StringBuilder();
            for(String segment: data) {
                sb.append(segment.replace('.', ','));
                sb.append("\n");
            }
            writer.write(sb.toString());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    // Теория по расчету кислородной фурмы
    /**
     * Окно теории расчета кислородной фурмы.
     * @param user_id - идентификатор пользователя.
     * */
    private static void theoryNozzle(Integer user_id) {
        JFrame frame = new JFrame("Сопло Лаваля");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);


        // Главная панель для добавления контента и кнопок
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        // Панель текста
        JPanel contentPanel = new JPanel(new BorderLayout());
        JTextArea text1 = new JTextArea("Эффективность применения многосопловых фурм зависит от числа сопел в них и угла между осями струй. Однако если число сопел превыша- ет некоторый предел при заданном наружном диаметре головки фурмы, усложняется ее конструкция, ухудшаются условия охлаждения фурмы и снижается ее стойкость.");
            //Текст
        text1.setWrapStyleWord(true);
        text1.setLineWrap(true);
        text1.setPreferredSize(new Dimension(200, 200));
            // Изображение
        ImageIcon firstImage = new ImageIcon("C:/Users/User/Desktop/Учёба/Горный/3 курс/Программирование/Курсач/Oxygen.png");
        JLabel image = new JLabel(firstImage);
        contentPanel.add(text1, BorderLayout.WEST);
        contentPanel.add(image);
        // Панель для кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton calculateButton = new JButton("Калькулятор");
        calculateButton.setPreferredSize(new Dimension(200, 40));
        JButton returnButton = new JButton("Назад");
        returnButton.setPreferredSize(new Dimension(200, 40));
        buttonPanel.add(returnButton);
        buttonPanel.add(calculateButton);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Nozzle(user_id);
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                mainWindow(user_id);
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                mainWindow(user_id);
            }
        });


        mainPanel.add(contentPanel, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.WEST);

        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }
    // Решение задачи два
    // Теория
    // Теория по расчету кислородной фурмы
    /**
     * Окно теории расчета параметров сопал Лаваля.
     * @param user_id - идентификатор пользователя.
     * */
    private static void theoryLaval(Integer user_id) {
        JFrame frame = new JFrame("Сопло Лаваля");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);


        // Главная панель для добавления контента и кнопок
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        // Панель текста
        JPanel contentPanel = new JPanel(new BorderLayout());
        JTextArea text1 = new JTextArea("Наиболее распространенным типом сопла в настоящее время являет- ся сопло Лаваля (см. рис.), которое состоит из суживающейся (дозвуковой), расширяющейся (сверхзвуковой) и узкой (звуковой) частей. Последнюю часть сопла называют еще критической, так как скорость потока в ней рав- на скорости звука. Сопло Лаваля формирует сверхзвуковую струю.");
        //Текст
        text1.setWrapStyleWord(true);
        text1.setLineWrap(true);
        text1.setPreferredSize(new Dimension(200, 200));
        // Изображение
        ImageIcon firstImage = new ImageIcon("C:/Users/User/Desktop/Учёба/Горный/3 курс/Программирование/Курсач/Laval_nozzle.png");
        JLabel image = new JLabel(firstImage);
        contentPanel.add(text1, BorderLayout.WEST);
        contentPanel.add(image);
        // Панель для кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton calculateButton = new JButton("Калькулятор");
        calculateButton.setPreferredSize(new Dimension(250, 40));
        JButton returnButton = new JButton("Назад");
        returnButton.setPreferredSize(new Dimension(250, 40));
        buttonPanel.add(returnButton);
        buttonPanel.add(calculateButton);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                lavalNozzle(user_id);
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                mainWindow(user_id);
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                mainWindow(user_id);
            }
        });


        mainPanel.add(contentPanel, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.WEST);

        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }
    // Задача
    /**
     * Окно расчета параметров сопла Лаваля.
     * @param user_id - идентификатор пользователя.
     * */
    private static void lavalNozzle(Integer user_id) {
        JFrame frame = new JFrame("Число сопел и угол наклона их к вертикали");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Увеличиваем GridLayout для 7 полей ввода + кнопка
        JPanel mainPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Создаем 7 полей ввода
        JTextField num1Field = new JTextField();
        JTextField num2Field = new JTextField();
        JTextField num3Field = new JTextField();
        JTextField num4Field = new JTextField();
        JTextField num5Field = new JTextField();
        JTextField num6Field = new JTextField();
        JTextField num7Field = new JTextField();

        JButton calculateButton = new JButton("Вычислить");
        JButton returnButton = new JButton("Назад");

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double num1 = Double.parseDouble(num1Field.getText());
                    double num2 = Double.parseDouble(num2Field.getText());
                    double num3 = Double.parseDouble(num3Field.getText());
                    double num4 = Double.parseDouble(num4Field.getText());
                    double num5 = Double.parseDouble(num5Field.getText());
                    double num6 = Double.parseDouble(num6Field.getText());
                    double num7 = Double.parseDouble(num7Field.getText());


                    // Создаем окно результатов
                    showResultWindow(user_id, num1, num2, num3, num4, num5, num6, num7);

                } catch (Exception ex) {
                    // Показываем окно с ошибкой
                    showErrorWindow("Ошибка ввода! Проверьте правильность введенных данных.");
                }
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                theoryLaval(user_id);
            }
        });

        // Добавляем компоненты с 7 полями ввода
        mainPanel.add(new JLabel("Садка конвертера: "));
        mainPanel.add(num1Field);
        mainPanel.add(new JLabel("Интенсивность продувки: "));
        mainPanel.add(num2Field);
        mainPanel.add(new JLabel("Плотность кислорода при нормальных условиях: "));
        mainPanel.add(num3Field);
        mainPanel.add(new JLabel("Температура кислорода: "));
        mainPanel.add(num4Field);
        mainPanel.add(new JLabel("Давление кислорода в магистрали: "));
        mainPanel.add(num5Field);
        mainPanel.add(new JLabel("Количество сопел: "));
        mainPanel.add(num6Field);
        mainPanel.add(new JLabel("Полуугол раскрытия сопла: "));
        mainPanel.add(num7Field);
        mainPanel.add(returnButton);
        mainPanel.add(calculateButton);

        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }
    /**
     * Окно результатов расчета параметров сопла Лаваля.
     * */
    private static void showResultWindow(Integer user_id, double num1, double num2, double num3, double num4, double num5, double num6, double num7) {
        String data[] = new String[17];
        Laval_nozzle a = new Laval_nozzle(num1, num2, num3, num4, num5, num6, num7);

        JFrame resultFrame = new JFrame("Результаты расчета");
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultFrame.setLocationRelativeTo(null);

        JPanel resultPanel = new JPanel(new GridLayout(21, 1, 10, 10));
        JPanel exportPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JPanel mainPanel = new JPanel(new FlowLayout());
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        //Кнопки для экспорта
        JButton export = new JButton("Экспорт");
        JTextField name = new JTextField();
        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultFrame.dispose();
            }
        });
        exportPanel.add(export);
        exportPanel.add(name);
        exportPanel.add(closeButton);

        // Стилизация результатов
        JLabel Pmax = new JLabel("Максимальное давление перед соплом: " + String.format("%.3f" ,a.maxPressureOne()) + " ата");
        data[0] = String.format("%.3f" ,a.maxPressureOne());
        JLabel Wcr = new JLabel("Критическая скорость кислородного дутья: " + String.format("%.3f" , a.criticalSpeed()) + " м/с");
        data[1] = String.format("%.3f" , a.criticalSpeed()) ;
        JLabel Tcr = new JLabel("Критическая температура кислородного дутья: " + String.format("%.3f" ,a.criticalTemperature()) + " К");
        data[2] = String.format("%.3f" ,a.criticalTemperature());
        JLabel pcr = new JLabel("Критическая плотность газа: " + String.format("%.3f" ,a.criticalDensity()) + " кг/см²");
        data[3] = String.format("%.3f" ,a.criticalDensity());
        JLabel Fcr = new JLabel("Площадь критического сечения сопла: " + String.format("%.3f" ,a.criticalArea()) + " м²");
        data[4] = String.format("%.3f" ,a.criticalArea());
        JLabel Dcr = new JLabel("Диаметр критического сечения: " + String.format("%.3f" ,a.criticalDiameter()) + " м");
        data[5] = String.format("%.3f" ,a.criticalDiameter());
        JLabel P2 = new JLabel("Давление на выходе из сопла: " + String.format("%.3f" , a.pressureInTheOut()) +  "кг/см²");
        data[6] = String.format("%.3f" , a.pressureInTheOut());
        JLabel T2 = new JLabel("Температура на выходе из сопла: " + String.format("%.3f" , a.temperatureInTheEnd()) + " K");
        data[7] = String.format("%.3f" , a.temperatureInTheEnd());
        JLabel RO2 = new JLabel("Плотность газа на выходе из сопла: " + String.format("%.3f" , a.densityInTheEnd()) +  "кг/см³");
        data[8] = String.format("%.3f" , a.densityInTheEnd());
        JLabel W2 = new JLabel("Скорость газа на выходе из сопла: " + String.format("%.3f" , a.speedInTheOut()) + " м/с");
        data[9] = String.format("%.3f" , a.speedInTheOut());
        JLabel F2 = new JLabel("Площадь выходного сечения сопла: " + String.format("%.3f" , a.areaInTheOut()) + " м²");
        data[10] = String.format("%.3f" , a.areaInTheOut());
        JLabel D2 = new JLabel("Диаметр выходного сечения сопла: " + String.format("%.3f" , a.deameterInTheOut()) + " м");
        data[10] = String.format("%.3f" , a.deameterInTheOut());
        JLabel l2 = new JLabel("Длина расширяющейся части сопла: " + String.format("%.3f" , a.lengthInTheOut()) + " м");
        data[11] = String.format("%.3f" , a.lengthInTheOut());
        JLabel l1 = new JLabel("Длина доктрической части сопла: " + String.format("%.3f" , a.lenDoctPartNozzle()) + " м");
        data[12] = String.format("%.3f" , a.lenDoctPartNozzle());
        JLabel D1 = new JLabel("Диаметр входного сечения сопла: " + String.format("%.3f" , a.inputDiameter()) + " м");
        data[13] = String.format("%.3f" , a.inputDiameter());
        JLabel Fkt = new JLabel("Площадь поперечного сечения кислородоподводящей: трубы к соплам: " + String.format("%.3f" , a.areaOxygenFlow()) + " м²");
        data[14] = String.format("%.3f" , a.areaOxygenFlow());
        JLabel Dkt = new JLabel("Внутренний диаметр трубы фурмы для подвода кислорода: " + String.format("%.3f" , a.interiorDiameter()) + " м");
        data[15] = String.format("%.3f" , a.interiorDiameter());
        JLabel Dc = new JLabel("Диаметр окружности, которую пересекают оси выходных сечений\n" +
                "сопел: " + String.format("%.3f" , a.circleDiameter()) + " м");
        data[16] = String.format("%.3f" , a.circleDiameter());
        JFrame im = new JFrame();
        MyGraphics paint = new MyGraphics(a.inputDiameter()*1000, a.criticalDiameter()*1000, a.deameterInTheOut()*1000, a.lenDoctPartNozzle()*1000, a.lengthInTheOut()*1000);
        im.add(paint);
        im.setSize(600,600);
        im.setVisible(true);

        for(String d: data)
            System.out.println(d);

        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String file_name = name.getText();
                exportNozzle(data, file_name);
                name.setText("Данные экспортированы в " + file_name + ".csv.");
            }
        });

        resultPanel.add(Pmax);
        resultPanel.add(Wcr);
        resultPanel.add(Tcr);
        resultPanel.add(pcr);
        resultPanel.add(Fcr);
        resultPanel.add(Dcr);
        resultPanel.add(P2);
        resultPanel.add(T2);
        resultPanel.add(RO2);
        resultPanel.add(W2);
        resultPanel.add(F2);
        resultPanel.add(D2);
        resultPanel.add(l2);
        resultPanel.add(l1);
        resultPanel.add(D1);
        resultPanel.add(Fkt);
        resultPanel.add(Dkt);
        resultPanel.add(Pmax);
        resultPanel.add(Dc);
        mainPanel.add(resultPanel);
        mainPanel.add(exportPanel);

        DatabaseManager.LavalNozzle(user_id, a.maxPressureOne(), a.criticalSpeed(), a.criticalPressure(), a.criticalTemperature(), a.criticalDensity(), a.criticalArea(), a.criticalDiameter(), a.pressureInTheOut(), a.temperatureInTheEnd(), a.densityInTheEnd(), a.speedInTheOut(), a.areaInTheOut(), a.deameterInTheOut(), a.lengthInTheOut(), a.lenDoctPartNozzle(), a.inputDiameter(), a.areaOxygenFlow(), a.interiorDiameter(), a.circleDiameter());

        resultFrame.add(mainPanel);
        resultFrame.pack();
        resultFrame.setVisible(true);
    }


    // Метод для отображения окна с ошибкой
    /**
     * Метод для отображения окна с ошибкой.
     * */
    private static void showErrorWindow(String errorMessage) {
        JFrame errorFrame = new JFrame("Ошибка");
        errorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        errorFrame.setLocationRelativeTo(null);

        JPanel errorPanel = new JPanel(new BorderLayout(10, 10));
        errorPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel errorLabel = new JLabel(errorMessage);
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton closeButton = new JButton("OK");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                errorFrame.dispose();
            }
        });

        errorPanel.add(errorLabel, BorderLayout.CENTER);
        errorPanel.add(closeButton, BorderLayout.SOUTH);

        errorFrame.add(errorPanel);
        errorFrame.pack();
        errorFrame.setVisible(true);
    }
    /**
     * Окно авторизации пользователя
     * */
    public static void Authorization() {
        // Создание основного окна
        JFrame frame = new JFrame("Авторизация");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);

        // Основная панель
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Создание компонентов
        JLabel label1 = new JLabel("Логин:");
        JTextField field1 = new JTextField();

        JLabel label2 = new JLabel("Пароль:");
        JPasswordField field2 = new JPasswordField();

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Регистрация");

        // Обработчик кнопки OK
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = field1.getText();
                String password = field2.getText();

                if(DatabaseManager.authenticateUser(login, password)){
                    frame.dispose();
                    mainWindow(DatabaseManager.getUserId(login));
                }
                else {
                    JOptionPane.showMessageDialog(frame,
                        "Вы ввели:\nНеверный логин: " + login + "\nили пароль: " + password,
                        "Результат",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Обработчик кнопки Отмена
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Registration();
            }
        });

        // Добавление компонентов на панель
        mainPanel.add(label1);
        mainPanel.add(field1);
        mainPanel.add(label2);
        mainPanel.add(field2);
        mainPanel.add(cancelButton);
        mainPanel.add(okButton);

        // Добавление панели в окно
        frame.add(mainPanel);

        // Отображение окна
        frame.setVisible(true);
    }

    //Регистрация пользователя
    /**
     * Окно регистрации пользователя
     * */
    public static void Registration() {
        JFrame frame = new JFrame("Регистрация");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);

        // Основная панель
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Создание компонентов
        JLabel label1 = new JLabel("Логин:");
        JTextField field1 = new JTextField();

        JLabel label2 = new JLabel("Пароль:");
        JTextField field2 = new JTextField();

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Отмена");

        // Обработчик кнопки OK
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = field1.getText();
                String password = field2.getText();
                DatabaseManager.addUser(login, password);
                frame.dispose();

            }
        });

        // Обработчик кнопки Отмена
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        // Добавление компонентов на панель
        mainPanel.add(label1);
        mainPanel.add(field1);
        mainPanel.add(label2);
        mainPanel.add(field2);
        mainPanel.add(cancelButton);
        mainPanel.add(okButton);

        // Добавление панели в окно
        frame.add(mainPanel);

        // Отображение окна
        frame.setVisible(true);
    }


}