package supplyproduct;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import javax.swing.JOptionPane;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lenovo
 */
public class Customer_Agent1 extends Agent {
    Inventory1 i = new Inventory1();
 List<AID> supplierAgents = new ArrayList<AID>();
         
       private Semaphore semaphore = new Semaphore(1); // only one thread can access the inventory at a time
   
    private Customer customer;
     @Override
    protected void setup() {
        System.out.println("[" + getAID().getName() + "] agent is ready.");

           DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("product-buyer");
        sd.setName("Supply_chain");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
       
        customer = new Customer(this);
         customer.show();
    
    }    

    void requestforproduct(String customerName,String productName, int quantity_product) {
   addBehaviour(new OneShotBehaviour() {
            @Override
   
          public void action() {
            System.out.println("[" + getAID().getName() + "] agent is ready.");
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent(productName);
            // create a reference to the container controller of the main container
             // add the receiver of the message (the inventory agent)
            msg.addReceiver(new AID("inventory-agent", AID.ISLOCALNAME));
            // send the message
            send(msg);
        
            ACLMessage reply = blockingReceive(3000);
            if (reply != null) {
            if (reply.getPerformative() == ACLMessage.INFORM) 
            {
            int quantity = Integer.parseInt(reply.getContent());
              JOptionPane.showMessageDialog(null,"PRODUCT IS AVAILABLE "+quantity);
               
      
          //  System.out.println("Product is available. Quantity: " + quantity);
           JOptionPane.showMessageDialog(null,"DELIVERY FOR THIS PRODUCT IS DOINGING READY..........");
            
          //System.out.println("DELIVERY FOR THIS PRODUCT IS DOINGING READY..........");
            doWait(1000);
            semaphore.release();
            System.out.println("[" + getAID().getName() + "] agent is ready.");
            ACLMessage msg2 = new ACLMessage(ACLMessage.REQUEST);
            msg2.setContent("\tPRODCUTNAME:\t"+productName+"\t QUANTITY:\t"+quantity_product);

            // create a reference to the container controller of the main container
            // add the receiver of the message (the inventory agent)
            msg2.addReceiver(new AID("delivery-agent", AID.ISLOCALNAME));

            // send the message
             send(msg2);
        
            ACLMessage reply4 = blockingReceive(3000);
            if (reply4 != null) {
                 if (reply4.getPerformative() == ACLMessage.INFORM) {
                     String success = reply4.getContent();
           JOptionPane.showMessageDialog(null,"\"CUSTOMER AGENT [\"+ getAID().getName() +\"]CONFIRMED THE DELIERY MESSAGE FROM DELIVERY AGENT AND MESSAGE IS\"\t"+success);
                     
                    // System.out.println("CUSTOMER AGENT ["+ getAID().getName() +"]CONFIRMED THE DELIERY MESSAGE FROM DELIVERY AGENT AND MESSAGE IS"+success);
                 
                    String url = "jdbc:derby://localhost:1527/supply_chain";
                    String user = "napier";
                    String password = "40526227";
                    try {
             java.sql.Connection conn;
             
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
              java.sql.Statement stmt1= (java.sql.Statement)conn.createStatement();
                String query= "UPDATE PRODUCT_DETAILS SET QUANTITY = QUANTITY - "+quantity_product+" WHERE PRODCUT_NAME = "+productName;
                    int rowsAffected = stmt1.executeUpdate(query);
                    System.out.println("Rows affected: " + rowsAffected);
          JOptionPane.showMessageDialog(null,"Inventory Updated..............");
        
                     //System.out.println("Inventory Updated.......");
                       doWait(1000);
                       
                     String query1= "select PRICE from PRODUCT_DETAILS where PRODCUT_NAME="+productName;
                
                 ResultSet rs;
                rs=stmt1.executeQuery(query1);
                   
                    // Check if the product is available in the inventory
                    if (rs.next()) {
                        int quantity1 = rs.getInt("PRICE");
                 
            if(quantity>0)
            {
                      String insertQuery = "INSERT INTO DELIVERY_CHECK(ITEM_NAME, QUANTITY,TOTAL_PRICE,SUPPLIER_NAME)VALUES("+productName+",'"+quantity_product+"','"+quantity1+"','"+customerName+"')";
                    int roweffected1= stmt1.executeUpdate(insertQuery);
                     System.out.println("effected row:"+roweffected1);
                  }              
                    }           
                      JOptionPane.showMessageDialog(null,"Thank you for Shopping");
            
                 //   System.out.println("Thank you for Shopping");
                    // Check if the product is available in the inventory
                    } catch (Exception e) {
                    e.printStackTrace();// send a reply message with the quantity information
                                            }

                 }      // proceed with the order
            } 
        }
        
        
        
        else if (reply.getPerformative() == ACLMessage.FAILURE) {
                        // the requested product is not available
                  JOptionPane.showMessageDialog(null,"PRODUCT IS NOT AVAILABLE ");
    JOptionPane.showMessageDialog(null,"Finding Supplier for ordering this product");
                    
//  System.out.println("Finding Supplier for ordering this product");
        
                doWait(1000);
                semaphore.release();
                System.out.println("[" + getAID().getName() + "] agent is ready.");
                ACLMessage msg1 = new ACLMessage(ACLMessage.REQUEST);
                msg1.setContent(productName);
                // create a reference to the container controller of the main container
                // add the receiver of the message (the inventory agent)
                msg1.addReceiver(new AID("Supplier-agent", AID.ISLOCALNAME));
                  // send the message
                send(msg1);
                
                 //  double bestPrice = Double.MAX_VALUE;
       
                   ACLMessage reply2 = blockingReceive(1000);
                   
                   if (reply2 != null) {
                   if (reply2.getPerformative() == ACLMessage.INFORM) {
                    // process the supplier's price offer
                     String name=reply2.getContent();
                    // String prodcutname=reply2.getContent();
                    // AID supplier = reply.getSender();
                   JOptionPane.showMessageDialog(null,"Received offer for  " + productName +"And Prodcut_price:"+name);
               
                   // System.out.println("Received offer for  " + productName +"And Prodcut_price:"+name);
                    
                    // keep track of the supplier with the best price
         /*           if (price < bestPrice) {
                        bestPrice = price;
                        bestSupplier = supplier;
                    }*/
                   JOptionPane.showMessageDialog(null,"UPDATEING INVENTORY..........");
                      String url = "jdbc:derby://localhost:1527/supply_chain";
                        String user = "napier";
                        String password = "40526227";
                  try {
                java.sql.Connection conn;

               Class.forName("com.mysql.jdbc.Driver");
               conn = DriverManager.getConnection(url, user, password);
                 java.sql.Statement stmt= (java.sql.Statement)conn.createStatement();

// Check if the product already exists in the table
                    String selectQuery = "SELECT * FROM PRODUCT_DETAILS WHERE PRODCUT_NAME ="+productName;
                 
                   
                 ResultSet rs;
                rs=stmt.executeQuery(selectQuery);
                
                    if (rs.next()) {
                      // If the product already exists, update the quantity
                      int currentQuantity = rs.getInt("QUANTITY");
                      int newQuantity = currentQuantity + quantity_product;

                      String updateQuery = "UPDATE PRODUCT_DETAILS SET QUANTITY ="+newQuantity+"WHERE PRODCUT_NAME = "+productName;
                  int rowsAffected = stmt.executeUpdate(updateQuery);
                    System.out.println("Rows affected: " + rowsAffected);
       
                   JOptionPane.showMessageDialog(null,"INVENTORY UPDATEING..........");
                      } else {
                      // If the product doesn't exist, insert a new row
                      String insertQuery = "INSERT INTO PRODUCT_DETAILS (PRODCUT_NAME, QUANTITY,PRICE)VALUES("+productName+","+quantity_product+",'"+name+"')";
                    int roweffected1= stmt.executeUpdate(insertQuery);
                     System.out.println("effected row:"+roweffected1);
                   JOptionPane.showMessageDialog(null,"INVENTORY UPDATEING..........");
             
                    }
                    
         JOptionPane.showMessageDialog(null,"DELIVERY FOR THIS PRODUCT IS DOINGING READY..........");
              doWait(1000);
            semaphore.release();
            System.out.println("[" + getAID().getName() + "] agent is ready.");
            ACLMessage msg2 = new ACLMessage(ACLMessage.REQUEST);
            msg2.setContent("\tPRODCUTNAME:\t"+productName+"\t QUANTITY:\t"+quantity_product+"\t PRICE\t"+name);

            // create a reference to the container controller of the main container
            // add the receiver of the message (the inventory agent)
            msg2.addReceiver(new AID("delivery-agent", AID.ISLOCALNAME));

            // send the message
             send(msg2);
        
            ACLMessage reply4 = blockingReceive(3000);
            if (reply4 != null) {
                 if (reply4.getPerformative() == ACLMessage.INFORM) {
                     String success = reply4.getContent();
           JOptionPane.showMessageDialog(null,"\"CUSTOMER AGENT [\"+ getAID().getName() +\"]CONFIRMED THE DELIERY MESSAGE FROM DELIVERY AGENT AND MESSAGE IS\"\t"+success);
                 
                    try {
             
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
                String query= "UPDATE PRODUCT_DETAILS SET QUANTITY = QUANTITY - "+quantity_product+" WHERE PRODCUT_NAME = "+productName;
                    int rowsAffected = stmt.executeUpdate(query);
                    System.out.println("Rows affected: " + rowsAffected);
                System.out.println("Inventory Updated.......");
                       doWait(1000);
                       
                     String query1= "select PRICE from PRODUCT_DETAILS where PRODCUT_NAME="+productName;
                
                rs=stmt.executeQuery(query1);
                       
                          if (rs.next()) {
                        int quantity1 = rs.getInt("PRICE");
                        
                     String insertQuery = "INSERT INTO DELIVERY_CHECK(ITEM_NAME, QUANTITY,TOTAL_PRICE,SUPPLIER_NAME)VALUES("+productName+",'"+quantity_product+"','"+quantity1+"','"+customerName+"')";
                    int roweffected1= stmt.executeUpdate(insertQuery);
                     System.out.println("effected row:"+roweffected1);
         
                          }       
                       
                       
                       
              JOptionPane.showMessageDialog(null,"THANK YOU FOR SHOPPING");
                 // Check if the product is available in the inventory
                    } catch (Exception e) {
                    e.printStackTrace();// send a reply message with the quantity information
                                            }

                 }      // proceed with the order
            }
                  }
                  catch (Exception e) {
                    e.printStackTrace();// send a reply message with the quantity information
                                            }
       
                    }
                          } 
                        else {
                              block();
                              System.out.println("no reply from supplier agent\t");
                             }
                        // do something else or cancel the order
                }
        }
         else{
               // if no reply message is received, try again or cancel the order
               System.out.println("No reply from inventory agent.");
             }
                
        }
     });
    
   }
}
 /*  if (bestSupplier != null) {
            // send order message to the supplier with the best price
            ACLMessage order = new ACLMessage(ACLMessage.REQUEST);
            order.addReceiver(bestSupplier);
            order.setContent(product1 + "," + quantity1);
            send(order);
            System.out.println("Sent order to " + bestSupplier.getLocalName());
            doWait(2000);
            System.out.println("Your order has been processsed");
            Inventory1 i1 = new Inventory1();
            i1.update_inventory(product1,quantity1);
            System.out.println("Inventory has updated and ready for send the delivery");
        } else {
            System.out.println("Could not find any supplier for the requested product.");
        }
         */       