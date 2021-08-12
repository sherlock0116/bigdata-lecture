
namespace java cgs.bigdata.thrift.example.service

service HelloWorldService {

	void ping(),
	string sayHello(1: string name)
}