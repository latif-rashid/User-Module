package in.hotel.system.controller;

import in.hotel.system.entity.User;
import in.hotel.system.payload.LoginDto;
import in.hotel.system.payload.TokenDto;
import in.hotel.system.repository.UserRepository;
import in.hotel.system.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private UserRepository userRepository;
    private UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

//    @PostMapping("signup")
//    public ResponseEntity<?> createUser(@RequestBody User user) {
//        try {
//            User createdUser = userService.createUser(user);
//            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


        @PostMapping("/signup")
        public ResponseEntity<?> createUser(
            @RequestBody User user
            ){
        Optional<User> opUsername = userRepository.findByUsername(user.getUsername());
        if(opUsername.isPresent()){
            return new ResponseEntity<>("Username already taken", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> opEmail = userRepository.findByUsername(user.getEmail());
        if(opEmail.isPresent()){
            return new ResponseEntity<>("Email already taken", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // Encrypt Password before saving it to the database
        String encryptPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(4));
        user.setPassword(encryptPassword);
        // set the roles associated with the user
        user.setRole("ROLE_USER");
        // Save user to the database and return the created user with status code 201 (Created)
        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/signup-property-owner")
    public ResponseEntity<?> createPropertyOwnerUser(
            @RequestBody User user
    ){
        Optional<User> opUsername = userRepository.findByUsername(user.getUsername());
        if(opUsername.isPresent()){
            return new ResponseEntity<>("Username already taken", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> opEmail = userRepository.findByUsername(user.getEmail());
        if(opEmail.isPresent()){
            return new ResponseEntity<>("Email already taken", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // Encrypt Password before saving it to the database
        String encryptPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(4));
        user.setPassword(encryptPassword);
        user.setRole("ROLE_OWNER");
        // Save user to the database and return the created user with status code 201 (Created)
        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }





    @GetMapping("/message")
    public String getMessage(){
        return "Hello Buddy";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginDto loginDto
    ){
        // jwt token is passed
        String token = userService.verifyLogin(loginDto);

        // if token is not null, return it with status code 200 (OK)
        if(token != null){
            // create a tokenDto object and return it with status code 200 (OK)
            TokenDto tokenDto = new TokenDto();
            tokenDto.setToken(token);
            tokenDto.setType("JWT");

            return new ResponseEntity<>(tokenDto, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Invalid Credentials", HttpStatus.FORBIDDEN);
        }
    }




//    @PostMapping("/add")
//    public String addCountry(){
//        return "added";
//    }




}
