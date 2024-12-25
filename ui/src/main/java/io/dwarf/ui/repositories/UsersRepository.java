package io.dwarf.ui.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dwarf.ui.models.Link;
import io.dwarf.ui.models.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public class UsersRepository {
    private final String root;

    public UsersRepository() {
        this.root = System.getenv("API_ENDPOINT") + "users";
    }

    public User create() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.root))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client.close();

        return objectMapper.readValue(response.body(), User.class);
    }

    public Link[] getAllLinks(UUID userId) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        ObjectMapper responseMapper = new ObjectMapper();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.root + "/" + userId + "/links"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client.close();

        return responseMapper.readValue(response.body(), Link[].class);
    }
}

