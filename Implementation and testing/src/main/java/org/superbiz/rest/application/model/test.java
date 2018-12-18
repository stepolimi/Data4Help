/*package org.superbiz.rest.application.model;

import junit.framework.TestCase;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import java.util.List;
import java.util.Properties;

//START SNIPPET: code
public class test extends TestCase {

    public void test() throws Exception {

        final Properties p = new Properties();
        p.put("mydb", "new://Resource?type=javax.sql.DataSource");
        p.put("mydb.JdbcDriver", "org.mariadb.jdbc.Driver");
        p.put("mydb.JdbcUrl", "jdbc:mariadb://localhost:3306/mydb");

        final Context context = EJBContainer.createEJBContainer().getContext();

        Users users = (Users) context.lookup("java:global/injection-of-entitymanager/User");

        users.addUser(new User("ehi"));
        users.addUser(new User("fuu"));
        users.addUser(new User("buu"));

        List<User> list = users.getUsers();
        assertEquals("List.size()", 3, list.size());

        for (User user: list) {
            users.deleteUser(user);
        }

        assertEquals("Users.getUsers", 0, users.getUsers().size());
    }
}
*/