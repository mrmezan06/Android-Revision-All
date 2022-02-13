package com.mezan.app5;


public class Model {
    String imageUrl,imageSize,id, fileName;

    public Model(){

    }

    public Model(String imageUrl, String imageSize, String id, String fileName){
        this.imageUrl = imageUrl;
        this.id = id;
        this.fileName = fileName;
        this.imageSize = imageSize;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageSize() {
        return imageSize;
    }
    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }
}
