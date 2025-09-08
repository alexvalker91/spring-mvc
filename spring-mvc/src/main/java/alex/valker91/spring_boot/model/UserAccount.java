package alex.valker91.spring_boot.model;

public interface UserAccount {

    long getId();
    void setId(long id);
    long getUserId();
    void setUserId(long userId);
    int getUserAmount();
    void setUserAmount(int userAmount);
}
