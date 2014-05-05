package finley.spring.transaction.scenario;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 	事务级别：
	TRANSACTION_NONE 说明不支持事务。
	TRANSACTION_READ_UNCOMMITTED 说明在提交前一个事务可以看到另一个事务的变化。这样脏读、不可重复的读和虚读都是允许的。
	TRANSACTION_READ_COMMITTED 说明读取未提交的数据是不允许的。这个级别仍然允许不可重复的读和虚读产生。
	TRANSACTION_REPEATABLE_READ 说明事务保证能够再次读取相同的数据而不会失败，但虚读仍然会出现。
	TRANSACTION_SERIALIZABLE 是最高的事务级别，它防止脏读、不可重复的读和虚读。
 */
public class BaseScenario {
	
	protected Runnable reader;
	protected Runnable writer;
	
	protected int commitLevel = TransactionDefinition.ISOLATION_READ_COMMITTED;
	
	private static ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	
	protected  JdbcTemplate template;
	protected  PlatformTransactionManager transactionManager;
	
	public static Object getBean(String beanName){
		if(ctx != null)
			return ctx.getBean(beanName);
		else{
			ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
			return ctx.getBean(beanName);
		}
	}
	
	public void runScenario() throws Exception{
		if(reader != null && writer != null){
			new Thread(reader).start();
			new Thread(writer).start();
		}else 
			throw new Exception("Reader or writer must be init");
	}

	public JdbcTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}

	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	protected void throwException(boolean isThrow){
		if(isThrow)
			throw new RuntimeException();
	}

}
