package no.kristiania.survey;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public class QuestionDao {

    private final DataSource dataSource;

    public QuestionDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void save(Question question) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into questions (description, survey_id, question_alternatives) values (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, question.getQuestionDescription());
                statement.setLong(2, question.getQuestionIdFk());
                statement.setString(3, question.getQuestionAlternatives());
                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    question.setQuestionId(rs.getLong("question_id"));
                }
            }
        }
    }

    public void update(Question question) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "update questions set description = ?, survey_id = ?, question_alternatives = ? where question_id = ?",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, question.getQuestionDescription());
                statement.setLong(2, question.getQuestionIdFk());
                statement.setString(3, question.getQuestionAlternatives());
                statement.setLong(4, question.getQuestionId());
                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    question.setQuestionId(rs.getLong("question_id"));
                }
            }
        }
    }


    public void update2(Question question, long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "update questions set description = ?, survey_id = ?, question_alternatives = ? where question_id = ?",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, question.getQuestionDescription());
                statement.setLong(2, question.getQuestionIdFk());
                statement.setString(3, question.getQuestionAlternatives());
                statement.setLong(4, question.getQuestionId());
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
        question.setQuestionAlternatives(rs.getString("question_alternatives"));
        return question;
    }


    public ArrayList<Question> listAll() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from questions")) {
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<Question> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(readFromResultSet(rs));
                    }
                    return result;
                }
            }
        }
    }


    public ArrayList<Question> listBySurvey(long surveyId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from questions where survey_id = ?")) {
                statement.setLong(1, surveyId);
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<Question> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(readFromResultSet(rs));
                    }
                    return result;
                }
            }
        }
    }
}