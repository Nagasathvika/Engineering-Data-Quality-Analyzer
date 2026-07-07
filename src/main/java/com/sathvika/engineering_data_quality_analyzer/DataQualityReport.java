package com.sathvika.engineering_data_quality_analyzer;
import jakarta.persistence.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name="data_quality_report")
public class DataQualityReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*  @Parameter → Documents method parameters (@PathVariable, @RequestParam)
       @Schema → Documents class fields (model/DTO/entity) */

    @Schema(
            description = "Status of the quality analysis",
            example = "SUCCESS"
           )
    private String status;

    @Schema(
            description = "Detailed analysis result",
            example ="Data Quality Analysis completed successfully"
          )
    private String message;

    @Schema(
            description = "Overall quality score",
            example = "92.5"
           )
    private Double qualityScore;

    @Schema(
            description = "Overall quality grade",
            example = "EXCELLENT"
          )
    private String grade;
    private Double completeness;

    @Schema(
            description = "Recommendation generated after analysis",
            example = "Data quality is EXCELLENT"
           )
    private String recommendation;
    private Double uniqueness;
    private Double consistency;
    private String analysisTime;


    public DataQualityReport() {
    }

    public DataQualityReport(String status, String message,Double qualityScore,
                             String grade,Double completeness,String recommendation,
                             Double uniqueness,Double consistency,String analysisTime) {

        this.status = status;
        this.message = message;
        this.qualityScore= qualityScore;
        this.grade= grade;
        this.completeness=completeness;
        this.recommendation=recommendation;
        this.uniqueness=uniqueness;
        this.consistency=consistency;
        this.analysisTime=analysisTime;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;

    }
    public void setStatus(String status)
    {
        this.status=status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public Double getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(double qualityScore) {
        this.qualityScore = qualityScore;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Double getCompleteness()
    {
        return completeness;
    }

    public void setCompleteness(double completeness) {
        this.completeness = completeness;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public Double getUniqueness()
    {
        return uniqueness;
    }

    public void setUniqueness(double uniqueness) {
        this.uniqueness = uniqueness;
    }

    public Double getConsistency()
    {
        return consistency;
    }

    public void setConsistency(double consistency) {
        this.consistency = consistency;
    }

    public String getAnalysisTime() {
        return analysisTime;
    }

    public void setAnalysisTime(String analysisTime) {
        this.analysisTime = analysisTime;
    }
}