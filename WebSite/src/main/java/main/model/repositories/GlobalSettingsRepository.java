package main.model.repositories;

import main.model.GlobalSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GlobalSettingsRepository extends JpaRepository<GlobalSetting, Integer> {

    @Query ("SELECT value FROM GlobalSetting WHERE code = 'MULTIUSER_MODE'")
    String getMultiUserModeValue();

    @Query ("SELECT value FROM GlobalSetting WHERE code = 'POST_PREMODERATION'")
    String getPostPremoderationValue();

    @Query ("SELECT value FROM GlobalSetting WHERE code = 'STATISTICS_IS_PUBLIC'")
    String getStatisticsIsPublicValue();

    GlobalSetting findByCode (String code);
}
