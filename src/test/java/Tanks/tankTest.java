package Tanks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class tankTest {

      @Test
        public void testTankConstructor() {
            Tank tank = new Tank(10, 20);
            assertNotNull(tank); // Ensure that the constructor creates an instance of Tank
            assertEquals(10, tank.getY()); // Check if row is initialized correctly
            assertEquals(20, tank.getX()); // Check if column is initialized correctly
            assertEquals(0, tank.getScore()); // Check if score is initialized to 0
            assertEquals(50, tank.getPower()); // Check if power is initialized to 50
            assertEquals(100, tank.getHealth()); // Check if health is initialized to 60
        }

}
