package main.controller;

import main.api.response.CalendarResponse;
import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagResponse;
import main.service.CalendarService;
import main.service.ImageService;
import main.service.SettingService;
import main.service.TagsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingService settingService;
    private final TagsService tagsService;
    private final CalendarService calendarService;
    private final ImageService imageService;

    public ApiGeneralController(InitResponse initResponse, SettingService settingService, TagsService tagsService, CalendarService calendarService, ImageService imageService) {
        this.initResponse = initResponse;
        this.settingService = settingService;
        this.tagsService = tagsService;
        this.calendarService = calendarService;
        this.imageService = imageService;
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
    private Object image (MultipartFile image) throws IOException {

        return imageService.getResponse(image);
    }
}
