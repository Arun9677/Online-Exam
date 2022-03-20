package Exam.Online.Exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailComponent {

	@Autowired
	JavaMailSender sender;
	
	public void sendMail()
	{
		
	}
	
}
