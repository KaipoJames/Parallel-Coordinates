import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.util.*;

public class App {

    private Vis mainPanel;
    private List<Axis> axes;
    private JFrame frame;

    // Database Variables
    private final String url = "jdbc:postgresql://localhost/kaipojames";
    private final String user = "kaipojames";
    private final String password = "kaipo";

    public App() {

        axes = new ArrayList<>();

        // Create GUI and variables
        frame = new JFrame();
        JMenuBar mb = setupMenu();
        frame.setJMenuBar(mb);
        mainPanel = new Vis();
        frame.setContentPane(mainPanel);

        frame.setSize(1100, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Parallel Coordinates");
        frame.setVisible(true);
    }

    private JMenuBar setupMenu() {
        // instantiate menubar, menus, and menu options
        JMenuBar menuBar = new JMenuBar();
        JMenu queryMenu = new JMenu("Data Set");
        JMenuItem item1 = new JMenuItem("cis2012");
        JMenuItem item2 = new JMenuItem("cis2019");

        // setup action listeners
        item1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setTitle("Parallel Coordinates: cis2012");
                performAllQuery("SELECT * FROM cis2012");
                mainPanel.setAxes(axes);
            }
        });
        item2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setTitle("Parallel Coordinates: cis2019");
                performAllQuery("SELECT * FROM cis2019");
                mainPanel.setAxes(axes);
            }
        });

        // connect menu items together
        queryMenu.add(item1);
        queryMenu.add(item2);
        menuBar.add(queryMenu);

        return menuBar;
    }

    private void performAllQuery(String q) {
        // List<Point2D> results = new ArrayList<>();
        try {
            axes.clear();
            Connection c = DriverManager.getConnection(url, user, password);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(q);
            ResultSetMetaData md = rs.getMetaData();
            int numColumns = md.getColumnCount();
            for (int i = 1; i <= numColumns; i++) {
                Axis seth = new Axis(md.getColumnName(i), md.getColumnTypeName(i));
                axes.add(seth);
            }
            System.out.println("Axes Length: " + axes.size());
            while (rs.next()) {
                for (Axis a : axes) {
                    a.extractData(rs);
                }
            }
            // for (Axis a : axes) {
            // a.debug();
            // }
        } catch (SQLException e) {
            System.out.println("could not connect to Postgres!");
        }
        // return results;
    }

    // Method to perform a two-column query
    private Map<String, Double> runQuery(String q) {
        Map<String, Double> results = new HashMap<>();
        try {
            Connection c = DriverManager.getConnection(url, user, password);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(q);
            while (rs.next()) {
                double num = rs.getDouble(2);
                String label = rs.getString(1);
                results.put(label, num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("could not connect to Postgres!");
        }
        return results;
    }

    public static void main(String[] args) throws Exception {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new App();
            }
        });
    }
}
