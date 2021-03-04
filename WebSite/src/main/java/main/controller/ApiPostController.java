package main.controller;


import main.api.response.PostByIdResponse;
import main.api.response.PostsResponse;
import main.service.PostByIdService;
import main.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.ParseException;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final PostsService postsService;
    private final PostByIdService postByIdService;


    @Autowired
    public ApiPostController(PostsService postsService, PostByIdService postByIdService) {
        this.postsService = postsService;
        this.postByIdService = postByIdService;
    }

    @GetMapping("")
    @ResponseBody
    private PostsResponse post( Integer offset, Integer limit, String mode){
        Pageable page = PageRequest.of(offset / limit, limit);
        return postsService.getPostsResponse(page, mode);
    }

    @GetMapping("/search")
    @ResponseBody
    private PostsResponse search( Integer offset, Integer limit, String query){
        Pageable page = PageRequest.of(offset / limit, limit);
        return postsService.getPostsResponseByQuery(page, query);
    }

    @GetMapping("/byDate")
    @ResponseBody
    private PostsResponse byDate (Integer offset, Integer limit, String date) throws ParseException {
        Pageable page = PageRequest.of(offset / limit, limit);
        return postsService.getPostsResponseByDate(page, date);
    }

    @GetMapping("/byTag")
    @ResponseBody
    private PostsResponse byTag (Integer offset, Integer limit, String tag)  {
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

    @GetMapping("/my")
    @ResponseBody
    private PostsResponse my(Integer offset, Integer limit, String status) {

        Pageable page = PageRequest.of(offset / limit, limit);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getAuthorities().forEach(System.out::println);
        return postsService.getPostsResponseForUser(page, authentication.getName(), status);
    }

}
