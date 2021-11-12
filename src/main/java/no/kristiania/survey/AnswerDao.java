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
                    "insert into user_answer (user_answer, answer_id) values (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, answer.getUser_answer());
                statement.setLong(2, answer.getAnswer_id());
                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    answer.setAnswer_id(rs.getLong("answer_id"));
                }
            }
        }


    }
}
