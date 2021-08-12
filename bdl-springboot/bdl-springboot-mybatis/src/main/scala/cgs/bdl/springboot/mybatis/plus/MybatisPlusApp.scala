package cgs.bdl.springboot.mybatis.plus

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 *
 */

@SpringBootApplication
@MapperScan(Array("cgs.bdl.springboot.mybatis.plus.mapper"))
class MybatisPlusApp {
	
}

object MybatisPlusApp extends App {
	
	SpringApplication.run(classOf[MybatisPlusApp])
}