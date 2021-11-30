package ge.invent.utils;

import ge.invest.entities.User;
import ge.invest.processing.DbProcessing;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Ucha Chaduneli
 */
@ManagedBean(name = "utilBean")
@SessionScoped
public class Util implements Serializable {

    private String userName;
    private boolean operator = false;
    private boolean admin = false;
    private List<User> workers;
    private User axaliUseri = new User();
    private String passwordConrfm;
    private boolean userEdit;
    private List<SelectItem> userStatuses;
    private List<SelectItem> countryList = new ArrayList<SelectItem>();
    private List<SelectItem> groupList = new ArrayList<SelectItem>();
    private int loginedUserId;
    private boolean editGroup = true;

    public Util() {
        if ((Integer) Util.getSessionParameter("userId") != null) {
            loginedUserId = (Integer) Util.getSessionParameter("userId");
            switch (((Integer) Util.getSessionParameter("user_type_id"))) {
                case 1:
                    operator = true;
                    break;
                case 2:
                    admin = true;
                    break;
                default:
                    operator = false;
                    admin = false;
                    break;
            }
        }
    }

    public static String paginator(int total_pages, int p, int limit, String targetpage, int adjacents) {
        targetpage = targetpage.replaceAll("(\\&(p|ns)\\=[0-9]+)", "");

        if (p == 0) {
            p = 1;
        }
        int prev = p - 1;
        int next = p + 1;
        double d = (double) -total_pages / (double) limit;
        int lastpage = (int) Math.floor(d);
        lastpage = Math.abs(lastpage);
        int lpm1 = lastpage - 1;
        int counter = 0;
        String pagination = "<div class=\"ui-datatable ui-widget rpPaginator\">";
        pagination += "<table role=\"grid\" style=\"border:none\"><thead style=\"border:none\"><tr style=\"border:none\" role=\"row\">";
        pagination += "<th style=\"border: 0 none;\" class=\"ui-paginator ui-paginator-top ui-widget-header\">";
        pagination += "<span class=\"ui-paginator-current rp-paginator-total\">(" + ka("sul: [") + total_pages + ka("] ") + ")</span>";

        if (lastpage > 1) {
            if (p > 1) {
                pagination += "<span class=\"ui-paginator-prev ui-state-default ui-corner-all\">";
                pagination += "<span onclick=\"window.location.href='" + targetpage + "&amp;p=" + prev + "'\" class=\"ui-icon ui-icon-seek-prev\">p</span>";
                pagination += "</span>";
            } else {
                pagination += "<span class=\"ui-paginator-prev ui-state-default ui-corner-all  ui-state-disabled\">";
                pagination += "<span class=\"ui-icon ui-icon-seek-prev\">p</span>";
                pagination += "</span>";
            }
            //pages
            pagination += "<span class=\"ui-paginator-pages\">";
            if (lastpage < 7 + (adjacents * 2)) {
                for (counter = 1; counter <= lastpage; counter++) {
                    if (counter == p) {
                        pagination += "<span class=\"ui-paginator-page ui-state-default ui-corner-all ui-state-active\">" + counter + "</span>";
                    } else {
                        pagination += "<span  onclick=\"window.location.href='" + targetpage + "&amp;p=" + counter + "'\" class=\"ui-paginator-page ui-state-default ui-corner-all\">" + counter + "</span>";
                    }
                }
            } else if (lastpage > 5 + (adjacents * 2)) {
                if (p < 1 + (adjacents * 2)) {
                    for (counter = 1; counter < 4 + (adjacents * 2); counter++) {
                        if (counter == p) {
                            pagination += "<span class=\"ui-paginator-page ui-state-default ui-corner-all ui-state-active\">" + counter + "</span>";
                        } else {
                            pagination += "<span  onclick=\"window.location.href='" + targetpage + "&amp;p=" + counter + "'\" class=\"ui-paginator-page ui-state-default ui-corner-all\">" + counter + "</span>";
                        }
                    }
                    pagination += "<span>...</span>";
                    pagination += "<span  onclick=\"window.location.href='" + targetpage + "&amp;p=" + lpm1 + "'\" class=\"ui-paginator-page ui-state-default ui-corner-all\">" + lpm1 + "</span>";
                    pagination += "<span  onclick=\"window.location.href='" + targetpage + "&amp;p=" + lastpage + "'\" class=\"ui-paginator-page ui-state-default ui-corner-all\">" + lastpage + "</span>";
                } else if (lastpage - (adjacents * 2) > p && p > (adjacents * 2)) {
                    pagination += "<span  onclick=\"window.location.href='" + targetpage + "&amp;p=1'\" class=\"ui-paginator-page ui-state-default ui-corner-all\">1</span>";
                    pagination += "<span  onclick=\"window.location.href='" + targetpage + "&amp;p=2'\" class=\"ui-paginator-page ui-state-default ui-corner-all\">2</span>";
                    pagination += "<span>...</span>";
                    for (counter = p - adjacents; counter <= p + adjacents; counter++) {
                        if (counter == p) {
                            pagination += "<span class=\"ui-paginator-page ui-state-default ui-corner-all ui-state-active\">" + counter + "</span>";
                        } else {
                            pagination += "<span  onclick=\"window.location.href='" + targetpage + "&amp;p=" + counter + "'\" class=\"ui-paginator-page ui-state-default ui-corner-all\">" + counter + "</span>";
                        }
                    }
                    pagination += "<span>...</span>";
                    pagination += "<span  onclick=\"window.location.href='" + targetpage + "&amp;p=" + lpm1 + "'\" class=\"ui-paginator-page ui-state-default ui-corner-all\">" + lpm1 + "</span>";
                    pagination += "<span  onclick=\"window.location.href='" + targetpage + "&amp;p=" + lastpage + "'\" class=\"ui-paginator-page ui-state-default ui-corner-all\">" + lastpage + "</span>";
                } else {
                    pagination += "<span  onclick=\"window.location.href='" + targetpage + "&amp;p=1'\" class=\"ui-paginator-page ui-state-default ui-corner-all\">1</span>";
                    pagination += "<span  onclick=\"window.location.href='" + targetpage + "&amp;p=2'\" class=\"ui-paginator-page ui-state-default ui-corner-all\">2</span>";
                    pagination += "<span>...</span>";
                    for (counter = lastpage - (2 + (adjacents * 2)); counter <= lastpage; counter++) {
                        if (counter == p) {
                            pagination += "<span class=\"ui-paginator-page ui-state-default ui-corner-all ui-state-active\">" + counter + "</span>";
                        } else {
                            pagination += "<span  onclick=\"window.location.href='" + targetpage + "&amp;p=" + counter + "'\" class=\"ui-paginator-page ui-state-default ui-corner-all\">" + counter + "</span>";
                        }
                    }
                }
            }
            pagination += "</span>";
            if (p < counter - 1) {
                pagination += "<span class=\"ui-paginator-next ui-state-default ui-corner-all\">";
                pagination += "<span onclick=\"window.location.href='" + targetpage + "&amp;p=" + next + "'\"  class=\"ui-icon ui-icon-seek-next\">p</span>";
                pagination += "</span>";
            } else {
                pagination += "<span class=\"ui-paginator-next ui-state-default ui-corner-all ui-state-disabled\">";
                pagination += "<span class=\"ui-icon ui-icon-seek-next\">p</span>";
                pagination += "</span>";
            }
            pagination += "</th></tr></thead><tbody></tbody></table></div>";
        } else {
            pagination = "";
        }
        return pagination;
    }

