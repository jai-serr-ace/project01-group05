package com.example.project01_group05.api;

import java.util.Map;

public class MangaAttributes {

    private Map<String, String> title;

    public Map<String, String> getTitle() {
        return title;
    }

    public String getDisplayTitle() {

        if (title == null || title.isEmpty()) {
            return "Unknown Title";
        }

        // Prefer English when available
        if (title.containsKey("en") && title.get("en") != null) {
            return title.get("en");
        }

        // Otherwise use whichever localized title MangaDex provides
        for (String value : title.values()) {
            if (value != null && !value.isEmpty()) {
                return value;
            }
        }

        return "Unknown Title";
    }
}
