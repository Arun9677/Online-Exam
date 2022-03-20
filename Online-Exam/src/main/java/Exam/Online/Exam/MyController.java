package Exam.Online.Exam;


import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.yaml.snakeyaml.error.Mark;

import net.bytebuddy.utility.RandomString;

@Controller
public class MyController {

	@Autowired
	UserService userService;
	
	@Autowired
	QuestionService questionService;
	
	@Autowired
	MarksService marksService;
	
	@Autowired
	TestService testService;
	
	@Autowired
	JavaMailSender sender;
	
	int page;
	
	@RequestMapping("/")
	public String index()
	{
		
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		if(authentication instanceof AnonymousAuthenticationToken)
		{
			return "index";
		}
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		
		if (principal instanceof UserDetails) {
		  username = ((UserDetails)principal).getUsername();
		} else {
		  username = principal.toString();
		}
		
		System.out.println("Username = "+username);
		
		
		return "home";
	}
	
	@RequestMapping("/signIn")
	public String signIn(Model model)
	{
//		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
//		if(authentication!=null || authentication instanceof AnonymousAuthenticationToken)
//		{
//			return "home";
//		}
//		return "redirect:/";
		
		return "home";	
	}
	
	@RequestMapping("/home")
	public String home()
	{
		return "home";
	}
	
	@GetMapping("/signUp")
	public String signUpForm(Model model)
	{
		User user = new User();
		model.addAttribute("user", user);
		return "signUp";
	}
	
	@PostMapping("/signUp")
	public String signUp(@ModelAttribute("user") User user)
	{
		String random = RandomString.make(64);
		Encrypter encrypter = new Encrypter();
		String pass = encrypter.encryptPass(user.getPassword());
		user.setPassword(pass);
		user.setEnable(false);
		user.setVerificationcode(random);
		userService.save(user);
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getEmail());
		message.setSubject("Mail verification");
		message.setText("Your verification code is = "+random);
		sender.send(message);
		
		return "redirect:/mailVerify";
	}
	
	@RequestMapping("/mailVerify")
	public String mailVerification(Model model)
	{
		String code = null;
		model.addAttribute("code", code);
		return "verify";
	}
	
	@PostMapping("/verify")
	public String mailVerified(@Param("code") String code)
	{
		System.out.println("Code = "+code);
		if(code != null)
		{
			System.out.println("Hi");
			User user = userService.findByCode(code);
			System.out.println("Hello");
			System.out.println("User = "+user);
			System.out.println("Tata");
			System.out.println("Id = "+user.getId());
			System.out.println("Email = "+user.getEmail());
			System.out.println("Username = "+user.getUsername());
			System.out.println("Password = "+user.getPassword());
			System.out.println("Mobile = "+user.getMobile());
			System.out.println("Code = "+user.getVerificationcode());
			System.out.println("Enable = "+user.isEnable());
			if(user.getId() != 0)
			{
				user.setEnable(true);
				user.setVerificationcode(null);
				userService.save(user);
				
				Marks marks = new Marks();
				marks.setId(user.id);
				marks.setDate(null);
				marks.setMarks(-1);
				marksService.save(marks);
				
				return "redirect:/";
			}
		}
		return "redirect:/verify";
	}
	
	@GetMapping("/questionForm")
	public String questionForm(Model model)
	{
		Question question = new Question();
		model.addAttribute("quest", question);
		return "questionForm";
	}
	
	@PostMapping("/questionForm")
	public String questionSave(@ModelAttribute("question") Question question)
	{
		questionService.save(question);
		return "redirect:/home";
	}
	
	@GetMapping("/testForm")
	public String FirstTest(Model model, RedirectAttributes attributes)
	{
		String username;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof UserDetails) {
		  username = ((UserDetails)principal).getUsername();
		} else {
		  username = principal.toString();
		}
		
		User user = userService.findByName(username);
		Marks marks = null;
		marks =	marksService.getById(user.getId());
		if(marks.getMarks() == -1)
		{
			System.out.println(marks);
			System.out.println(marks.getId());
			System.out.println(user.getId());
			System.out.println("Hi");
			page = 0;
			return "redirect:/page";
//			ExamForm(model, null);
		}	
//		attended();
		
		attributes.addFlashAttribute("error", "Test already attended");
