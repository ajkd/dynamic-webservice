package devuws;
import devuws.*;
public class devuwsclient {
    
    

     private String callws(java.lang.String arg0, java.lang.String arg1) {
         
         //System.getProperties().put("proxySet", "false");
	//	System.getProperties().put("proxyHost", "172.20.8.100");
	//	System.getProperties().put("proxyPort", "8002");
                
        devuws.devuwsImplService service = new devuws.devuwsImplService();
        devuws.devuwsci port = service.getdevuwsImplPort();
        return port.calldevuws(arg0, arg1);
    }
     
    public static void main(String[] args) {
      for ( int i=0;i<1; i++) {
         String lp = "<msg><userid>abcd1234</userid><password>abcd1234</password><funcid>DEFREC01</funcid></msg>";
            String dp="<msg><ac>1234</ac></msg>";
            System.out.println(new devuwsclient().callws( lp, dp));
      }
      System.exit(0);
    }
}