package jonatasSantos.royalLux.core.application.mappers;

public class UserAuthCodeMapper {

    public String username;
    public String code;
    public String token;

    public UserAuthCodeMapper(String username, String code, String token) {
        this.username = username;
        this.code = code;
        this.token = token;
    }
}
