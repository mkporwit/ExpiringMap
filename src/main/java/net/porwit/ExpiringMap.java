package net.porwit;

import java.util.HashMap;

public class ExpiringMap<K, V>
{
	private HashMap<K, Value> map = new HashMap<K, Value>();

	private final long DEFAULT_LIFE_MILLIS = 5000;

	private class Value<V> {
		protected V val;
		protected long expiryMillis;

		public Value(V value, long lifeMillis) {
			val = value;
			expiryMillis = System.currentTimeMillis() + lifeMillis;
		}
	}

	public V get(K key) {
		return null;
	}

	public V put(K key, V value) {
		return put(key, value, DEFAULT_LIFE_MILLIS);
	}

	public V put(K key, V value, long lifeMillis) {
		Value<V> v = new Value<V>(value, lifeMillis);
		Value<V> ret = map.put(key, v);
		System.out.println("ret is " + ret == null ? "null" : ret);
		System.out.println("ret.val is " + ret.val == null ? "null" : ret.val);
		if(ret != null)
		{
			return ret.val;
		} 
		return null;
	}
}
