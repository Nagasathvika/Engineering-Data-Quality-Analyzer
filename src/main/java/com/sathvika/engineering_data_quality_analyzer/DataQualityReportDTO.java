package com.sathvika.engineering_data_quality_analyzer;

public class DataQualityReportDTO
{
    private String status;
    private String message;
    private Double qualityScore;
    private String grade;
    private String recommendation;

    public DataQualityReportDTO()
    {

    }
    public DataQualityReportDTO(DataQualityReport report)
    {
        this.status=report.getStatus();
        this.message=report.getMessage();
        this.qualityScore=report.getQualityScore();
        this.grade=report.getGrade();
        this.recommendation=report.getRecommendation();
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Double getQualityScore() {
        return qualityScore;
    }

    public String getGrade() {
        return grade;
    }

    public String getRecommendation() {
        return recommendation;
    }


    // Q: Why do we use DTO instead of returning Entity?
    //
    //A good answer:
    //
    //"DTO (Data Transfer Object) is used to transfer only the required data between the server and the client.
    // It helps hide unnecessary fields, improves security,
    // reduces response size, and keeps the API independent of the database entity."

}
