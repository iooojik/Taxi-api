package octii.dev.taxi

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class TaxiApplicationTests {

	@Test
	fun contextLoads() {
		println(LocalDateTime.now().month)
	}

}
