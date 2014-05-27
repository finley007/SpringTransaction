package finley.spring.transaction;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface ITransaction {

	public void doTransaction();
	
}
