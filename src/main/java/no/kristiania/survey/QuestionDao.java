package no.kristiania.survey;

import javax.sql.DataSource;
import java.sql.*;

public class QuestionDao {

    private final DataSource dataSource;

    public QuestionDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void save(Question question) throws SQLException {
        try (Connection connection = dataSource.getConnection())
        {
            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into questions (description, survey_id) values (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, question.getQuestionDescription());
                statement.setLong(2, question.getQuestionIdFk());
                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    question.setQuestionId(rs.getLong("question_id"));
                }
            }
        }
    }


    public Question retrieve(long questionId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from questions where question_id = ?"
            )) {
                statement.setLong(1, questionId);

                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();
                    return readFromResultSet(rs);
                }
            }
        }
    }


    private Question readFromResultSet(ResultSet rs) throws SQLException {
        Question question = new Question();
        question.setQuestionId(rs.getLong("question_id"));
        question.setQuestionDescription(rs.getString("description"));
        question.setQuestionIdFk(rs.getLong("survey_id"));
        return question;
    }
}
