package main.service;

import main.api.response.TagResponse;
import main.model.Tag;
import main.model.repositories.PostRepository;
import main.model.repositories.TagRepository;
import main.service.ServiceData.TagWeight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagsService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostRepository postRepository;

    public TagResponse getTagResponse(String query) {
        Iterable <Tag> tagIterable = tagRepository.getByQuery(query);
        List <Tag> tags = new ArrayList<>();
        for (Tag tag : tagIterable){
            tags.add(tag);
        }
        Tag maxCountTag = tags.get(0);
        int postCount = (int) postRepository.count();
        int mainTagCount = postRepository.getCountByTag(maxCountTag.getId());

        double dWeightMax = mainTagCount / postCount;
        double k = 1 / dWeightMax;

        List <TagWeight> tagResponse = new ArrayList<>();

        for (Tag tag : tags){
            int countByTag = postRepository.getCountByTag(tag.getId());
            double weight = countByTag / k;
            TagWeight tagWeight = new TagWeight(tag.getName(), weight);
            tagResponse.add(tagWeight);
        }
        return new TagResponse(tagResponse);
    }

}
