package com.sathvika.engineering_data_quality_analyzer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import java.util.Optional;

import org.springframework.http.ResponseEntity;


import javax.xml.crypto.Data;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;

//because of name conflict
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

//csv upload
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.util.ArrayList;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/*  @Parameter → Documents method parameters (@PathVariable, @RequestParam)
    @Schema → Documents class fields (model/DTO/entity) */

//swagger annotation
@Tag(
        name="Engineering Data Quality APIs",
        description = "APIs for managing Engineering Data Quality Reports"
)
@RestController
public class DataQualityController {

    private static final Logger log=LoggerFactory.getLogger(DataQualityController.class);

    @Autowired
    private DataQualityService dataQualityService;

    @GetMapping("/quality")
    public String checkQuality() {

        return "Data Quality Analysis Started";
    }
    //without response entity
    /*@PostMapping("/engineering-data")
    public DataQualityReport createEngineeringData(@Valid @RequestBody EngineeringData engineeringData) {
        return dataQualityService.analyzeData(engineeringData);
    }
    */
    //with response entity
    /*@PostMapping("/engineering-data")
    public ResponseEntity<DataQualityReport> createEngineeringData
            (@Valid @RequestBody EngineeringData engineeringData)
    {
        DataQualityReport report=dataQualityService.analyzeData(engineeringData);
        return ResponseEntity.status(201).body(report);
    }
    */

    //ApiResponse
    @PostMapping("/engineering-data")
    public ResponseEntity<ApiResponse<DataQualityReport>> createEngineeringData
    (@Valid @RequestBody EngineeringData engineeringData) {
        DataQualityReport report = dataQualityService.analyzeData(engineeringData);

        ApiResponse<DataQualityReport> response = new ApiResponse<>
                (true, "Report created succesfully", report);

        return ResponseEntity.status(201).body(response);
        //ResponseEntity.ok() = shortcut for ResponseEntity.status(200).body(...).
        /*ResponseEntity.status(201).body(...) = returns 201 Created,
         which is the correct REST response for a successful POST that creates a new resource.*/
        //200 OK means:Your request was successful.
        //201 Created means: Your request was successful, and I created a new resource.
    }

    //CRUD operations
    //without response entity
    /*
    @GetMapping("/reports")
    public List<DataQualityReport> getAllReports() {

        return dataQualityService.getAllReports();
    }
    */
    //with response entity
    /*@GetMapping("/reports")
    public ResponseEntity<List<DataQualityReport>> getAllReports()
    {
        List<DataQualityReport> reports=dataQualityService.getAllReports();

        return ResponseEntity.ok(reports);
    }*/
    //Api response
    @GetMapping("/reports")
    public ResponseEntity<ApiResponse<List<DataQualityReport>>> getAllReports() {
        List<DataQualityReport> reports = dataQualityService.getAllReports();

        ApiResponse<List<DataQualityReport>> response = new ApiResponse<>
                (true, "Reports fetched successfully", reports);

        return ResponseEntity.ok(response);
    }

    //without response entity
    /*@GetMapping("/reports/{id}")
    public DataQualityReport getReportById(
            @PathVariable Long id) {
        return dataQualityService.getReportById(id);
    }
    */
    //with response entity
    /*@GetMapping("/reports/{id}")
    public ResponseEntity<DataQualityReport> getReportById(@PathVariable Long id)
    {

        DataQualityReport report = dataQualityService.getReportById(id);

        return ResponseEntity.ok(report);
        /* What is ResponseEntity.ok(report)?

       This single line means
       Status = 200 OK
       Body = report

       It is equivalent to writing:

         return ResponseEntity.status(200).body(report);

         ok() is simply a shortcut.

    }

     */


    /*ResponseEntity

    ResponseEntity is a Spring Boot class used to return the complete HTTP response to the client.
     It allows us to control:

    Response Body (the data)
    HTTP Status Code (200, 201, 404, etc.)
    HTTP Headers (optional)

    It is commonly used in REST APIs because it gives full control over the response.*/

