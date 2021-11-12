package no.kristiania.http;

import no.kristiania.survey.Question;
import no.kristiania.survey.QuestionDao;

import java.sql.SQLException;
import java.util.Map;

public class CreateQuestionController implements HttpController {

    private final QuestionDao questionDao;
    public CreateQuestionController(QuestionDao questionDao){
        this.questionDao = questionDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
        Question question = new Question();
        question.setQuestionDescription(queryMap.get("question"));
        question.setQuestionIdFk(Long.parseLong(queryMap.get("survey")));
        questionDao.save(question);

        return new HttpMessage("HTTP/1.1 200 ok", "It is done");
    }
}
