package com.sathvika.engineering_data_quality_analyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Service
public class DataQualityService {

    private final DataQualityReportRepository repository;

    @Autowired
    public  DataQualityService(DataQualityReportRepository repository)
    {

        this.repository=repository;
    }
    public DataQualityReport analyzeData(EngineeringData engineeringData)
    {
        String validationResult= validate(engineeringData);
        if(!validationResult.equals("VALID"))
        {
            DataQualityReport report= new DataQualityReport(

                    "FAILED",
                    validationResult,
                    0.0,
                    "N/A",
                    0.0,
                    "Fix validation errors",
                    0.0,
                    0.0,
                    "N/A"
            );
            repository.save(report);
            return report;
        }
        double score=calculateQualityScore(engineeringData);

        double completeness=calculateCompleteness(engineeringData);

        double uniqueness= calculateUniqueness(engineeringData);

        double consistency=calculateConsistency(engineeringData);

        String grade= getGrade(score);


        String recommendation= getRecommendation(score);

        String analysisTime=java.time.LocalDateTime.now().toString();

        DataQualityReport report=  new DataQualityReport(
                "SUCCESS",
                "File accepted",
                score,
                grade,
                completeness,
                recommendation,
                uniqueness,
                consistency,
                analysisTime
        );
        repository.save(report);
        return report;

    }

    public String validate(EngineeringData engineeringData)
    {
        /*
        if(engineeringData.getFileName()==null || engineeringData.getFileName().isBlank())
        {
            return "Invalid File:fileName cannot be empty";

        }
        if(engineeringData.getTotalRows()<=0)
        {
            return "Invalid file: totalRows must be greater than 0";
        }
        if(engineeringData.getNullValues()<0)
        {
            return "Invalid File: nullValues cannot be negative";
        }
        if(engineeringData.getDuplicateRows()<0)
        {
            return "Invalid File: duplicateRows cannot be negative";
        }
        */
        if(engineeringData.getNullValues()> engineeringData.getTotalRows())
        {
            return "Invalid File: nullValues cannot exceed totalRows";
        }

        if(engineeringData.getDuplicateRows()> engineeringData.getTotalRows())
        {
            return "Invalid File: duplicateRows cannot exceed totalRows";

        }
        return "VALID";

    }
    public double calculateQualityScore(EngineeringData engineeringData)
    {
        int totalRows= engineeringData.getTotalRows();
        int issues=engineeringData.getNullValues()+ engineeringData.getDuplicateRows();

        double score=((double)(totalRows-issues)/totalRows)*100;
        return score;

    }
    public String getGrade(double score)
    {
        if(score>=90)
        {
            return "EXCELLENT";
        }
        if(score>=70)
        {
            return "GOOD";
        }
        if(score>=50)
        {
            return "AVERAGE";
        }
        return "POOR";
    }
    public double calculateCompleteness(EngineeringData engineeringData)
    {
        int totalRows= engineeringData.getTotalRows();
        int nullValues= engineeringData.getNullValues();

        int completeRows= totalRows-nullValues;

        double completeness=((double) completeRows/totalRows)*100;

        return completeness;
    }

    public String getRecommendation(double score)
    {
        if(score>=90)
        {
            return "Data quality is EXCELLENT";
        }
        if(score>=70)
        {
            return "Minor improvements recommended";
        }
        if(score>=50)
        {
            return "Reduce null values and duplicates";
        }
        return "Immediate data cleanup required";
    }
    public double calculateUniqueness(EngineeringData engineeringData)
    {
        int totalRows= engineeringData.getTotalRows();
        int duplicateRows= engineeringData.getDuplicateRows();
        int uniqueRows=totalRows-duplicateRows;

        double uniqueness=((double) uniqueRows/totalRows)*100;
        return uniqueness;
    }
    public double calculateConsistency(EngineeringData engineeringData)
    {
        int totalRows= engineeringData.getTotalRows();
        int totalIssues=engineeringData.getNullValues() + engineeringData.getDuplicateRows();

        double issuePercentage= ((double) totalIssues/totalRows)*100;
        double consistency=100-issuePercentage;

        return consistency;
    }
    //CRUD operations
    public List<DataQualityReport> getAllReports()
    {
        return repository.findAll();
    }
    public DataQualityReport getReportById(Long id)
    {
        return repository.findById(id)
                .orElseThrow(()->
                        new ReportNotFoundException(
                                "No record found with id "+ id));

    }
    public String deleteReport(Long id)
    {
        repository.deleteById(id);
        return "Report Deleted Successfully";
    }
    public DataQualityReport updateReport(Long id, DataQualityReport updatedReport)
    {
        DataQualityReport existingReport = repository.findById(id).orElse(null);

        if(existingReport == null)
        {
            return null;
        }

        existingReport.setStatus(updatedReport.getStatus());

        existingReport.setMessage(updatedReport.getMessage());

        return repository.save(existingReport);
    }
    //Database fetching
    public List<DataQualityReport> getReportsByGrade(String grade)
    {
        return repository.findByGrade(grade);
    }

    public List<DataQualityReport> getReportsByStatus(String status)
    {
        return repository.findByStatus(status);
    }

    public List<DataQualityReport> getReportsByGradeAndStatus(String grade,String status)
    {
        return repository.findByGradeAndStatus(grade, status);
    }
    public List<DataQualityReport> getReportsByQualityScoreGreaterThan(double score)
    {
        return repository.findByQualityScoreGreaterThan(score);
    }
    public List<DataQualityReport> getReportsByQualityScoreBetween(double minScore,double maxScore)
    {
        return repository.findByQualityScoreBetween(minScore,maxScore);
    }
    //sorting
    public List<DataQualityReport> getReportsSortedByQualityScoreDescending()
    {
        return repository.findAll(Sort.by("qualityScore").descending());
    }
    public List<DataQualityReport> getReportsSortedByQualityScoreAscending()
    {
        return repository.findAll(Sort.by("qualityScore").ascending());
    }
    public List<DataQualityReport> getReportsSortedByGrade()
    {
        //by default sorted by ascending
        return repository.findAll(Sort.by("grade"));
    }
    public List<DataQualityReport> getReportsSortedByStatus()
    {
        //by default sorted by ascending
        return repository.findAll(Sort.by("status"));
    }
    //mutiple sort
    public List<DataQualityReport> getReportsSortedByGradeAndStatus()
    {
        //first priority for grade and then status
        return repository.findAll(Sort.by("grade").ascending()
                .and(Sort.by("status").descending()));
    }
    //pagination
    //here Page is an interface
    public Page<DataQualityReport>  getReportsByPage(int page,int size)
    {
        //offset in sql=page*size that is how many rows to skip
        //limit=size
        return repository.findAll(PageRequest.of(page,size));
    }
    public Page<DataQualityReport> getReportsByPageSortedByQualityScoreDesc(int page,int size)
    {
        return repository.findAll(PageRequest.of(page,size,Sort.by("qualityScore").descending()));
    }
    //dynamic sorting
    public List<DataQualityReport> getReportsSortedByField(String field)
    {
        return repository.findAll(Sort.by("field"));
    }









































}
