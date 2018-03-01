package mac.flanigan.service;

import mac.flanigan.model.User;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();
    User findById(long id);
    User findByName(String name);
    void saveUser(User user);
    boolean isExists(User user);
    User updateUser(long id, User user);
    void deleteUser(long id);
    void deleteAllUsers();

}
