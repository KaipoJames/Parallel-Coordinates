import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;

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

    private double getMax(ArrayList<Object> list) {
        double max = 0;
        for (Object in : list) {
            String val = in.toString();
            Double db = Double.valueOf(val);
            if (db > max) {
                max = db;
            }
        }
        return max;
    }

    private double getMin(ArrayList<Object> list) {
        double min = 0;
        String val = list.get(0).toString();
        min = Double.valueOf(val);
        int index = 0;
        while (index < list.size()) {
            String st = list.get(index).toString();
            double db = Double.valueOf(st);
            if (db < min) {
                min = db;
            }
            index++;
        }
        return min;
    }

    private ArrayList<Double> getDoubleLabels(ArrayList<Object> list) {
        DecimalFormat df = new DecimalFormat("###.##");
        ArrayList<Double> values = new ArrayList<Double>();
        double max = getMax(list);
        double min = getMin(list);
        double middle = Double.valueOf(df.format((max + min) / 2));
        double second = Double.valueOf(df.format((min + middle) / 2));
        double fourth = Double.valueOf(df.format((middle + max) / 2));
        values.add(max);
        values.add(fourth);
        values.add(middle);
        values.add(second);
        values.add(min);
        return values;
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

        // DRAW VERTICAL BAR LINES
        int numAxes = axes.size();
        lines.clear();
        int i = 0;
        int bottomMargin = 25;
        for (Axis a : axes) {
            double buffer = w / (numAxes + 1);
            String columnName = a.columnName;
            int columnNameWidth = g.getFontMetrics().stringWidth(columnName);
            int xPos = (int) ((i + 1) * buffer);
            System.out.println("XPOS first: " + xPos);
            int yPos = h;
            Line2D verticalLine = a.setGeometry(xPos, yPos - bottomMargin);
            verticalLines.add(verticalLine);
            a.draw(g);
            g.drawString(columnName, xPos - (columnNameWidth / 2), yPos - 10);
            i++;
        }

        // DRAW LABELS ON VERTICAL LINES
        g.setColor(Color.RED);
        i = 0;
        for (Axis a : axes) {
            a.addRelativeData();

            if (a.relativeData.size() > 0) {
                Polyline pl = new Polyline();
                var ySpacing = h / (5 + 1);
                ArrayList<Double> labels = getDoubleLabels(a.data);
                System.out.println("COLUMN Name: " + a.columnName);
                System.out.println(labels.get(0));
                System.out.println(labels.get(1));
                System.out.println(labels.get(2));
                System.out.println(labels.get(3));
                System.out.println(labels.get(4));
                for (int j = 0; j < labels.size(); j++) {
                    int yPos = ySpacing * (j + 1);
                    double buffer = w / (numAxes + 1);
                    int xPos = (int) ((i + 1) * buffer);
                    System.out.println("xPos: " + xPos);
                    System.out.println("yPos: " + yPos);
                    Point2D p = axes.get(j).getPointAt(xPos, yPos);
                    System.out.println("D: " + labels.get(j));
                    String label = labels.get(j).toString();
                    g.drawString(label, xPos + 5, yPos);
                    pl.addPoint(p);
                    lines.add(pl);
                }
                for (var line : lines) {
                    line.draw(g);
                }
                i++;
            } else {
                // LOOP Through all distinct values
                Polyline pl = new Polyline();
                var ySpacing = h / (a.data.size() + 1);
                System.out.println("Column Name: " + a.columnName);
                for (int j = 0; j < a.data.size(); j++) {
                    int yPos = ySpacing * (j + 1);
                    double buffer = w / (numAxes + 1);
                    int xPos = (int) ((i + 1) * buffer);
                    // System.out.println("xPos: " + xPos);
                    // System.out.println("yPos: " + yPos);
                    Point2D p = axes.get(j).getPointAt(xPos, yPos);
                    // System.out.println("D: " + a.data.get(j));
                    String s = a.data.get(j).toString();
                    g.drawString(s, xPos + 5, yPos);
                    pl.addPoint(p);
                    lines.add(pl);
                }
                for (var line : lines) {
                    line.draw(g);
                }
                i++;
            }
        }

    }

}
