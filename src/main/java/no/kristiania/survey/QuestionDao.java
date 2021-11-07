package no.kristiania.survey;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionDao {

    private final DataSource dataSource;


    public QuestionDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void save(String questionName) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into questions (question_text) values (?)")) {
                statement.setString(1, questionName);
                statement.executeUpdate();
            }
        }

    }


    public List<String> listAll() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from questions")) {
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<String> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(rs.getString("question_text"));
                    }
                    return result;
                }
            }
        }
    }
}
