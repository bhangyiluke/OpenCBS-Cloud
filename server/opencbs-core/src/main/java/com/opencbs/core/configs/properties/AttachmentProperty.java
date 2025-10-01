package com.opencbs.core.configs.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotBlank;

import java.nio.file.Path;
import java.nio.file.Paths;

@ConfigurationProperties(ignoreUnknownFields = false, prefix = "attachment")
public class AttachmentProperty {
    @NotBlank(message = "Location for the attachments is not configured.")
    private String location;

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public Path getPath(){
        return Paths.get(location);
    }
}
