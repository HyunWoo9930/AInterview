package com.example.ainterview.domain.oauth2;

public interface OAuth2Response {
    String getProvider();

    String getProviderId();

    String getNickname();

    String getImage();
}