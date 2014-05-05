package finley.spring.transaction.scenario;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import finley.spring.transaction.ITransaction;


/**
 *	模拟脏读的场景
 *  在一个事务读取一条记录的同时另外一个事务在修改这条记录
 *  导致读取事务拿到的并不是这条记录的最终结果
 *  脏读会有什么问题？如下场景：像转账中的限额校验，事实上如果另一笔事务已经更改了日限额，只是还没有提交，这时又来了一笔转账,
 *  按说这笔转账是不允许发生的，但由于脏读就有可能发生
 *  我的理解有误：上面所说的并不是脏读的含义，而是一个事务可以看到另一个事务未经提交的中间结果，因为一般数据库默认的隔离级别都是
 *  read commit 所以基本不会发生这个问题。我说的那个转账问题应该属于不可重复读取，因为两次读取了commit之后的不一致数据
 */
public class DirtyReadScenario extends BaseScenario {

	public DirtyReadScenario(){
		this.reader = new Reader();
		this.writer = new Writer();
	}
	
	/**
	 *	500ms后才读取数据
	 */
	private  class Reader implements Runnable{
		public void run(){
			DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
			definition.setIsolationLevel(commitLevel);
			System.out.println(definition.getIsolationLevel());
			TransactionStatus status = transactionManager.getTransaction(definition);
			try {
				Thread.currentThread().sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Reader get the record");
			template.update("select * from CLASS_STATISTIC t where t.sid = 1 for update");//oracle的锁不支持对与查询的阻塞，可以用这种办法实现一个阻塞的查询
			List list = template.queryForList("select * from CLASS_STATISTIC t where t.sid = 1");
			if(list != null && list.size() > 0){
				System.out.println(list.get(0).toString());
			}
			transactionManager.commit(status);
		}
	}
	
	/**
	 *  一开始就更改数据，但是1000ms后才提交
	 */
	private class Writer implements Runnable{
		public void run(){
			DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
			definition.setIsolationLevel(commitLevel);
			System.out.println(definition.getIsolationLevel());
			TransactionStatus status = transactionManager.getTransaction(definition);
			try{
				System.out.println("Writer change the record");
				template.update("update CLASS_STATISTIC t set t.score = 100 where t.sid = 1");
				try {
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				throwException(false);
				transactionManager.commit(status);
				System.out.println("Writer commit");
			}catch(Exception e){
				transactionManager.rollback(status);
			}
		}
	}
}
