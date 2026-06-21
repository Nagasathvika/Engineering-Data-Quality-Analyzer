package com.sathvika.engineering_data_quality_analyzer;
import jakarta.persistence.*;

@Entity
@Table(name="data_quality_report")
public class DataQualityReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private String message;
    private Double qualityScore;
    private String grade;
    private Double completeness;
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