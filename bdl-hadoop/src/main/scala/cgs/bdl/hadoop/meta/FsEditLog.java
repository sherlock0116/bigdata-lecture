package cgs.bdl.hadoop.meta;

import lombok.*;


import java.util.LinkedList;

/**
 * Hadoop Fsimage  和 EditsLog 实现原理
 */
public class FsEditLog {

	private long txid = 0L;
	private DoubleBuffer doubleBuffer = new DoubleBuffer();
	private volatile boolean isSyncRunning = false;
	private volatile boolean isWaitSync = false;
	private volatile long syncMaxTxid = 0L;
	private ThreadLocal<Long> localTxid = new ThreadLocal<>();

	/**
	 * 方法加锁的目的就是为了事务 ID 的唯一, 而且是递增
	 * @param content
	 */
	public void logEdit(String content) {
		synchronized (this) {
			txid++;
			localTxid.set(txid);
			EditLog editLog = new EditLog(txid, content);
			doubleBuffer.write(editLog);
		}
		logSync();
	}

	private void logSync () {

		synchronized (this) {
			if (isSyncRunning) {
				Long txid = localTxid.get();
				if (txid <= syncMaxTxid) {
					return;
				}
				if (isWaitSync) {
					return;
				}
				isWaitSync = true;

				while (isSyncRunning) {
					try {
						wait(2000L);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			// 交换内存
			doubleBuffer.setReadyToSync();

			if (doubleBuffer.syncBuffer.size() > 0) {
				syncMaxTxid = doubleBuffer.getSyncMaxTxid();
			}
			isSyncRunning = true;
		}

		doubleBuffer.flush();

		synchronized (this) {
			isSyncRunning = false;
			notifyAll();
		}
	}

	/**
	 * 抽象每条客户端的操作
	 */
	class EditLog {

		/**
		 * 日志的事务 ID, 顺序递增
		 */
		long txid;

		/**
		 * 请求的日志的内容 ,eg, mkdir, ......
		 */
		String content;

		public EditLog (long txid, String content) {
			this.txid = txid;
			this.content = content;
		}

		@Override
		public String toString () {
			return "EditLog{" +
					"txid=" + txid +
					", content='" + content + '\'' +
					'}';
		}
	}

	/**
	 * 双缓冲方案
	 */
	class DoubleBuffer {

		LinkedList<EditLog> currentBuffer = new LinkedList<>();
		LinkedList<EditLog> syncBuffer = new LinkedList<>();

		public void write(EditLog log) {
			currentBuffer.add(log);
		}

		/**
		 * 交换内存
		 */
		public void setReadyToSync() {
			LinkedList<EditLog> tmp = this.currentBuffer;
			currentBuffer = syncBuffer;
			syncBuffer = tmp;
		}

		/**
		 * 获取 SyncBuffer 中日志的最大的 txid
		 * @return
		 */
		public Long getSyncMaxTxid() {
			return syncBuffer.getLast().txid;
		}

		public void flush() {
			for (EditLog log : syncBuffer) {
				System.out.println("===> 存入磁盘的日志信息: " + log.toString());
			}

			// 刷写磁盘后, 清空 SyncBuffer 内存中的数据
			syncBuffer.clear();
		}
	}


	public static void main (String[] args) {

		FsEditLog fsEditLog = new FsEditLog();
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				for (int i1 = 0; i1 < 5000; i1++) {
					fsEditLog.logEdit("日志 log");
				}
			}).start();
		}
	}
}
