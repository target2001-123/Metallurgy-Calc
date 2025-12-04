// Задание 1
// Кислородная фурма
/**
 * Класс для решения задачи расчета кислородной фурмы
 * @author Топалов Максим
 * @version 1.0
 * */
public class oxygenTuyere {
    double q, M, theta;
    // Инициализация
    /**
     * Конструктор класса расчета кислородной фкрмы
     * @param M - садка конвертера
     * @param q - интенсивность продувки
     * @param theta - угол раскрытия струи
     * */
    public oxygenTuyere(double q, double M, double theta) {
        this.q = q;
        this.M = M;
        this.theta = theta;
    }
    // Расчет количества сопел в многоструйной фурме
    /**
     * Метод для расчета количества сопел для установки
     * */
    public int nozzleCount() {
        double m = (q/(1.168 - 9.42 * M * Math.pow(10, -4)) + 1);
        return (int)Math.round(m);
    }
    //Расчет угла наклона оси сопел к вертикали
    /**
     * Метод расчета угла наклона оси сопел к вертикали
     * */
    public double nozzleAngle() {
        double theta_rad = Math.toRadians(theta);
        double m_rad = Math.toRadians(180 / nozzleCount());
        double gamma = Math.asin(Math.tan(theta_rad / 2) / Math.sin(m_rad));
        return Math.toDegrees(gamma);
    }
}
