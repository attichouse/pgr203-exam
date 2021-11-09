package no.kristiania.survey;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import javax.sql.DataSource;
import java.util.Random;

public class TestData {

    public static DataSource testDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:postgres;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }

    public static String pickOne(String ... alternatives) {
        return alternatives[new Random().nextInt(alternatives.length)];
    }

    public static long pickOneInt(long ... alternatives) {
        return alternatives[new Random().nextInt(alternatives.length)];
    }
}
