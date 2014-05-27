package finley.spring.transaction;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import finley.spring.transaction.util.SpringConstants;

public class SubTransactionImpl implements ITransaction {

	protected  JdbcTemplate template;
	
	public JdbcTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}

	@Override
//	@Transactional(propagation=Propagation.REQUIRED)
	public void doTransaction() {
		// TODO Auto-generated method stub
		template.update("update CLASS_STATISTIC t set t.score = " + SpringConstants.VALUE + " where t.sid = 2");
		throw new RuntimeException();
	}

}
