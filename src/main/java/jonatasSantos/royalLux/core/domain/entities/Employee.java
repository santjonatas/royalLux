package jonatasSantos.royalLux.core.domain.entities;

import jonatasSantos.royalLux.core.domain.entities.common.Base;

public class Employee extends Base {
    protected int UserId;
    protected int RoleId;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getRoleId() {
        return RoleId;
    }

    public void setRoleId(int roleId) {
        RoleId = roleId;
    }
}
