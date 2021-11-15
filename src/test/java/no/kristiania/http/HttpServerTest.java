package no.kristiania.http;

import no.kristiania.survey.Survey;
import no.kristiania.survey.SurveyDao;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerTest {

    private final HttpServer server = new HttpServer(0);
    private SurveyDao surveyDao = new SurveyDao(TestData.testDataSource());

    public HttpServerTest() throws IOException {
    }


    @Test
    void shouldReturn404NotFound() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/non-existing");
        assertEquals(404, client.getStatusCode());
    }


    @Test
    void shouldRespond404() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/non-existing");
        assertEquals("File not found: /non-existing", client.getMessageBody());
    }


    @Test
    void shouldServeFiles() throws IOException {
        String fileContent = "A file created at " + LocalTime.now();
        Files.write(Paths.get("target/test-classes/example-file.txt"), fileContent.getBytes());

        HttpClient client = new HttpClient("localhost", server.getPort(), "/example-file.txt");
        assertEquals(fileContent, client.getMessageBody());
        assertEquals("text/plain", client.getHeader("Content-Type"));
    }


    @Test
    void shouldUseFileExtensionForContentType() throws IOException {
        String fileContent = "<p>Hello</p>";
        Files.write(Paths.get("target/test-classes/example-file.html"), fileContent.getBytes());

        HttpClient client = new HttpClient("localhost", server.getPort(), "/example-file.html");
        assertEquals("text/html; charset=UTF-8", client.getHeader("Content-Type"));
    }


    @Test
    void shouldAffirmContentTypeHTML() throws IOException {
        File contentRoot = new File("target/test-classes");

        Files.writeString(new File(contentRoot, "index.html").toPath(), "<h2>Hello World</h2>");

        HttpClient client = new HttpClient("localhost", server.getPort(), "/index.html");
        assertEquals("text/html; charset=UTF-8", client.getHeader("Content-Type"));
    }


    @Test
    void shouldAffirmContentTypeCSS() throws IOException {
        File contentRoot = new File("target/test-classes");

        Files.writeString(new File(contentRoot, "style.css").toPath(), "body { margin: 0 }");

        HttpClient client = new HttpClient("localhost", server.getPort(), "/style.css");
        assertEquals("text/css", client.getHeader("Content-Type"));
    }


    /* Funker når vi kjører testene, men maven is a bish
    @Test
    void shouldReturnSurveyNameFromServer() throws IOException, SQLException {
        Survey survey = exampleSurvey();
        surveyDao.save(survey);
        server.addController("/api/categoryOptions", new SurveyOptionsController(surveyDao));

        HttpClient client = new HttpClient("localhost", server.getPort(), "/api/categoryOptions");
        assertTrue(client.getMessageBody().startsWith("<option value=1>Are you smarter than a 5th grader?</option>"));
    }*/


    //Test objects
    private Survey exampleSurvey() {
        Survey survey = new Survey();
        survey.setSurveyName("Are you smarter than a 5th grader?");
        return survey;
    }

}
