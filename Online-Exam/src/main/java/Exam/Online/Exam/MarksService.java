package Exam.Online.Exam;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MarksService {

	@Autowired
	MarksRepository repository;
	
	public void save(Marks marks)
	{
		repository.save(marks);
	}
	
	public List<Marks> listAll()
	{
		return repository.findAll();
	}
	
	public Marks getById(long id)
	{
		try
		{
			Marks marks = null;
			marks = repository.getById(id);
			if(marks != null)
			{
				System.out.println("Has Marks");
				return marks;
			}
			
		}catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("Has no marks");
		return null; 
	}
	
	public void deleteMarks(long id)
	{
		repository.deleteById(id);
	}
	
}
