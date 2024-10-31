package electricity;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;

public class CustomerDetails extends JFrame implements ActionListener {

    JTable table;
    JButton print;

    CustomerDetails() {
        super("Customer Details");

        setSize(1200, 650);
        setLocation(200, 150);
        setLayout(new BorderLayout()); // Set a layout to arrange components

        // Table to display customer data
        table = new JTable();

        // Load customer data into the table
        loadCustomerData();

        // Add table with a scroll pane
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        // Print button
        print = new JButton("Print");
        print.addActionListener(this);
        add(print, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Method to load customer data into the JTable
    private void loadCustomerData() {
        try {
            Conn c = new Conn(); // Assuming Conn class handles the database connection
            String query = "SELECT * FROM customer";
            ResultSet rs = c.s.executeQuery(query);

            // Set the table model with data from ResultSet
            table.setModel(buildTableModel(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to build a DefaultTableModel from ResultSet
    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // Get column names
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        // Create a model and add rows to it
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

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            // Print the table content
            table.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new CustomerDetails();
    }
}
