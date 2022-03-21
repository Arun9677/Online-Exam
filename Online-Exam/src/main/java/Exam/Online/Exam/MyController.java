package Exam.Online.Exam;


 import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
		
		return "home";
	}
	
	@RequestMapping("/signIn")
	public String signIn()
	{
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
		if(code != null)
		{
			User user = userService.findByCode(code);
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

	@RequestMapping("/questionList")
	public void questionPage(Model model, RedirectAttributes attributes)
	{
		questionList(model, 1);
	}
	
	@RequestMapping("/page/{page}")
	public String questionList(Model model, @PathVariable(name = "page") int page)
	{
		Page<Question> pg = questionService.ListPage(page);
		List<Question> questions = pg.getContent();
		model.addAttribute("questions", questions);
		model.addAttribute("currentpage", page);
		model.addAttribute("tp", pg.getTotalPages());
		model.addAttribute("tq", pg.getTotalElements());
		return "QuestionList";	
	}
	
	@RequestMapping("/edit/{id}")
	public ModelAndView ListeditQuestion(@PathVariable(name = "id") int id)
	{
		ModelAndView view = new ModelAndView("Edit");
		Question question = questionService.getById(id);
		view.addObject("question", question);
		return view;
	}
	
	@RequestMapping("/delete/{id}")
	public String deleteQuestion(@PathVariable(name = "id") int id)
	{
		questionService.deleteQuestion(id);
		return "redirect:/questionList";
	}
	
	@GetMapping("/deleteAll")
	public String deleteAllQuestions()
	{
		questionService.deleteAllQuestions();
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
			page = 0;
			return "redirect:/page";
		}	

		attributes.addFlashAttribute("error", "Test already attended");
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
			int size = questions.size();

			model.addAttribute("score", marks.getMarks());
			model.addAttribute("total", size);
			
			return "score";
		}
		
		attributes.addFlashAttribute("error", "Test not yet Attended");
		
		return "redirect:/ErrorPage";
	}
	
	@GetMapping("/result")
	public String result(Model model)
	{
		List<Marks> marks = marksService.listAll();
		List<User> users = userService.listAll();
		List<Result> results = new ArrayList<Result>();
		
		for(Marks m : marks)
		{
			for(User u : users)
			{
				if(m.getId() == u.getId())
				{
					Result result = new Result();
					result.setStudent(u.getUsername());
					result.setMarks(m.getMarks());
					results.add(result);
					break;
				}
			}
		}
		
		model.addAttribute("results", results);
		
		return "result";
	}
	
}	