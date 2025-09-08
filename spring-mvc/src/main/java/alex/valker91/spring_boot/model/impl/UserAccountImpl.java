package alex.valker91.spring_boot.model.impl;

import alex.valker91.spring_boot.model.UserAccount;

public class UserAccountImpl implements UserAccount {

    private Long id;
    private Long userId;
    private int userAmount;

    public UserAccountImpl() {}

    public UserAccountImpl(Long id, Long userId, int userAmount) {
        this.id = id;
        this.userId = userId;
        this.userAmount = userAmount;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public int getUserAmount() {
        return userAmount;
    }

    @Override
    public void setUserAmount(int userAmount) {
        this.userAmount = userAmount;
    }
}
