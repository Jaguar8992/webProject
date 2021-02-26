package main.api.response;

public class CaptchaResponse {

    private String secret;
    private String image;

    public CaptchaResponse(String code, String image) {
        this.secret = code;
        this.image = image;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
