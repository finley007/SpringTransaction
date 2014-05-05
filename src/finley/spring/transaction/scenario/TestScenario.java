package finley.spring.transaction.scenario;

public class TestScenario extends BaseScenario {

	public TestScenario(){
		this.reader = new Reader();
		this.writer = new Writer();
	}
	
	private static class Reader implements Runnable{
		public void run(){
			int count = 100;
			while(count-- > 0){
				System.out.println("Reader:" + count);
			}
		}
	}
	
	private static class Writer implements Runnable{
		public void run(){
			int count = 100;
			while(count-- > 0){
				System.out.println("Writer:" + count);
			}
		}
	}
}
