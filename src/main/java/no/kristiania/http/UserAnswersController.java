package no.kristiania.http;

import no.kristiania.survey.Answer;
import no.kristiania.survey.AnswerDao;
import no.kristiania.survey.Question;
import no.kristiania.survey.QuestionDao;

import java.sql.SQLException;
import java.util.Map;

public class UserAnswersController implements HttpController {

    private final AnswerDao answerDao;
    public UserAnswersController(AnswerDao answerDao){
        this.answerDao = answerDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
        Answer answer = new Answer();
        answer.setUser_answer(queryMap.get("answer"));
        answer.setQuestion_id(Long.parseLong(queryMap.get("question")));
        answerDao.save(answer);

        return new HttpMessage("HTTP/1.1 200 ok", "It is done");
    }

}
