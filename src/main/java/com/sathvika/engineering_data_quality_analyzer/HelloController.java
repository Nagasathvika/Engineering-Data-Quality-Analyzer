package com.sathvika.engineering_data_quality_analyzer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Sathvika!";
    }
    @GetMapping("/project")
    public String project()
    {
        return "Engineering Data Quality Analyzer Project";
    }
    @GetMapping("/student")
    public Student student()
    {
        return new Student(
                "Sathvika",
                    "Engineering Data Quality Analyzer"
            );
    }
    @PostMapping("/student")
    public Student createStudent(@RequestBody Student student)
    {
        return student;
    }


}



