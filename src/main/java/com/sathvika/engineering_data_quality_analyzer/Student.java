package com.sathvika.engineering_data_quality_analyzer;

public class Student {

    private String name;
    private String project;

    public Student()
    {

    }
    public Student(String name,String project)
    {
        this.name=name;
        this.project=project;
    }
    public String getName()
    {
        return name;
    }
    public String getProject()
    {
        return project;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
