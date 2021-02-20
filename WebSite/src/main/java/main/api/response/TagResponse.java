package main.api.response;

import main.service.ServiceData.TagWeight;

import java.util.List;

public class TagResponse {
    private List<TagWeight> tags;

    public TagResponse(List<TagWeight> tags) {
        this.tags = tags;
    }

    public List<TagWeight> getTags() {
        return tags;
    }

    public void setTags(List<TagWeight> tags) {
        this.tags = tags;
    }
}
