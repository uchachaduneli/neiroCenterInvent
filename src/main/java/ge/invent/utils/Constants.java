package ge.invent.utils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

/**
 * @author Ucha Chaduneli
 */
@ManagedBean(name = "constantsBean")
@SessionScoped
public class Constants implements Serializable {

    public static final String projectPath = "/inventar";
//    public static final String uploadPath = "C:\\Program Files\\Apache Software Foundation\\Apache Tomcat 7.0.41\\webapps\\ROOT\\uploads";//chemi
//    private String ip = "http://localhost:8084";

//    public static final String uploadPath = "D:\\uploads";//leptopi
}
