package com.sathvika.engineering_data_quality_analyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;


import javax.xml.crypto.Data;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
@RestController
public class DataQualityController {

    @Autowired
    private DataQualityService dataQualityService;

    @GetMapping("/quality")
    public String checkQuality()
    {

        return "Data Quality Analysis Started";
    }

    @PostMapping("/engineering-data")
    public DataQualityReport createEngineeringData(@Valid @RequestBody EngineeringData engineeringData) {
        return dataQualityService.analyzeData(engineeringData);
    }
//CRUD operations
    @GetMapping("/reports")
    public List<DataQualityReport> getAllReports()
    {
        return dataQualityService.getAllReports();
    }

    @GetMapping("/reports/{id}")
    public DataQualityReport getReportById(
            @PathVariable Long id)
    {
        return dataQualityService.getReportById(id);
    }

    @DeleteMapping("/reports/{id}")
    public String deleteReport(
            @PathVariable Long id)
    {
        return dataQualityService.deleteReport(id);
    }

    @PutMapping("/reports/{id}")
    public DataQualityReport updateReport(
            @PathVariable Long id,
            @RequestBody DataQualityReport report)
    {
        return dataQualityService
                .updateReport(id, report);
    }
    /*
    @PutMapping("/reports/{id}")
    public String updateReport(
            @PathVariable Long id,
            @RequestBody String body)
    {
        return body;
    }
    */

   // Database fetching
    @GetMapping("/reports/grade/{grade}")
    public List<DataQualityReport> getReportsByGrade(@PathVariable String grade)
    {
        return dataQualityService.getReportsByGrade(grade);
    }

    @GetMapping("/reports/status/{status}")
    public List<DataQualityReport> getReportsByStatus(@PathVariable String status)
    {
        return dataQualityService.getReportsByStatus(status);
    }

    @GetMapping("/reports/search/{grade}/{status}")
    public List<DataQualityReport> getReportsByGradeAndStatus(@PathVariable String grade,@PathVariable String status)
    {
        return dataQualityService.getReportsByGradeAndStatus(grade,status);
    }

    @GetMapping("/reports/score/{score}")
    public List<DataQualityReport> getReportsByQualityScoreGreaterThan(@PathVariable double score)
    {
        return dataQualityService.getReportsByQualityScoreGreaterThan(score);
    }

    @GetMapping("/reports/score/{minScore}/{maxScore}")
    public List<DataQualityReport> getReportsByQualityScoreBetween(@PathVariable double minScore
                                                                   ,@PathVariable double maxScore)
    {
        return dataQualityService.getReportsByQualityScoreBetween(minScore, maxScore);
    }

    @GetMapping("/reports/sort/score/desc")
    public List<DataQualityReport> getReportsSortedByQualityScoreDescending()
    {
        return dataQualityService.getReportsSortedByQualityScoreDescending();
    }

    @GetMapping("/reports/sort/score/asc")
    public List<DataQualityReport> getReportsSortedByQualityScoreAscending()
    {
        return dataQualityService.getReportsSortedByQualityScoreAscending();
    }

    @GetMapping("/reports/sort/grade")
    public List<DataQualityReport> getReportsSortedByGrade()
    {
        return dataQualityService.getReportsSortedByGrade();
    }

    @GetMapping("/reports/sort/status")
    public List<DataQualityReport> getReportsSortedByStatus()
    {
        return dataQualityService.getReportsSortedByStatus();
    }
    //multiple sort
    @GetMapping("/reports/sort/grade-status")
    public List<DataQualityReport> getReportsSortedByGradeAndStatus()
    {
        return dataQualityService.getReportsSortedByGradeAndStatus();
    }
    //pagination
    @GetMapping("/reports/page/{page}/{size}")
    public Page<DataQualityReport> getReportsByPage(@PathVariable int page,@PathVariable int size)
    {
        return dataQualityService.getReportsByPage(page,size);
    }

    @GetMapping("/reports/page/{page}/{size}/sort")
    public Page<DataQualityReport> getReportsByPageSortedByQualityScoreDesc
            (@PathVariable int page,@PathVariable int size)
    {
        return dataQualityService.getReportsByPageSortedByQualityScoreDesc(page,size);
    }
    //dynamic sorting(ie asking from user which filed to sort)
    @GetMapping("/reports/sort/{field}")
    public List<DataQualityReport> getReportsSortedByField(@PathVariable String field)
    {
        return dataQualityService.getReportsSortedByField(field);
    }

    @GetMapping("/reports/sort/{field}/{direction}")
    public List<DataQualityReport> getReportsSortedByFiledAndDirection
            (@PathVariable String field,@PathVariable String direction)
    {
        return dataQualityService.getReportsSortedByFieldAndDirection(field,direction);
    }
}

