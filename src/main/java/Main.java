import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Nasa nasa;
        StringBuffer apiKey = new StringBuffer("https://api.nasa.gov/planetary/apod?api_key=");
        ObjectMapper mapper = new ObjectMapper();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your NASA API key:");
        apiKey.append(scanner.nextLine()); //https://api.nasa.gov/planetary/apod?api_key=xn3bMkCfuBC52BmOiho5ul9aE3KMH0l09Fra9hIH

        HttpGet request = new HttpGet(String.valueOf(apiKey)); //xn3bMkCfuBC52BmOiho5ul9aE3KMH0l09Fra9hIH
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            //final CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(request);
            nasa = mapper.readValue(response.getEntity().getContent(), new TypeReference<>() {
            });
            String fileUrl = nasa.getHdurl();
            String[] fileUrlMas = fileUrl.split("/");
            System.out.println(nasa.getHdurl());
            URL url = new URL(nasa.getHdurl());
            InputStream inputStream = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = inputStream.read(buf))) {
                outputStream.write(buf, 0, n);
            }
            byte[] responseImage = outputStream.toByteArray();
            FileOutputStream fos = new FileOutputStream(fileUrlMas[fileUrlMas.length - 1]);
            fos.write(responseImage);
            fos.close();
        } catch (RuntimeException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}