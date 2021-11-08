package no.kristiania.survey;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QuestionDao {

    private final DataSource dataSource;

    public QuestionDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void save(String surveyName) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into surveys (survey_name) values (?)")) {
                statement.setString(1, surveyName);
                statement.executeUpdate();
            }
        }
    }
}
