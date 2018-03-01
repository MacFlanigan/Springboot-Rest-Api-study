package mac.flanigan.controller;

import mac.flanigan.model.User;
import mac.flanigan.service.UserService;
import mac.flanigan.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestApiController {

    public static final Logger LOGGER = LoggerFactory.getLogger(RestApiController.class);

    @Autowired
    UserService userService;

    // ------------ Retrieve all Users ---------------
    @RequestMapping(value = "/user/", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listAllUsers(){
        List<User> users = userService.findAllUsers();
        if (users.isEmpty()){
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        LOGGER.info("returning all users");
        return new ResponseEntity<List<User>>(users,HttpStatus.OK);
    }

    // ------------ Retrieve User by id ---------------
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable("id") long id){
        LOGGER.info("fetching user with id {}", id);
        User user = userService.findById(id);

        if (user == null) {
            LOGGER.error("user with id {} not found", id);
            return new ResponseEntity(new CustomErrorType("user with id " + id +" not found"),
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(user, HttpStatus.OK);
    }

    // ------------ Create user ---------------
    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder){
        LOGGER.info("creating user {}", user);

        if (userService.isExists(user)){
            LOGGER.error("user with name {} already exists", user.getName());
            return new ResponseEntity(new CustomErrorType("user with name  + " +
                    user.getName() + " already exists"), HttpStatus.CONFLICT);
        }
        userService.saveUser(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    // ------------ Update User by id ---------------
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody User user){

        LOGGER.info("updating user with id: {}", id);
        User currentUser = userService.findById(id);

        if (currentUser == null){
            LOGGER.error("user with name {} already exists", user.getName());
            return new ResponseEntity(new CustomErrorType("user with id: " + id +
            " not found"), HttpStatus.NOT_FOUND);
        }
        userService.updateUser(id, user);
        return new ResponseEntity<User>(currentUser, HttpStatus.OK);
    }

    // ------------ Delete User by id ---------------
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser (@PathVariable("id") long id){
        LOGGER.info("deleting user with id: {}", id);
        User user = userService.findById(id);

        if (user == null){
            LOGGER.error("user with id {} not found", id);
            return new ResponseEntity(new CustomErrorType("user with id " + id +" not found"),
                    HttpStatus.NOT_FOUND);
        }

        userService.deleteUser(id);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }

    // ------------ Delete User by id ---------------
    @RequestMapping(value = "/user/", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteAllUsers(){
        LOGGER.info("deleting all user");
        userService.deleteAllUsers();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
