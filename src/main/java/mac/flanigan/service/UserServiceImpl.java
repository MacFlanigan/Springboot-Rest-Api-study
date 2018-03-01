package mac.flanigan.service;

import mac.flanigan.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserServiceImpl implements UserService {

    private static List<User> users;
    private static final AtomicLong counter = new AtomicLong();


    static {
        users = populateUsers();
    }

    @Override
    public List<User> findAllUsers() {
        return users;
    }

    @Override
    public User findById(long id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public User findByName(String name) {
        return users.stream()
                .filter(user -> user.getName().toLowerCase().equals(name.toLowerCase()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void saveUser(User user) {
        user.setId(counter.incrementAndGet());
        users.add(user);
    }

    @Override
    public boolean isExists(User user) {
        return findByName(user.getName()) != null;
    }

    @Override
    public User updateUser(long id, User user) {
        User currentUser = findById(id);
        if (currentUser == null) return null;

        currentUser.setName(user.getName());
        currentUser.setAge(user.getAge());
        currentUser.setSalary(user.getSalary());

        return currentUser;
    }

    @Override
    public void deleteUser(long id) {
        User deleteUser =users.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .get();
        users.remove(deleteUser);
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
    }

    private static List<User> populateUsers() {

        List<User> users = new ArrayList<>();
        users.add(new User(counter.incrementAndGet(),"one", 10, 1000));
        users.add(new User(counter.incrementAndGet(),"two", 22, 1200));
        users.add(new User(counter.incrementAndGet(),"three", 23, 1400));
        users.add(new User(counter.incrementAndGet(),"four", 41, 3000));
        return users;
    }
}