    // swagger annotation
    @Operation(
            summary = "Get Report by ID",
            description = "Fetches a Data Quality Report using its unique ID."
    )
    @ApiResponses(
            {
                    //@ApiResponse- replacing it because of name conflict
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Report fetched successfully"
                    ),
                    //@ApiResponse- replacing it because of name conflict
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Report not found"
                    )

            }
    )
    //ApiResponse

    @GetMapping("/reports/{id}")
    public ResponseEntity<ApiResponse<DataQualityReport>> getReportById(/* for swagger parameter explanation*/
            @Parameter(
                    description = "Unique ID of the Data Quality Report",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {
        DataQualityReport report = dataQualityService.getReportById(id);
       /*ApiResponse<DataQualityReport> response=new ApiResponse<>();

    response.setSuccess(true);
    response.setMessage("Report fetched successfully");
    response.setData(report);*/
        // (or)can be written like this
        ApiResponse<DataQualityReport> response = new ApiResponse<>
                (true, "report fetched successfully", report);

        return ResponseEntity.ok(response);
    }
    //Why do we use ApiResponse?

    //We use ApiResponse to provide a consistent structure for all API responses.
    /* Instead of every endpoint returning data in a different format,
          each response includes common fields such as success, message, and data.
          This makes APIs easier for frontend applications to consume and simplifies future changes
          because common fields can be added or modified in one place.
     */


    /*@DeleteMapping("/reports/{id}")
    public String deleteReport(
            @PathVariable Long id) {
        return dataQualityService.deleteReport(id);
    }*/

    //with response entity
    @DeleteMapping("/reports/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        dataQualityService.deleteReport(id);

        return ResponseEntity.noContent().build();
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
    // without Response Entity
    /*@PutMapping("/reports/{id}")
    public DataQualityReport updateReport(
            @PathVariable Long id,
            @RequestBody DataQualityReport report) {
        return dataQualityService
                .updateReport(id, report);
    }
    */
    // with Response Entity
    /*@PutMapping("/reports/{id}")
    public ResponseEntity<DataQualityReport> updateReport
            (@PathVariable Long id, @RequestBody DataQualityReport report)
    {
        DataQualityReport updatedReport=dataQualityService.updateReport(id,report);
        return ResponseEntity.ok(updatedReport);
    }*/
    //ApiResponse
    @PutMapping("/reports/{id}")
    public ResponseEntity<ApiResponse<DataQualityReport>> updatedReport
    (@PathVariable Long id, @RequestBody DataQualityReport report) {
        DataQualityReport updatedReport = dataQualityService.updateReport(id, report);

        ApiResponse<DataQualityReport> response = new ApiResponse<>
                (true, "Report updated successfully", updatedReport);

        return ResponseEntity.ok(response);
    }


    // Database fetching
    @GetMapping("/reports/grade/{grade}")
    public List<DataQualityReport> getReportsByGrade(@PathVariable String grade) {
        return dataQualityService.getReportsByGrade(grade);
    }

    @GetMapping("/reports/status/{status}")
    public List<DataQualityReport> getReportsByStatus(@PathVariable String status) {
        return dataQualityService.getReportsByStatus(status);
    }

    @GetMapping("/reports/search/{grade}/{status}")
    public List<DataQualityReport> getReportsByGradeAndStatus(@PathVariable String grade, @PathVariable String status) {
        return dataQualityService.getReportsByGradeAndStatus(grade, status);
    }

    @GetMapping("/reports/score/{score}")
    public List<DataQualityReport> getReportsByQualityScoreGreaterThan(@PathVariable double score) {
        return dataQualityService.getReportsByQualityScoreGreaterThan(score);
    }

    @GetMapping("/reports/score/{minScore}/{maxScore}")
    public List<DataQualityReport> getReportsByQualityScoreBetween(@PathVariable double minScore
            , @PathVariable double maxScore) {
        return dataQualityService.getReportsByQualityScoreBetween(minScore, maxScore);
    }

    @GetMapping("/reports/sort/score/desc")
    public List<DataQualityReport> getReportsSortedByQualityScoreDescending() {
        return dataQualityService.getReportsSortedByQualityScoreDescending();
    }

    @GetMapping("/reports/sort/score/asc")
    public List<DataQualityReport> getReportsSortedByQualityScoreAscending() {
        return dataQualityService.getReportsSortedByQualityScoreAscending();
    }

    @GetMapping("/reports/sort/grade")
    public List<DataQualityReport> getReportsSortedByGrade() {
        return dataQualityService.getReportsSortedByGrade();
    }

    @GetMapping("/reports/sort/status")
    public List<DataQualityReport> getReportsSortedByStatus() {
        return dataQualityService.getReportsSortedByStatus();
    }

    //multiple sort
    @GetMapping("/reports/sort/grade-status")
    public List<DataQualityReport> getReportsSortedByGradeAndStatus() {
        return dataQualityService.getReportsSortedByGradeAndStatus();
    }

    //pagination
    @GetMapping("/reports/page/{page}/{size}")
    public Page<DataQualityReport> getReportsByPage(@PathVariable int page, @PathVariable int size) {
        return dataQualityService.getReportsByPage(page, size);
    }

    @GetMapping("/reports/page/{page}/{size}/sort")
    public Page<DataQualityReport> getReportsByPageSortedByQualityScoreDesc
            (@PathVariable int page, @PathVariable int size) {
        return dataQualityService.getReportsByPageSortedByQualityScoreDesc(page, size);
    }

    //dynamic sorting(ie asking from user which filed to sort)
    @GetMapping("/reports/sort/{field}")
    public List<DataQualityReport> getReportsSortedByField(@PathVariable String field) {
        return dataQualityService.getReportsSortedByField(field);
    }

    @GetMapping("/reports/sort/{field}/{direction}")
    public List<DataQualityReport> getReportsSortedByFiledAndDirection
            (@PathVariable String field, @PathVariable String direction) {
        return dataQualityService.getReportsSortedByFieldAndDirection(field, direction);
    }

    @GetMapping("/reports/page/{page}/{size}/{field}/{direction}")
    public Page<DataQualityReport> getReportsByPageAndSort(@PathVariable int page, @PathVariable int size,
                                                           @PathVariable String field, @PathVariable String direction) {
        return dataQualityService.getReportsByPageAndSort(page, size, field, direction);
    }

    @GetMapping("/reports/sort/{field1}/{direction1}/{field2}/{direction2}")
    public List<DataQualityReport> getReportSortedByTwoFields(@PathVariable String field1, @PathVariable String direction1,
                                                              @PathVariable String field2, @PathVariable String direction2) {
        return dataQualityService.getReportsSortedByTwoFields(field1, direction1, field2, direction2);
    }

    @GetMapping("/reports/page/{page}/{size}/{field1}/{direction1}/{field2}/{direction2}")
    public Page<DataQualityReport> getReportsByPageAndTwoFieldSort(@PathVariable int page, @PathVariable int size,
                                                                   @PathVariable String field1, @PathVariable String direction1,
                                                                   @PathVariable String field2, @PathVariable String direction2) {
        return dataQualityService.getReportsByPageAndTwoFieldSort(page, size, field1, direction1, field2, direction2);
    }

    @GetMapping("/reports/search/{grade}/page/{page}/{size}/{field}/{direction}")
    public Page<DataQualityReport> getReportsByGradeWithPaginationAndSorting(
            @PathVariable String grade, @PathVariable int page, @PathVariable int size,
            @PathVariable String field, @PathVariable String direction) {
        return dataQualityService.getReportsByGradeWithPaginationAndSorting(grade, page, size, field, direction);
    }


    @GetMapping("/reports/dto/{id}")
    public DataQualityReportDTO getReportDTOById(@PathVariable Long id) {
        return dataQualityService.getReportDTOById(id);
    }

    @GetMapping("/reports/dto")
    public List<DataQualityReportDTO> getAllReportsDTOs() {
        return dataQualityService.getAllReportsDTOs();
    }

    //csv upload
    /*
    @PostMapping(value="/upload",consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestParam("file" ) MultipartFile file)
    {
         @RequestBody is used to receive structured request data such as JSON or XML and convert it into a Java object.
          MultipartFile is used to receive uploaded files such as CSVs, PDFs, or images in a multipart/form-data request.

        return "File received: "+file.getOriginalFilename();

    }
    */
    //using file reader
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<DataQualityReport>/*String*/ uploadFile(@RequestParam("file") MultipartFile file) throws Exception
    {
        log.info("CSV upload started;");

        log.info("Uploaded file size: {} bytes",file.getSize());

        if(file.isEmpty())
        {
            log.warn("Uploaded file is empty");
            throw new RuntimeException("CSV file is empty");
        }
        //BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        //CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build().parse(reader);
        /*
        A try-with-resources statement works with objects that implement the AutoCloseable interface.
        The Java compiler automatically generates code that calls the close() method in a hidden finally block.
        This ensures that resources are closed whether the code completes normally or an exception occurs.
        try
        {
        // your code
        }
        finally
        {
            if(reader != null)
            {
                reader.close() or parser.close();
            }
         }
         You don't see this.
         The compiler generates it automatically.*/

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build().parse(reader))
        {
            List<DataQualityReport> reports=new ArrayList<>();
            for (CSVRecord record : parser)
            {
                log.info("Processing record {}",record.getRecordNumber());
                try
                {
                    EngineeringData engineeringData=new EngineeringData();
                    engineeringData.setFileName(record.get("fileName"));
                    engineeringData.setTotalRows(Integer.parseInt(record.get("totalRows")));
                    engineeringData.setNullValues(Integer.parseInt(record.get("nullValues")));
                    engineeringData.setDuplicateRows(Integer.parseInt(record.get("duplicateRows")));

                    //System.out.println(record);
                    // System.out.println(engineeringData);
                    DataQualityReport report=dataQualityService.analyzeData(engineeringData);

                    reports.add(report);
                }
                catch (NumberFormatException e)
                {
                    log.error("Invalid number in CSV at record {}",record.getRecordNumber());
                    throw new RuntimeException("Invalid numeric value at record"+record.getRecordNumber());
                }
                catch (IllegalArgumentException e)
                {
                    log.error("Invalid CSV header."+ e);
                    throw new RuntimeException("Invalid CSV header. Expected: fileName,totalRows,nullValues,duplicateRows ",e);
                }
            }
            if(reports.isEmpty())
            {
                log.warn("CSV contains no records.");
                throw new RuntimeException("CSV contains no records.");
            }

            //return "CSV Read Successfully";
            log.info("CSV upload completed. {} reports generated.",reports.size());
            return reports;
            /* Why didn't file.isEmpty() work?

            "file.isEmpty()" checks whether the uploaded file has zero bytes.
            My test file contained 2 bytes because it had a newline character, so it wasn't considered empty */
        }

    }

    /* file.getInputStream():        ->The uploaded file is stored in memory.
                                     ->This method gives us a stream to read it.
                                     ->Think of it as opening the file.
        InputStreamReader:           ->Converts bytes into readable characters.
        BufferedReader:              ->Reads the file efficiently, line by line.
        CSVParser:                   ->Reads the CSV format.
        setHeader():                 ->Tells the parser:
                                     ->First row contains column names.
                                     ->So it treats:  fileName,totalRows,nullValues,duplicateRows as headers, not data.
        setSkipHeaderRecord(true):   ->Skips the header row while iterating.
                                     ->So the loop starts from: EngineData.csv,100,10,5
       for (CSVRecord record : parser)->This means:
                                      Read Row 1
                                      ↓
                                      Read Row 2
                                      ↓
                                      Read Row 3
                                      One by one.



      System.out.println(record);     For now, we're simply printing each row.
                                       Expected console output:
                                       CSVRecord [comment='null', recordNumber=1, values=[EngineData.csv, 100, 10, 5]]

*/
}

