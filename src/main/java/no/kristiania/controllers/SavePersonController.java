package no.kristiania.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpServer;
import no.kristiania.survey.Person;
import no.kristiania.survey.PersonDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;

public class SavePersonController implements HttpController{

    private final PersonDao personDao;
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public SavePersonController(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Override
    public void handle(HttpMessage request, Socket socket) throws SQLException, IOException {
        HttpMessage response = handle(request);
        //response.getHeader().put("Set-Cookie", "");
        response.write(socket);
    }

    private HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
        Person person = new Person();
        person.setFirstName(queryMap.get("firstName"));
        person.setLastName(queryMap.get("lastName"));
        person.setEmail(queryMap.get("email"));
        personDao.save(person);
        logger.info("Added person in the database");

        HttpMessage redirect = new HttpMessage();
        redirect.setStartLine("HTTP/1.1 302 Redirect");
        redirect.getHeader().put("Location", "/takeSurvey.html");
        return  redirect;
    }
}
