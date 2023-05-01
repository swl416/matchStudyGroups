package com.swl.groupMatch.controllers;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.swl.groupMatch.documents.Admin;
import com.swl.groupMatch.documents.Group;
import com.swl.groupMatch.documents.Student;
import com.swl.groupMatch.documents.Takes;
import com.swl.groupMatch.models.AdminId;
import com.swl.groupMatch.repositories.AdminRepository;
import com.swl.groupMatch.repositories.GroupRepository;
import com.swl.groupMatch.repositories.StudentRepository;
import com.swl.groupMatch.repositories.TakesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin")
public class AdminController {
    public static Logger LOGGER = Logger.getLogger(StudentController.class.getName());
    @Autowired
    AdminRepository adminRepo;
    @Autowired
    GroupRepository groupRepo;
    @Autowired
    TakesRepository takesRepo;
    @Autowired
    StudentRepository studentRepo;

    @GetMapping("/auth/{user}/{pw}")
    public ResponseEntity<?> authAdmin(@PathVariable("user") String user, @PathVariable("pw") String pw) {
        Admin admin = adminRepo.authAdmin(user, pw);
        if (admin != null) {
            LOGGER.info("valid admin login");
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        else {
            LOGGER.info("invalid admin login");
            return new ResponseEntity<>(
                    "invalid user or password",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("/register/{user}/{name}/{pw}")
    public ResponseEntity<?> registerAdmin(@PathVariable("user") String user, @PathVariable("name") String name, @PathVariable("pw") String pw) {
        Admin admin = adminRepo.findAdminByUser(user);
        if (admin != null) {
            return new ResponseEntity<>(
                    "admin with same username already exists",
                    HttpStatus.BAD_REQUEST
            );
        }
        else {
            adminRepo.save(new Admin(new AdminId(user), name, pw));
            return new ResponseEntity<>("successfully registered", HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/del/{user}/{pw}")
    public ResponseEntity<?> delAdmin(@PathVariable("user") String user, @PathVariable("pw") String pw) {
        Admin admin = adminRepo.findAdminByUser(user);
        if (admin == null) {
            return new ResponseEntity<>(
                    "admin with username does not exist",
                    HttpStatus.BAD_REQUEST
            );
        }
        else {
            adminRepo.delete(admin);
            return new ResponseEntity<>("successfully deleted account of " + user, HttpStatus.OK);
        }
    }

    @GetMapping("/searchGroup/{groupName}/{sem}")
    public ResponseEntity<?> searchGroup(@PathVariable("groupName") String groupName, @PathVariable("sem") String sem) {
        Group group = groupRepo.searchGroup(groupName, sem);
        ObjectWriter ow = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writer().withDefaultPrettyPrinter();
        if (group != null) {
            try {
                String groupJson = ow.writeValueAsString(group);
                LOGGER.info(groupJson);
                return new ResponseEntity<>(groupJson, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                LOGGER.info(e.toString());
                return new ResponseEntity<>("invalid group", HttpStatus.BAD_REQUEST);
            }
        } else {
            LOGGER.info("invalid group");
            return new ResponseEntity<>(
                    "invalid group",
                    HttpStatus.BAD_REQUEST
            );
        }


    }

//    @PostMapping("/addGroup/{groupName}/{sem}")
//    public  ResponseEntity<?> addGroup(@PathVariable("groupName") String groupName, @PathVariable("sem") String sem) {
//        Group group = groupRepo.searchGroup(groupName, sem);
//        if (group != null) {
//            return new ResponseEntity<>("group already exists", HttpStatus.BAD_REQUEST);
//        } else {
//            String[] emails = groupName.split(" ");
//            HashSet<String> seen = new HashSet<>();
//            List<HashMap<String, Object>> matching = new ArrayList<>();
//            List<HashMap<String, String>> students = new ArrayList<>();
//            for (String e: emails) {
//                Student student = studentRepo.findStudentByEmail(e);
//                if (student == null) {
//                    return new ResponseEntity<>("group contains invalid email/ student", HttpStatus.BAD_REQUEST);
//                } else {
//                    String name = student.getStudentName();
//                    HashMap<String, String> s = new HashMap<>();
//                    s.put("email", e);
//                    s.put("studentName", name);
//                    students.add(s);
//                    List<HashMap<String, String>> courses = takesRepo.findStudentCourses(e);
//                    for ()
//                }
//            }
//        }
//    }

    @DeleteMapping("/delGroup/{groupName}/{sem}")
    public ResponseEntity<?> delGroup(@PathVariable("groupName") String groupName, @PathVariable("sem") String sem) {
        Group group = groupRepo.searchGroup(groupName, sem);
        if (group != null) {
            groupRepo.delete(group);
            return new ResponseEntity<>("successfully deleted group " + groupName + " " + sem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("group not found", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/searchCourse/{courseName}/{startDT}/{days}/{loc}")
    public ResponseEntity<?> searchCourse(@PathVariable("courseName") String courseName, @PathVariable("startDT") String startDT,
                                          @PathVariable("days") String days, @PathVariable("loc") String loc) {
        List<Object> resp = takesRepo.findAllStudentsOfCourse(loc, courseName, startDT, days);
        if (resp != null && resp.size() > 0) {
            ObjectWriter ow = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writer().withDefaultPrettyPrinter();
            try {
                String respJson = ow.writeValueAsString(resp);
                LOGGER.info(respJson);
                return new ResponseEntity<>(respJson, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                LOGGER.info(e.toString());
                return new ResponseEntity<>("invalid course", HttpStatus.BAD_REQUEST);
            }
        } else {
            LOGGER.info("invalid course");
            return new ResponseEntity<>(
                    "invalid course",
                    HttpStatus.BAD_REQUEST
            );
        }

    }
}
