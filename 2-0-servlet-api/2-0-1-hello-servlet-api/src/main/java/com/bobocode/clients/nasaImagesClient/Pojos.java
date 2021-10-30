package com.bobocode.clients.nasaImagesClient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

public class Pojos {

    @Getter
    @AllArgsConstructor
    static class BoboCodeRequest {
        private final Picture picture;
        private final User user;
    }

    @Getter
    @AllArgsConstructor
    static class User {
        private final String firstName;
        private final String lastName;
    }

    @Getter
    @AllArgsConstructor
    static class Picture {
        private final String url;
        private final Integer size;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    static class NasaImages {
        @JsonProperty("photos")
        List<NasaImageInfo> nasaImageInfos;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @ToString
    static class NasaImageInfo {
        @JsonProperty("img_src")
        String imgSrc;
    }
}
