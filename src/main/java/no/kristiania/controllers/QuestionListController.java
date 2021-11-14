package no.kristiania.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.survey.QuestionDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class QuestionListController implements HttpController{
    private final QuestionDao questionDao;

    public QuestionListController(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public void handle(HttpMessage request, Socket socket) throws SQLException, IOException {
        HttpMessage response = new HttpMessage(getBody());
        response.write(socket);
    }

    private String getBody() throws SQLException {
        return questionDao.listAll()
                .stream().map(m -> "<option value=" + m.getQuestionIdFk() + ">" + m.getQuestionDescription() + "</option>")
                .collect(Collectors.joining());

    }
}

