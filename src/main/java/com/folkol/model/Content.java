package com.folkol.model;

import java.util.HashMap;
import java.util.Map;

public class Content {

    String id;
    String description;

    Map<String, Map<String, Object>> parts = new HashMap<>();

    public Content(String id, String description, Map<String, Map<String, Object>> parts) {
        setId(id);
        setDescription(description);
        setParts(parts);
    }

    public Content() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Map<String, Object>> getParts() {
        return parts;
    }

    public void setParts(Map<String, Map<String, Object>> parts) {
        this.parts = parts;
    }
}
