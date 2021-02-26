package main.service;

import main.api.response.CalendarResponse;
import main.model.Post;
import main.model.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CalendarService {

    @Autowired
    private PostRepository repository;

    public CalendarResponse getResponse (Integer year){
        CalendarResponse response = new CalendarResponse();
        TreeSet <Integer> years = new TreeSet<>();
        TreeMap <String, Integer> postsByDates = new TreeMap<>();
        List<Post> posts;
        if (year == null){
            posts = repository.getPostsAllTime();
        } else {
            posts = repository.getByYear(year);
        }

        for (Post post : posts){
            Integer postYear = Integer.parseInt(new SimpleDateFormat("yyyy")
                    .format(post.getTime()));
            years.add(postYear);
            postsByDates.put(new SimpleDateFormat("yyyy-MM-dd")
                    .format(post.getTime()), repository.countByDate(post.getTime()));
        }

        response.setYears(years);
        response.setPosts(postsByDates);

        return response;
    }
}
