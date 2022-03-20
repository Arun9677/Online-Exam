package Exam.Online.Exam;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long>
{
	@Query("SELECT u FROM User u WHERE u.email = ?1 ")
	public User getByEmail(String mail);
	
	@Query("SELECT u FROM User u WHERE u.username = ?1 ")
	public User getByName(String name);
	
	@Query("SELECT u FROM User u WHERE u.verificationcode = ?1 ")
	public User getByCode(String code);
	
	@Query("SELECT u FROM User u WHERE u.role = ?1 ")
	public List<User> getByRole(String role);
	
//	@Query("SELECT u FROM User u CONCAT(u.email,' ',u.username) LIKE %?1% ")
//	public List<User> searchKey(String keyword);

}
