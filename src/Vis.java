import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.util.*;

public class Vis extends JPanel {

    private List<Axis> axes;
    private List<Polyline> lines;

    public Vis() {
        super();
        axes = new ArrayList<Axis>();
        lines = new ArrayList<Polyline>();
    }

    public void setAxes(List<Axis> list) {
        axes = list;
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
            a.setGeometry(xPos, yPos - bottomMargin);
            a.draw(g);
            g.drawString(columnName, xPos - (columnNameWidth / 2), yPos - 10);
            i++;
        }
        for (Axis a : axes) {
            System.out.println("name: " + a.columnName);
        }
    }

}
