package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class HttpPostClient {

    private final HttpMessage httpMessage;
    private final int statusCode;


    public HttpPostClient(String host, int port, String requestTarget, String contentBody) throws IOException {
        Socket socket = new Socket();

        String request = "POST " + requestTarget + " HTTP/1.1\r\n" +
                "HOST: " + host + "\r\n" +
                "Connection: close\r\n" +
                "Content-Length: " + contentBody.length() + "\r\n" +
                "\r\n" +
                URLDecoder.decode(contentBody, StandardCharsets.UTF_8);
        socket.getOutputStream().write(request.getBytes());

        httpMessage = new HttpMessage(socket);
        String[] statusLine = httpMessage.startLine.split(" ");
        this.statusCode = Integer.parseInt(statusLine[1]);
    }


    public int getStatusCode() {
        return statusCode;
    }
}
