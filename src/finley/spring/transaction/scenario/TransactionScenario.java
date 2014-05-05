package finley.spring.transaction.scenario;

import java.util.List;

import org.omg.CORBA.CTX_RESTRICT_SCOPE;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import finley.spring.transaction.ITransaction;

public class TransactionScenario extends BaseScenario {

	public TransactionScenario(){
		this.reader = new Reader();
		this.writer = new Writer();
	}
	
	private  class Reader implements Runnable{
		public void run(){
			ITransaction trs = (ITransaction)getBean("transaction");
			trs.doTransaction();
		}
	}
	
	private class Writer implements Runnable{
		public void run(){
		}
	}
	
}
