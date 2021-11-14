package no.kristiania.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.survey.Answer;
import no.kristiania.survey.AnswerDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class ListUserAnswersController implements HttpController{
    private final AnswerDao answerDao;
    private final Answer answer = new Answer();
    long questionId = answer.getQuestionId();

    public ListUserAnswersController(AnswerDao answerDao) {
        this.answerDao = answerDao;
    }

    @Override
    public void handle(HttpMessage request, Socket socket) throws SQLException, IOException {
        HttpMessage response = new HttpMessage(getBody());
        response.write(socket);
    }

    private String getBody() throws SQLException {
        return answerDao.listAll()
                .stream().map(m -> "<option value=" + m.getAnswerId() + ">" + m.getAnswerText() + "</option>")
                .collect(Collectors.joining());
    }
}
