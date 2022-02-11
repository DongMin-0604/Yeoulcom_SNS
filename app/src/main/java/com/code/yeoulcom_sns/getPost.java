package com.code.yeoulcom_sns;

public class getPost {
    private String name;
    private String main_text;
    private String generation;
    private String title;
    private String Time;

    public getPost(){}

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getMain_text(){
        return main_text;
    }
    public void setMain_text(String main_text){
        this.main_text = main_text;
    }

    public String getGeneration(){
        return generation;
    }
    public void setGeneration(String generation){
        this.generation = generation;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getTime(){
        return Time;
    }
    public void setTime(String Time){
        this.Time = Time;
    }

    //여러 값을 참조해야 할 때
    public getPost(String name, String main_text,String generation, String title,String Time){
        this.name = name;
        this.main_text = main_text;
        this.generation = generation;
        this.title = title;
        this.Time = Time;
    }
}
