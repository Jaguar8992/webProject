package main.service.general.setting;

import main.model.GlobalSetting;
import main.model.repositories.GlobalSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PutSettingService {

    @Autowired
    private GlobalSettingsRepository repository;

    public void setSettings (boolean multiUseMode, boolean postPreModeration, boolean statisticsIsPublic) {

        GlobalSetting multiUserModeSetting = repository.findByCode("MULTIUSER_MODE");
        GlobalSetting postPreModerationSetting = repository.findByCode("POST_PREMODERATION");
        GlobalSetting statisticsIsPublicSetting = repository.findByCode("STATISTICS_IS_PUBLIC");

        if (multiUserModeSetting != null) {
            multiUserModeSetting.setValue(multiUseMode ? "YES" : "NO");
            repository.save(multiUserModeSetting);
        } else {
            GlobalSetting setting = new GlobalSetting();
            setting.setCode("MULTIUSER_MODE");
            setting.setName("Многопользовательский режим");
            setting.setValue(multiUseMode ? "YES" : "NO");
            repository.save(setting);
        }

        if (postPreModerationSetting != null) {
            postPreModerationSetting.setValue(postPreModeration ? "YES" : "NO");
            repository.save(postPreModerationSetting);
        } else {
            GlobalSetting setting = new GlobalSetting();
            setting.setCode("POST_PREMODERATION");
            setting.setName("Премодерация постов");
            setting.setValue(postPreModeration ? "YES" : "NO");
            repository.save(setting);
        }

        if (statisticsIsPublicSetting != null) {
            statisticsIsPublicSetting.setValue(statisticsIsPublic ? "YES" : "NO");
            repository.save(statisticsIsPublicSetting);
        } else {
            GlobalSetting setting = new GlobalSetting();
            setting.setCode("STATISTICS_IS_PUBLIC");
            setting.setName("Показывать всем статистику блога");
            setting.setValue(statisticsIsPublic ? "YES" : "NO");
            repository.save(setting);
        }
    }
}
