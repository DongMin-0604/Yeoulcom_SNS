package com.code.yeoulcom_sns;

public class addPostSave {
    //포스트 저장공간에 올릴 정보
    String name;
    String generation;
    String title;
    String main_text;
    String Time;

    public addPostSave(){}

    //파이어베이스에 여러 값을 넣기 위한 getter setter

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
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

    public String getMain_text(){
        return main_text;
    }
    public void setMain_text(String main_text){
        this.main_text = main_text;
    }

    public String getTime(){
        return Time;
    }
    public void setTime(String Time){
        this.Time = Time;
    }


    //값을 추가할 때 쓸 함수
    public addPostSave(String name, String generation, String title, String main_text, String Time){
        this.name = name;
        this.generation = generation;
        this.title = title;
        this.main_text = main_text;
        this.Time = Time;
    }
}
