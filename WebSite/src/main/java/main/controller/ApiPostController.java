package main.controller;


import main.api.request.PostRequest;
import main.api.request.VoteRequest;
import main.api.response.PostByIdResponse;
import main.api.response.PostMethodResponse;
import main.api.response.PostsResponse;
import main.api.response.ResultResponse;
import main.model.Post;
import main.model.User;
import main.model.repositories.PostRepository;
import main.model.repositories.UserRepository;
import main.service.PostByIdService;
import main.service.GetPostsService;
import main.service.PostService;
import main.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final GetPostsService getPostsService;
    private final PostByIdService postByIdService;
    private final PostService postService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final VoteService voteService;


    @Autowired
    public ApiPostController(GetPostsService postsService, PostByIdService postByIdService, PostService postService, UserRepository userRepository, PostRepository postRepository, VoteService voteService) {
        this.getPostsService = postsService;
        this.postByIdService = postByIdService;
        this.postService = postService;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.voteService = voteService;
    }

    @GetMapping("")
    @ResponseBody
    private PostsResponse getPosts( Integer offset, Integer limit, String mode){
        Pageable page = PageRequest.of(offset / limit, limit);
        return getPostsService.getPostsResponse(page, mode);
    }

    @GetMapping("/search")
    @ResponseBody
    private PostsResponse search( Integer offset, Integer limit, String query){
        Pageable page = PageRequest.of(offset / limit, limit);
        return getPostsService.getPostsResponseByQuery(page, query);
    }

    @GetMapping("/byDate")
    @ResponseBody
    private PostsResponse byDate (Integer offset, Integer limit, String date) throws ParseException {
        Pageable page = PageRequest.of(offset / limit, limit);
        return getPostsService.getPostsResponseByDate(page, date);
    }

    @GetMapping("/byTag")
    @ResponseBody
    private PostsResponse byTag (Integer offset, Integer limit, String tag)  {
        Pageable page = PageRequest.of(offset / limit, limit);
        return getPostsService.getPostsResponseByTag(page, tag);
    }

    @GetMapping("/{id}")
    private ResponseEntity getById (@PathVariable int id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = authentication != null ?
                userRepository.findByEmail(authentication.getName()) : null;

        PostByIdResponse response = postByIdService.getPostByIdResponse(id, currentUser);

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
        String currentPrincipalName = authentication.getName();
        return getPostsService.getPostsResponseForUser(page, currentPrincipalName, status);
    }

    @GetMapping("/moderation")
    @ResponseBody
    private PostsResponse moderation(Integer offset, Integer limit, String status) {

        Pageable page = PageRequest.of(offset / limit, limit);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return getPostsService.getPostsResponseForModeration(page, currentPrincipalName, status);
    }

    @PostMapping("")
    private PostMethodResponse post (@RequestBody PostRequest postRequest){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        return postService.getResponse(postRequest.getTimestamp(),
                postRequest.getActive() == 1, postRequest.getTitle(),
                postRequest.getTags(), postRequest.getText(), user);
    }

    @PutMapping("/{id}")
    private PostMethodResponse putPost(@RequestBody PostRequest postRequest, @PathVariable int id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post with id " + id + " not found"));

        return postService.putPost(postRequest.getTimestamp(),
                postRequest.getActive() == 1, postRequest.getTitle(),
                postRequest.getTags(), postRequest.getText(), post, user);
    }

    @PostMapping ("/like")
    private ResultResponse like (@RequestBody VoteRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        return voteService.getResponse(true, request.getPostId(), user);
    }

    @PostMapping ("/dislike")
    private ResultResponse dislike (@RequestBody VoteRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        return voteService.getResponse(false, request.getPostId(), user);
    }
}
