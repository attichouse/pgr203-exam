package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpMessage {

    public String startLine;
    public final Map<String, String> headerFields = new HashMap<>();
    public String messageBody;


    public HttpMessage(Socket socket) throws IOException {
        startLine = HttpMessage.readLine(socket);
        readHeaders(socket);
        if (headerFields.containsKey("Content-Length")) {
            messageBody = HttpMessage.readBytes(socket, getContentLength());
        }
    }


    public HttpMessage (String startLine, String messageBody) {
        this.startLine = startLine;
        this.messageBody = messageBody;

    }


    public static Map<String, String> parseRequestParameters(String query) {
        Map<String, String> queryMap = new HashMap<>();
        for (String queryParameter : query.split("&")) {
            int equalPos = queryParameter.indexOf('=');
            String parameterName = queryParameter.substring(0, equalPos);
            String parameterValue = queryParameter.substring(equalPos + 1);
            queryMap.put(parameterName, URLDecoder.decode(parameterValue, StandardCharsets.UTF_8));
        }
        return queryMap;
    }


    private int getContentLength() {
        return Integer.parseInt(getHeader("Content-Length"));
    }


    private String getHeader(String headerName) {
        return headerFields.get(headerName);
    }


    static String readBytes(Socket socket, int contentLength) throws IOException {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < contentLength; i++) {
            result.append((char)socket.getInputStream().read());
        }
        return result.toString();
    }


    private void readHeaders(Socket socket) throws IOException {
        String headerLine;
        while (!(headerLine = HttpMessage.readLine(socket)).isBlank()) {
            int colonPos = headerLine.indexOf(':');
            String headerField = headerLine.substring(0, colonPos);
            String headerValue = headerLine.substring(colonPos + 1).trim();
            headerFields.put(headerField, headerValue);
        }
    }


    static String readLine(Socket socket) throws IOException {
        StringBuilder result = new StringBuilder();
        int c;
        while ((c = socket.getInputStream().read()) != '\r') {
            result.append((char)c);
        }

        int expectedNewLine = socket.getInputStream().read();
        assert expectedNewLine == '\n';
        return result.toString();
    }


    public void write(Socket clientSocket) throws IOException {
        String response = startLine + "\r\n" +
                //getBytes i stedet for length
                "Content-Length: " + messageBody.length() + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                messageBody;
        clientSocket.getOutputStream().write(response.getBytes());
    }
}
