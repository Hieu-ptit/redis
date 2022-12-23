package com.viettel.commons.util;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.JsonWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.commons.exception.BusinessException;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Getter
@Log4j2
public class RestClient {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public RestClient(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Call API for GET method
     * Handle if have errors, (ex: 400, 500, ...)
     *
     * @param reader  decoder body response
     * @param uri     destination url
     * @param headers headers
     * @param <RS>    type response data
     * @return CompletableFuture<RS>
     */
    public <RS> CompletableFuture<RS> getForObject(URI uri, Map<String, String> headers, JsonReader.ReadObject<RS> reader) {
        return get(uri, headers)
                .thenApply(response -> handleResponse(uri, reader, response));
    }

    /**
     * Call API for GET method
     * Handle if have errors, (ex: 400, 500, ...)
     *
     * @param typeReference  decoder body response
     * @param uri     destination url
     * @param headers headers
     * @param <RS>    type response data
     * @return CompletableFuture<RS>
     */
    public <RS> CompletableFuture<RS> getForObject(URI uri, Map<String, String> headers, TypeReference<RS> typeReference) {
        return get(uri, headers)
                .thenApply(response -> handleResponse(uri, typeReference, response));
    }

    /**
     * Call API for GET method
     *
     * @param uri     destination url
     * @param headers headers
     * @return CompletableFuture<HttpResponse < byte [ ]>>
     */
    public CompletableFuture<HttpResponse<byte[]>> get(URI uri, Map<String, String> headers) {
        var httpRequest = newHttpRequestBuilder(uri, headers).GET().build();
        log.info(() -> "URL :" + uri);
        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
    }

    /**
     * Call API for PUT method
     * Handle if have errors, (ex: 400, 500, ...)
     *
     * @param request Object Body
     * @param writer  encoder body request
     * @param reader  decoder body response
     * @param uri     destination url
     * @param headers headers
     * @param <RS>    type response data
     * @return CompletableFuture<RS>
     */
    public <RQ, RS> CompletableFuture<RS> putForObject(URI uri, Map<String, String> headers, RQ request, JsonWriter.WriteObject<RQ> writer, JsonReader.ReadObject<RS> reader) {
        return put(uri, headers, request, writer)
                .thenApply(response -> handleResponse(uri, reader, response));
    }

    /**
     * Call API for PUT method
     *
     * @param request Object Body
     * @param writer  encoder body request
     * @param uri     destination url
     * @param headers headers
     * @param <RQ>    class request body
     * @return CompletableFuture<HttpResponse < byte [ ]>>
     */
    public <RQ> CompletableFuture<HttpResponse<byte[]>> put(URI uri, Map<String, String> headers, RQ request, JsonWriter.WriteObject<RQ> writer) {
        var body = Json.encode(request, writer);
        HttpRequest.BodyPublisher bodyPublisher = (request == null) ?
                HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofByteArray(body);
        var httpRequest = newHttpRequestBuilder(uri, headers)
                .PUT(bodyPublisher)
                .build();
        log.info(() -> "URL :" + uri + ", body " + new String(body, StandardCharsets.UTF_8));
        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
    }

    /**
     * Call API for POST method
     * Handle if this call has errors, (ex: 400, 500, ...)
     *
     * @param request Object Body
     * @param writer  encoder body request
     * @param reader  decoder body response
     * @param uri     destination url
     * @param headers headers
     * @param <RS>    type response data
     * @return CompletableFuture<RS>
     */
    public <RQ, RS> CompletableFuture<RS> postForObject(URI uri, Map<String, String> headers, RQ request, JsonWriter.WriteObject<RQ> writer, JsonReader.ReadObject<RS> reader) {
        return post(uri, headers, request, writer)
                .thenApply(response -> handleResponse(uri, reader, response));
    }

    /**
     * Call API for POST method
     *
     * @param body    Object Body
     * @param uri     destination url
     * @param headers headers
     * @return CompletableFuture<RS>
     */
    public <RS> CompletableFuture<RS> postForObject(URI uri, Map<String, String> headers, byte[] body, TypeReference<RS> typeReference) {
        var httpRequest = newHttpRequestBuilder(uri, headers)
                .POST(HttpRequest.BodyPublishers.ofByteArray(body))
                .build();
        log.info(() -> "URL :" + uri + ", body " + new String(body, StandardCharsets.UTF_8));
        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(response -> handleResponse(uri, typeReference, response));
    }

    /**
     * Call API for POST method
     *
     * @param request Object Body
     * @param writer  encoder body request
     * @param uri     destination url
     * @param headers headers
     * @param <RQ>    class request body
     * @return CompletableFuture<RS>
     */
    public <RQ> CompletableFuture<HttpResponse<byte[]>> post(URI uri, Map<String, String> headers, RQ request, JsonWriter.WriteObject<RQ> writer) {
        var body = Json.encode(request, writer);
        var httpRequest = newHttpRequestBuilder(uri, headers)
                .POST(HttpRequest.BodyPublishers.ofByteArray(body))
                .build();
        log.info(() -> "URL :" + uri + ", body " + new String(body, StandardCharsets.UTF_8));
        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
    }

    /**
     * Call API for POST method
     *
     * @param body    Object Body
     * @param uri     destination url
     * @param headers headers
     * @return CompletableFuture<RS>
     */
    public CompletableFuture<HttpResponse<byte[]>> post(URI uri, Map<String, String> headers, byte[] body) {
        var httpRequest = newHttpRequestBuilder(uri, headers)
                .POST(HttpRequest.BodyPublishers.ofByteArray(body))
                .build();
        log.info(() -> "URL :" + uri + ", body " + new String(body, StandardCharsets.UTF_8));
        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
    }

    public CompletableFuture<HttpResponse<byte[]>> post(URI uri, Map<String, String> headers, Object body) {
        try {
            var httpRequest = newHttpRequestBuilder(uri, headers)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(body)))
                    .build();
            log.info("URL: {}, body: {}", uri, body);
            return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
        } catch (JsonProcessingException e) {
            log.error("can't deserialize body to byte[]", e);
            var exFut = new CompletableFuture<HttpResponse<byte[]>>();
            exFut.completeExceptionally(e);
            return exFut;
        }
    }


    /**
     * Handle response
     * throw Exception when
     *
     * @param reader
     * @param uri
     * @param response
     * @param <RS>
     * @return
     */
    private <RS> RS handleResponse(URI uri, JsonReader.ReadObject<RS> reader, HttpResponse<byte[]> response) {
        var body = new String(response.body(), StandardCharsets.UTF_8);
        if (response.statusCode() == 200) {
            log.info("Call to {} successful, result {}", uri, body);
            if ("".equals(body)) {
                return null;
            }
            return Json.decode(response.body(), reader);
        } else if (response.statusCode() >= 400 && response.statusCode() <= 500) {
            log.error("Call to {} rejected, because {} ", uri, body);
            throw new BusinessException(ErrorCode.INVALID_PARAMETERS, body);
        } else {
            log.error("Call to {} got error, because {} ", uri, response.body());
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, body);
        }
    }

    /**
     * Handle response
     * throw Exception when
     *
     * @param typeReference
     * @param uri
     * @param response
     * @param <RS>
     * @return
     */
    @SneakyThrows
    private <RS> RS handleResponse(URI uri, TypeReference<RS> typeReference, HttpResponse<byte[]> response) {
        var body = new String(response.body(), StandardCharsets.UTF_8);
        if (response.statusCode() == 200) {
            log.info("Send result {} to {} successful", body, uri);
            return objectMapper.readValue(body, typeReference);
        } else if (response.statusCode() >= 400 && response.statusCode() <= 500) {
            log.error("Send result to {} rejected, because: {}", uri, body);
            throw new BusinessException(ErrorCode.INVALID_PARAMETERS, body);
        } else {
            log.error("Send result to {} got error, because: {}", uri, response.body());
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, body);
        }
    }

    /**
     * Handle delete response
     *
     * @param uri     String
     * @param headers Map<String, String>
     * @return CompletableFuture<HttpResponse < byte [ ]>>
     */
    public CompletableFuture<HttpResponse<byte[]>> delete(URI uri, Map<String, String> headers) {
        var httpRequest = newHttpRequestBuilder(uri, headers).DELETE().build();
        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
    }

    /**
     * Handle download file response
     *
     * @param uri     String
     * @return CompletableFuture< InputStream>
     */
    public CompletableFuture<InputStream> downloadFile(URI uri) {
        var httpRequest = HttpRequest.newBuilder(uri).build();
        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofInputStream())
                .thenApply(HttpResponse::body);
    }

    /**
     * Add header into request
     *
     * @param uri
     * @param headers
     * @return
     */
    private HttpRequest.Builder newHttpRequestBuilder(URI uri, Map<String, String> headers) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(uri);
        headers.forEach(requestBuilder::header);
        return requestBuilder;
    }
}
