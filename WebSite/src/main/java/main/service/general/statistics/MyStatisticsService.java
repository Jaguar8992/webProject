package main.service.general.statistics;

import main.api.response.StatisticsResponse;
import main.model.Post;
import main.model.User;
import org.springframework.stereotype.Service;

@Service
public class MyStatisticsService {

    public StatisticsResponse getResponse (User user){

        StatisticsResponse response = new StatisticsResponse();

        int postsCounts = user.getPost().size();
        int likesCount = (int) user.getPost().stream().
                flatMap((o)->o.getPostVotes().stream()).filter((o)->o.getValue() == 1)
                .count();
        int dislikesCount = (int) user.getPost().stream()
                .flatMap((o)->o.getPostVotes().stream()).filter((o)->o.getValue() == -1)
                .count();
        int viewsCount = user.getPost().stream().map(Post::getViewCount)
                .reduce(Integer::sum).get();
        long firstPublication = user.getPost().stream()
                .sorted((o1, o2) -> o1.getTime().compareTo(o2.getTime()))
                .findFirst().get().getTime().getTime() / 1000;

        response.setPostsCount(postsCounts);
        response.setLikesCount(likesCount);
        response.setDislikesCount(dislikesCount);
        response.setViewsCount(viewsCount);
        response.setFirstPublication(firstPublication);

        return response;
    }
}
