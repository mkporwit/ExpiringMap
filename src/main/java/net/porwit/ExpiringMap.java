package net.porwit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class ExpiringMap<K, V>
{
	private ConcurrentHashMap<K, Value> map = new ConcurrentHashMap<K, Value>();
	private PriorityQueue<Key> pq = new PriorityQueue<Key>();
	
	private Timer cleanupTimer;
	
	public ExpiringMap(long cleanupMillis) {
		cleanupTimer = new Timer();
		cleanupTimer.scheduleAtFixedRate(new CleanupTask(this), cleanupMillis, cleanupMillis);
	}
	
	private class Key implements Comparable<Key>{
		protected K key;
		protected long expiryMillis;
		
		public Key(K key, long expiryMillis) {
			this.key = key;
			this.expiryMillis = expiryMillis;
		}

		@Override
		public int compareTo(Key k) {
			if(this.expiryMillis < k.expiryMillis) {
				return -1;
			} else if(this.expiryMillis > k.expiryMillis) {
				return 1;
			}
			return 0;
		}
	}
	
	public int size() {
		return map.size();
	}
	
	private class Value {
		protected V val;
		protected long expiryMillis;

		public Value(V value, long expiryMillis) {
			val = value;
			this.expiryMillis = expiryMillis;
		}
	}

	public V get(K key) {
		Value v = map.get(key);
		if(v != null) {
			long curTime = System.currentTimeMillis();
			if(curTime <= v.expiryMillis) {
				return v.val;
			} else {
				return null;
			}
		}
		return null;
	}

	public V put(K key, V value, long lifeMillis) {
		long expiryMillis = System.currentTimeMillis() + lifeMillis;
		Value v = new Value(value, expiryMillis);
		Key k = new Key(key, expiryMillis);
		pq.add(k);
		Value ret = map.put(key, v);
		if(ret == null) {
			return null;
		} else {
			return ret.val;
		}
	}
	
	public void cleanup() {
		Key k = pq.peek();
		Set<Key> delSet = new HashSet<>();
		long curTime = System.currentTimeMillis();
		while(k != null && k.expiryMillis < curTime) {
			delSet.add(pq.remove());
			k = pq.peek();
		}
		
		Iterator<ExpiringMap<K, V>.Key> keyIter = delSet.iterator();
		while(keyIter.hasNext()) {
			k = keyIter.next();
			map.remove(k.key);
		}
	}
	
	class CleanupTask extends TimerTask {

		ExpiringMap<K, V> map;
		
		public CleanupTask(ExpiringMap<K, V> expiringMap) {
			map = expiringMap;
		}

		@Override
		public void run() {
			map.cleanup();
		}
		
	}
}
