package com.swl.groupMatch.controllers;

import com.swl.groupMatch.documents.Student;
import com.swl.groupMatch.documents.Takes;
import com.swl.groupMatch.models.TakesId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

import static java.util.Map.entry;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/cal")
public class CalendarController {

    public static Logger LOGGER = Logger.getLogger(CalendarController.class.getName());



    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file) {
        return file.isEmpty() ?
                new ResponseEntity<String>(HttpStatus.NOT_FOUND) : new ResponseEntity<String>(HttpStatus.OK);
    }
}
