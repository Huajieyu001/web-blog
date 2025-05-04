package top.huajieyu001.blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @Author huajieyu
 * @Date 5/3/2025 9:33 PM
 * @Version 1.0
 * @Description TODO
 */
@SpringBootTest
public class MyTest {

    @Test
    void testBCrypt() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String raw = "1234567890";
        String raw2 = "1234567890";
        String encoded = encoder.encode(raw);
        String encoded2 = encoder.encode(raw2);

        System.out.println(encoded);
        System.out.println(encoded2);
        boolean matches = encoder.matches(raw, encoded);
        System.out.println("--------------------------");
        System.out.println(matches);
    }
}
