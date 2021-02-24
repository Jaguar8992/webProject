package main.api.response;

import main.service.dto.DTOTag;

import java.util.List;

public class TagResponse {
    private List<DTOTag> tags;

    public TagResponse(List<DTOTag> tags) {
        this.tags = tags;
    }

    public List<DTOTag> getTags() {
        return tags;
    }

    public void setTags(List<DTOTag> tags) {
        this.tags = tags;
    }
}
