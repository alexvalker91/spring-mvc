package alex.valker91.spring_boot.service;

import alex.valker91.spring_boot.model.UserAccount;

public interface UserAccountService {

    int refillUserAccount(long userId, int amount);

    UserAccount getUserAccountByUserId(long userId);

    UserAccount updateUserAccount(UserAccount userAccount);
}
