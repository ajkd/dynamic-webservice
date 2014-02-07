package devuws;
import java.text.*;
import java.util.*; 
import java.net.*;
import java.util.Date;
import java.lang.reflect.*;
import javax.jws.WebService;
import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import java.text.*;
import javax.naming.*;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.*;
import org.w3c.dom.*;


@WebService(endpointInterface = "devuws.devuwsci")
public class devuwsImpl implements devuwsci{
  private SimpleDateFormat dformatter = new SimpleDateFormat ("yyyyMMdd");
  private SimpleDateFormat tformatter = new SimpleDateFormat ("HHmmss");

  @Override
  public String calldevuws( String logindata, String userdata ) {
    String returnValue = "";
    try {
       Date currentTime = new Date();
       DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
       DocumentBuilder db = dbf.newDocumentBuilder();
       Document doc = db.parse(new InputSource( new StringReader(logindata) ));
       XPath xp = XPathFactory.newInstance().newXPath();
       String uid = xp.evaluate("/msg/userid", doc).trim();
       String pwd = xp.evaluate("/msg/password", doc).trim();
       String fid = xp.evaluate("/msg/funcid", doc).trim();
       if ( ( uid != null ) &&  ( uid.trim().length() > 0 ) ) {
         if ( ( pwd != null ) &&  ( pwd.trim().length() > 0 ) ) {
           if ( ( fid != null ) &&  ( fid.trim().length() > 0 ) ) {
             InitialContext ic = new InitialContext();
             String devuwspf = (String)ic.lookup("java:comp/env/devuwspf");
             Properties p = new Properties();
             FileInputStream fi = new FileInputStream( devuwspf );
             p.load( fi );
             String libdefs = p.getProperty("LIBDEF");
             StringTokenizer st = new StringTokenizer(libdefs, ",");
             URL[] url = new URL[ st.countTokens() ];
             int i = 0;
             while ( st.hasMoreTokens() )
               url[i++] = new URL( st.nextToken() );
             URLClassLoader loader = new URLClassLoader (url);
             String pv = p.getProperty("LOGINUER");
             st = new StringTokenizer(pv, "+");
             String class_name = st.nextToken().trim();
             String method_name = st.nextToken().trim();
             Class cl = Class.forName (class_name, true, loader);
             Object whatInstance = cl.newInstance();
             Method myMethod = cl.getMethod( method_name, new Class[] { String.class, String.class, String.class, Properties.class } );
             String loginhm = (String) myMethod.invoke( whatInstance, new Object[] { uid.trim(), pwd.trim(), fid.trim(), p } );
             if ( loginhm.charAt(0) == '0' ) {
               if ( loginhm.trim().length() > 1 ) {
                 st = new StringTokenizer(loginhm.substring(1,loginhm.length()), "+");
                 class_name = st.nextToken().trim();
                 method_name = st.nextToken().trim();
                 cl = Class.forName (class_name, true, loader);
                 whatInstance = cl.newInstance();
                 myMethod = cl.getMethod( method_name, new Class[] { String.class, String.class, String.class, Properties.class } );
                 returnValue = "<rmsg><rcode>0</rcode><msg>" +
                                (String) myMethod.invoke( whatInstance, new Object[] { uid.trim(), fid.trim(), userdata, p } ) + 
                               "</msg>";
               } else {
                   returnValue = "<rmsg><rcode>9</rcode><msg>Request function cannot be performed</msg></rmsg>";
                   System.out.println("devuws.devuwsImpl.calldevuws() -- No UER Given By the Login Module -- " + pv + " for function id -- " + fid );
               }
             } else
                 returnValue = "<rmsg><rcode>" + loginhm.charAt(0) + "</rcode><msg>" + loginhm.substring(1,loginhm.length()) + "</msg></rmsg>";
           } else
               returnValue = "<rmsg><rcode>4</rcode><msg>Function Id is missing</msg></rmsg>";
         } else
             returnValue = "<rmsg><rcode>4</rcode><msg>Password is missing</msg></rmsg>";
       } else
           returnValue = "<rmsg><rcode>4</rcode><msg>User Id missing</msg></rmsg>";
    } catch ( Exception e ) {
        returnValue = "<rmsg><rcode>9</rcode><msg>Request function cannot be performed</msg></rmsg>";
        System.out.println("devuws.devuwsImpl.calldevuws() -- " + e.toString());
    } 
    return returnValue;
  }

}
