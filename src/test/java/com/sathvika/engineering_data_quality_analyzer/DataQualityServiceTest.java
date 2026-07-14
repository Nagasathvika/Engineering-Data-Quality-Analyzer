package com.sathvika.engineering_data_quality_analyzer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;

@ExtendWith(MockitoExtension.class)
public class DataQualityServiceTest
{
    private EngineeringData engineeringData;

    @BeforeEach
    void setUp()
    {
        engineeringData=new EngineeringData();
        engineeringData.setFileName("sample.csv");
        engineeringData.setTotalRows(100);
    }
    @Mock
    private DataQualityReportRepository repository;

    @InjectMocks
    private  DataQualityService service;

    @Test
    @DisplayName("Should generate SUCCESS report for valid engineering data")
    void testAnalyseData()
    {
        //assertEquals(10,10);

        //EngineeringData engineeringData=new EngineeringData();
       // engineeringData.setFileName("sample.csv");
        //engineeringData.setTotalRows(100);
        engineeringData.setNullValues(10);
        engineeringData.setDuplicateRows(5);

        DataQualityReport expectedReport=new DataQualityReport();
        expectedReport.setStatus("SUCCESS");
        expectedReport.setMessage("File accepted");

        when(repository.save(any(DataQualityReport.class)))
                .thenReturn(expectedReport);

        //call service
        DataQualityReport actualReport=service.analyzeData(engineeringData);

        assertNotNull(actualReport);

        //assert
        assertEquals("SUCCESS",actualReport.getStatus());
        assertEquals("File accepted",actualReport.getMessage());

        //assertEquals("FAILED",actualReport.getStatus());
        //assertEquals("Invalid File: nullValues cannot exceed totalRows",actualReport.getMessage());
        //engineeringData.setNullValues(120);
        // engineeringData.setDuplicateRows(10);

        //assertEquals("FAILED",actualReport.getStatus());
        //assertEquals("Invalid File: duplicateRows cannot exceed totalRows",actualReport.getMessage());
        //engineeringData.setTotalRows(100);
        // engineeringData.setNullValues(20);
        //engineeringData.setDuplicateRows(120);

        assertEquals(90.0,actualReport.getCompleteness());
        assertEquals(95.0,actualReport.getUniqueness());
        assertEquals(85.0,actualReport.getConsistency());

        assertEquals(85.0,actualReport.getQualityScore());
        assertTrue(actualReport.getQualityScore()>80);
        assertFalse(actualReport.getQualityScore()<50);

        assertEquals("GOOD",actualReport.getGrade());
        assertEquals("Minor improvements recommended",actualReport.getRecommendation());

        //verify
        verify(repository,times(1)).save(any(DataQualityReport.class));

    }
    //we'll use Mockito to create a fake repository instead of talking to the real database.
    // This keeps the test focused on your business logic and makes it fast and reliable.

    @Test
    @DisplayName("Should return FAILEd when null values exceed total rows")
    void shouldReturnFailureWhenNullValuesExceedTotalRows()
    {
        engineeringData.setNullValues(120);
        engineeringData.setDuplicateRows(10);

        //call service
        DataQualityReport actualReport=service.analyzeData(engineeringData);



        assertEquals("FAILED",actualReport.getStatus());
        assertEquals("Invalid File: nullValues cannot exceed totalRows",actualReport.getMessage());

        //verify
        verify(repository,times(1)).save(any(DataQualityReport.class));

    }

    @Test
    @DisplayName("Should return FAILED when duplicate rows exceed total rows")
    void shouldReturnFailureWhenDuplicateRowsExceedTotalRows()
    {
         engineeringData.setNullValues(20);
        engineeringData.setDuplicateRows(120);

        //call service
        DataQualityReport actualReport=service.analyzeData(engineeringData);

        assertEquals("FAILED",actualReport.getStatus());
        assertEquals("Invalid File: duplicateRows cannot exceed totalRows",actualReport.getMessage());

        //verify
        verify(repository,times(1)).save(any(DataQualityReport.class));
    }

    @Test
    @DisplayName("Should throw ReportNotFoundException when report is not found")
    void shouldThrowReportNotFoundException()
    {
        when(repository.findById(100L))
                .thenReturn(Optional.empty());
        /*
         Why Optional.empty()?
         Because Spring Data JPA's findById() never returns null.
         It returns:
         Optional.of(report) → report found
         Optional.empty() → report not found

         Mockito should imitate the real method's behavior
         */

        ReportNotFoundException exception=assertThrows(ReportNotFoundException.class,
                ()->service.getReportById(100L));

        assertEquals("No record found with id 100",exception.getMessage());
    }
    @AfterEach
    void tearDown()
    {
        System.out.println("Test finished");
    }

}
