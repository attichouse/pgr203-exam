package no.kristiania.http;

import no.kristiania.survey.Question;
import no.kristiania.survey.QuestionDao;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;

public class CreateQuestionController implements HttpController {

    private final QuestionDao questionDao;
    public CreateQuestionController(QuestionDao questionDao){
        this.questionDao = questionDao;
    }

    @Override
    public void handle(HttpMessage request, Socket socket) throws SQLException, IOException {
        HttpMessage response = handle(request);
        response.write(socket);
    }

    private HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
        Question question = new Question();
        question.setQuestionDescription(queryMap.get("question"));
        question.setQuestionAlternatives(queryMap.get("questionAlternatives"));
        question.setQuestionIdFk(Long.parseLong(queryMap.get("survey")));
        questionDao.save(question);

        HttpMessage redirect = new HttpMessage();
        redirect.setStartLine("HTTP/1.1 302 Redirect");
        redirect.getHeader().put("Location", "/createSurvey.html");
        return  redirect;
    }
}
