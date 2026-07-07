package com.sathvika.engineering_data_quality_analyzer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DataQualityReportRepository extends JpaRepository<DataQualityReport,Long>
{
    List<DataQualityReport> findByGrade(String grade);

    

    List<DataQualityReport> findByStatus(String status);

    List<DataQualityReport> findByGradeAndStatus(String grade,String status);

    List<DataQualityReport> findByQualityScoreGreaterThan(double score);

    List<DataQualityReport> findByQualityScoreBetween(double minScore,double maxScore);

    Page<DataQualityReport> findByGrade(String grade,Pageable pageable);

}


