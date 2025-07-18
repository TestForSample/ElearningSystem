package com.example.ElearningSystem.service;

import com.example.ElearningSystem.model.*;
import com.example.ElearningSystem.repository.PasswordResetTokenRepository;
import com.example.ElearningSystem.repository.PreviousPasswordDAO;
import com.example.ElearningSystem.repository.UserRepository;
import com.example.ElearningSystem.utils.AccountStatus;
import com.example.ElearningSystem.utils.GrantTypes;
import com.example.ElearningSystem.utils.UtilityMethod;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService, UtilityMethod {

    @Autowired
    private UserRepository userRepository;
    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private PreviousPasswordDAO previousPasswordDAO;

    static String tempPassword;


    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<Object> addAUser(Users users) throws MessagingException {
        Users users1=userRepository.findByUsername(users.getUsername());
        if(!(users1 ==null)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user is duplicated");
        }
        List<Users> usersList=userRepository.findAll();
        for(Users users2:usersList){
            if(users2.getNic().equals(users.getNic())){
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("nic is duplicated");
            }
        }
         tempPassword=getRandomString(10);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        users.setPassword(encoder.encode(tempPassword));
        users.setAccountStatus(AccountStatus.UnVerified);
        Users users2= userRepository.save(users);
        emailService.sendHtmlEmail(
                users2.getUsername(),
                "Welcome!",
                "<h1>Hello!</h1><p>This is your temp password <b>  " + tempPassword+
                        "  please active your account by resetting the password through this temp password," +
                        " you can use it once</p>"
        );

        return ResponseEntity.status(HttpStatus.ACCEPTED).contentType(MediaType.APPLICATION_JSON).body(users2);


    }


    public Users getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public String updateTheUser(String username, Users users) {
        Users users1 = userRepository.findByUsername(username);
        if (users.getUsername().equals(users1.getUsername())) {
            users1.setNic(users.getNic());
            users1.setUserFullName(users.getUserFullName());
            users1.setRole(users.getRole());
            userRepository.save(users1);
        } else {
            return "the username is not matched";
        }

        return "user details are successfully updated";
    }

    public String deleteTheUser(String username) {
        Users users = userRepository.findByUsername(username);
        if (users.getUsername().equals(username)) {
            System.out.println(users.getUsername());
            userRepository.deleteTheUsersByUsername(username);
        } else {
            return "the username is not available";
        }
        return "user deleted successfully";


    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByUsername(username);
        if (users == null)
            throw new UsernameNotFoundException(HttpStatus.NOT_FOUND.name());
        return new UserPrincipal(users);
    }

    public ResponseEntity<String> verify(LoginRequest loginRequest) throws AuthenticationException, NoSuchMethodException {
       if(!loginRequest.getGrantType().equals(GrantTypes.password.name())) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body("Invalid grant type. Expected 'password'.");
       }
           Users users = userRepository.findByUsername(loginRequest.getUsername());
        if (users == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
//        if (users.getAccountStatus().name().equals(AccountStatus.UnVerified.name())){
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account is not verified, please active your account by resetting your password and try again");
//        }
            if (users.getAccountStatus().name().equals(AccountStatus.Locked.name())) {
            return ResponseEntity.status(HttpStatus.LOCKED).body("Account is locked. Try again later.");
        } if (users.getAccountStatus().name().equals(AccountStatus.Archived.name())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");
        }

        try {
            if(new BCryptPasswordEncoder().matches(loginRequest.getPassword(),users.getPassword())
                    && users.getAccountStatus().equals(AccountStatus.UnVerified)){
                UsernamePasswordAuthenticationToken authToken
                      = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
             Authentication authentication = authenticationManager.authenticate(authToken);
             if(authentication.isAuthenticated()){
                    return ResponseEntity.status(HttpStatus.OK).body(generateResetToken(users.getUsername()));
             }
            }
            UsernamePasswordAuthenticationToken authToken
                    = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);
            if (authentication.isAuthenticated() && users.getAccountStatus().name().equals(AccountStatus.Active.name())) {


                AuthResponse authResponse =
                            new AuthResponse(jwtService.generateToken(loginRequest.getUsername(), users.getAuthorities()));
                    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(authResponse.getAuthToken());
                }

         else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthenticated");
            }
        }
        catch (BadCredentialsException e){
            increaseFailedAttempts(loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username or password");
        }


    }


    public void increaseFailedAttempts(String username) {
        Users user = userRepository.findByUsername(username);
        if (user != null) {
            int newFailAttempts = user.getFailedLoginAttempt() + 1;
            user.setFailedLoginAttempt(newFailAttempts);
            if (newFailAttempts >= 5) {
                user.setAccountStatus(AccountStatus.Locked);
            }
            userRepository.save(user);
        }
    }

    public void resetFailedAttempts(String username) {
        Users user = userRepository.findByUsername(username);
        if (user != null && user.getFailedLoginAttempt() > 0) {
            user.setFailedLoginAttempt(0);
            userRepository.save(user);
        }
    }

    public ResponseEntity<String> updateLockStatus(String username) {
        Users users=userRepository.findByUsername(username);
        if(users.getAccountStatus().name().equals(AccountStatus.Locked.name())){
            users.setAccountStatus(AccountStatus.UnVerified);
            userRepository.save(users);
        }
        resetFailedAttempts(username);

      return ResponseEntity.status(HttpStatus.OK)
                .body(username+"'s account status is unlocked, but please active your account by resetting the password");

    }

    public ResponseEntity<String> archiveTheUser(String username) {
        Users users=userRepository.findByUsername(username);
        if(users!=null && users.getAccountStatus().ordinal()!=4){
            users.setAccountStatus(AccountStatus.Archived);
            userRepository.save(users);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(username+" is archived, if you want to unArchive the user account please reach out to our admin");
    }

    @Override
    public String getRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = rand.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

//    public ResponseEntity<String> activateTheUserAccount(String username) {
//        Users users=userRepository.findByUsername(username);
//        if(users.getAccountStatus()==AccountStatus.UnVerified.ordinal()){
//
//        }
//    }


    public void createPasswordResetTokenForUser(Users user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        tokenRepository.save(myToken);
    }

    public String generateResetToken(String username) {
        Users user = userRepository.findByUsername(username);
               if(user==null) {
                   throw new UsernameNotFoundException("user not found");
               }

        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(user, token);
        return token;
    }
    public boolean validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> passToken = tokenRepository.findByToken(token);

        if (passToken.isEmpty()) {
            return false;
        }
        Date expiryDate = passToken.get().getExpiryDate();
        if (expiryDate.before(new Date())) {
            return false;
        }

        return true;
    }

    public void resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> passToken = tokenRepository.findByToken(token);
        if (passToken.isEmpty() || !validatePasswordResetToken(token)) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        Users user = passToken.get().getUsers();
        List<PreviousPasswords> previousPasswordsList=previousPasswordDAO.findByCreatedBy(user.getUsername());
        String newPasswordOne=new BCryptPasswordEncoder(12).encode(newPassword);
        for(PreviousPasswords password:previousPasswordsList){
        if(new BCryptPasswordEncoder(12).matches(password.getPreviousPassword(),newPasswordOne)){
            throw new BadCredentialsException("password is already used, please use new password");
        }}
        user.setPassword(newPasswordOne);
        user.setAccountStatus(AccountStatus.Active);
        Users users=userRepository.save(user);
        PreviousPasswords previousPasswords=new PreviousPasswords();
        previousPasswords.setPreviousPassword(users.getPassword());
        previousPasswords.setCreatedDate(new Date());
        previousPasswords.setCreatedBy(users.getUsername());
        previousPasswordDAO.save(previousPasswords);
        calculateThePasswordCount(users.getUsername());
        tokenRepository.delete(passToken.get()); // optional: delete token after use
    }
    public ResponseEntity<String> forgotPassword(String username) throws MessagingException {
        Users users=userRepository.findByUsername(username);
        if(users!=null && users.getAccountStatus().equals(AccountStatus.Locked)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("this account is locked");
        }
        assert users != null;
        if(users.getAccountStatus().equals(AccountStatus.Archived)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("this account is not found");
        }
        users.setAccountStatus(AccountStatus.ResetPassword);
        userRepository.save(users);
        String resetToken=generateResetToken(username);
        emailService.sendHtmlEmail(users.getUsername(),
                "Welcome!",
                "<h1>Hello!</h1><p>This is your temp password <b>  http://localhost:8081/api/student/reset-password/" + resetToken+
                        "  please active your account by resetting the password through this reset password link," +
                        " you can use it once</p>");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("This is the token: "+resetToken+" <br>reset password link has been send to this particular email address</br>");

    }

    public void calculateThePasswordCount(String username) {
        List<PreviousPasswords> previousPasswords = previousPasswordDAO.findByCreatedBy(username);
        if (previousPasswords.size() == 25) {
          previousPasswords.sort(Comparator.comparing(PreviousPasswords::getCreatedDate));
           previousPasswordDAO.delete(previousPasswords.getFirst());
            }
        }

    public List<PreviousPasswords> addPasswords(List<PreviousPasswords> passwords) {
      return previousPasswordDAO.saveAll(passwords);
    }
}
