package no.kristiania.survey;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SurveyDao {

    private final DataSource dataSource;

    public SurveyDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void save(Survey survey) throws SQLException {
        try (Connection connection = dataSource.getConnection())
        {
            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into surveys (survey_name) values (?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, survey.getSurveyName());
                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    survey.setSurveyId(rs.getLong("survey_id"));
                }
            }
        }
    }


    public Survey retrieve(long surveyId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from surveys where survey_id = ?")) {
                statement.setLong(1, surveyId);

                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();
                    return readFromResultSet(rs);
                }
            }
        }
    }


    private Survey readFromResultSet(ResultSet rs) throws SQLException {
        Survey survey = new Survey();
        survey.setSurveyId(rs.getLong("survey_id"));
        survey.setSurveyName(rs.getString("survey_name"));
        return survey;
    }


    public List<Survey> listAll() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from surveys"))
            {
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<Survey> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(readFromResultSet(rs));
                    }
                    return result;
                }
            }
        }
    }
}
