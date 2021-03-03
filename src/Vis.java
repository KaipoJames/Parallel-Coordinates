import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.swing.JPanel;
import java.util.*;

public class Vis extends JPanel {

    private List<Axis> axes;
    private List<Polyline> lines;
    private List<Line2D> verticalLines;

    public Vis() {
        super();
        axes = new ArrayList<Axis>();
        lines = new ArrayList<Polyline>();
        verticalLines = new ArrayList<Line2D>();
    }

    public void setAxes(List<Axis> list) {
        axes = list;
        repaint();
    }

    public void getColumnData(Graphics2D g) {
        System.out.println("\nDATA:");
        for (Axis a : axes) {
            System.out.println("\nColumn: " + a.columnName);
            var data = a.data;
            if (data.get(0) instanceof Integer) {
                System.out.println("Array Of Integers");
                ArrayList<Double> values = getIntLabels(data);
                for (Double d : values) {
                    System.out.println(d);
                }
            } else if (data.get(0) instanceof BigDecimal) {
                // Convert BigDecimal values into Doubles
                for (int i = 0; i < data.size(); i++) {
                    BigDecimal bg = (BigDecimal) data.get(i);
                    double item = bg.doubleValue();
                    data.remove(data.get(i));
                    data.add(item);
                }
                System.out.println("Array Of BigDecimal");
                ArrayList<Double> values = getIntLabels(data);
                for (Double d : values) {
                    System.out.println(d);
                }
            } else if (data.get(0) instanceof Double) {
                System.out.println("Array Of Double");
                ArrayList<Double> values = getIntLabels(data);
                for (Double d : values) {
                    System.out.println(d);
                }
            } else if (data.get(0) instanceof String) {
                System.out.println("Array Of String");
                for (Object d : data) {
                    String value = d.toString();
                    System.out.println(value);
                }
            } else {
                System.out.println("TYPE: " + ((Object) data.get(0)).getClass().getName());
            }
            for (Object d : data) {
                // System.out.println(d + ". TYPE: " + ((Object) d).getClass().getName());
            }
        }
    }

    private ArrayList<Double> getIntLabels(ArrayList<Object> list) {
        ArrayList<Double> values = new ArrayList<Double>();
        double max = 0;
        double min = 0;
        if (list.get(0) instanceof BigDecimal) {
            for (int i = 0; i < list.size(); i++) {
                BigDecimal bg = (BigDecimal) list.get(i);
                if (bg.doubleValue() > max) {
                    max = (int) i;
                }
            }
        } else if (list.get(0) instanceof Double) {
            for (int i = 0; i < list.size(); i++) {
                Double db = (Double) list.get(i);
                if (db > max) {
                    max = (int) i;
                }
            }
        } else {
            for (Object i : list) {
                if ((int) i > max) {
                    max = (int) i;
                }
            }
        }
        int index = 0;
        while (index < list.size()) {
            if ((double) list.get(index) < min) {
                min = (double) list.get(index);
            }
            index++;
        }
        double middle = (max + min) / 2;
        double second = (min + middle) / 2;
        double fourth = (middle + max) / 2;
        values.add(max);
        values.add(fourth);
        values.add(middle);
        values.add(second);
        values.add(min);
        return values;
    }

    private ArrayList<Double> getBDLabels(ArrayList<BigDecimal> list) {
        ArrayList<Double> values = new ArrayList<Double>();
        double max = Collections.max(list).doubleValue();
        double min = Collections.min(list).doubleValue();
        double middle = (max + min) / 2;
        double second = (min + middle) / 2;
        double fourth = (middle + max) / 2;
        values.add(max);
        values.add(min);
        values.add(middle);
        values.add(second);
        values.add(fourth);
        return values;
    }

    private ArrayList<Double> getDBLabels(ArrayList<Double> list) {
        ArrayList<Double> values = new ArrayList<Double>();
        double max = Collections.max(list);
        double min = Collections.min(list);
        double middle = (max + min) / 2;
        double second = (min + middle) / 2;
        double fourth = (middle + max) / 2;
        values.add(max);
        values.add(min);
        values.add(middle);
        values.add(second);
        values.add(fourth);
        return values;
    }

    private ArrayList<String> getStringLabels(ArrayList<String> list) {
        int size = list.size();

        return list;
    }

    @Override
    public void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;

        // draw blank background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // render visualization
        g.setColor(Color.BLACK);

        final int h = getHeight();
        final int w = getWidth();

        int numAxes = axes.size();
        lines.clear();
        int i = 0;
        int bottomMargin = 25;
        for (Axis a : axes) {
            double buffer = w / (numAxes + 1);
            String columnName = a.columnName;
            int columnNameWidth = g.getFontMetrics().stringWidth(columnName);
            int xPos = (int) ((i + 1) * buffer);
            int yPos = h;
            Line2D verticalLine = a.setGeometry(xPos, yPos - bottomMargin);
            verticalLines.add(verticalLine);
            a.draw(g);
            g.drawString(columnName, xPos - (columnNameWidth / 2), yPos - 10);
            i++;
        }

        getColumnData(g);

        g.setColor(Color.RED);
        for (int x = 0; x < 5; x++) {
            var polyline = new Polyline();
            for (int j = 0; j < numAxes; j++) {
                Point2D p = axes.get(j).getPointAt(i);
                polyline.addPoint(p);
                lines.add(polyline);
            }
        }
        for (var line : lines) {
            line.draw(g);
        }

        // Draw points and values on vertical lines
        g.setColor(Color.RED);

    }

}
