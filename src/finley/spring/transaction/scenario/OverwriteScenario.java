package finley.spring.transaction.scenario;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 模拟覆盖的场景
 * 这个场景经常出现，两个事务并行修改一条记录并先后提交，导致先一次提交的结果被覆盖掉
 * 危害在于如果覆盖的值是基于这条记录当前的值的话，会导致计算的结果有误，最常见的情景就是某种计数器
 * 这个例子选的也不好，没有体现出锁的价值
 *
 */
public class OverwriteScenario extends BaseScenario {

	public OverwriteScenario(){
		this.reader = new Reader();
		this.writer = new Writer();
	}
	
	/**
	 *	这里不再只是reader，也要修改记录
	 *	3号同学的成绩算错了，多算了5分，所以要在现在的基础上减掉5分
	 */
	private  class Reader implements Runnable{
		public void run(){
			try {
				Thread.currentThread().sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
			definition.setIsolationLevel(commitLevel);
			TransactionStatus status = transactionManager.getTransaction(definition);
			List list = template.queryForList("select * from CLASS_STATISTIC t where t.sid = 3");
			Object score = ((Map)list.get(0)).get("SCORE");
			System.out.println("No 3 student current score:" + score);
			System.out.println("Remove 5 points to that score");
			template.update("update CLASS_STATISTIC t set t.score = ? where t.sid = 3",
					new Object[]{Integer.valueOf(score.toString()) - 5});
			transactionManager.commit(status);
		}
	}
	
	/**
	 *  因为一道题计分错误，所有的同学的成绩都少算了2分，所以要加上2分
	 */
	private class Writer implements Runnable{
		public void run(){
			DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
			definition.setIsolationLevel(commitLevel);
			TransactionStatus status = transactionManager.getTransaction(definition);
			List list = template.queryForList("select * from CLASS_STATISTIC t");
			if(list != null && list.size() > 0){
				for(Object obj:list){
					Object sid = ((Map)obj).get("SID");
					Object score = ((Map)obj).get("SCORE");
					System.out.println("No " + sid + " student current score:" + score);
					System.out.println("Add 2 points to that score");
					template.update("update CLASS_STATISTIC t set t.score = ? where t.sid = ?",
							new Object[]{Integer.valueOf(score.toString()) + 2,sid});
				}
			}
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			transactionManager.commit(status);
		}
	}
}
