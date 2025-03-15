package jonatasSantos.royalLux.core.domain.valueobjects;

public enum UserRole {
    ADMIN("admin"),
    EMPLOYEE("employee"),
    CLIENT("client");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return this.role;
    }
}
