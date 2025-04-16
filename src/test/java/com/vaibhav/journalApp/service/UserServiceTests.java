package com.vaibhav.journalApp.service;

import com.vaibhav.journalApp.entity.User;
import com.vaibhav.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

//    @Test
//    public void testFindByUsername(){
////         assertEquals(4,  2 + 2);
//        User user = userRepository.findByUsername("vaibhav777");
//
//         assertNotNull(user);
//         assertTrue(!user.getJournalEntries().isEmpty());
//    }


//    @ParameterizedTest
//    @ValueSource(strings = {
//            "vaibhav777",
//            "user123"
//    })
//    public void testFindByUsername(String name){
//
//        User user = userRepository.findByUsername(name);
//
//        assertNotNull(user, "failed for user: " + name);
////        assertTrue(!user.getJournalEntries().isEmpty());
//    }




//    @ParameterizedTest
//    @ArgumentsSource(UserArgumentsProvider.class)
//    public void testSaveNewUser(User user){
//
//        assertTrue(userService.saveNewUser(user));
//
//    }



//    @ParameterizedTest
//    @CsvSource({
//            "1, 1, 2",
//            "2, 10, 12",
//            "3, 3, 5",
//    })
//    public void test(int a, int b, int expected){
//        assertEquals(expected,  a + b);
//    }


}
