package main.service;

import main.api.response.StatisticsResponse;
import main.model.Post;
import main.model.repositories.GlobalSettingsRepository;
import main.model.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllStatisticsService {

    @Autowired
    private GlobalSettingsRepository globalSettingsRepository;
    @Autowired
    private PostRepository postRepository;

    public ResponseEntity getResponse (boolean isModerator){

        boolean statisticsIsPublic = globalSettingsRepository
                .getStatisticsIsPublicValue().equals("YES");

        if (!statisticsIsPublic && !isModerator) {
            return ResponseEntity.status(401).body(null);
        }
        else {
            StatisticsResponse response = new StatisticsResponse();

            List<Post> posts = postRepository.findAll();

            int postsCounts = posts.size();
            int likesCount = (int) posts.stream().flatMap((o)->o.getPostVotes()
                    .stream()).filter((o)->o.getValue() == 1).count();
            int dislikesCount = (int) posts.stream().flatMap((o)->o.getPostVotes()
                    .stream()).filter((o)->o.getValue() == -1).count();
            int viewsCount = posts.stream().map(Post::getViewCount)
                    .reduce(Integer::sum).get();
            long firstPublication = posts.stream().sorted((o1, o2) -> o1.getTime()
                    .compareTo(o2.getTime())).findFirst().get().getTime().getTime()
                    / 1000;

            response.setPostsCount(postsCounts);
            response.setLikesCount(likesCount);
            response.setDislikesCount(dislikesCount);
            response.setViewsCount(viewsCount);
            response.setFirstPublication(firstPublication);

            return ResponseEntity.ok().body(response);
        }
    }
}
