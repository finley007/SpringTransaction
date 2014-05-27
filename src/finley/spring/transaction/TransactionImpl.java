package finley.spring.transaction;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import finley.spring.transaction.util.SpringConstants;

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
	//事务标签可以配置在接口级别也可以配置在类级别，如果不配置则该方法中的操作会立即提交
//	@Transactional(propagation=Propagation.REQUIRED)
	public void doTransaction() {
		// TODO Auto-generated method stub
		template.update("update CLASS_STATISTIC t set t.score = " + SpringConstants.VALUE + " where t.sid = 1");
		try {
			subTransaction.doTransaction();
		}catch(Exception e){
			e.printStackTrace();
		}
//		throw new RuntimeException();
	}

	public ITransaction getSubTransaction() {
		return subTransaction;
	}

	public void setSubTransaction(ITransaction subTransaction) {
		this.subTransaction = subTransaction;
	}

}
