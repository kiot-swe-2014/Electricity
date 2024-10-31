package electricity;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;

public class DepositDetails extends JFrame implements ActionListener {

    Choice meternumber, cmonth;
    JTable table;
    JButton search, print;

    DepositDetails() {
        super("Deposit Details");

        setSize(700, 700);
        setLocation(400, 100);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Meter Number Label
        JLabel lblmeternumber = new JLabel("Search By Meter Number");
        lblmeternumber.setBounds(20, 20, 150, 20);
        add(lblmeternumber);

        // Meter Number Choice
        meternumber = new Choice();
        meternumber.setBounds(180, 20, 150, 20);
        add(meternumber);
        
        // Load Meter Numbers
        loadMeterNumbers();

        // Month Label
        JLabel lblmonth = new JLabel("Search By Month");
        lblmonth.setBounds(400, 20, 100, 20);
        add(lblmonth);

        // Month Choice
        cmonth = new Choice();
        cmonth.setBounds(520, 20, 150, 20);
        addMonthsToChoice();
        add(cmonth);

        // Table to Display Results
        table = new JTable();
        loadAllBills();

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(0, 100, 700, 600);
        add(sp);

        // Search Button
        search = new JButton("Search");
        search.setBounds(20, 70, 80, 20);
        search.addActionListener(this);
        add(search);

        // Print Button
        print = new JButton("Print");
        print.setBounds(120, 70, 80, 20);
        print.addActionListener(this);
        add(print);

        setVisible(true);
    }

    private void loadMeterNumbers() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT meter_no FROM customer");
            while (rs.next()) {
                meternumber.add(rs.getString("meter_no"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addMonthsToChoice() {
        String[] months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        };
        for (String month : months) {
            cmonth.add(month);
        }
    }

    private void loadAllBills() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM bill");
            table.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == search) {
            searchBills();
        } else if (ae.getSource() == print) {
            printTable();
        }
    }

    private void searchBills() {
        String query = "SELECT * FROM bill WHERE meter_no = '" + meternumber.getSelectedItem() + 
                       "' AND month = '" + cmonth.getSelectedItem() + "'";
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery(query);
            table.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void printTable() {
        try {
            table.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new DepositDetails();
    }

    // Helper method to build a TableModel from ResultSet
    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // Names of columns
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        // Data of the table
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        while (rs.next()) {
            Object[] rowData = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                rowData[i - 1] = rs.getObject(i);
            }
            model.addRow(rowData);
        }

        return model;
    }
}
