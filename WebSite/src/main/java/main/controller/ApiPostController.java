package main.controller;


import main.api.response.PostsResponse;
import main.service.PostsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final PostsService postsService;

    public ApiPostController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping("")
    @ResponseBody
    private PostsResponse post( Integer offset, Integer limit, String mode){
        if (offset==null){
            offset = 0;
        }
        if (limit ==null){
            limit = 10;
        }
        if (mode==null){
            mode = "recent";
        }
        return postsService.getPostsResponse(offset, limit, mode, "");
    }

    @GetMapping("/search")
    @ResponseBody
    private PostsResponse search( Integer offset, Integer limit, String query){
        if (offset==null){
            offset = 0;
        }
        if (limit ==null){
            limit = 10;
        }
        if (query==null){
            query = "";
        }
        return postsService.getPostsResponse(offset, limit, "recent", query);
    }

}
