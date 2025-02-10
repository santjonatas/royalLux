package jonatas.santos.royal.lux.core.domain.entities;

import jonatas.santos.royal.lux.core.domain.entities.common.Base;

public class Client extends Base {
    protected int UserId;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }
}
