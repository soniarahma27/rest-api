package com.dansmultipro.restapi.controller;

import com.dansmultipro.restapi.utils.FileDownloadUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;


@RestController
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping(value = "/positions", produces = {"application/json"})
    public ResponseEntity<Object> getAllPositions() {
        String url = "https://dev6.dansmultipro.com/api/recruitment/positions.json";
        RestTemplate restTemplate = new RestTemplate();

        Object[] positions = restTemplate.getForObject(url, Object[].class);
        return ResponseEntity.ok(positions);
    }

    @GetMapping(value = "/positions/{ID}", produces = {"application/json"})
    public ResponseEntity<Object> getDetailPosition(@PathVariable(value = "ID") String ID) {
        String url = "https://dev6.dansmultipro.com/api/recruitment/positions/{ID}";
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Object> detailPositions = restTemplate.getForEntity(url, Object.class, ID);
        return ResponseEntity.ok(detailPositions.getBody());
    }

    @GetMapping(value = "positions/download", produces = "text/csv")
    public void downloadAllPositions(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String url = "https://dev6.dansmultipro.com/api/recruitment/positions.json";

        FileDownloadUtil fileDownload = new FileDownloadUtil();
        String fileName = fileDownload.getFileCSV(url);

        Resource resource = resourceLoader.getResource("classpath:" + "positions.csv");
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", String.format("attachment; filename=" + resource.getFilename()));
        response.setContentLength((int) resource.contentLength());
        InputStream inputStream = resource.getInputStream();
        FileCopyUtils.copy(inputStream, response.getOutputStream());

    }

}
