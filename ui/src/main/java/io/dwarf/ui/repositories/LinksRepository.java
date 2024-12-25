package io.dwarf.ui.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dwarf.ui.errors.LinkGone;
import io.dwarf.ui.models.Link;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@Component
public class LinksRepository {
    private final String root;

    public LinksRepository() {
        this.root = System.getenv("API_ENDPOINT") + "links";
    }

    public void delete(
            String id,
            UUID userId
    ) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        ObjectMapper responseMapper = new ObjectMapper();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.root + "/" + id))
                .header("X-User-UUID", userId.toString())
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client.close();
    }

    public Link getByIdAsOwner(
            String id,
            UUID userId
    ) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        ObjectMapper responseMapper = new ObjectMapper();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.root + "/" + id))
                .header("X-User-UUID", userId.toString())
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client.close();

        return responseMapper.readValue(response.body(), Link.class);
    }

    public Link getById(
            String id
    ) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        ObjectMapper responseMapper = new ObjectMapper();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.root + "/" + id))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client.close();

        if (response.statusCode() == 410) {
            throw new LinkGone();
        }

        return responseMapper.readValue(response.body(), Link.class);
    }

    public Link create(String link, String expires, UUID userId) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        ObjectMapper requestMapper = new ObjectMapper();
        ObjectMapper responseMapper = new ObjectMapper();
        ObjectNode payload = requestMapper.createObjectNode();

        payload.put("href", link);
        payload.put("expires", expires);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.root))
                .header("Content-Type", "application/json")
                .header("X-User-UUID", userId.toString())
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client.close();

        return responseMapper.readValue(response.body(), Link.class);
    }


    public Link update(String id, String link, int r_limit, UUID userId) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        ObjectMapper requestMapper = new ObjectMapper();
        ObjectMapper responseMapper = new ObjectMapper();
        ObjectNode payload = requestMapper.createObjectNode();

        payload.put("href", link);
        payload.put("r_limit", r_limit);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.root + "/" + id))
                .header("Content-Type", "application/json")
                .header("X-User-UUID", userId.toString())
                .PUT(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client.close();

        if (response.statusCode() == 410) {
            throw new LinkGone();
        }

        return responseMapper.readValue(response.body(), Link.class);
    }
}
