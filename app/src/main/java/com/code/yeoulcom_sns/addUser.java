package com.code.yeoulcom_sns;

public class addUser{
    //파이어베이스에 유저 정보 업데이트를 위한 자바
    String name;
    String generation;

    public addUser(){}

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

    //값을 추가할 때 쓸 함수
   public addUser(String name, String generation){
        this.name = name;
        this.generation = generation;
    }


}