package com.code.yeoulcom_sns;

public class addPostSaveImg {
    //포스트 저장공간에 올릴 정보 + 이미지
        String name;
        String generation;
        String title;
        String main_text;
        private String imgURL;

//    String Time;

        public addPostSaveImg() {
        }

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMain_text() {
            return main_text;
        }

        public void setMain_text(String main_text) {
            this.main_text = main_text;
        }


        public String getImgURL() {
            return imgURL;
        }

        public void setImgURL(String imgURL) {
            this.imgURL = imgURL;
        }

//    public String getTime(){
//        return Time;
//    }
//    public void setTime(String Time){
//        this.Time = Time;
//    }

        //이미지를 포함한 값을 추가할 때 쓸 함수]
        public addPostSaveImg(String name, String generation, String title, String main_text,String imgURL) {
            this.name = name;
            this.generation = generation;
            this.title = title;
            this.main_text = main_text;
            this.imgURL = imgURL;
        }

}
