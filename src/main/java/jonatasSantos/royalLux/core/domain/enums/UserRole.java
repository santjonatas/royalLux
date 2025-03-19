package jonatasSantos.royalLux.core.domain.enums;

import java.util.Arrays;
import java.util.List;

public enum UserRole {
    ADMIN("ADMIN"),
    EMPLOYEE("EMPLOYEE"),
    CLIENT("CLIENT");

    private String role;

    public static final List<UserRole> ROLES = Arrays.asList(UserRole.values());

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return this.role;
    }
}
