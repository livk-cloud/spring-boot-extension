package com.livk.export.controller;

import com.livk.export.service.AuthorsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author livk
 */
@RestController
@RequiredArgsConstructor
public class AuthorsController {

    private final AuthorsService authorsService;

    @GetMapping("download")
    public HttpEntity<Void> download(HttpServletResponse response) throws IOException {
        String fileName = System.currentTimeMillis() + ".csv";
        response.addHeader("Content-Type", "application/csv");
        response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            authorsService.download(writer);
        }
        return ResponseEntity.ok().build();
    }
}
