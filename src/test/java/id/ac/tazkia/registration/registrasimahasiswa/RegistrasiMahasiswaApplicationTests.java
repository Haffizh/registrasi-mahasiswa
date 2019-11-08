package id.ac.tazkia.registration.registrasimahasiswa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegistrasiMahasiswaApplicationTests {

	@Autowired private PasswordEncoder passwordEncoder;

	@Test
	public void encodePassword() {
		System.out.println("akad123 : ["+passwordEncoder.encode("akad123")+"]");
//		System.out.println("1234 : ["+passwordEncoder.encode("123")+"]");
	}

}
