package com.dansmultipro.restapi.utils;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class FileDownloadUtil {
    public String getFileCSV(String file) throws IOException {
        URL url = new URL(file);
        String resultFile = "src/main/resources/positions.csv";
        JsonNode jsonTree = new ObjectMapper().readTree(url);

        Builder csvSchemaBuilder = CsvSchema.builder();
        JsonNode firstObject = jsonTree.elements().next();
        firstObject.fieldNames().forEachRemaining(fieldName -> {csvSchemaBuilder.addColumn(fieldName);} );
        CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();

        CsvMapper csvMapper = new CsvMapper();
        csvMapper.writerFor(JsonNode.class)
                .with(csvSchema)
                .writeValue(new File(resultFile), jsonTree);
        return resultFile;
    }
}
