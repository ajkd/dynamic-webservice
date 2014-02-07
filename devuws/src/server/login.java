package devuws;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Calendar;
import java.util.Date;
import java.lang.reflect.*;
import java.util.Properties;
import java.util.StringTokenizer;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import java.util.Vector;
import java.util.Iterator;
import java.io.*;
import org.xml.sax.*;
import java.text.*;
import java.net.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.*;
import org.w3c.dom.*;

public class login {    

   
    public String doLogin(String userid, String password, String funcid, Properties p ) {
      String out = "";
      try {	
        File xmlFile = new File(p.getProperty("uprof"));
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(xmlFile);
        String exp = "/uprof/user[id='" + userid.trim() + "']/funcid";
        XPath xp = XPathFactory.newInstance().newXPath();
        String func = xp.evaluate(exp, doc);
        if ( func.trim().length() > 0 ) {
          exp = "/uprof/user[id='" + userid + "'] and //user[password='" + password + "']";
          if ( new Boolean(xp.evaluate(exp, doc)).booleanValue() ) {
            exp = "/uprof/user[id='" + userid + "'] and //user[status='A']";
            if ( new Boolean(xp.evaluate(exp, doc)).booleanValue() ) {
              StringTokenizer st = new StringTokenizer( func, "+" );
              boolean ff = false;
              while ( st.hasMoreTokens() ) {
                if ( funcid.compareTo(st.nextToken()) == 0 ) {
                  ff = true;
                  break;
                }
              }
              if ( ff ) { 
                xmlFile = new File(p.getProperty("ufunc"));
                dbf = DocumentBuilderFactory.newInstance();
                db = dbf.newDocumentBuilder();
                doc = db.parse(xmlFile);
                exp = "/ufunc/func[id='" + funcid + "']/uer";
                xp = XPathFactory.newInstance().newXPath();
                func = xp.evaluate(exp, doc).trim();
                if ( func.length() > 0 ) {
                  exp = "/ufunc/func[id='" + funcid + "'] and //func[status='A']";
                  xp = XPathFactory.newInstance().newXPath();
                  if ( new Boolean(xp.evaluate(exp, doc)).booleanValue() ) {
                     out = "0" + func;
                  } else {
                      out = "4Function is not in active state";
                  }
                } else {
                    System.out.println("devuws.login doLogin() -- Funcid " + funcid + " not found in functions definitions" );
                    out = "9Could not login to the system.";
                }
              } else {
                  out = "4Invalid Function.";
              }
            } else {
                out = "4Your profile is not in active state";
            }
          } else {
              out = "4Password is incorrect";
          }
        } else {
            out = "4Userid is incorrect";
        }
      }catch (Exception e){
            System.out.println("devuws.login doLogin() -- " + e.toString() );
            out = "9Could not login to the system.";
      }
      return out;
    }
         
}

