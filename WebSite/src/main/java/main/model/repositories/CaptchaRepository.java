package main.model.repositories;

import main.model.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CaptchaRepository extends JpaRepository<CaptchaCode, Integer> {

    @Query(value = "SELECT * FROM captcha_codes WHERE time < DATE_SUB(now(), INTERVAL 1 HOUR)", nativeQuery = true)
    List<CaptchaCode> getStaleCaptcha ();

    @Query("FROM CaptchaCode WHERE secretCode = :secretCode")
    CaptchaCode getBySecretCode (@Param("secretCode") String secretCode);
}
