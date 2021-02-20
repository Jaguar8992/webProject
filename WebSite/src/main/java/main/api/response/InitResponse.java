package main.api.response;

import org.springframework.stereotype.Component;

@Component
public class InitResponse {

    private String title = "DevPub";
    private String subtitle =  "Рассказы разработчиков";
    private String phone = "+7 903 666-44-55";
    private String email = "mail@mail.ru";
    private String copyright = "Дмитрий Сергеев";
    private String copyrightFrom = "2005";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getCopyrightFrom() {
        return copyrightFrom;
    }

    public void setCopyrightFrom(String copyrightFrom) {
        this.copyrightFrom = copyrightFrom;
    }
}
