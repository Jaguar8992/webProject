package main.controller;

import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagResponse;
import main.service.SettingService;
import main.service.TagsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingService settingService;
    private final TagsService tagsService;

    public ApiGeneralController(InitResponse initResponse, SettingService settingService, TagsService tagsService) {
        this.initResponse = initResponse;
        this.settingService = settingService;
        this.tagsService = tagsService;
    }
    @GetMapping("/tag")
    @ResponseBody
    private TagResponse tag (String query){
        if (query == null){
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
}
