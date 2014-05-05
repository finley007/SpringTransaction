package finley.spring.transaction.scenario;

import java.util.List;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *	模拟虚读场景
 *  这个不是对某一条记录而言，而是对一张表而言，一个事务按某种条件扫描一张表，另一个事务却在插入和删除数据
 *  最终导致前一个事务查到的数据是不准确的
 */
public class ImageReadScenario extends BaseScenario {

	public ImageReadScenario(){
		this.reader = new Reader();
		this.writer = new Writer();
	}
	
	/**
	 *	启动时每隔100ms读取一次记录总数，读取5次
	 */
	private  class Reader implements Runnable{
		public void run(){
			DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
			TransactionStatus status = transactionManager.getTransaction(definition);
			int count = 5;
			while(count-- > 0){
				System.out.println("Reader get the record count");
				long num = template.queryForLong("select count(*) from CLASS_STATISTIC t");
				System.out.println(num);
				try {
					Thread.currentThread().sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			transactionManager.commit(status);
		}
	}
	
	/**
	 *  插入数据
	 */
	private class Writer implements Runnable{
		public void run(){
			int startId = 10;
			int count = 5;
			while(count -- > 0){
				System.out.println("insert record");
				try{
					template.update("insert into CLASS_STATISTIC t values(?,'C','90')",new Object[]{startId});
				}catch(Exception e){
					e.printStackTrace();
				}
				startId ++;
				try {
					Thread.currentThread().sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			template.update("delete from CLASS_STATISTIC t where t.cid = 'C'");
		}
	}
}
