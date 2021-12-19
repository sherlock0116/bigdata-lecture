package cgs.bdl.springboot.mybatis.plus.config

import com.baomidou.mybatisplus.core.injector.ISqlInjector
import com.baomidou.mybatisplus.extension.plugins.{OptimisticLockerInterceptor, PaginationInterceptor}
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.{Bean, Configuration}

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */

@Configuration
@MapperScan(Array("cgs.bdl.springboot.mybatis.plus.mapper"))
class MybatisPlusConfiguration {
	
	// 乐观锁插件
	@Bean
	def optimisticLockerInterceptor(): OptimisticLockerInterceptor = {
		new OptimisticLockerInterceptor
	}
	
	// 分页插件
	@Bean
	def paginationInterceptor(): PaginationInterceptor = {
		new PaginationInterceptor
	}
	
	// 逻辑删除插件(从 3.1.1 把那本开始不需要配置该插件)
	/*@Bean
	def sqlInjector(): ISqlInjector = {
		new LogicSqlInjector();
	}*/
}
