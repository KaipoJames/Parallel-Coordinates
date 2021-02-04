import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.util.*;

public class App extends JFrame {

    private Vis mainPanel;

    // Database Variables
    private final String url = "jdbc:postgresql://localhost/kaipojames";
    private final String user = "kaipojames";
    private final String password = "kaipo";

    public App() {

        // Create GUI and variables
        JMenuBar mb = setupMenu();
        setJMenuBar(mb);
        mainPanel = new Vis();
        setContentPane(mainPanel);

        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Bar Graphs and Line Charts");
        setVisible(true);
    }

    private JMenuBar setupMenu() {
        // instantiate menubar, menus, and menu options
        JMenuBar menuBar = new JMenuBar();
        JMenu queryMenu = new JMenu("Queries");
        JMenuItem item1 = new JMenuItem("# of students in each major");
        JMenuItem item2 = new JMenuItem("# of students from each home country");
        JMenuItem item3 = new JMenuItem("average GPA of students in each major");
        JMenuItem item4 = new JMenuItem("average # of credits attempted per year");
        JMenuItem item5 = new JMenuItem("# of students from each country with a GPA > 3.5");
        JMenuItem item6 = new JMenuItem("# of students in each GPA category");
        JMenu graphMenu = new JMenu("Graphs");
        JMenuItem barChart = new JMenuItem("Bar Graph");
        JMenuItem lineChart = new JMenuItem("Line Chart");

        // setup action listeners
        item1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var adrie = runQuery("SELECT Major, COUNT(*) FROM cis2019 GROUP BY Major");
                for (var a : adrie.keySet()) {
                    double num = adrie.get(a);
                    System.out.println(a + " : " + num);
                }
                mainPanel.setData(adrie);
            }
        });
        item2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var adrie = runQuery("SELECT Home, COUNT(*) FROM cis2019 GROUP BY Home");
                for (var a : adrie.keySet()) {
                    double num = adrie.get(a);
                    System.out.println(a + " : " + num);
                }
                mainPanel.setData(adrie);
            }
        });
        item3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                var adrie = runQuery("SELECT Major, AVG(Gpa) FROM cis2019 GROUP BY Major");
                for (var a : adrie.keySet()) {
                    double num = adrie.get(a);
                    System.out.println(a + " : " + num);
                }
                mainPanel.setData(adrie);
            }
        });
        item4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var adrie = runQuery("SELECT Gradyear, AVG(credits_attempted) FROM cis2019 GROUP BY Gradyear");
                for (var a : adrie.keySet()) {
                    double num = adrie.get(a);
                    System.out.println(a + " : " + num);
                }
                mainPanel.setData(adrie);
            }
        });
        item5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var adrie = runQuery("SELECT Home, COUNT(*) FROM cis2019 WHERE Gpa>3.5 GROUP BY Home");
                for (var a : adrie.keySet()) {
                    double num = adrie.get(a);
                    System.out.println(a + " : " + num);
                }
                mainPanel.setData(adrie);
            }
        });
        item5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var adrie = runQuery("SELECT Home, COUNT(*) FROM cis2019 WHERE Gpa>3.5 GROUP BY Home");
                for (var a : adrie.keySet()) {
                    double num = adrie.get(a);
                    System.out.println(a + " : " + num);
                }
                mainPanel.setData(adrie);
            }
        });
        barChart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.chooseChart("Bar");
            }
        });
        lineChart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.chooseChart("Line");
            }
        });

        // connect menu items together
        queryMenu.add(item1);
        queryMenu.add(item2);
        queryMenu.add(item3);
        queryMenu.add(item4);
        queryMenu.add(item5);
        queryMenu.add(item6);
        graphMenu.add(barChart);
        graphMenu.add(lineChart);
        menuBar.add(queryMenu);
        menuBar.add(graphMenu);

        return menuBar;
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
