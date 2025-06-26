package jonatasSantos.royalLux.core.application.mappers;

public class UserAuthCodeMapper {

    private String username;
    private String code;
    private String token;

    public UserAuthCodeMapper() {
    }

    public UserAuthCodeMapper(String username, String code, String token) {
        this.username = username;
        this.code = code;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
