package cgs.bdl.springboot.mybatis.plus;

import cgs.bdl.springboot.mybatis.plus.entity.User;
import cgs.bdl.springboot.mybatis.plus.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MybatisPlusApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MybatisPlusApplicationTest {

	@Autowired
	private UserMapper userMapper;

	@Test
	public void findAll() {
		List<User> users = userMapper.selectList(null);
		System.out.println("user list size: " + users.size());
	}
}
