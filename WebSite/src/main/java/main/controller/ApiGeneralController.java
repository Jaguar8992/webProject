package main.controller;

import main.api.request.CommentRequest;
import main.api.request.ModerationRequest;
import main.api.request.MyProfileRequest;
import main.api.response.*;
import main.model.User;
import main.model.repositories.UserRepository;
import main.service.general.*;
import main.service.general.setting.PutSettingService;
import main.service.general.setting.SettingService;
import main.service.general.statistics.AllStatisticsService;
import main.service.general.statistics.MyStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private final MyStatisticsService myStatisticsService;
    private final AllStatisticsService allStatisticsService;
    private final PutSettingService putSettingService;

    public ApiGeneralController(InitResponse initResponse, SettingService settingService, TagsService tagsService, CalendarService calendarService, ImageService imageService, UserRepository userRepository, ModerationDecisionService moderationDecisionService, CommentService commentService, MyProfileService myProfileService, MyStatisticsService myStatisticsService, AllStatisticsService allStatisticsService, PutSettingService putSettingService) {
        this.initResponse = initResponse;
        this.settingService = settingService;
        this.tagsService = tagsService;
        this.calendarService = calendarService;
        this.imageService = imageService;
        this.userRepository = userRepository;
        this.moderationDecisionService = moderationDecisionService;
        this.commentService = commentService;
        this.myProfileService = myProfileService;
        this.myStatisticsService = myStatisticsService;
        this.allStatisticsService = allStatisticsService;
        this.putSettingService = putSettingService;
    }

    @GetMapping("/tag")
    @ResponseBody
    private ResponseEntity tag(String query) {
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
    private ResponseEntity image(MultipartFile image, HttpServletRequest request) throws IOException {

        return imageService.getResponse(image, request);
    }

    @PostMapping("/moderation")
    private ResultResponse moderation(@RequestBody ModerationRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User moderator = userRepository.findByEmail(authentication.getName());

        return moderationDecisionService.getResponse(request.getPostId(),
                request.getDecision(), moderator.getId());
    }

    @PostMapping("/comment")
    private ResponseEntity comment(@RequestBody CommentRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        return commentService.getResponse(request.getParentId(), request.getPostId(), request.getText(), user);
    }

    @PostMapping(value = "/profile/my")
    private PostMethodResponse myProfileData (@RequestBody MyProfileRequest requestProfile,
                                              HttpServletRequest request) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        return myProfileService.getResponse(requestProfile.getEmail(), requestProfile.getName(),
                requestProfile.getPassword(), requestProfile.getRemovePhoto(),
                null, user, request);

    }

    @PostMapping(value = "/profile/my", consumes = "multipart/form-data")
    private PostMethodResponse myProfilePhoto (@RequestParam (value = "email") String email,
                                               @RequestParam (value = "removePhoto") int removePhoto,
                                               @RequestParam (value = "photo") MultipartFile photo,
                                               @RequestParam (value = "name") String name,
                                               @RequestParam (value = "password", required = false) String password,
                                               HttpServletRequest request) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        return myProfileService.getResponse(email, name,
                password, removePhoto,
                photo, user, request);

    }

    @GetMapping("/statistics/my")
    private StatisticsResponse myStatistics() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        return myStatisticsService.getResponse(user);
    }

    @GetMapping("/statistics/all")
    private ResponseEntity allStatistics() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        return allStatisticsService.getResponse(user.getIsModerator() == 1);
    }

    @PutMapping("/settings")
    private void settings(@RequestBody SettingsResponse response) {
        putSettingService.setSettings(response.isMultiUserMode(),
                response.isPostPreModeration(), response.isStatisticIsPublic());
    }
}
