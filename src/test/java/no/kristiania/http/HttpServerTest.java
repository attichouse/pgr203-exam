package no.kristiania.http;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;

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
        HttpServer server = new HttpServer(0);
        server.setRoot(Paths.get("target/test-classes"));

        String fileContent = "A file created at " + LocalTime.now();
        Files.write(Paths.get("target/test-classes/example-file.txt"), fileContent.getBytes());

        HttpClient client = new HttpClient("localhost", server.getPort(), "/example-file.txt");
        assertEquals(fileContent, client.getMessageBody());
        assertEquals("text/plain", client.getHeader("Content-Type"));
    }

    @Test
    void shouldUseFileExtensionForContentType() throws IOException {
        server.setRoot(Paths.get("target/test-classes"));

        String fileContent = "<p>Hello</p>";
        Files.write(Paths.get("target/test-classes/example-file.html"), fileContent.getBytes());

        HttpClient client = new HttpClient("localhost", server.getPort(), "/example-file.html");
        assertEquals("text/html", client.getHeader("Content-Type"));
    }

    @Test
    void shouldReturnCategoriesFromServer() throws IOException {
        //need to change the options to what we want them to be
        server.setCategories(List.of("Test","Test2"));

        HttpClient client = new HttpClient("localhost", server.getPort(), "/api/categoryOptions");
        assertEquals(
                "<option value=1>Test</option><option value=2>Test2</option>",
                client.getMessageBody()
        );
    }

    @Test
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
    }
}
