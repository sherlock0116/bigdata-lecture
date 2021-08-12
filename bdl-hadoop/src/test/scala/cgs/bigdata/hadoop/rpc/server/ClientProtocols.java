package cgs.bigdata.hadoop.rpc.server;

/**
 *
 */
public interface ClientProtocols {

	long versionID = 1314L;
	// 模拟一个 protocol
	void makeDir(String path);
}
