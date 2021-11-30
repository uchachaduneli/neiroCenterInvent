package ge.invest.processing;

import com.mysql.jdbc.CallableStatement;
import ge.invent.utils.Util;
import ge.invest.connection.DBConnection;
import ge.invest.entities.Inventar;
import ge.invest.entities.User;

import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ucha Chaduneli
 */
public class DbProcessing implements Serializable {

    static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    public static List<User> getUsers(int userId, String username, String password) {
        List<User> users = new ArrayList<User>();
        try {
            DBConnection.openDbConn();
            CallableStatement cs;
            cs = (com.mysql.jdbc.CallableStatement) DBConnection.getDbConn().prepareCall("{CALL prc_get_user(?,?,?)}");
            cs.setString(1, username);
            cs.setString(2, password != null ? Util.MD5(password) : null);
            cs.setInt(3, userId);
            ResultSet res = cs.executeQuery();
            User user;
            while (res.next()) {
                user = new User();
                user.setId(res.getInt("user_id"));
                user.setUserName(res.getString("user_name"));
                user.setDescription(res.getString("user_description"));
                user.setPassword(res.getString("user_password"));
                user.setTypeId(res.getInt("type_id"));
                user.setTypeName(res.getString("user_type_name"));
                users.add(user);
            }
            return users;
        } catch (Exception ex) {
            return null;
        } finally {
            DBConnection.closeDbConn();
        }
    }

    public static int userAction(User user, int authorId) {
        try {
            DBConnection.openDbConn();
            CallableStatement cs;
            cs = (CallableStatement) DBConnection.getDbConn().prepareCall("{CALL prc_users(?,?,?,?,?,?,?)}");
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.setInt(2, user.getId());
            cs.setString(3, user.getUserName());
            cs.setString(4, user.getDescription());
            cs.setString(5, Util.MD5(user.getPassword()));
            cs.setInt(6, user.getTypeId());
            cs.setInt(7, user.isEditPass() ? 1 : 0);
            cs.execute();
            return cs.getInt("p_result_id");
        } catch (SQLException exception) {
            return 0;
        } finally {
            DBConnection.closeDbConn();
        }
    }

    public static int checkUserNameAvailability(String userName) {
        try {
            DBConnection.openDbConn();
            Statement cs = DBConnection.getDbConn().createStatement();
            ResultSet res = cs.executeQuery("select user_id from users where user_name='" + userName.trim() + "'");
            int yes = 0;
            while (res.next()) {
                yes = 1;
            }
            return yes;
        } catch (SQLException exception) {
            return 0;
        }
    }

    public static int startNewSeason() {
        try {
            DBConnection.openDbConn();
            CallableStatement cs;
            cs = (CallableStatement) DBConnection.getDbConn().prepareCall("{CALL prc_start_new_season(?)}");
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.execute();
            return cs.getInt("p_result_id");
        } catch (Exception exception) {
            return 0;
        } finally {
            DBConnection.closeDbConn();
        }
    }

    public static List<SelectItem> getUserTypes(int typeId) {
        List<SelectItem> types = new ArrayList<SelectItem>();
        try {
            DBConnection.openDbConn();
            CallableStatement cs;
            cs = (com.mysql.jdbc.CallableStatement) DBConnection.getDbConn().prepareCall("{CALL prc_get_user_types(?)}");
            cs.setInt(1, typeId);
            ResultSet res = cs.executeQuery();
            SelectItem type;
            while (res.next()) {
                type = new SelectItem(res.getInt("user_type_id"), res.getString("user_type_name"));
                types.add(type);
            }
            return types;
        } catch (SQLException ex) {
            return null;
        } finally {
            DBConnection.closeDbConn();
        }
    }

    public static void getInnvestDetails(List<Inventar> detailses) {
        try {
            Statement statement = DBConnection.getDbConn().createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM invent_database order by invent_id desc");
            while (rs.next()) {
                Inventar details = new Inventar();
                details.setId(rs.getInt("invent_id"));
                details.setBarcode(rs.getString("invent_code"));
                details.setDescription(rs.getString("invent_description"));
                details.setDepartment(rs.getString("department"));
                details.setCity(rs.getString("city"));
                details.setStelaji(rs.getString("stelaji"));
                details.setUnitPrice(rs.getDouble("invent_one_cost"));
                details.setCount(rs.getInt("invent_amount"));
                details.setResponsiblePerson(rs.getString("invent_person"));
                details.setStatus_old(rs.getString("invent_status_old"));
                details.setStatus_new(rs.getString("invent_status_new"));
                details.setTotalCost(rs.getDouble("invent_sum_cost"));
                details.setItem(rs.getString("invent_thing"));
                details.setNote(rs.getString("invent_remark"));
                detailses.add(details);
            }
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            DBConnection.closeDbConn();
        }

    }

