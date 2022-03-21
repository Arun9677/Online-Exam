package Exam.Online.Exam;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encrypter {

	public String encryptPass(String password)
	{
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(password);
	}
	
}
