package main.service;

import main.api.response.SettingsResponse;
import main.model.GlobalSetting;
import main.model.repositories.GlobalSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {

    @Autowired
    private GlobalSettingsRepository repository;

    public SettingsResponse getGlobalSetting(){
        SettingsResponse settingsResponse = new SettingsResponse();
        List<GlobalSetting> settings = repository.findAll();

        for (GlobalSetting setting : settings){
            switch (setting.getCode()){
                case ("MULTIUSER_MODE"):
                    settingsResponse.setMultiUserMode(setting.getValue().equals("YES"));
                    break;
                case ("POST_PREMODERATION"):
                    settingsResponse.setPostPreModeration(setting.getValue().equals("YES"));
                    break;
                case ("STATISTICS_IS_PUBLIC"):
                    settingsResponse.setStatisticIsPublic(setting.getValue().equals("YES"));
                    break;
            }
        } return settingsResponse;
    }
}
