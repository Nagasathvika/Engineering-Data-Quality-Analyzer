package com.sathvika.engineering_data_quality_analyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;

@Service
public class DataQualityService {

    //for logging
    private static final Logger log=LoggerFactory.getLogger(DataQualityService.class);

    private final DataQualityReportRepository repository;

    @Autowired
    public  DataQualityService(DataQualityReportRepository repository)
    {

        this.repository=repository;
    }
    public DataQualityReport analyzeData(EngineeringData engineeringData)
    {
        //logging
        log.info("Starting Data Quality Analysis");

        //log.info("Processing file: {}",engineeringData.getFileName());
        //multiple placeholders({})
        log.info(
                "File {} has {} rows and {} columns",
                engineeringData.getFileName(),
                engineeringData.getTotalRows(),
                engineeringData.getDuplicateRows()
        );


        String validationResult= validate(engineeringData);
        if(!validationResult.equals("VALID"))
        {
            //logging
            log.warn("Validation failed: {}",validationResult);

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

            log.info("Validation failed report saved to database.");

            return report;
        }
        double score=calculateQualityScore(engineeringData);
        log.info("Calculated Quality Score: {}",score);

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
        log.info("Saving report to database.");
        repository.save(report);
        log.info("Report saved to database");

        return report;

    }

    public String validate(EngineeringData engineeringData)
    {

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
        //offset in sql= page*size that is how many rows to skip
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
        return repository.findAll(Sort.by(field));
    }
    //dynamic sort for field and direction
    public List<DataQualityReport> getReportsSortedByFieldAndDirection(String field,String direction)
    {
        if(direction.equalsIgnoreCase("desc"))
        {
            return repository.findAll(Sort.by(field).descending());
        }
        return repository.findAll(Sort.by(field).ascending());
    }
    //dynamic pagination and dynamic sorting
    public Page<DataQualityReport>  getReportsSortedByPageAndSort(int page,int size,String field,String direction)
    {
        Sort sort;
        if(direction.equalsIgnoreCase("desc"))
        {
            sort=Sort.by(field).descending();
        }
        else
        {
           sort=Sort.by(field).ascending();
        }

        return repository.findAll(PageRequest.of(page,size,sort));
    }
    public Page<DataQualityReport> getReportsByPageAndSort(int page,int size,String field,String direction)
    {
        Sort sort;

        if(direction.equalsIgnoreCase("desc"))
        {
            sort=Sort.by(field).descending();
        }
        else
        {
            sort=Sort.by(field).ascending();
        }
        return repository.findAll(PageRequest.of(page,size,sort));
    }

    public List<DataQualityReport> getReportsSortedByTwoFields(String field1,String direction1,
                                                               String field2 ,String direction2)
    {
        Sort sort1;
        if(direction1.equalsIgnoreCase("desc"))
        {
            sort1=Sort.by(field1).descending();
        }
        else
        {
            sort1=Sort.by(field1).ascending();
        }

        Sort sort2;

        if(direction2.equalsIgnoreCase("desc"))
        {
            sort2=Sort.by(field2).descending();
        }
        else
        {
            sort2=Sort.by(field2).ascending();
        }
        return repository.findAll(sort1.and(sort2));

    }
    public Page<DataQualityReport> getReportsByPageAndTwoFieldSort(int page,int size,String field1,String direction1,
                                                                   String field2 ,String direction2)
    {
        Sort sort1;
        if(direction1.equalsIgnoreCase("desc"))
        {
            sort1=Sort.by(field1).descending();
        }
        else
        {
            sort1=Sort.by(field1).ascending();
        }

        Sort sort2;

        if(direction2.equalsIgnoreCase("desc"))
        {
            sort2=Sort.by(field2).descending();
        }
        else
        {
            sort2=Sort.by(field2).ascending();
        }
        return repository.findAll(PageRequest.of(page,size,sort1.and(sort2)));

    }
    public Page<DataQualityReport> getReportsByGradeWithPaginationAndSorting(String grade,int page,int size, String field,String direction)
    {
        Sort sort;
        if(direction.equalsIgnoreCase("desc"))
        {
            sort=Sort.by(field).descending();
        }
        else
        {
            sort=Sort.by(field).ascending();
        }
        Pageable pageable=PageRequest.of(page,size,sort);

        return repository.findByGrade(grade,pageable);
    }
    public DataQualityReportDTO getReportDTOById(Long id)
    {
        DataQualityReport report=repository.findById(id).orElseThrow(()->
                                                         new ReportNotFoundException("No report found with id"+ id));

        /*DataQualityReportDTO dto=new DataQualityReportDTO();
        dto.setStatus(report.getStatus());
        dto.setMessage(report.getMessage());
        dto.setQualityScore(report.getQualityScore());
        dto.setGrade(report.getGrade());
        dto.setRecommendation(report.getRecommendation());

        return dto;*/


        return new DataQualityReportDTO(report);
    }

    public List<DataQualityReportDTO> getAllReportsDTOs()
    {
        List<DataQualityReport> reports=repository.findAll();
        List<DataQualityReportDTO> dtoList=new ArrayList<>();
        for(int i=0;i< reports.size();i++)
        {
            DataQualityReport report= reports.get(i);
            /* why new keyword used?
            Because DataQualityReportDTO is a class, not a method.
            To create an object, Java requires the new keyword.

            new DataQualityReportDTO-->it creates an Entity, not a DTO.

             */
            dtoList.add(new DataQualityReportDTO(report));

            // DataQualityReportDTO dto=new DataQualityReportDTO(report);

            //System.out.println(dto);
            //System.out.println(dto.getMessage());

            //dtoList.add(dto);

        }

        return dtoList;
    }
}
