package com.sathvika.engineering_data_quality_analyzer;

public class ApiResponse<T>
{
    private boolean success;
    private String message;
    private T data;


    public ApiResponse(boolean success,String message,T data)
    {
        this.success=success;
        this.message=message;
        this.data=data;
    }

    public boolean getSuccess()
    {
        return success;
    }

    public String getMessage() {
        return message;
    }
    public T getData()
    {
        return data;
    }
}
