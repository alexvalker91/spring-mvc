package alex.valker91.spring_boot.dao;

import alex.valker91.spring_boot.model.User;

import java.util.List;

public interface UserDAO {

    User getById(long id);

    List<User> getAll();

    User insert(User user);

    User update(User user);

    boolean delete(long userId);
}
