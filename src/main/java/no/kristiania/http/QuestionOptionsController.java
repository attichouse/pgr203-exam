package no.kristiania.http;

import no.kristiania.survey.Question;
import no.kristiania.survey.QuestionDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class QuestionOptionsController implements HttpController{

    private final QuestionDao questionDao;

    public QuestionOptionsController(QuestionDao questionDao){
        this.questionDao = questionDao;
    }


    @Override
    public void handle(HttpMessage request, Socket socket) throws SQLException, IOException {
        HttpMessage response = new HttpMessage(getBody());
        response.write(socket);
    }

    private String getBody() throws SQLException {
        String responseText = "";

        int value = 1;
        for (Question question : questionDao.listAll()) {
            responseText += question;
        }
        return responseText;
    }
}
