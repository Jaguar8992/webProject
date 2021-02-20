package main.controller;


import main.api.response.PostsResponse;
import main.service.PostsResponseFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final PostsResponseFactory factory;

    public ApiPostController(PostsResponseFactory factory) {
        this.factory = factory;
    }

    @GetMapping("/")
    private PostsResponse post(Integer offset, Integer limit, String mode){
        if (offset==null){
            offset = 0;
        }
        if (limit ==null){
            limit = 10;
        }
        if (mode==null){
            mode = "recent";
        }
        return factory.getPostsResponse(offset, limit, mode);
    }


}
