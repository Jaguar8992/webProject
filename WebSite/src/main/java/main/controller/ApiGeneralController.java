package main.controller;

import main.api.request.CommentRequest;
import main.api.request.ModerationRequest;
import main.api.request.ProfileResponse;
import main.api.response.*;
import main.model.User;
import main.model.repositories.UserRepository;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingService settingService;
    private final TagsService tagsService;
    private final CalendarService calendarService;
    private final ImageService imageService;
    @Autowired
    private final UserRepository userRepository;
    private final ModerationDecisionService moderationDecisionService;
    private final CommentService commentService;
    private final MyProfileService myProfileService;

    public ApiGeneralController(InitResponse initResponse, SettingService settingService, TagsService tagsService, CalendarService calendarService, ImageService imageService, UserRepository userRepository, ModerationDecisionService moderationDecisionService, CommentService commentService, MyProfileService myProfileService) {
        this.initResponse = initResponse;
        this.settingService = settingService;
        this.tagsService = tagsService;
        this.calendarService = calendarService;
        this.imageService = imageService;
        this.userRepository = userRepository;
        this.moderationDecisionService = moderationDecisionService;
        this.commentService = commentService;
        this.myProfileService = myProfileService;
    }

    @GetMapping("/tag")
    @ResponseBody
    private TagResponse tag(String query) {
        if (query == null) {
            query = "";
        }
        return tagsService.getTagResponse(query);
    }

    @GetMapping("/settings")
    private SettingsResponse settings() {
        return settingService.getGlobalSetting();
    }

    @GetMapping("/init")
    private InitResponse init() {
        return initResponse;
    }


    @GetMapping("/calendar")
    @ResponseBody
    private CalendarResponse calendar(Integer query) {
        return calendarService.getResponse(query);
    }

    @PostMapping("/image")
    @ResponseBody
    private Object image (MultipartFile image, HttpServletRequest request) throws IOException {

        return imageService.getResponse(image, request);
    }

    @PostMapping("/moderation")
    private ResultResponse moderation (@RequestBody ModerationRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User moderator = userRepository.findByEmail(authentication.getName());

        return moderationDecisionService.getResponse(request.getPostId(),
                request.getDecision(), moderator.getId());
    }

    @PostMapping("/comment")
    private Object comment (@RequestBody CommentRequest request){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        return commentService.getResponse(request.getParentId(), request.getPostId(), request.getText(), user);
    }

    @RequestMapping(value = "/profile/my", method = RequestMethod.POST)
    private PostMethodResponse myProfile (@ModelAttribute ProfileResponse response, HttpServletRequest request) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        return myProfileService.getResponse(response.getEmail(), response.getName(),
                response.getPassword(), response.getRemovePhoto(),
                response.getPhoto(), user, request);
    }


}
