package com.viettel.commons.util;

import com.viettel.commons.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class FormDataConverter {

    private static final String BOUNDARY = Long.toString(System.currentTimeMillis());
    public static final String MULTI_FORM_DATA = String.format("multipart/form-data; boundary=%s", BOUNDARY);
    private static final byte[] DOUBLE_NEW_LINE = "\r\n\r\n".getBytes(StandardCharsets.UTF_8);
    private static final byte[] SEPARATOR = ("--" + BOUNDARY + "\r\nContent-Disposition: form-data; name=")
            .getBytes(StandardCharsets.UTF_8);
    private static final byte[] END = ("--" + BOUNDARY + "--").getBytes(StandardCharsets.UTF_8);
    private static final String NEW_LINE = "\r\n";

    public static List<byte[]> convert(Map<String, Object> data) throws IOException {
        var byteArrays = new ArrayList<byte[]>();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            byteArrays.add(SEPARATOR);
            if (entry.getValue() instanceof MultipartFile) {
                byteArrays.add(getFileSeparator(entry.getKey(), ((MultipartFile) entry.getValue()).getOriginalFilename()));
                byteArrays.add(Objects.requireNonNull(((MultipartFile) entry.getValue()).getContentType()).getBytes(StandardCharsets.UTF_8));
                byteArrays.add(DOUBLE_NEW_LINE);
                byteArrays.add(((MultipartFile) entry.getValue()).getBytes());
                byteArrays.add(NEW_LINE.getBytes(StandardCharsets.UTF_8));
                continue;
            }
            if (entry.getValue() instanceof Part) {
                byteArrays.add(getFileSeparator(entry.getKey(), ((Part) entry.getValue()).getSubmittedFileName()));
                byteArrays.add(Objects.requireNonNull(((Part) entry.getValue()).getContentType()).getBytes(StandardCharsets.UTF_8));
                byteArrays.add(DOUBLE_NEW_LINE);
                byteArrays.add(((Part) entry.getValue()).getInputStream().readAllBytes());
                byteArrays.add(NEW_LINE.getBytes(StandardCharsets.UTF_8));
                continue;
            }
            byteArrays.add(
                    ("\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue() + NEW_LINE).getBytes(StandardCharsets.UTF_8));
        }

        byteArrays.add(END);
        return byteArrays;
    }
    private static byte[] getFileSeparator(String fileField, String fileName) {
        return ("\"" + fileField + "\"; filename=\""
                + fileName + "\"\r\nContent-Type: ").getBytes(StandardCharsets.UTF_8);
    }

    public static Map<String, Object> toFormDataRequest(HttpServletRequest request) throws ServletException, IOException {
        Map<String, Object> giftForwardRequest = request.getParts()
                .stream().filter(part -> part.getContentType() != null).collect(Collectors.toMap(Part::getName, part -> part));

        request.getParameterMap().forEach((name, values) -> {
            if (isDuplicatedParam(values)) {
                throw new BusinessException(ErrorCode.DUPLICATE_PARAMS, name + " is duplicated");
            }
            giftForwardRequest.put(name, values[0]);
        });

        return giftForwardRequest;
    }

    private static boolean isDuplicatedParam(String[] paramsValues) {
        return paramsValues.length > 1;
    }
}
