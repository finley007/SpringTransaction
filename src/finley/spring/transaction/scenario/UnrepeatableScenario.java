package finley.spring.transaction.scenario;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *	模拟不可重复读取的场景
 *	一个事务中会多次读取一条记录，二次过程中另一个事务修改这条记录，导致第一个事务在多次读取中读到的记录不一致
 *  为什么要在一个事务中多次读取同一条记录还没想到具体的应用场景，不过可以假设这样的场景：多次按照不同的字段选取了同一条记录
 */
public class UnrepeatableScenario extends BaseScenario {

	public UnrepeatableScenario(){
		this.reader = new Reader();
		this.writer = new Writer();
	}
	
	/**
	 *	先读取学生2的成绩A，过一段时间再查询成绩大于等于A的学生，发现结果没有学生2
	 */
	private  class Reader implements Runnable{
		public void run(){
			System.out.println("Reader query No 2 student's score");
			List list = template.queryForList("select * from CLASS_STATISTIC t where t.sid = 2");
			Object score = ((Map)list.get(0)).get("SCORE");
			System.out.println("No 2 student's score: " + score);
			try {
				Thread.currentThread().sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Query all the students that score equals to " + score);
			list = template.queryForList("select * from CLASS_STATISTIC t where t.score = ?",new Object[]{score});
			if(list != null && list.size() > 0){
				System.out.println("Student " + ((Map)list.get(0)).get("SID"));
			}else
				System.out.println("No body");
			
		}
	}
	
	/**
	 *  更改学生2的成绩，加上2分
	 */
	private class Writer implements Runnable{
		public void run(){
			DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
			TransactionStatus status = transactionManager.getTransaction(definition);
			try {
				Thread.currentThread().sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Change No 2 student's score");
			List list = template.queryForList("select * from CLASS_STATISTIC t where t.sid = 2");
			Object score = ((Map)list.get(0)).get("SCORE");
			template.update("update CLASS_STATISTIC t set t.score = ? where t.sid = 2",
					new Object[]{Integer.valueOf(score.toString()) + 2});
			transactionManager.commit(status);
		}
	}
}
