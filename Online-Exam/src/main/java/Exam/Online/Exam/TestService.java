package Exam.Online.Exam;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TestService {

	@Autowired
	TestRepository repository;
	
	public void save(Test test)
	{
		repository.save(test);
	}
	
	public List<Test> listAll()
	{
		return repository.findAll();
	}
	
	public void deleteAll()
	{
		repository.deleteAll();
	}
	
}
