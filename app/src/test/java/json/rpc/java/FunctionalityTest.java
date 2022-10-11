package json.rpc.java;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.NullPointerException;


public class FunctionalityTest {
	Integer port = 8080;
	@Test
	void createApp() {
		App app = new App(port);
		assertNotNull(app);
	}
	
	@Test
	void nullServerFails() {
		assertThrows(NullPointerException.class, () -> {
			JavaHttpServer javaHttpServer = new JavaHttpServer(0, null, null);
		});
	}

}
