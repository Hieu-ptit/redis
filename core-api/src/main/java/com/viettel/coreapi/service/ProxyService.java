package com.viettel.coreapi.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface ProxyService {
    CompletableFuture<Void> forward(HttpServletRequest request, HttpServletResponse response) throws IOException;

    CompletableFuture<Void> forwardWithBody(HttpServletRequest request, HttpServletResponse response, Object body) throws IOException;

    CompletableFuture<Void> forwardWithBody(HttpServletRequest request, HttpServletResponse response, byte[] body) throws IOException;
}
