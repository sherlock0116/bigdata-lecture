package cgs.bdl.springboot.mybatis.plus

import cgs.bdl.springboot.mybatis.plus.entity.User
import cgs.bdl.springboot.mybatis.plus.mapper.UserMapper
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

import java.util
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
	
	@Test
	def selectById(): Unit ={
		val user: User = userMapper.selectById("0245")
		println(s"===> User: [$user]")
	}
	
	@Test
	def selectBatchByIds(): Unit = {
		val users: Seq[User] = userMapper.selectBatchIds(Seq("0023", "0245").asJava).asScala
		users.foreach(println)
	}
	
	@Test
	def addUser(): Unit = {
		val user: User = User("0245", "yanjun", 28)
		val rs: Int = userMapper.insert(user)
		println(s"受影响的行数: $rs")
	}
	
	@Test
	def updateUser(): Unit = {
		val user: User = new User()
		user.setId("0245")
		user.setAge(29)
		val rs: Int = userMapper.updateById(user)
		println(s"受影响的行数: $rs")
	}
	
	@Test
	def addUserForOptimisLock(): Unit = {
		val user: User = new User()
		user.setId("0246")
		user.setName("regina")
		user.setAge(29)
		val rs: Int = userMapper.insert(user)
		println(s"受影响的行数: $rs")
	}
	
	@Test
	def updateByOptimisLock(): Unit = {
		// 乐观锁修改表数据
		val user: User = userMapper.selectById("0246")
		user.setAge(25)
		val rs: Int = userMapper.updateById(user)
		println(s"受影响的行数: $rs")
	}
	
	@Test
	def pageSelect(): Unit = {
		// 分页查询
		val page: Page[User] = new Page[User](1, 3)
		userMapper.selectPage(page, null)
		println(
			s"""
			   |当前页: ${page.getCurrent}
			   |当前页的记录: ${page.getRecords}
			   |当前页的记录数: ${page.getSize}
			   |查询的总记录数: ${page.getTotal}
			   |查询结果的总页数: ${page.getPages}
			   |================================当前页码: ${page.getCurrent}
			   |""".stripMargin)
		
	}
	
	@Test
	def logicalDelete(): Unit = {
		val rs: Int = userMapper.deleteById("0023")
		println(s"受影响的行数: $rs")
	}
	
	@Test
	def selectByCondition(): Unit = {
		// 创建 QueryWrapper
		/*
			常用的条件有:
				1. ge, gt, le, lt
				2. eq, ne
				3. between, notBetween
				4. like, notLike, likeLeft, likeRight
				5. orderBy, orderByDesc, orderByAsc
				6. last
				7. 查询指定的列
		 */
		val wrapper: QueryWrapper[User] = new QueryWrapper[User]()
		wrapper.ge("user_age", 20)
				.orderByAsc("user_age")
				.like("user_name", "zhou")
				.select("user_name")
		val users: Seq[User] = userMapper.selectList(wrapper).asScala
		println(s"===> result size: [${users.foreach(user => println(user.name))}]")
	}
}
