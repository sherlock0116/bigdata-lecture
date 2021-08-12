package cgs.bigdata.thrift.example.impl;

import cgs.bigdata.thrift.example.service.HelloWorldService;
import org.apache.thrift.TException;


public class HelloWordServiceImpl implements HelloWorldService.Iface {

	@Override
	public void ping () throws TException {

	}

	@Override
	public String sayHello (String name) throws TException {
		return "Hello " + name + ", welcome to use thrift !";
	}
}
