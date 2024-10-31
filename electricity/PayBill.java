package electricity;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;

public class PayBill extends JFrame implements ActionListener{

    Choice cmonth;
    JButton pay, back;
    String meter;
    JLabel labelstatus; // Declare labelstatus at class level

    PayBill(String meter) {
        this.meter = meter;
        setTitle("Electricity Billing System");
        setLayout(null);
        setBounds(300, 150, 900, 600);

        // Heading Label
        JLabel heading = new JLabel("Electricity Bill");
        heading.setFont(new Font("Tahoma", Font.BOLD, 28));
        heading.setHorizontalAlignment(JLabel.CENTER);
        heading.setBounds(250, 10, 400, 40);
        add(heading);

        // Meter Number Label
        JLabel lblmeternumber = new JLabel("Meter Number");
        lblmeternumber.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblmeternumber.setBounds(50, 80, 200, 30);
        add(lblmeternumber);

        JLabel meternumber = new JLabel(meter);
        meternumber.setFont(new Font("Tahoma", Font.PLAIN, 18));
        meternumber.setBounds(300, 80, 200, 30);
        add(meternumber);

        // Name Label
        JLabel lblname = new JLabel("Name");
        lblname.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblname.setBounds(50, 140, 200, 30);
        add(lblname);

        JLabel labelname = new JLabel("");
        labelname.setFont(new Font("Tahoma", Font.PLAIN, 18));
        labelname.setBounds(300, 140, 200, 30);
        add(labelname);

        // Month Label
        JLabel lblmonth = new JLabel("Month");
        lblmonth.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblmonth.setBounds(50, 200, 200, 30);
        add(lblmonth);

        cmonth = new Choice();
        cmonth.setBounds(300, 200, 200, 30);
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
            cmonth.add(month);
        }
        add(cmonth);

        // Units Label
        JLabel lblunits = new JLabel("Units");
        lblunits.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblunits.setBounds(50, 260, 200, 30);
        add(lblunits);

        JLabel labelunits = new JLabel("");
        labelunits.setFont(new Font("Tahoma", Font.PLAIN, 18));
        labelunits.setBounds(300, 260, 200, 30);
        add(labelunits);

        // Total Bill Label
        JLabel lbltotalbill = new JLabel("Total Bill");
        lbltotalbill.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lbltotalbill.setBounds(50, 320, 200, 30);
        add(lbltotalbill);

        JLabel labeltotalbill = new JLabel("");
        labeltotalbill.setFont(new Font("Tahoma", Font.PLAIN, 18));
        labeltotalbill.setBounds(300, 320, 200, 30);
        add(labeltotalbill);

        // Status Label
        JLabel lblstatus = new JLabel("Status");
        lblstatus.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblstatus.setBounds(50, 380, 200, 30);
        add(lblstatus);

        labelstatus = new JLabel(""); // Initialize at class level
        labelstatus.setFont(new Font("Tahoma", Font.BOLD, 18));
        labelstatus.setForeground(Color.RED);
        labelstatus.setBounds(300, 380, 200, 30);
        add(labelstatus);

        // Fetching customer details from the database
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from customer where meter_no = '" + meter + "'");
            while (rs.next()) {
                labelname.setText(rs.getString("name"));
            }

            rs = c.s.executeQuery("select * from bill where meter_no = '" + meter + "' AND month = 'January'");
            while (rs.next()) {
                labelunits.setText(rs.getString("units"));
                labeltotalbill.setText(rs.getString("totalbill"));
                labelstatus.setText(rs.getString("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Update bill details when month changes
        cmonth.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ae) {
                try {
                    Conn c = new Conn();
                    ResultSet rs = c.s.executeQuery("select * from bill where meter_no = '" + meter + "' AND month = '" + cmonth.getSelectedItem() + "'");
                    while (rs.next()) {
                        labelunits.setText(rs.getString("units"));
                        labeltotalbill.setText(rs.getString("totalbill"));
                        labelstatus.setText(rs.getString("status"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Pay Button
        pay = new JButton("Pay");
        pay.setBackground(Color.BLACK);
        pay.setForeground(Color.WHITE);
        pay.setFont(new Font("Tahoma", Font.BOLD, 16));
        pay.setBounds(100, 460, 120, 30);
        pay.addActionListener(this);
        add(pay);

        // Back Button
        back = new JButton("Back");
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.setFont(new Font("Tahoma", Font.BOLD, 16));
        back.setBounds(250, 460, 120, 30);
        back.addActionListener(this);
        add(back);

        // Background Color
        getContentPane().setBackground(Color.WHITE);

        // Adding Bill Image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/bill.png"));
        Image i2 = i1.getImage().getScaledInstance(600, 300, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(500, 150, 350, 300);
        add(image);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == pay) {
            if (labelstatus.getText().equalsIgnoreCase("not paid")) {
                try {
                    Conn c = new Conn();
                    c.s.executeUpdate("update bill set status = 'Paid' where meter_no = '" + meter + "' AND month = '" + cmonth.getSelectedItem() + "'");
                    labelstatus.setText("Paid");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Bill is already paid");
            }
        } else if (ae.getSource() == back) {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new PayBill("");
    }
}
