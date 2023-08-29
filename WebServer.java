import java.io.*;
import java.nio.file.Files;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    public static void main(String[] args) {
        int port = 8080;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor web en funcionamiento en el puerto " + port);
            while (true) {
                //Request
                Socket clientSocket = serverSocket.accept();
                //Request Thread
                Thread requestHandler = new Thread(() -> handleRequest(clientSocket));
                requestHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream out = clientSocket.getOutputStream();
            String requestLine = in.readLine();
            String[] parts = requestLine.split(" ");
            String filePath = parts[1].substring(1);
            String contentType;
            if (filePath.endsWith(".html")) {
                contentType = "text/html";
            } else if (filePath.endsWith(".js")) {
                contentType = "application/javascript";
            } else if (filePath.endsWith(".css")) {
                contentType = "text/css";
            } else if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (filePath.endsWith(".png")) {
                contentType = "image/png";
            } else {
                contentType = "application/octet-stream";
            }

            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                byte[] fileData = Files.readAllBytes(file.toPath());
                String response = "HTTP/1.1 200 OK\r\nContent-Type: " + contentType + "\r\n\r\n";
                out.write(response.getBytes());
                out.write(fileData);
            } else {
                String response = "HTTP/1.1 404 Not Found\r\n\r\n";
                out.write(response.getBytes());
            }
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
