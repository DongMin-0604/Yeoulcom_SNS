package com.code.yeoulcom_sns;

public class addUser{
    //파이어베이스에 유저 정보 업데이트를 위한 자바
    String name;
    String generation;
    boolean adminCheck;


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

    public boolean isAdminCheck() {
        return adminCheck;
    }

    public void setAdminCheck(boolean adminCheck) {
        this.adminCheck = adminCheck;
    }

    //값을 추가할 때 쓸 함수
   public addUser(String name, String generation, boolean adminCheck){
        this.name = name;
        this.generation = generation;
        this.adminCheck  = adminCheck;
    }
}
