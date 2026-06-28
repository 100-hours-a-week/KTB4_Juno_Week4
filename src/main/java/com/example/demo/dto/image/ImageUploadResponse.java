package com.example.demo.dto.image;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageUploadResponse {

    @JsonProperty("image_url")
    private String imageUrl;

    public ImageUploadResponse(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}