import java.util.LinkedList;

public class Queue<T> {

	protected LinkedList<T> queue = new LinkedList<T>();
	private boolean endDay;

	public synchronized void insert(T t) {
		queue.add(t);
	}
	
	//this method returns the first element in queue. if no elements exist, awaits
	public synchronized T extract(){
		while(queue.isEmpty() && !endDay){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(endDay){
			return null;
		}
		else return queue.remove();
	}

	public boolean isEmpty(){
		return queue.isEmpty();
	}

	public void endTheDay(){
		endDay = true;
	}
}
