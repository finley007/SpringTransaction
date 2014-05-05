package finley.spring.transaction;

import org.springframework.jdbc.core.JdbcTemplate;

public class TransactionImpl implements ITransaction {

	protected  JdbcTemplate template;
	private ITransaction subTransaction;
	
	public JdbcTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}

	@Override
	public void doTransaction() {
		// TODO Auto-generated method stub
		template.update("update CLASS_STATISTIC t set t.score = 70 where t.sid = 1");
//		try {
			subTransaction.doTransaction();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		throw new RuntimeException();
	}

	public ITransaction getSubTransaction() {
		return subTransaction;
	}

	public void setSubTransaction(ITransaction subTransaction) {
		this.subTransaction = subTransaction;
	}

}
