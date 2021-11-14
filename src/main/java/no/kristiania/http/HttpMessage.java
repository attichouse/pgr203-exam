package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpMessage {

    public String startLine;
    public final Map<String, String> headerFields;
    public String messageBody;

    //Constructors
    public HttpMessage(Socket socket) throws IOException {
        startLine = readLine(socket);
        headerFields = readHeaders(socket);

        String contentLength = headerFields.get("Content-Length");
        if (contentLength != null) {
            messageBody = readBytes(socket, Integer.parseInt(contentLength));
        } else {
            messageBody = null;
        }
    }


    public HttpMessage (String messageBody) {
        startLine = "HTTP/1.1 200 OK";
        headerFields = new HashMap<>();
        headerFields.put("Content-Length", String.valueOf(messageBody.getBytes().length));
        headerFields.put("Connection", "close");
        this.messageBody = messageBody;

    }


    public HttpMessage() {
        headerFields = new HashMap<>();
        this.messageBody = null;
    }


    //Methods
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

    public String parameterLine() {
        String s = startLine.substring(startLine.indexOf("?")+1);
        String[] sr = s.split(" ");
        return sr[0];

    }


    static String readBytes(Socket socket, int contentLength) throws IOException {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < contentLength; i++) {
            result.append((char)socket.getInputStream().read());
        }
        return result.toString();
    }


    static Map<String, String> readHeaders(Socket socket) throws IOException {
        Map <String, String> headerFields = new HashMap<>();
        String headerLine;
        while (!(headerLine = HttpMessage.readLine(socket)).isBlank()) {
            int colonPos = headerLine.indexOf(':');
            String headerField = headerLine.substring(0, colonPos);
            String headerValue = headerLine.substring(colonPos + 1).trim();
            headerFields.put(headerField, headerValue);
        }
        return headerFields;
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
        clientSocket.getOutputStream().write((startLine + "\r\n").getBytes());
        for (String headerName : headerFields.keySet()) {
            clientSocket.getOutputStream().write((headerName + ": " + headerFields.get(headerName) + "\r\n").getBytes());
        }
        clientSocket.getOutputStream().write(("\r\n").getBytes());
        if (messageBody != null) {
            clientSocket.getOutputStream().write(messageBody.getBytes());
        }

    }


    //Getters and setters
    public String getContentLength() {
        return messageBody;
    }


    public Map<String, String> getHeader(){
        return headerFields;
    }


    public void setStartLine(String startLine) {
        this.startLine = startLine;
    }


    public String getStartLine() {
        return startLine;
    }
}
