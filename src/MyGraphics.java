import javax.swing.*;
import java.awt.*;
import java.lang.Math;

/**
 * Класс для построения простых чертежей согласно вычисленным значениям
 * @author Топалов Максим
 * @version 1.0
 * */

public class MyGraphics extends JPanel {
    double d1, dcr, d2, l1, l2;
    /**
     * Конструктор класса MyGraphics
     * @param d1 Диаметра доктрическрй части сопла
     * @param dcr Диаметр критической части сопла
     * @param d2 диаметр выходного сечения сопла
     * @param l1 длина доктрической части сопла
     * @param l2 длина выходной части сопла
     * */
    public MyGraphics(double d1, double dcr, double d2, double l1, double l2){
        this.d1 = d1;
        this.dcr = dcr;
        this.d2 = d2;
        this.l1 = l1;
        this.l2 = l2;
    }
    @Override
    /**
     * Метод, реализующий алгоритм построения чертежа сопла Лаваля
     * */
    protected void paintComponent(Graphics g) { // d1, dкр, d2, l1, l2
        super.paintComponent(g);
        int D1 = (int)Math.round(d1);
        int Dcr = (int)Math.round(dcr);
        int D2 = (int)Math.round(d2);
        int L1 = (int)Math.round(l1);
        int L2 = (int)Math.round(l2);
        g.translate(getWidth() /2, getHeight() / 2);
        g.setColor(Color.BLACK);
        // Рисуем линию от (x1, y1) до (x2, y2)
        g.drawLine(0, 0, 0, D1); // d1
        g.drawLine(0, D1, L1, D1 - (D1 - Dcr) / 2); // l1
        g.drawLine(L1, D1 - (D1-Dcr) / 2, L2+L1, D1 + (D2-D1)/2); // l2
        g.drawLine(L2+L1, D1 + (D2-D1)/2, L2+L1, -(D2-D1)/2); //d2
        g.drawLine(L2+L1, -(D2-D1)/2, L1, (D1-Dcr) / 2); // l2_up
        g.drawLine(L1, ((D1-Dcr) / 2), 0, 0); // l1_up
        g.drawLine(L1, D1 - (D1-Dcr) / 2,L1, (D1-Dcr) / 2);

    }


}