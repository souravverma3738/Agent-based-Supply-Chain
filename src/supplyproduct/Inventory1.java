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
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Inventory1 extends Agent {
    int QUANTITY;
    @Override          
    protected void setup() {
        
        // add behaviours to the agent
        addBehaviour(new InventoryCheckBehaviour());
    }

 /*   void update_inventory(String productName, int quantity) {
       
        if(productCatalog1.containsKey(productName))
       {
           productCatalog1.put(productName,quantity);
          System.out.println("\t"+productCatalog1);
       }
       else
       {
           productCatalog1.put(productName,quantity);
           System.out.println("\t"+productCatalog1);
       }
      
    }
   */ 

    private class InventoryCheckBehaviour extends CyclicBehaviour {
        public void action() {
            // receive messages from other agents
      //     System.out.println("Agent2 " + getLocalName() + " is ready.");
            ACLMessage msg = receive();

            if (msg != null) {
                  System.out.println("Agent2 " + getLocalName() + " is ready.");

                // check if the message is a request to check product inventory
                if (msg.getPerformative() == ACLMessage.REQUEST) {
                    String content = msg.getContent();

       
        String url = "jdbc:derby://localhost:1527/supply_chain";
        String user = "napier";
        String password = "40526227";
        try {
             java.sql.Connection conn;
             
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
              java.sql.Statement stmt= (java.sql.Statement)conn.createStatement();
                String query= "select PRODCUT_NAME,QUANTITY from NAPIER.PRODUCT_DETAILS where PRODCUT_NAME="+content;
                
                 ResultSet rs;
                rs=stmt.executeQuery(query);
                   
                    // Check if the product is available in the inventory
                    if (rs.next()) {
                        int quantity1 = rs.getInt("QUANTITY");
                        String prodcut_Name = rs.getString("PRODCUT_NAME");
                     if (quantity1 > 0) {
                    ACLMessage reply = msg.createReply();
                    
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent(Integer.toString(quantity1
                        ));
                        conn.close();
                    send(reply);
                   
                     } else {
                         ACLMessage reply = msg.createReply();
                    
                        reply.setPerformative(ACLMessage.FAILURE);
                        reply.setContent("Product not in stock");
                    conn.close();
                    send(reply);
                   
                     } }
                    else {
                    ACLMessage reply = msg.createReply();
                    
                        reply.setPerformative(ACLMessage.FAILURE);
                        reply.setContent("Product not available");
                   conn.close();
                    send(reply);
                   
                    }
                    
        
                    
        } catch (Exception e) {
            e.printStackTrace();

                    // send a reply message with the quantity information
                    
                }
            } else {
                // if no message is received, block the behaviour
                block();
            }
        }
        }
    }
}
