package top.huajieyu001.blog;

import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @Author huajieyu
 * @Date 5/3/2025 9:33 PM
 * @Version 1.0
 * @Description TODO
 */
@SpringBootTest
public class Test {

    @Test
    void testBCrypt() {
        String raw = "admin123";
        String encoded = passwordEncoder.encode(raw);
        assertTrue(passwordEncoder.matches(raw, encoded));
    }
}
