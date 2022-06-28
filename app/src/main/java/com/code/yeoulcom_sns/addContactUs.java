package com.code.yeoulcom_sns;

public class addContactUs {
    //문의에 올릴 정보 + 이미지
    String name;
    String generation;
    String category;
    String main_text;

    //파이어베이스에 여러 값을 넣기 위한 getter setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }


    public void setTime(String Time) {

    }

    //이미지를 포함한 값을 추가할 때 쓸 함수]
    public addContactUs(String name, String generation, String category,String main_text) {
        this.name = name;
        this.generation = generation;
        this.category = category;
        this.main_text = main_text;
    }

}