    public static List<Inventar> getPersonChanges(int inventId) {
        try {
            Statement statement = DBConnection.getDbConn().createStatement();
            List<Inventar> list = new ArrayList<>();
            ResultSet rs = statement.executeQuery("SELECT invent_id, old_person, new_person, insert_date FROM log where invent_id='" + inventId + "'");
            Inventar details = null;
            while (rs.next()) {
                details = new Inventar();
                details.setId(rs.getInt("invent_id"));
                details.setResponsiblePerson(rs.getString("new_person"));
                details.setOldPerson(rs.getString("old_person"));
                details.setInsertDate(formatter.format(rs.getDate("insert_date")));
                list.add(details);
            }
            return list;
        } catch (Exception e) {
            return null;
        } finally {
            DBConnection.closeDbConn();
        }

    }

    ///remove invest delail
    public static boolean removeIvent(Inventar invent) {
        try {
            if (invent.getId() > 0) {
                String sql = "DELETE FROM invent_database " + "WHERE invent_id = " + invent.getId();
                Statement statement = DBConnection.getDbConn().createStatement();
                ;
                int executeUpdate = statement.executeUpdate(sql);
                if (executeUpdate > 0) {
                    return true;
                } else {
                    throw new IllegalArgumentException();
                }
            }
        } catch (SQLException | IllegalArgumentException e) {
            return false;
        } finally {
            DBConnection.closeDbConn();
        }
        return true;
    }

    //insert invest detail
    public static boolean updateInvetDetailes(Inventar details) {
        try {
            StringBuilder query = new StringBuilder("UPDATE invent_database SET ");
            query.append("invent_code='").append(details.getBarcode()).append("', ");
            query.append("invent_person='").append(details.getResponsiblePerson()).append("', ");
            query.append("invent_thing='").append(details.getItem()).append("', ");
            query.append("invent_description='").append(details.getDescription()).append("', ");
            query.append("invent_one_cost=").append(details.getUnitPrice()).append(", ");
            query.append("invent_amount=").append(details.getCount()).append(", ");
            query.append("invent_sum_cost=").append(details.getTotalCost()).append(", ");
            query.append("invent_status_old='").append(details.getStatus_old()).append("', ");
            query.append("invent_status_new='").append(details.getStatus_new()).append("', ");
            query.append("department='").append(details.getDepartment()).append("', ");
            query.append("city='").append(details.getCity()).append("', ");
            query.append("stelaji='").append(details.getStelaji()).append("', ");
            query.append("invent_remark='").append(details.getNote()).append("' ");
            query.append("WHERE invent_id = ").append(details.getId()).append(";");
            Statement statement = DBConnection.getDbConn().createStatement();
            ;
            int executeUpdate = statement.executeUpdate(query.toString());
            if (executeUpdate > 0) {
                return true;
            } else {
                throw new IllegalArgumentException();
            }
        } catch (SQLException | IllegalArgumentException e) {
            return false;
        } finally {
            DBConnection.closeDbConn();
        }
    }

    // redaqtireba 
    public static boolean insertInvetDetailes(Inventar details) {
        try {
            StringBuilder query = new StringBuilder("INSERT INTO invent_database (");
            query.append("invent_code,");
            query.append("invent_person,");
            query.append("invent_thing,");
            query.append("invent_description,");
            query.append("invent_one_cost,");
            query.append("invent_amount,");
            query.append("invent_sum_cost,");
            query.append("invent_status_old,");
            query.append("invent_status_new,");
            query.append("department,");
            query.append("city,");
            query.append("stelaji,");
            query.append("invent_remark");
            query.append(")");
            query.append(" VALUES(");
            query.append("'" + details.getBarcode() + "',");
            query.append("'" + details.getResponsiblePerson()).append("', ");
            query.append("'" + details.getItem()).append("', ");
            query.append("'" + details.getDescription()).append("', ");
            query.append(details.getUnitPrice()).append(", ");
            query.append(details.getCount()).append(", ");
            query.append(details.getTotalCost()).append(", ");
            query.append("'" + details.getStatus_old()).append("', ");
            query.append("'" + details.getStatus_new()).append("', ");
            query.append("'" + details.getDepartment()).append("', ");
            query.append("'" + details.getCity()).append("', ");
            query.append("'" + details.getStelaji()).append("', ");
            query.append("'" + details.getNote()).append("'); ");
            Statement statement = DBConnection.getDbConn().createStatement();
            ;
            int executeUpdate = statement.executeUpdate(query.toString());
            if (executeUpdate > 0) {
                return true;
            } else {
                throw new IllegalArgumentException();
            }
        } catch (SQLException | IllegalArgumentException e) {
            return false;
        } finally {
            DBConnection.closeDbConn();
        }
    }

    public static void main(String[] args) {
        //
    }
}
