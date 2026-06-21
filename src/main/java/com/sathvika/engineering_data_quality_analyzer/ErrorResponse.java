package com.sathvika.engineering_data_quality_analyzer;

public class ErrorResponse
{
    private String timeStamp;
    private int status;
    private String message;

    public ErrorResponse()
    {

    }
    public  ErrorResponse(String timeStamp,int status,String message)
    {
        this.timeStamp=timeStamp;
        this.status=status;
        this.message=message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
