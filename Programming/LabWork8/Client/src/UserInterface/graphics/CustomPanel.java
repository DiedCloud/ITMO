package UserInterface.graphics;

import common.MusicBand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CustomPanel extends JPanel {
    private List<CustomObject> objects = new ArrayList<>();
    double scale = 1.0;
    private final HashMap<String, Color> colors = new HashMap<>(); // Хранилище цветов для логинов

    public CustomPanel(JTable jt, ReentrantLock lock) {
        // Добавляем обработчик клика мыши
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (CustomObject object : objects) {
                    if (object.containsPoint(e.getX(), e.getY())) {
                        // Если клик попал в объект, то выполняем действия
                        handleObjectClick(object, jt, lock);
                        break; // Выходим из цикла после первого совпадения
                    }
                }
            }
        });
    }

    public void updateObjectsPosition(int Xdif, int Ydif) {
        // Обновляем позицию объектов
        for (CustomObject object : objects) {
            object.moveX(Xdif);
            object.moveY(Ydif);
        }

        // Перерисовываем панель
        repaint();
    }

    public void addMusicBand(MusicBand mb) {
        Color color;
        String ownerLogin = mb.getOwnerLogin();

        if (colors.containsKey(ownerLogin)) {
            color = colors.get(ownerLogin);
        } else {
            color = generateUniqueColor();
            colors.put(ownerLogin, color);
        }

        addObjectAt(mb.getID(), (int) mb.getX(), (int) mb.getY(), color);
    }

    private Color generateUniqueColor() {
        int r = (int) (Math.random() * 255);
        int g = (int) (Math.random() * 255);
        int b = (int) (Math.random() * 255);

        return new Color(r, g, b);
    }

    public void addObjectAt(long id, int x, int y, Color color) {
        CustomObject object = new CustomObject(id, x, y, color);
        int animationDuration = 1000;
        Timer colorAnimationTimer = new Timer(20, new ActionListener() {
            private long startTime = System.currentTimeMillis();

            @Override
            public void actionPerformed(ActionEvent e) {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;

                // Вычисляем прогресс анимации (от 0.0 до 1.0)
                float progress = (float) elapsedTime / animationDuration;
                if (progress > 1.0f) {
                    progress = 1.0f;
                    ((Timer) e.getSource()).stop(); // Останавливаем таймер после завершения анимации
                }

                // Вычисляем промежуточный цвет между начальным и целевым цветами
                Color interpolatedColor = interpolateColor(Color.WHITE, color, progress);
                object.setColor(interpolatedColor); // Устанавливаем цвет объекта

                repaint();
            }
        });

        // Запускаем таймер анимации
        colorAnimationTimer.start();
        objects.add(object);

        repaint();
    }

    public void clearMusicBand() {
        objects.clear();
        colors.clear();
        repaint();
    }

    private Color interpolateColor(Color startColor, Color endColor, float progress) {
        int r = (int) (startColor.getRed() + progress * (endColor.getRed() - startColor.getRed()));
        int g = (int) (startColor.getGreen() + progress * (endColor.getGreen() - startColor.getGreen()));
        int b = (int) (startColor.getBlue() + progress * (endColor.getBlue() - startColor.getBlue()));
        return new Color(r, g, b);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Установка масштаба только для отрисовываемых объектов
        g2d.scale(scale, scale);

        // Отрисовка объектов
        for (CustomObject object : objects) {
            object.draw(g2d);
        }
    }

    private static class CustomObject {
        private int x;
        private int y;

        private long id;
        private Color color;
        private static final int DIAMETER = 50; // Диаметр круга

        public CustomObject(long id, int x, int y, Color color) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.color = color;
        }

        public long getID(){
            return id;
        }

        public void draw(Graphics2D g2d) {
            // Рисуем внешний круг (черный)
            g2d.setColor(Color.BLACK);
            g2d.fillOval(x, y, DIAMETER, DIAMETER);

            // Рисуем внутренний круг (с цветом пользователя)
            int circleDiameter = 25; // Диаметр колечка
            int circleX = x + (DIAMETER - circleDiameter) / 2;
            int circleY = y + (DIAMETER - circleDiameter) / 2;
            g2d.setColor(color);
            g2d.fillOval(circleX, circleY, circleDiameter, circleDiameter);

            // Рисуем внутренний круг (белый)
            int HoleDiameter = 3; // Диаметр колечка
            circleX = x + (DIAMETER - HoleDiameter) / 2;
            circleY = y + (DIAMETER - HoleDiameter) / 2;
            g2d.setColor(Color.WHITE);
            g2d.fillOval(circleX, circleY, HoleDiameter, HoleDiameter);
        }

        public boolean containsPoint(int mouseX, int mouseY) {
            // Проверяем, попадает ли точка клика мыши в объект
            int centerX = x + DIAMETER / 2;
            int centerY = y + DIAMETER / 2;
            double distance = Math.sqrt(Math.pow(centerX - mouseX, 2) + Math.pow(centerY - mouseY, 2));
            return distance <= DIAMETER / 2;
        }

        public void setColor(Color interpolatedColor) {
            this.color = interpolatedColor;
        }

        public void moveX(int x) {
            this.x += x;
        }

        public void moveY(int y) {
            this.y += y;
        }
    }

    private void handleObjectClick(CustomObject object, JTable table1, ReentrantLock lock) {
        // Ваш код обработки клика на объекте, например, вывод информации о MusicBand
        System.out.println("Клик на объекте с цветом: " + object.color);

        lock.lock();
        try{
            int[] arr = IntStream.range(0, table1.getRowCount()).
                    filter((int i) -> Long.parseLong(table1.getValueAt(i, 0).toString()) == object.getID()).toArray();
            int index0 = arr[0];
            table1.setRowSelectionInterval(index0, index0);
        }finally {
            lock.unlock();
        }
    }
}
