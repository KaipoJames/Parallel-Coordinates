import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Axis {
    String columnName;
    String type;
    ArrayList<Object> data;
    ArrayList<Number> relativeData;
    private Line2D.Double geometry;

    public Axis(String name) {
        columnName = name;
        data = new ArrayList<>();
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

    public Point2D.Double getPointAt(int i) {
        double y = Math.random() * (geometry.y2 - geometry.y1);
        return new Point2D.Double(geometry.x1, y + 25);
    }

    public void assignType(String columnTypeName) {
        if (columnTypeName.equalsIgnoreCase("numeric") || columnTypeName.equalsIgnoreCase("int4")
                || columnTypeName.equalsIgnoreCase("float8")) {
            this.type = "Double";
        } else {
            this.type = "String";
        }
    }

}
