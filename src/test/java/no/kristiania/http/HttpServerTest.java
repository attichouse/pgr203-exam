package no.kristiania.http;

import no.kristiania.survey.SurveyDao;
import no.kristiania.survey.TestData;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalTime;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerTest {

    private final HttpServer server = new HttpServer(0);

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
        assertEquals("text/html", client.getHeader("Content-Type"));
    }

    @Test
    void shouldReturnSurveyNameFromServer() throws IOException, SQLException {
        //need to change the options to what we want them to be
        SurveyDao surveyDao = new SurveyDao(TestData.testDataSource());
        surveyDao.save("ost");
        surveyDao.save("Test2");
        server.addController("/api/categoryOptions", new SurveyOptionsController(surveyDao));

        HttpClient client = new HttpClient("localhost", server.getPort(), "/api/categoryOptions");
        assertEquals(
                "<option value=1>ost</option><option value=2>Test2</option>",
                client.getMessageBody()
        );
    }

   /* @Test
    void shouldCreateNewProduct () throws IOException{
        HttpPostClient postClient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/newProduct",
                "productName=Banan"
        );
        assertEquals(200, postClient.getStatusCode());
        Product product = server.getProducts().get(0);
        assertEquals("Banan", product.getProductName());
    }*/
}
