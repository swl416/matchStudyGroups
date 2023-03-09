package com.swl.groupMatch.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.swl.groupMatch.documents.Group;
import com.swl.groupMatch.documents.Student;
import com.swl.groupMatch.documents.Takes;
import com.swl.groupMatch.models.StudentId;
import com.swl.groupMatch.models.TakesId;
import com.swl.groupMatch.repositories.GroupRepository;
import com.swl.groupMatch.repositories.StudentRepository;
import com.swl.groupMatch.repositories.TakesRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/student")
public class StudentController {
    public static Logger LOGGER = Logger.getLogger(StudentController.class.getName());
    @Autowired
    StudentRepository studentRepo;
    @Autowired
    GroupRepository groupRepo;
    @Autowired
    TakesRepository takesRepo;

    @GetMapping("/getStudentByEmail/{email}")
    public ResponseEntity<?> getStudentByEmail(@PathVariable("email") String email) {
        LOGGER.info("getStudentByEmail");
        Student student = studentRepo.findStudentByEmail(email);
        return new ResponseEntity<>(student.toString(), HttpStatus.OK);
    }

    @GetMapping("/auth/{email}/{pw}")
    public ResponseEntity<?> authStudent(@PathVariable("email") String email, @PathVariable("pw") String pw) {
        Student student = studentRepo.authStudent(email, pw);
        if (student != null) {
            LOGGER.info("valid student login");
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        else {
            LOGGER.info("invalid student login");
            return new ResponseEntity<>(
                    "invalid email or password",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/viewGroups/{email}")
    public ResponseEntity<?> viewGroups(@PathVariable("email") String email) {
        List<Group> groups = groupRepo.findStudentGroups(email);
        if (groups != null && groups.size() > 0) {
            ArrayList<HashMap<String, Object>> groupsList = new ArrayList<>();
            for (Group g: groups) {
                HashMap<String, Object> group = new HashMap<>();
                group.put("Group Name", g.getGroupId().getGroupName());
                group.put("Semester", g.getGroupId().getSemester());
                group.put("Matching Courses", g.getMatchingCourses());
                group.put("Students", g.getStudents().stream().map(Student::getStudentName).toList().toString());
                groupsList.add(group);
            }
            LOGGER.info(email + " groups: ");
            ObjectWriter ow = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writer().withDefaultPrettyPrinter();
            try {
                String groupsJson = ow.writeValueAsString(groupsList);
                LOGGER.info(groupsJson);
                return new ResponseEntity<>(groupsJson, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                LOGGER.info(e.toString());
                return new ResponseEntity<>("no groups yet", HttpStatus.BAD_REQUEST);
            }
        }
        else {
            LOGGER.info("no groups yet");
            return new ResponseEntity<>(
                    "no groups yet",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/viewCourses/{email}")
    public ResponseEntity<?> viewCourses(@PathVariable("email") String email) {
        List<Takes> takes = takesRepo.findStudentTakes(email);
        if (takes != null && takes.size() > 0) {
            ArrayList<HashMap<String, Object>> takesList = new ArrayList<>();
            for (Takes t: takes) {
                HashMap<String, Object> takesMap = new HashMap<>();
                takesMap.put("Semester", t.getTakesId().getSemester());
                takesMap.put("Courses", t.getCourses());
                takesList.add(takesMap);
            }
            LOGGER.info(email + " courses: ");

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                String takesJson = ow.writeValueAsString(takesList);
                LOGGER.info(takesJson);
                return new ResponseEntity<>(takesJson, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                LOGGER.info(e.toString());
                return new ResponseEntity<>("no courses", HttpStatus.BAD_REQUEST);
            }
        }
        else {
            LOGGER.info("no courses");
            return new ResponseEntity<>(
                    "no courses",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("/register/{email}/{name}/{pw}")
    public ResponseEntity<?> registerStudent(@PathVariable("email") String email, @PathVariable("name") String name, @PathVariable("pw") String pw) {
        Student student = studentRepo.findStudentByEmail(email);
        if (student != null) {
            return new ResponseEntity<>(
                    "student with same email already exists",
                    HttpStatus.BAD_REQUEST
            );
        }
        else {
            studentRepo.insert(new Student(new StudentId(email), name, pw));
            return new ResponseEntity<>("successfully registered", HttpStatus.CREATED);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getCal() {
        LOGGER.info("getCal");
        return new ResponseEntity<>("cal", HttpStatus.CREATED);
    }

    @PostMapping(value="/uploadCal", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadCal(@RequestPart("file") MultipartFile file, @RequestParam("email") String email) throws IOException {
        LOGGER.info("uploadCal " + email);
        List<Map<String, String>> courses = parseCal(file);
        if (courses != null && courses.size() > 0) {
            String studentName = studentRepo.findStudentByEmail(email).getStudentName();
            Takes takes = new Takes(new TakesId(email, courses.get(0).get("startDT").substring(0,7)), studentName, courses);
            takesRepo.insert(takes);
            return new ResponseEntity<>("successfully uploaded", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("invalid file", HttpStatus.BAD_REQUEST);
        }
    }

    private List<Map<String, String>> parseCal(MultipartFile file) {
        try {
            InputStreamReader isReader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isReader);
            String str;
            Boolean beg = false;
            Map<String, String> curr = new HashMap<>();
            List<Map<String, String>> courses = new ArrayList();
            int NUMFT = 4;
            Map<String, String> attributes = Map.ofEntries(
                    entry("SUMMARY","courseName"),
                    entry("DTSTART","startDT"),
                    entry("LOCATION","loc"),
                    entry("RRULE","days")
            );
            while ((str = reader.readLine()) != null) {
                if (str.equals("BEGIN:VEVENT")) {
                    beg = true;
                }
                List<String> ls = Arrays.asList(str.split(":|\\;"));
                if (ls.size() >= 2 && attributes.containsKey(ls.get(0)) && beg) {
                    if (ls.get(0).equals("RRULE")) {
                        curr.put(attributes.get(ls.get(0)), ls.get(ls.size()-1).split("=")[1]);
                    } else if (ls.get(0).equals("DTSTART")) {
                        String dts = ls.get(ls.size()-1);
                        dts = dts.substring(0,4) + "-" + dts.substring(4,6) + "-" + dts.substring(6,8) + " "
                                + dts.substring(9,11) + ":" + dts.substring(11,13) + ":" + dts.substring(13,15);
                        curr.put(attributes.get(ls.get(0)), dts);
                    } else {
                        curr.put(attributes.get(ls.get(0)), String.join(" ", ls.subList(1, ls.size())));
                    }
                }
                if (curr.size() == NUMFT) {
                    courses.add(curr);
                    curr = new HashMap<>();
                    beg = false;
                }
            }
            LOGGER.info(courses.toString());
            return courses;
        } catch (IOException ex) {
            LOGGER.info(ex.toString());
            return null;
        }
    }
}
