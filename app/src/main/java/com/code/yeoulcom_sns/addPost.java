package com.code.yeoulcom_sns;

public class addPost {
    //파이어베이스에 게시물 업데이트를 위한 자바
    String name;
    String generation;
    String title;
    String main_text;

    public addPost(){}

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


    //값을 추가할 때 쓸 함수
    public addPost(String name, String generation,String title, String main_text){
        this.name = name;
        this.generation = generation;
        this.title = title;
        this.main_text = main_text;
    }
}
