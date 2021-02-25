package main.controller;


import main.api.response.PostByIdResponse;
import main.api.response.PostsResponse;
import main.service.PostByIdService;
import main.service.PostsService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final PostsService postsService;
    private final PostByIdService postByIdService;

    public ApiPostController(PostsService postsService, PostByIdService postByIdService) {
        this.postsService = postsService;
        this.postByIdService = postByIdService;
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
        Pageable page = PageRequest.of(offset / limit, limit);
        return postsService.getPostsResponse(page, mode);
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
        Pageable page = PageRequest.of(offset / limit, limit);
        return postsService.getPostsResponseByQuery(page, query);
    }

    @GetMapping("/byDate")
    @ResponseBody
    private PostsResponse byDate (Integer offset, Integer limit, String date) throws ParseException {
        if (offset==null){
            offset = 0;
        }
        if (limit ==null){
            limit = 10;
        }
        Pageable page = PageRequest.of(offset / limit, limit);
        return postsService.getPostsResponseByDate(page, date);
    }

    @GetMapping("/byTag")
    @ResponseBody
    private PostsResponse byTag (Integer offset, Integer limit, String tag)  {
        if (offset==null){
            offset = 0;
        }
        if (limit ==null){
            limit = 10;
        }
        Pageable page = PageRequest.of(offset / limit, limit);
        return postsService.getPostsResponseByTag(page, tag);
    }

    @GetMapping("/{id}")
    private ResponseEntity getById (@PathVariable int id){
        PostByIdResponse response = postByIdService.getPostByIdResponse(id);
        if (response == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
