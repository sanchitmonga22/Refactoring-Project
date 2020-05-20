package controller;

import java.util.*;
import java.util.ArrayList;

public class Queue {
	private List<Object> list;
	
	/** Queue()
	 * creates a new queue
	 */
	public Queue() {
		list = new ArrayList();
	}

    /** next()
     * @return next element in the queue
     */
	public Object next() {
		return list.remove(0);
	}

    /** add()
     * Adds a new element to the queue
     * @param o element to add
     */
	public void add(Object o) {
		list.add(o);
	}

    /** hasMoreElements()
     * checks to see if the queue is empty
     * @return true if not empty
     */
	public boolean hasMoreElements() {
		return list.size() != 0;
	}

    /** get()
     * get's the element at a given index
     * @param index the index to check
     * @return the element at that index
     */
	public Object get(int index){
		return list.get(index);
	}

    /** size()
     * @return size of the queue
     */
	public int size(){
		return list.size();
	}
}
