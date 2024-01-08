/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supplyproduct;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Lenovo
 */
public class Supplier_2 extends Agent{
    @Override
    protected void setup() {
        System.out.println("Supplier agent " + getAID().getName() + " is ready.");

        // Add behaviour to listen for product order requests
        addBehaviour(new Supplier_2.ProductOrderServer());
    }

    
    
    
     private class ProductOrderServer extends CyclicBehaviour {
        public void action() {
   
     ACLMessage msg = receive();
      if (msg != null) {
                  System.out.println("Agent2 " + getLocalName() + " is ready.");

                // check if the message is a request to check product inventory
                if (msg.getPerformative() == ACLMessage.REQUEST) {
                    
                  System.out.println("Agent2 " + getLocalName() + " is ready.");
                  String productName = msg.getContent();
                  System.out.println("Agent2 " + getLocalName() + " is ready."+productName);

      String url = "jdbc:derby://localhost:1527/supply_chain";
        String user = "napier";
        String password = "40526227";
        try {
             java.sql.Connection conn;
             
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
              java.sql.Statement stmt= (java.sql.Statement)conn.createStatement();
                String query= "SELECT SUPPLIER_NAME,PRODUCT_NAME,PRODUCT_PRICE FROM NAPIER.SUPPLIER_DETAILS WHERE PRODUCT_NAME = \n" +productName+"ORDER BY PRODUCT_PRICE ASC ";
                
                 ResultSet rs;
                rs=stmt.executeQuery(query);
                   
                    // Check if the product is available in the inventory
                    if (rs.next()) {
                        int quantity1 = rs.getInt("PRODUCT_PRICE");
                        String prodcut_Name = rs.getString("PRODUCT_NAME");
                        String Supplier_Name = rs.getString("SUPPLIER_NAME");
                        if(prodcut_Name!=null && Supplier_Name !=null && quantity1 >0 )
                        {
                        ACLMessage reply = msg.createReply();
                    
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent(Integer.toString(quantity1));
                      //  reply.setContent(prodcut_Name);
                        
                        
                        
                    send(reply);
                        }
                        else
                        {
                            ACLMessage reply = msg.createReply();
                    
                        reply.setPerformative(ACLMessage.FAILURE);
                        reply.setContent("Product not in stock");
                    
                    send(reply);
                        }
                    }
        }
        catch (Exception e) {
            e.printStackTrace();

                    // send a reply message with the quantity information
                    
                }
                }
      }
        }
     }
}
