package finley.spring.transaction;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import finley.spring.transaction.scenario.BaseScenario;
import finley.spring.transaction.scenario.DirtyReadScenario;
import finley.spring.transaction.scenario.TestScenario;

public class Main {

	
	public static void main(String[] args) {
		testTransactionScenario();
		
	}
	
	private static void testDB(){
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		JdbcTemplate template = (JdbcTemplate)ctx.getBean("jdbcTemplate");
		List list = template.queryForList("select * from CLASS_STATISTIC t where t.sid = 1");
		if(list != null && list.size() > 0){
			System.out.println(list.get(0).toString());
		}
		template.update("update CLASS_STATISTIC t set t.score = 99 where t.sid = 1");
	}
	
	private static void testTestScenario(){
		BaseScenario sn = new TestScenario();
		try {
			sn.runScenario();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void testDirtyReadScenario(){
		BaseScenario sn = (BaseScenario)BaseScenario.getBean("dirtyRead");
		try {
			sn.runScenario();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void testOverwriteScenario(){
		BaseScenario sn = (BaseScenario)BaseScenario.getBean("overwrite");
		try {
			sn.runScenario();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void testUnrepeatableScenario(){
		BaseScenario sn = (BaseScenario)BaseScenario.getBean("unrepeatable");
		try {
			sn.runScenario();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void testImageReadScenario(){
		BaseScenario sn = (BaseScenario)BaseScenario.getBean("imageread");
		try {
			sn.runScenario();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void testTransactionScenario(){
		BaseScenario sn = (BaseScenario)BaseScenario.getBean("transactionScenario");
		try {
			sn.runScenario();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}