package com.sathvika.engineering_data_quality_analyzer;
import jakarta.persistence.*;

import jakarta.persistence.Entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class EngineeringData {


    @NotBlank(message="File cannot be empty")
    private String fileName;

   @Positive(message = "Total Rows must be greater than zero")
    private int totalRows;

   @PositiveOrZero(message= "Null values cannot be negative")
    private int nullValues;

   @PositiveOrZero(message = "Duplicate Rows cannot be negative")
    private int duplicateRows;

    private Long id;

    public String getFileName() {

        return fileName;
    }

    public void setFileName(String fileName) {

        this.fileName = fileName;
    }

    public int getTotalRows() {

        return totalRows;
    }

    public void setTotalRows(int totalRows) {

        this.totalRows = totalRows;
    }

    public int getNullValues() {

        return nullValues;
    }

    public void setNullValues(int nullValues) {

        this.nullValues = nullValues;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public int getDuplicateRows() {

        return duplicateRows;
    }

    public void setDuplicateRows(int duplicateRows) {

        this.duplicateRows = duplicateRows;
    }
}