package supplyproduct;

import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class jade extends Agent {
    public void setup() {
        // Setup the JADE environment
        

        Profile p = new ProfileImpl(); 
        Runtime myRuntime = Runtime.instance();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.MAIN_PORT, "1099");
        p.setParameter(Profile.GUI, "false");

        ContainerController cc = myRuntime.createMainContainer(p);

        try {
            // Start the agent controller, which is itself an agent (rma)
            AgentController rma = cc.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
            rma.start();

            // Now start our own SimpleAgent, called Fred.
       
      
          
            AgentController ac3 = cc.createNewAgent("Custmor_product", Customer_Agent1.class.getCanonicalName(), new Object[0]);
            ac3.start();
            
            AgentController ac2 = cc.createNewAgent("inventory-agent", Inventory1.class.getCanonicalName(), new Object[0]);
            ac2.start();


            AgentController ac4 = cc.createNewAgent("delivery-agent", Delivery_product.class.getCanonicalName(), new Object[0]);
            ac4.start();   
            AgentController ac5 = cc.createNewAgent("Supplier-agent",Supplier_2.class.getCanonicalName(), new Object[0]);
            ac5.start(); 
            
           

// make the request for the product
           
        } catch (Exception e) {
            System.out.println("Exception starting agent: " + e.toString());
        }
    }
}
