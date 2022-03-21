package Exam.Online.Exam;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Test{
	
	@Id
	long id;
	String ans;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAns() {
		return ans;
	}
	public void setAns(String ans) {
		this.ans = ans;
	}

}
