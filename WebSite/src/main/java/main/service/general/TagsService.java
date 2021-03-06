package main.service.general;

import main.api.response.TagResponse;
import main.model.Tag;
import main.model.repositories.PostRepository;
import main.model.repositories.TagRepository;
import main.service.dto.DTOTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagsService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostRepository postRepository;

    public ResponseEntity getTagResponse(String query) {
        List <DTOTag> tagResponse = new ArrayList<>();
        List <Tag> tags = tagRepository.getByQuery(query);

        if (tags.size() > 0) {
            Tag maxCountTag = tags.get(0);
            int postCount = (int) postRepository.count();
            int mainTagCount = postRepository.countByTagId(maxCountTag.getId());

            double dWeightMax = (double) mainTagCount / postCount;
            double k = 1 / dWeightMax;

            for (Tag tag : tags) {
                int countByTag = postRepository.countByTagId(tag.getId());
                double tagCount = (double) countByTag / postCount;
                double weight = tagCount * k;
                DTOTag tagWeight = new DTOTag(tag.getName(), weight);
                tagResponse.add(tagWeight);
            }
        }
        return ResponseEntity.ok().body(new TagResponse(tagResponse));
    }

}
