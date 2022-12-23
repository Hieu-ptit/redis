package com.viettel.core.config;

import com.viettel.commons.model.response.Response;
import com.viettel.core.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/configs")
@Validated
public class ConfigController {
    private final ConfigService configService;

    @PostMapping("/cache")
    public Response<Void> clearCache() {
        configService.clearCache();
        return Response.ofSucceeded();
    }

}
