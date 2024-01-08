/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supplyproduct;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author Lenovo
 */
public class Delivery_product extends Agent {
    
    @Override
    protected void setup()
    {
        
        System.out.println("Delivery agent " + getAID().getName() + " is ready.");

        // Add behaviour to listen for product order requests
        addBehaviour(new Delivery_product.ProductOrderDelivery());
    
    }

    private class ProductOrderDelivery extends CyclicBehaviour {
        private boolean delivered = false;
                
        public void action() {
            
            ACLMessage msg = receive();
      if (msg != null) {
                  System.out.println("Agent2 " + getLocalName() + " is ready.");

                // check if the message is a request to check product inventory
                if (msg.getPerformative() == ACLMessage.REQUEST) {
                    
                  int step=0;
                  switch (step) {
                case 0:
                    System.out.println("Delivery Agent: Preparing to deliver " +msg.getContent());
                    // TODO: Implement delivery preparation logic
                    step++;
                  case 1:
                    System.out.println("Delivery Agent: Delivering " +msg.getContent());
                    // TODO: Implement delivery logic
                    delivered = true;
                    step++;
                case 2:
                    System.out.println("Delivery Agent: " +msg.getContent()+ " \n \tsuccessfully delivered " );
                    // TODO: Implement post-delivery logic
                           ACLMessage reply = msg.createReply();
                     reply.setPerformative(ACLMessage.INFORM);
                           reply.setContent("Delivery Successfull");
                   
                    step++;
                    send(reply);
            
                    break;
                    }
                }
            }
        }
    }
}
