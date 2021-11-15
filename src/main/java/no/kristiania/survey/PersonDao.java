package no.kristiania.survey;

import javax.sql.DataSource;
import java.sql.*;

public class PersonDao {

    private final DataSource dataSource;

    public PersonDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Person person) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into person (first_name, last_name, email) values (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, person.getFirstName());
                statement.setString(2, person.getLastName());
                statement.setString(3, person.getEmail());
                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    person.setPersonId(rs.getLong("person_id"));
                }
            }
        }
    }


    public Person retrieve(long personId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from person where person_id = ?"
            )) {
                statement.setLong(1, personId);
                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();
                    return readFromResultSet(rs);
                }
            }
        }
    }

    private Person readFromResultSet(ResultSet rs) {
        Person person = new Person();
        person.setPersonId(rs);
    }
}
