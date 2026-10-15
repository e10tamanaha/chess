package service;

import dataaccess.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTests {
    @Test
    void clearSucceeds() {
        ClearService service = new ClearService(new MemoryUserDAO(), new MemoryGameDAO(), new MemoryAuthDAO());
        assertDoesNotThrow(service::clear);
    }
}