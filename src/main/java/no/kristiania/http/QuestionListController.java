package no.kristiania.http;

import no.kristiania.survey.Question;
import no.kristiania.survey.QuestionDao;

import java.sql.SQLException;
import java.util.Map;

public class QuestionListController implements HttpController{
    private final QuestionDao questionDao;

    public QuestionListController(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
            String responseText = "";
            Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.parameterLine());
            Long sid = Long.parseLong(queryMap.get("surveyid"));
            for (Question question : questionDao.listSurvey(sid)) {
                responseText += question.toListString();
            }
            return new HttpMessage("HTTP/1.1 200 OK", responseText);
        }
    }

