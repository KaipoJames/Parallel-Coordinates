import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Axis {
    String columnName;
    // what type is it? (string, numbers)
    ArrayList<Object> data;
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

    public void setGeometry(double x, double h) {
        geometry = new Line2D.Double(x, 0, x, h);
    }

    public void draw(Graphics2D g) {
        g.draw(geometry);
    }

    public Point2D.Double getPointAt(int i) {
        double y = Math.random() * (geometry.y2 - geometry.y1);
        return new Point2D.Double(geometry.x1, y);
    }
}
