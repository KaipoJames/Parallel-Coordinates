import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;

public class Axis {
    String columnName;
    String columnType;
    ArrayList<Object> data;
    ArrayList<Double> relativeData;
    private Line2D.Double geometry;

    public Axis(String name, String type) {
        columnName = name;
        columnType = this.assignType(type);
        data = new ArrayList<>();
        relativeData = new ArrayList<>();
    }

    public void extractData(ResultSet rs) {
        try {
            Object item = rs.getObject(columnName);
            data.add(item);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void debug() {
        System.out.println("PRINTING DATA FOR COLUMN " + columnName);
        for (var d : data) {
            System.out.println(d);
        }
    }

    public Line2D setGeometry(double x, double h) {
        geometry = new Line2D.Double(x, 25, x, h);
        return geometry;
    }

    public void draw(Graphics2D g) {
        g.draw(geometry);
    }

    public Point2D.Double getPointAt(int xPos, int yPos) {
        double y = yPos;
        // double y = yPos * (geometry.y2 - geometry.y1);
        return new Point2D.Double(xPos, y + 25);
    }

    public String assignType(String columnTypeName) {
        if (columnTypeName.equalsIgnoreCase("numeric") || columnTypeName.equalsIgnoreCase("int4")
                || columnTypeName.equalsIgnoreCase("float8")) {
            return "Double";
        } else if (columnTypeName.equalsIgnoreCase("varchar") || columnTypeName.equalsIgnoreCase("char")) {
            return "String";
        } else {
            return columnTypeName;
        }
    }

    public void removeDuplicates() {
        Set<Object> set = new HashSet<>(this.data);
        this.data.clear();
        this.data.addAll(set);
    }

    private void addRelativeData(ArrayList<Object> data) {
        double max = 0;
        for (var value : data) {
            double val = convertToDouble(value);
            if (val > max) {
                max = val;
            }
        }
        for (var value : data) {
            double val = convertToDouble(value);
            relativeData.add(val / max);
        }
    }

    private double convertToDouble(Object value) {
        String val = value.toString();
        double db = Double.parseDouble(val);
        return db;
    }

    public void addRelativeData() {
        if (this.columnType.equalsIgnoreCase("String")) {
            removeDuplicates();
            for (Object d : data) {
                d = d.toString();
                // System.out.println(d);
            }
        } else {
            addRelativeData(this.data);
            // System.out.println("\nRelativeDATA: ");
            // for (var val : this.relativeData) {
            // System.out.println(val);
            // }
        }
    }

}
