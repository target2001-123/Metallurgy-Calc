/**
 * Класс для решения задачи расчета параметров сопла Лаваля
 * @author Топалов Максим
 * @version 1.0
 * */

public class Laval_nozzle {
    double g = 9.8;
    double k = 1.43;
    double R = 26.5;
    double p_atm = 1;
    double M, q, ro, delta_p, T, mag, m, theta;
    // Инициализация
    /**
     * Конструктор класса
     * @param q - интенсивность продувки
     * @param M - садка конвертера
     * @param m - количество сопел
     * @param mag - давление в магистрали
     * @param ro - плотность кислорода
     * @param T - температура кислорода
     * @param theta - угол раскрытия струи
     * */
    public Laval_nozzle(double M, double q, double ro, double T, double mag, double m, double theta) {
        this.M = M;
        this.q = q;
        this.ro = ro;
        this.T = T + 273;
        this.mag = mag;
        this.m = m;
        this.theta = (theta / 2) * 0.017;
        // Определение давления определяемого слоем шлакометаллической эмульсии
        if (M <= 60)
            delta_p = 0.1;
        else if (M >= 100 && M <= 160)
            delta_p = 0.2;
        else if (M >= 200)
            delta_p = 0.4;
    }


    // Расчет максимального давления перед соплом
    /**
     * Расчет максимального давления перед соплом
     * */
    public double maxPressureOne() {
        return 0.85 * mag;
    }
    /**
     * Расчет критической скорости
     * */
    // Расчет критической скорости
    public double criticalSpeed() {
        return Math.sqrt(2 * g * k / (k + 1) * R * T);
    }
    /**
     * Расчет критического давления
     * */
    // Расчет критического давления
    public double criticalPressure() {
        return maxPressureOne() * Math.pow((2/ (k + 1)), (k / (k - 1)));
    }
    /**
     * Расчет критической температуры
     * */
    // Расчет критической температуры
    public double criticalTemperature() {
        return T * 2 / (k + 1);
    }
    /**
     * Расчет критической плотности газа
     * */
    // Расчет критической плотности газа
    public double criticalDensity() {
        return criticalPressure() * Math.pow(10, 4) / (R * criticalTemperature());
    }
    /**
     * Расчет площади критического сечения сопла
     * */
    // Расчет площади критического сечения сопла
    public double criticalArea() {
        return q * M * ro / (0.95 * 60 * m * criticalPressure() * criticalSpeed());
    }
    /**
     * Расчет диаметра критического сечения
     * */
    // Расчет диаметра критического сечения
    public double criticalDiameter() {
        return Math.sqrt(4 * criticalArea() / Math.PI);
    }


    // Параметры кислородного литья на выходе из сопла
    /**
     * Расчет давления на выходе из сопла
     * */
    // Расчет давления
    public double pressureInTheOut() {
        return p_atm + delta_p;
    }
    /**
     * Расчет температуры на выходе из сопла
     * */
    // Расчет температуры
    public double temperatureInTheEnd() {
        return T * Math.pow(pressureInTheOut() / maxPressureOne(), (k - 1) / k);
    }
    /**
     * Расчет плотности газа на выходе из сопла
     * */
    // Расчет плотности
    public double densityInTheEnd() {
        return pressureInTheOut() / (Math.pow(10, -4) * R * temperatureInTheEnd());
    }
    /**
     * Скорость газа на выходе из сопла
     * */
    // Скорость газа на выходе из сопла
    public double speedInTheOut() {
        double sqr = 2 * g * 1.4 / (1.4 - 1) * 26.5 * T * (1 - Math.pow(pressureInTheOut() / maxPressureOne(), (k - 1)/k));
        return 0.98 * Math.sqrt(sqr);
    }
    /**
     * Площадь выходного сечения сопла
     * */
    // Площадь выходного сечения сопла
    public double areaInTheOut() {
        return q * M * ro / (0.95 * 60 * m * densityInTheEnd() * speedInTheOut());
    }
    /**
     * Диаметр выходного сечения сопла
     * */
    // Диаметр выходного сечения сопла
    public double deameterInTheOut() {
        return Math.sqrt(4 * areaInTheOut() / Math.PI);
    }
    /**
     * Длина расширяющейся части сопла
     * */
    // Длина расширяющейся части сопла
    public double lengthInTheOut() {
        return Math.abs((deameterInTheOut() - criticalDiameter()) / (2 * Math.tan(theta)));
    }


    // ДОКТРИЧЕСКИЕ ПОКАЗАТЕЛИ
    /**
     * Длина докритической (суживающейся) части сопла
     * */
    // Длина докритической (суживающейся) части сопла
    public double lenDoctPartNozzle() {
        return 0.7 * criticalDiameter();
    }
    /**
     * Диаметр доктрической части сопла
     * */
    // Диаметр доктрической части сопла
    public double inputDiameter() {
        return 1.2 * criticalDiameter();
    }
    /**
     * Площадь поперечного сечения кислородопроводящей трубы к соплам
     * */
    // Площадь поперечного сечения кислородопроводящей трубы к соплам
    public double areaOxygenFlow() {
        double density_flow = maxPressureOne() / (Math.pow(10, -4) * R * T);
        return q * M * ro / (60 * density_flow * 40);
    }
    /**
     * Внутренний диаметр трубы фурмы для подвода кислорода
     * */
    // Внутренний диаметр трубы фурмы для подвода кислорода
    public double interiorDiameter() {
        return Math.sqrt(4 * areaOxygenFlow() / Math.PI);
    }
    /**
     * Диаметр окружности которую пересекают оси выходных сечений сопел
     * */
    // Диаметр окружности которую пересекают оси выходных сечений сопел
    public double circleDiameter() {
        return (deameterInTheOut() + 50) / (Math.sin(Math.toRadians(180 / m)));
    }
}
