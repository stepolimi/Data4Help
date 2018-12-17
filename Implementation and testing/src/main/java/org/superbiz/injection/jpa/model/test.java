/*package org.superbiz.injection.jpa.model;

import junit.framework.TestCase;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import java.util.List;
import java.util.Properties;

//START SNIPPET: code
public class test extends TestCase {

    public void test() throws Exception {

        final Properties p = new Properties();
        p.put("movieDatabase", "new://Resource?type=DataSource");
        p.put("movieDatabase.JdbcDriver", "org.hsqldb.jdbcDriver");
        p.put("movieDatabase.JdbcUrl", "jdbc:hsqldb:mem:moviedb");

        final Context context = EJBContainer.createEJBContainer(p).getContext();

        Credentials movies = (Credentials) context.lookup("java:global/injection-of-entitymanager/Credentials");

        movies.addCredential(new Credential("email", "Reservoir Dogs"));
        movies.addCredential(new Credential("Joel Coen", "Fargo"));
        movies.addCredential(new Credential("Joel Coen", "The Big Lebowski"));

        List<Credential> list = movies.getCredential();
        assertEquals("List.size()", 3, list.size());

        for (Credential movie : list) {
            movies.deleteCredential(movie);
        }

        assertEquals("Movies.getMovies()", 0, movies.getCredential().size());
    }
}
*/