//		return "home";
		
		return "redirect:/ErrorPage";
	}
	
	@RequestMapping("/ErrorPage")
	public String errorPage()
	{
		return "ErrorPage";
	}
	
	@RequestMapping("/page")
	public String ExamForm(Model model, @Param("ans") String ans)
	{		
		System.out.println("Hi");
		Page<Question> pg = questionService.pagination(++page);
		if(page > pg.getTotalElements())
		{
			return "redirect:/submitExam";
		}
		List<Question> questions = pg.getContent();
		if(ans != null)
		{
			saveMarks(page, ans);
		}
		model.addAttribute("currentpage", page);
		model.addAttribute("tp",pg.getTotalPages());
		model.addAttribute("ti",pg.getTotalElements());
		model.addAttribute("questions", questions);
		return "testForm";
	}	
	
	public void saveMarks(int id, String ans)
	{
		Test test = new Test();
		test.setId(--id);
		test.setAns(ans);
		testService.save(test);
	}
	
	@RequestMapping("/submitExam")
	public String score(Model model, @Param("ans") String ans)
	{
		if(ans != null)
		{
			saveMarks(++page, ans);
		}
		
		int score = 0, total = 0;
		List<Question> questions = questionService.listAll();
		List<Test> tests = testService.listAll();
		
		for(Question q : questions)
		{
			for(Test t : tests)
			{
				if((q.getId()==t.getId()) && (q.getAnswer().equals(t.getAns())))
				{
					score++;
					break;
				}
			}
			total++;
		}
		
		String username;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof UserDetails) {
		  username = ((UserDetails)principal).getUsername();
		} else {
		  username = principal.toString();
		}
		
		testService.deleteAll();
		
		User user = userService.findByName(username);
		
		Marks marks = new Marks();
		marks.setId(user.getId());
		marks.setDate(new Date());
		marks.setMarks(score);
		marksService.save(marks);
		
		model.addAttribute("score", score);
		model.addAttribute("total", total);
		
		return "score";
	}
	
	@RequestMapping("/score")
	public String result(Model model, RedirectAttributes attributes)
	{
		String username;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof UserDetails) {
		  username = ((UserDetails)principal).getUsername();
		} else {
		  username = principal.toString();
		}
		
		User user = userService.findByName(username);
		Marks marks = marksService.getById(user.getId());
		List<Question> questions = questionService.listAll();
		
		if(marks.getMarks() != -1)
		{
			int total = 0;
			
			for(Question q : questions)
			{
				total++;
			}
			
			model.addAttribute("score", marks.getMarks());
			model.addAttribute("total", total);
			
			return "score";
		}
		
		attributes.addFlashAttribute("error", "Test not yet Attended");
		
		return "redirect:/ErrorPage";
	}
	
	@GetMapping("/result")
	public String allResult(Model model)
	{
		Result result = new Result();
		List<Marks> marks = marksService.listAll();
		List<User> users = new LinkedList<User>();
		List<Result> results = new LinkedList<Result>();
		
		for(Marks m: marks)
		{
			System.out.println("Id = "+m.getId());
			System.out.println("Marks = "+m.getMarks());
		}
		System.out.println();
		for(Marks m : marks)
		{
			User user = userService.findById(m.getId());
			users.add(user);
//			System.out.println("Username = "+user.getUsername());
//			System.out.println("Marks = "+m.getMarks());
//			result.setStudent(user.getUsername());
//			result.setMarks(m.marks);
//			results.add(result);
		}
		System.out.println();
		for(Marks m : marks)
		{
			for(User u : users)
			{
				System.out.println("Id = "+u.getId());
				if(m.getId() == u.getId())
				{
					System.out.println("UserMarks");
//					result.setStudent(u.getUsername());
//					String name = u.getUsername();
					String name = u.findName();
					System.out.println("Username = "+name);
					result.setStudent(name);
					result.setMarks(m.getMarks());
					results.add(result);
				}
			}
			System.out.println("MarkUser");
			System.out.println();
		}
		System.out.println();
		for(Result r : results)
		{
			System.out.println("Username = "+r.getStudent());
			System.out.println("Marks = "+r.getMarks());
		}
		
		model.addAttribute("results", results);
		
		return "result";
	}
	
}	