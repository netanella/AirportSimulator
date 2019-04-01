
//bounded Queue is a Queue with a max size limit
public class BoundedQueue<T> extends Queue<T> {
	private final int maxSize;

	public BoundedQueue(int maxSize){
		super();
		this.maxSize = maxSize;
	}
	
	//wait while the queue size reaches the limit
	public synchronized void insert(T t) {
		while(queue.size()==maxSize){
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		queue.add(t);
	}

}
