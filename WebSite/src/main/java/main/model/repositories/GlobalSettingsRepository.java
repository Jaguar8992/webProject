package main.model.repositories;

import main.model.GlobalSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface GlobalSettingsRepository extends JpaRepository<GlobalSetting, Integer> {

}
