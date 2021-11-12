package no.kristiania.survey;

import javax.sql.DataSource;
import java.sql.*;

public class AnswerDao {
    private final DataSource dataSource;

    public AnswerDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Answer answer) throws SQLException {
        try (Connection connection = dataSource.getConnection())
        {
            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into user_answer (answer_text, question_id) values (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, answer.getAnswer_text());
                statement.setLong(2, answer.getQuestion_id());
                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    answer.setAnswer_id(rs.getLong("answer_id"));
                }
            }
        }
    }


    public Answer retrieve(long answerId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from user_answer where answer_id = ?"
            )) {
                statement.setLong(1, answerId);

                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();
                    return readFromResultSet(rs);
                }
            }
        }
    }


    private Answer readFromResultSet(ResultSet rs) throws SQLException {
        Answer answer = new Answer();
        answer.setAnswer_id(rs.getLong("answer_id"));
        answer.setAnswer_text(rs.getString("answer_text"));
        answer.setQuestion_id(rs.getLong("question_id"));
        return answer;
    }
}
