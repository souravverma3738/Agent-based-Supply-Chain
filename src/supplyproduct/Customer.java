package supplyproduct;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lenovo
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public final class Customer extends JFrame {
    private JLabel customerLabel, productLabel, priceLabel, quantityLabel;
    private JTextField customerField, productField, priceField, quantityField;
    private JButton purchaseButton,deliveryInfoButton,suppliersInfoButton,inventoryListButton;
 private JPanel jPanel1, jPanel2;
   public Customer(Customer_Agent1 agent) {
    super("Product-Buyer: Customer");
   
    setTitle("Supply Chain Management");
    setBounds(400, 150, 100, 110);
    setSize(500, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    // create components
    customerLabel = new JLabel("Customer Name:");
    productLabel = new JLabel("Product Name:");
    quantityLabel = new JLabel("Quantity:");
    
    customerField = new JTextField(20);
    productField = new JTextField(20);
    quantityField = new JTextField(5);
    
    deliveryInfoButton = new JButton("Delivery Information");
    suppliersInfoButton = new JButton("Suppliers Information");
    inventoryListButton = new JButton("Inventory List");
    purchaseButton = new JButton("Purchase");
    
    // add action listeners to the buttons
    deliveryInfoButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          DeliveryGUI dg = new DeliveryGUI();     // code for handling delivery info button click
        }
    });

    suppliersInfoButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          SupplierGUI sg = new SupplierGUI();  // code for handling suppliers info button click
        }
    });

    inventoryListButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          InventoryGUI inventory= new InventoryGUI();  // code for handling inventory list button click
        }
    });

    purchaseButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            // perform action when button is clicked
            String customerName = customerField.getText();
            String productName = productField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            
            agent.requestforproduct(customerName,productName, quantity);
        }
    });

    // create layout
    JPanel panel = new JPanel();
    
    panel.setLayout(new GridLayout(6, 30, 10, 10));
    
    panel.add(customerLabel);
    panel.add(customerField);
    panel.add(productLabel);
    panel.add(productField);
    panel.add(quantityLabel);
    panel.add(quantityField);
    panel.add(new JLabel(""));
    panel.add(new JLabel(""));
    panel.add(purchaseButton);
    panel.add(deliveryInfoButton);
    panel.add(suppliersInfoButton);
    panel.add(inventoryListButton);
    panel.setBackground(Color.WHITE);

    // add panel to frame
    add(panel);
    setVisible(true);
}


}
