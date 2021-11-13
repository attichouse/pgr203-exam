package no.kristiania.http;

import no.kristiania.survey.Question;
import no.kristiania.survey.QuestionDao;
import no.kristiania.survey.Survey;
import no.kristiania.survey.SurveyDao;

import java.sql.SQLException;

public class QuestionOptionsController implements HttpController{
    private final QuestionDao questionDao;

    public QuestionOptionsController(QuestionDao questionDao){
        this.questionDao = questionDao;
    }


    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        String responseText = "";

        int value = 1;
        for (Question question : questionDao.listAll()) {
            responseText += question;
        }
        return new HttpMessage("HTTP/1.1 200 OK", responseText);
    }
}
