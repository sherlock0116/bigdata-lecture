
### SelectionKey
SelectionKey: 它表示 Selector 与网络通道的注册关系, 共四种:
1. int OP_ACCEPT: 有新的网络连接可以 accept, 值为 16
2. int OP_CONNECT: 连接已经建立, 值为 8
3. int OP_READ: 读操作, 值为 1
4. int OP_WRITE: 写操作, 值为 4

源码中:
```java
public class SelectionKey {
	public static final int OP_ACCEPT = 1 << 4;
	public static final int OP_CONNECT = 1 << 3;
	public static final int OP_READ = 1 << 0;
	public static final int OP_WRITE = 1 << 2;
}
```
