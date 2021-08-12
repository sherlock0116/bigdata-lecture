package cgs.bdl.springboot.mybatis.plus

import cgs.bdl.springboot.mybatis.plus.entity.User
import cgs.bdl.springboot.mybatis.plus.mapper.UserMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

import scala.collection.JavaConverters._

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */

@RunWith(classOf[SpringRunner])
@SpringBootTest(classes = Array(classOf[MybatisPlusApp]), webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MybatisPlusAppTest {
	
	@Autowired
	var userMapper: UserMapper = _
	
	@Test
	def selectList(): Unit = {
		val users: Seq[User]= userMapper.selectList(null).asScala
		println(s"=====> users size: ${users.size}")
	}
}
