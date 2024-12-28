package in.hotel.system.service;

import in.hotel.system.entity.User;
import in.hotel.system.payload.LoginDto;
import in.hotel.system.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private JWTService jwtService;
    public UserService(UserRepository userRepository, JWTService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public String verifyLogin(LoginDto dto) {
        Optional<User> opUser = userRepository.findByUsername(dto.getUsername());
        if (opUser.isPresent()) {
            User user = opUser.get();
            // verify the existing user return true if passwords match, otherwise return false
            // password should be encrypted before comparing
            // use return BCrypt.checkpw(plainTextPassword, encryptedPassword) method to compare the passwords
            // return  BCrypt.checkpw(dto.getPassword(), user.getPassword());

            if (BCrypt.checkpw(dto.getPassword(), user.getPassword())) {

                // generate JWT token
                String token = jwtService.generateToken(user.getUsername());
                return token;

            }
        } else {
            return null;
        }
        return null;
    }

//    public User createUser(User user) {
//        // Check if the username is already taken
//        Optional<User> opUsername = userRepository.findByUsername(user.getUsername());
//        if (opUsername.isPresent()) {
//            throw new IllegalArgumentException("Username already taken");
//        }
//        // Check if the email is already taken
//        Optional<User> opEmail = userRepository.findByEmail(user.getEmail());
//        if (opEmail.isPresent()) {
//            throw new IllegalArgumentException("Email already taken");
//        }
//        // Encrypt the password
//        // Use BCrypt.gensalt(4) to generate a secure salt and BCrypt.hashpw() to hash the password
//        // Store the encrypted password in the user object and save it to the database
//        // Example: String encryptedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(4));
//        // user.setPassword(encryptedPassword);
//        // userRepository.save(user);
//
//        String encryptedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(4));
//        user.setPassword(encryptedPassword);
//
//        // Save the user to the database
//        return userRepository.save(user);
//
//    }
}
