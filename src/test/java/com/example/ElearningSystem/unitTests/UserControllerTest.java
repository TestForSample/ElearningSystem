package com.example.ElearningSystem.unitTests;//package com.example.E_Learning_System.unitTests;
//
//import com.example.E_Learning_System.utils.UserRoles;
//import com.example.E_Learning_System.model.Users;
//import com.example.E_Learning_System.repository.UserRepository;
//import com.example.E_Learning_System.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.transaction.annotation.Transactional;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.when;
//
//
//@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
//@Transactional
//public class UserControllerTest {
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private UserService userService;
//    static private Users users;
//    @BeforeClass
//    public static void setUp(){
//        users=new Users();
//        users.setUserFullName("abc def");
//        users.setNic("1235675V");
//        users.setUsername("usertest01@gmail.com");
//        users.setPassword("456");
//        users.setRole(UserRoles.Admin);
//
//    }
//@Test
//public void addAUser(){
//        userService.addAUser(users);
//        Users users1 = userRepository.findByUsername(users.getUsername());
//        assertNotNull(users1);
//        System.out.println(users1.getUsername());
//
//
//    }
//    @Test
//    public void getUserByUserName() {
//        when(userRepository.findByUsername(users.getUsername())).thenReturn(users);
//        UserDetails users1=userService.loadUserByUsername(users.getUsername());
//        assertEquals(users.getUsername(), users1.getUsername());
//        System.out.println(users.getUsername());}
//    @Test
//    void getAllUsers(){
//        List<Users> usersList=new ArrayList<>();
//        usersList.add(users);
//      when(userRepository.findAll()).thenReturn((usersList));
//      List<Users> users1=userService.getAllUsers();
//      assertEquals(usersList.getFirst().getUsername(),users1.getFirst().getUsername());
//        System.out.println(usersList.getFirst().getUsername()+","+users1.getFirst().getUsername());
//    }
//
//}