    public static String getURLFullPath() {
        //HttpServletRequest req;
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String scheme = req.getScheme();             // http
        String serverName = req.getServerName();     // hostname.com
        int serverPort = req.getServerPort();        // 80
        String contextPath = req.getContextPath();   // /mywebapp
        String servletPath = req.getServletPath();   // /servlet/MyServlet
        String pathInfo = req.getPathInfo();         // /a/b;c=123
        String queryString = req.getQueryString();          // d=789

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        if ((serverPort != 80) && (serverPort != 443)) {
            url.append(":").append(serverPort);
        }

        url.append(contextPath).append(servletPath);

        if (pathInfo != null) {
            url.append(pathInfo);
        }
        if (queryString != null) {
            url.append("?").append(queryString);
        }
        String baseUrl = url.toString();
        if (!baseUrl.contains(".jsf?")) {
            baseUrl += "?";
        }
        return baseUrl;
    }

    public static String ka(String str) {
        if (str != null) {
            return str.replace("R", "ღ").
                    replace("j", "ჯ").
                    replace("u", "უ").
                    replace("k", "კ").
                    replace("e", "ე").
                    replace("n", "ნ").
                    replace("g", "გ").
                    replace("S", "შ").
                    replace("w", "წ").
                    replace("z", "ზ").
                    replace("f", "ფ").
                    replace("Z", "ძ").
                    replace("v", "ვ").
                    replace("a", "ა").
                    replace("p", "პ").
                    replace("r", "რ").
                    replace("o", "ო").
                    replace("l", "ლ").
                    replace("W", "ჭ").
                    replace("C", "ჩ").
                    replace("y", "ყ").
                    replace("s", "ს").
                    replace("m", "მ").
                    replace("i", "ი").
                    replace("t", "ტ").
                    replace("q", "ქ").
                    replace("b", "ბ").
                    replace("h", "ჰ").
                    replace("d", "დ").
                    replace("J", "ჟ").
                    replace("x", "ხ").
                    replace("c", "ც").
                    replace("T", "თ");
        } else {
            return null;
        }
    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static void redirect(String path) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            externalContext.redirect(Constants.projectPath + path);
        } catch (IOException ex) {
        }
    }

    public static boolean hasAdminOrSalePerms() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session == null) {
            return false;
        } else {
            if ((Integer) session.getAttribute("user_type_id") != 2) {
                return true;
            } else {
                return false;
            }
        }

    }

    public static Object getSessionParameter(String key) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session == null) {
            return null;
        } else {
            return session.getAttribute(key);
        }
    }

    public static void removeSessionAttribute(String key) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(key);
    }

    public static void setSessionParameter(String key, Object value) throws Exception {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session == null) {
            throw new Exception();
        } else {
            session.setAttribute(key, value);
        }
    }

    public static void checkSession() {
        if (Util.getSessionParameter("userId") == null) {
            Util.redirect("/login.jsf");
        }
    }

    public static boolean hasSession() {
        if (Util.getSessionParameter("userId") == null) {
            return false;
        } else {
            return true;
        }
    }

    public static void logout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
