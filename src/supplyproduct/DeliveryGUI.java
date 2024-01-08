/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supplyproduct;

/**
 *
 * @author Lenovo
 */

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class DeliveryGUI extends JFrame {

    private JPanel topPanel;
    private JPanel bottomPanel;
    private JTable table;
    private DefaultTableModel model;
    private JButton back;
    public DeliveryGUI() {
        super("Delivery");

        // Set up the top panel
        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel titleLabel = new JLabel("Delivery Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
 back = new JButton("Back");
 topPanel.add(back);
 
        topPanel.add(titleLabel);
    
    // add action listeners to the buttons
    back.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            // code for handling delivery info button click
          dispose();
        }
    });

        // Set up the bottom panel
        bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.setBackground(Color.WHITE);

        // Set up the table
        table = new JTable();
        model = new DefaultTableModel(new Object[]{"Cusotmer Name","Product Name","Product Price","Quantity"}, 0);
        table.setModel(model);
        table.setRowHeight(25);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
       
        // Load data into the table
        try {
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/supply_chain", "napier", "40526227");
            String query = "SELECT * FROM DELIVERY_CHECK";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String SupplierName = rs.getString("SUPPLIER_NAME");
               
                String productName = rs.getString("ITEM_NAME");
                double productPrice = rs.getDouble("TOTAL_PRICE");
                int number = rs.getInt("QUANTITY");
               
                model.addRow(new Object[]{SupplierName, productName, productPrice,number});
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        // Add the table to a scroll pane and then add the scroll pane to the bottom panel
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        // Add the top and bottom panels to the frame
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);

        // Set the size and visibility of the frame
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
