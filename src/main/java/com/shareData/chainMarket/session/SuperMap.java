package com.shareData.chainMarket.session;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class SuperMap<K, V> {
	/*
	 *并发KEY-VALUE容器SuperMap<Integer,class>介绍 
	 *SuperMap要解决什么问题？它比ConcurrentHashMap<> 优点在哪儿？
	 *当我们查询一个主键,如果不存在，则NEW 一个新类进去，并初始化一定数据，如果是传统MAP 无论是不是线程安全都无法保证线程安全
	 *案例1
	 *  if(get(key)==null){
	 *     map.put(new POJO(Object initData))
	 *  }
	 *  传统线程安全ConcurrentHashMap<>
	 *  因为当进程1get到一个空的VALUE，进入判断内部，当进程1还没有PUT到一个新实体类的时候，进程2再次GET到一个空的对象也依次进入，
	 *  所以此时无论这个容器是否是线程安全，他们都会NEW 一个对象并PUT进去，后者会覆盖掉前者
	 *  而我们GET到实体类 也是一个引用，我们修改参数的时候是不需要PUT的，因为你拿到了引用，直接修改引用就会修改值
	 *  这样所谓传统MAP put的线程安全就是形同虚设。
	 *  SuperMap 对提供三个API initObj(int,V),get(int),delete(int)
	 *  在SuperMap 看来 put是完全没有必要的（对于VALUE是实体类）,因为你GET 就拿到了引用，修改数值无需PUT
	 *  所以SUPERMAP 只提供初始化initObj(int,V)，它会返回布尔值，这个结果告诉你是否这个主键已经存在了。
	 *  所以SuperMap把案例1转化为：
	 *  do{
	 *   if(get(key)==null){
	 *     if(!map.initObj(new POJO(object initData))){
	 *       break;
	 *     }
	 *   }else{
	 *     XXXXXXXXX
	 *     break;
	 *   }
	 *  }while(true);
	 *  如果返回TRUE 说明这个主键已经存在返回重新get 获取到已经初始化后的数据后再次执行XXXX 最后跳出
	 *  SuperMap  get() 同样也是无锁的，保证高效率的应用
	 * */
	private static final boolean RED = true;
	private static final boolean BLACK = false;
	private Node<V> root;

	private class Node<V> {
		int key;
		V val;
		Node<V> left, right;
		int N;
		boolean color;
		private ReentrantReadWriteLock rwl;
		private AtomicReference<Integer> ato;
		private int nownub;
		private boolean together = false;// 是否处于并发状态

		public boolean isTogether() {
			return together;
		}

		public void setTogether(boolean together) {
			this.together = together;
		}

		public boolean unlock() {
			nownub = ato.get();
			return ato.compareAndSet(nownub, nownub + 1);
		}

		Node(int key, V val, int N, boolean color) {
			this.key = key;
			this.val = val;
			this.N = N;
			this.color = color;
			this.rwl = new ReentrantReadWriteLock();
			ato = new AtomicReference<Integer>();
			ato.set(0);
		}

		public ReentrantReadWriteLock getRwl() {
			return rwl;
		}

	}

	public synchronized boolean initObj(int key, V val) {
		if (root != null) {
			root.setTogether(false);
		}
		root = put(root, key, val);
		root.color = BLACK;
		boolean bm = root.isTogether();
		return bm;
	}

	public V get(int key) {
		V val = null;
		Node<V> node;
		node = root;
		boolean bm = true;
		do {
			if (node != null) {
				ReadLock lock = node.getRwl().readLock();
				lock.lock();
				int old = node.key;
				if (old > key) {
					node = node.left;
				} else if (old < key) {
					node = node.right;
				} else {
					// 更新并发值
					do {
						if (node.unlock()) {
							val = node.val;
							break;
						}
					} while (true);
					val = node.val;
					bm = false;
				}
				lock.unlock();
			} else {
				bm = false;
			}
		} while (bm);

		return val;
	}

	private void exchangeValue(Node<V> x, Node<V> h) {// 跟他的左子树交换值
		x.key = h.key;
		x.val = h.val;
	}

	private void exchangeValueSelf(Node<V> x, Node<V> h) {// 跟他的左子树交换值
		int key = x.key;
		V val = x.val;
		x.key = h.key;
		x.val = h.val;
		h.key = key;
		h.val = val;
	}

	private void exchange(Node<V> x, Node<V> h) {// 完全交换两个节点
		x.key = h.key;
		x.val = h.val;
		x.left = h.left;
		x.right = h.right;
	}

	private Node<V> put(Node<V> h, int key, V val) {
		if (h == null) {
			return new Node<V>(key, val, 1, RED);
		}
		int old = h.key;
		if (key > old) {
			do {
				if (h.unlock()) {
					h.right = put(h.right, key, val);
					if (h.right.isTogether()) {// 产生并发
						h.right.setTogether(false);
						h.setTogether(true);
					}
					break;
				}
			} while (true);
		} else if (key < old) {
			do {
				if (h.unlock()) {
					h.left = put(h.left, key, val);
					if (h.left.isTogether()) {// 产生并发
						h.left.setTogether(false);
						h.setTogether(true);
					}
					break;
				}
			} while (true);
		} else {// 有并发产生
			h.setTogether(true);
		}
		WriteLock lock = h.getRwl().writeLock();
		lock.lock();
		if (isRed(h.right) && !isRed(h.left)) {
			h = rotateLeft(h);
		}
		if (isRed(h.left) && isRed(h.left.left)) {
			h = rotateRight(h);
		}
		if (isRed(h.left) && isRed(h.right)) {
			flipColors(h);
		}
		lock.unlock();
		return h;
	}

	private Node<V> blance(Node<V> h, int key) {
		if (h == null) {
			return null;
		}
		int old = h.key;
		if (key > old) {
			h.right = blance(h.right, key);
		} else if (key < old) {
			h.left = blance(h.left, key);
		}
		if (isRed(h.right) && !isRed(h.left)) {
			h = rotateLeft(h);
		}
		if (isRed(h.left) && isRed(h.left.left)) {
			h = rotateRight(h);
		}
		if (isRed(h.left) && isRed(h.right)) {
			flipColors(h);
		}
		return h;
	}

	public void delete(int key) {
		Node<V> node;
		node = root;
		boolean bm = true;
		do {
			if (node != null) {
				WriteLock lock = node.getRwl().writeLock();
				lock.lock();
				int old = node.key;
				if (old > key) {
					if (node.left != null && node.left.key == key) {
						delete(node.left, node, true);
						bm = false;
					} else {
						node = node.left;
					}
				} else if (old < key) {
					if (node.right != null && node.right.key == key) {
						delete(node.right, node, false);
						bm = false;
					} else {
						node = node.right;
					}
				} else {
					delete(node, null, true);
					bm = false;
				}
				lock.unlock();
			} else {
				bm = false;
			}
		} while (bm);

	}

	private void delete(Node<V> h, Node<V> father, boolean b) {
		Node<V> left = h.left;
		Node<V> right = h.right;
		if (left == null) {// 最底层节点
			if (father != null) {// 跟节点的叶子节点删除
				if (b) {
					if (father.right != null) {
						exchangeValueSelf(father, h);
						exchangeValueSelf(father, father.right);
						father.left.color = RED;
						father.right = null;
						blance(root, father.left.key);
					} else {
						father.left = null;
					}
				} else {
					father.right = null;
					if (father.left != null) {
						father.left.color = RED;
						blance(root, father.left.key);
					}

				}
			} else {
				root = null;// 根节点删除
			}
		} else {
			if (left.right != null) {
				Node<V> max = left.right;
				do {
					if (max.right == null) {
						break;
					} else {
						left = max;
						max = max.right;
					}
				} while (true);
				// 值交换max 与h
				exchangeValue(h, max);
				left.right = null;
				if (left.left != null) {
					left.left.color = RED;
					blance(root, left.left.key);
				}
			} else {
				if (right != null) {
					if (right.left != null) {
						Node<V> min = right.left;
						do {
							if (min.left == null) {
								break;
							} else {
								right = min;
								min = min.left;
							}
						} while (true);
						// 交换min 与h
						exchangeValue(h, min);
						right.right = null;
						if (right.left != null) {
							right.left.color = RED;
							blance(root, right.left.key);
						}
					} else {
						exchangeValue(h, right);
						h.right = null;
						if (h.left != null) {
							h.left.color = RED;
							blance(root, h.left.key);
						}
						// 直接将right 与h交换 并将right.left =left
					}
				} else {
					exchange(h, left);
					blance(root, left.key);
					// 直接将left 与h交换
				}
			}
		}

	}

	private Node<V> moveRedLeft(Node<V> h) {
		flipColors(h);
		if (isRed(h.right.left)) {
			h.right = rotateRight(h.right);
			h = rotateLeft(h);
		}
		return h;
	}

	private Node<V> moveRedRight(Node<V> h) {
		flipColors(h);
		if (!isRed(h.left.left)) {
			h = rotateRight(h);
		}
		return h;
	}

	private boolean isRed(Node<V> x) {
		if (x == null) {
			return false;
		}
		return x.color == RED;
	}

	private void flipColors(Node<V> h) {
		h.color = RED;
		h.left.color = BLACK;
		h.right.color = BLACK;
	}

	private Node<V> rotateLeft(Node<V> h) {//
		Node<V> x = h.right;
		h.right = x.left;
		x.left = h;
		x.color = h.color;
		h.color = RED;
		x.N = h.N;
		return x;
	}

	private Node<V> rotateRight(Node<V> h) {
		Node<V> x = h.left;
		h.left = x.right;
		x.right = h;
		x.color = h.color;
		h.color = RED;
		x.N = h.N;
		return x;
	}
}
