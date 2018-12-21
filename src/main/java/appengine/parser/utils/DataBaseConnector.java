package appengine.parser.utils;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by anand.kurapati on 09/12/17.
 */
public class DataBaseConnector {

    private static DSLContext create = null;

    public static DSLContext getDSLContext() {

        if (create == null) {
            Connection conn = null;

            String userName = "user";
            String password = "";
            String url = "jdbc:mysql://35.184.102.21:3306/parser?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                conn = DriverManager.getConnection(url, userName, password);
                create = DSL.using(conn, SQLDialect.MYSQL);
            } catch (Exception e) {
                 e.printStackTrace();
            }
        }
        return create;

    }
}
