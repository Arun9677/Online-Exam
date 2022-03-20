package Exam.Online.Exam;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

	@Autowired
	UserRepository repository;
	
	public void save(User user)
	{
		repository.save(user);
	}
	
	public User findById(long id)
	{
		return repository.getById(id);
	}
	
	public User findByEmail(String email)
	{
		return repository.getByEmail(email);
	}
	
	public User findByName(String name)
	{
		return repository.getByName(name);
	}
	
	public User findByCode(String code)
	{
		return repository.getByCode(code);
	}
	
	public List<User> listByRole(String role)
	{
		return repository.getByRole(role);
	}
	
//	public List<User> searchUser(String keyword)
//	{
//		return repository.searchKey(keyword);
//	}
	
	public List<User> listAll()
	{
		return repository.findAll();
	}
	
	public void deleteUser(long id)
	{
		repository.deleteById(id);
	}
	
}
