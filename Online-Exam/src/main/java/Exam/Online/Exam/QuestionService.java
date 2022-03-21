package Exam.Online.Exam;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QuestionService {

	@Autowired
	QuestionRepository repository;
	
	public void save(Question question)
	{
		repository.save(question);
	}
	
	public List<Question> listAll()
	{
		return repository.findAll();
	}
	
	public Question getById(long id)
	{
		return repository.getById(id);
	}
	
	public void deleteQuestion(long id)
	{
		repository.deleteById(id);
	}
	
	public void deleteAllQuestions()
	{
		repository.deleteAll();
	}
	
	public Page<Question> pagination(int page)
	{
		Pageable pageable = PageRequest.of(page-1, 1);		
		return repository.findAll(pageable);
	}
	
	public Page<Question> ListPage(int page)
	{
		Pageable pageable = PageRequest.of(page-1, 5);
		return repository.findAll(pageable);
	}
	
}
