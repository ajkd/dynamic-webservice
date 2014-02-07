package devuws;
 
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import java.io.*;
 
//Service Endpoint Interface
@WebService
@SOAPBinding(style = Style.RPC)
public interface devuwsci{
 
	String calldevuws( String logindata, String userdata );
 
}
