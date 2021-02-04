import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.security.auth.x500.X500Principal;
import javax.swing.JPanel;
import java.util.*;

public class Vis extends JPanel {

    private String textToDisplay;
    private Color barColor;
    private Color textColor;

    private String chartChoice;
    public Map<String, Double> data;
    public Map<String, Double> relativeData;

    public Vis() {
        super();
        textToDisplay = "Bar graphs and line charts!";
        chartChoice = "Bar";
        barColor = Color.CYAN;
        textColor = Color.BLACK;
        data = new HashMap<String, Double>();
        relativeData = new HashMap<String, Double>();
    }

    public void setData(Map<String, Double> result) {
        data.clear();
        relativeData.clear();
        data = result;
        var allDataValues = data.values();
        System.out.println("keyset: " + data.keySet());
        double max = 0;
        for (var value : allDataValues) {
            if (value > max) {
                max = value;
            }
        }
        for (var value : data.keySet()) {
            relativeData.put(value, data.get(value) / max);
        }
        repaint();
    }

    public void chooseChart(String choice) {
        chartChoice = choice;
        repaint();
    }

    public void setText(String t) {
        textToDisplay = t;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;

        // draw blank background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // render visualization
        g.setColor(Color.BLACK);
        g.drawString(textToDisplay, 10, 20);

        // Draw Lines For Graphs
        int h = getHeight(), w = getWidth(), padding = 50;
        g.drawLine(padding, h - padding, w - padding, h - padding);
        g.drawLine(padding, padding, padding, h - padding);

        if (chartChoice == "Bar") {
            drawBarChart(g, padding);
        }
        if (chartChoice == "Line") {
            drawLineChart(g, padding);
        }
    }

    private void drawLineChart(Graphics2D g, int padding) {
        int x, y = getHeight();
        int barCount = relativeData.keySet().size();
        int[] yValues = new int[barCount];
        int[] xValues = new int[barCount];
        int xSpacing = getWidth() / (barCount + 1);
        x = xSpacing;
        int i = 0;
        for (var kolea : relativeData.keySet()) {
            double barHeight = getHeight() * relativeData.get(kolea);
            yValues[i] = y - (int) barHeight;
            xValues[i] = x;
            i++;
            g.setColor(Color.GREEN);
            g.fillOval(x - 4, (y - (int) barHeight) - 4, 10, 10);
            // g.drawLine(x, y - (int) barHeight, x, y - (int) (barHeight));
            x += xSpacing;
        }
        g.drawPolyline(xValues, yValues, barCount);
        g.setColor(textColor);
        x = xSpacing;
        for (var label : data.keySet()) {
            int width = g.getFontMetrics().stringWidth(label); // DO Math to center text on top of each bar
            x += (xSpacing / 2);
            x -= (width / 2);
            g.drawString(label, x, y - 20);
            x -= (xSpacing / 2);
            x += (width / 2);
            x += xSpacing;
        }
        // Draw values on left side of chart
        x = 10;
        y = getHeight() - padding;
        String valueText;
        for (var value : data.values()) {
            value = (double) Math.round(value);
            valueText = value.toString();
            g.drawString(valueText, x, y);
            y -= getHeight() / (barCount + 1);
        }
    }

    private void drawBarChart(Graphics2D g, int padding) {
        final int h = getHeight();
        int x, y = h - padding;
        int barCount = relativeData.keySet().size();
        int xSpacing = getWidth() / (barCount + 1);
        x = xSpacing;
        for (var kolea : relativeData.keySet()) {
            double barHeight = h * relativeData.get(kolea);
            g.setColor(barColor);
            g.fillRect(x, y - (int) barHeight, xSpacing, (int) barHeight);
            g.setColor(Color.BLACK);
            g.drawRect(x, y - (int) barHeight, xSpacing, (int) barHeight);
            x += xSpacing;
        }
        x = xSpacing; // reset x position to start drawing bottom labels
        y += 25; // set y position for bottom lables
        // Draw row names on X-Axis
        g.setColor(textColor);
        for (var label : data.keySet()) {
            int width = g.getFontMetrics().stringWidth(label); // DO Math to center text on top of each bar
            x += (xSpacing / 2);
            x -= (width / 2);
            g.drawString(label, x, y);
            x -= (xSpacing / 2);
            x += (width / 2);
            x += xSpacing;
        }
        // Draw values on Y-Axis
        x = 10;
        y = getHeight() - padding;
        String valueText;
        for (var value : data.values()) {
            value = (double) Math.round(value);
            valueText = value.toString();
            g.drawString(valueText, x, y);
            y -= getHeight() / (barCount + 1);
        }
    }

}
