package cgs.bdl.springboot.mybatis.plus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author sherlock
 *
 * 1.  @MapperScan: 扫描 Mapper 接口的包的全路径
 *
 */

@SpringBootApplication
@MapperScan("cgs.bdl.springboot.mybatis.plus.mapper")
public class MybatisPlusApplication {

	public static void main (String[] args) {
		SpringApplication.run(MybatisPlusApplication.class, args);
	}
}
