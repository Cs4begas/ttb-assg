package ttb.assg.utils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerUtilsTest {

    @Test
    public void testIsNullOrBlank_nullString_returnsTrue() {
        assertTrue(CustomerUtils.isNullOrBlank(null));
    }

    @Test
    public void testIsNullOrBlank_emptyString_returnsTrue() {
        assertTrue(CustomerUtils.isNullOrBlank(""));
    }

    @Test
    public void testIsNullOrBlank_blankString_returnsTrue() {
        assertTrue(CustomerUtils.isNullOrBlank("   "));
    }

    @Test
    public void testIsNullOrBlank_nonBlankString_returnsFalse() {
        assertFalse(CustomerUtils.isNullOrBlank("hello"));
    }
}