//        this.userName = "";
        Util.redirect("/login.jsf");
    }

    public static boolean hasPermission(String permissionKey) {
        List<String> per = (List<String>) getSessionParameter("permissions");
        if (per != null) {
            return per.contains(permissionKey);
        } else {
            return false;
        }

    }

    public static String getGetParameter(String key) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getParameter(key);
    }

    public static Integer getGetParam(String key) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String id = request.getParameter(key);
        if (id != null) {
            return Integer.parseInt(id);
        } else {
            return 0;
        }
    }

    public static void executeScript(String script) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute(script);
    }

    public static Integer getParam(String paramName) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String _id = (String) facesContext.getExternalContext().getRequestParameterMap().get(paramName);
            if (_id != null) {
                return Integer.parseInt(_id);
            } else {
                return 1;
            }
        } catch (Exception e) {
            return 1;
        }

    }

    public void validateGroupEdit() {
        if (axaliUseri.getTypeId() == 2) {
            if (admin) {
//                editGroup = false;
                axaliUseri.setGroupId((Integer) getSessionParameter("company_group_id"));
            }
        } else {
//            editGroup = true;
        }
    }

    public void checkUserName() {
        if (DbProcessing.checkUserNameAvailability(axaliUseri.getUserName()) != 0) {
            Messages.warn(Util.ka("niki dakavebulia scadeT sxva"));
        } else {
            Messages.info(Util.ka("niki Tavisufalia SegiZliaT gamoiyenoT"));
        }
    }

    public void userAction() {
        if (axaliUseri.isEditPass() && !(axaliUseri.getPassword().equals(passwordConrfm))) {
            Messages.warn(Util.ka("Tqvens mier miTiTebuli parolebi ar emTxveva erTmaneTs"));
        } else {
            if (!userEdit && DbProcessing.checkUserNameAvailability(axaliUseri.getUserName()) != 0) {//usernami dakavebulia
                Messages.warn(Util.ka("aseTi nikiT momxmarebeli ukve arsebobs scadeT sxva"));
            } else {
                if (DbProcessing.userAction(axaliUseri, loginedUserId) == 1) {
                    Messages.info(Util.ka("monacemebis Senaxva dasrulda warmatebiT"));

                } else {
                    Messages.error(Util.ka("monacemebis Senaxva ver xerxdeba"));
                }
                loadWorkers();
                Util.executeScript("insertUserDlgwidg.hide();");
            }
        }
    }

    private String upload(UploadedFile file, String dirName) throws Exception {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = file.getFileName().substring(0, file.getFileName().lastIndexOf('.') - 1) + "_" + fmt.format(new Date()) + file.getFileName().substring(file.getFileName().lastIndexOf('.'));
        File dir = new File(dirName);
        if (dir.isDirectory() == false) {
            dir.mkdirs();
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    throw new Exception(Util.ka("direqtoria ver Seiqmna"));
                }
            }
        }
        File files = new File(dirName + "/" + fileName);
        OutputStream out;
        InputStream is = file.getInputstream();
        out = new FileOutputStream(files);
        byte buf[] = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        is.close();
        out.close();
        return fileName;
    }

    public void editUser() {
        userEdit = true;
        if (userStatuses == null || userStatuses.isEmpty()) {
            userStatuses = DbProcessing.getUserTypes(0);
        }
    }

    public void deleteUser() {
        int result;
        if (axaliUseri != null && axaliUseri.getId() > 0) {
            axaliUseri.setId(-axaliUseri.getId());
            result = DbProcessing.userAction(axaliUseri, loginedUserId);
            if (result > 0) {
                Messages.info(Util.ka("operacia dasrulda warmatebiT"));
            } else {
                Messages.warn(Util.ka("momxmareblis waSla ar sruldeba"));
            }
            loadWorkers();
            axaliUseri = new User();
            Util.executeScript("confirmOfUserDeletion.hide();");
        }
    }

    public void initAddUser() {
        userEdit = false;
        editGroup = true;
        passwordConrfm = "";
        axaliUseri = new User();
        axaliUseri.setEditPass(true);
        userStatuses = DbProcessing.getUserTypes(0);
        if (admin) {
            axaliUseri.setHeadId(loginedUserId);
        }
    }

    public void loadWorkers() {
        if (workers != null && !workers.isEmpty()) {
            workers.clear();
        } else {
            workers = new ArrayList<User>();
        }
        workers = DbProcessing.getUsers(0, "", "");
    }

    public boolean isEditGroup() {
        return editGroup;
    }

    public void setEditGroup(boolean editGroup) {
        this.editGroup = editGroup;
    }

    public List<SelectItem> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<SelectItem> groupList) {
        this.groupList = groupList;
    }

    public List<SelectItem> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<SelectItem> countryList) {
        this.countryList = countryList;
    }

    public List<User> getWorkers() {
        return workers;
    }

    public void setWorkers(List<User> workers) {
        this.workers = workers;
    }

    public User getAxaliUseri() {
        if (this.axaliUseri == null) {
            this.axaliUseri = new User();
        }
        return axaliUseri;
    }

    public void setAxaliUseri(User axaliUseri) {
        this.axaliUseri = axaliUseri;
    }

    public String getPasswordConrfm() {
        return passwordConrfm;
    }

    public void setPasswordConrfm(String passwordConrfm) {
        this.passwordConrfm = passwordConrfm;
    }

    public boolean isUserEdit() {
        return userEdit;
    }

    public void setUserEdit(boolean userEdit) {
        this.userEdit = userEdit;
    }

    public List<SelectItem> getUserStatuses() {
        return userStatuses;
    }

    public void setUserStatuses(List<SelectItem> userStatuses) {
        this.userStatuses = userStatuses;
    }

    public int getLoginedUserId() {
        return loginedUserId;
    }

    public void setLoginedUserId(int loginedUserId) {
        this.loginedUserId = loginedUserId;
    }

    public boolean isOperator() {
        return operator;
    }

    public void setOperator(boolean operator) {
        this.operator = operator;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getUserName() {
        try {
            this.userName = getSessionParameter("userDesc").toString();
        } catch (NullPointerException ex) {
            logout();
        }
        return userName;
    }
}
