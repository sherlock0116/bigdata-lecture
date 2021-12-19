package cgs.bdl.springboot.mybatis.plus.entity

import com.baomidou.mybatisplus.annotation._

import scala.beans.BeanProperty

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */

@TableName("dim_user")
class User() {
	
	@BeanProperty
	@TableId(value = "user_id", `type` = IdType.NONE)
	var id: String = _
	
	@BeanProperty
	@TableField(value = "user_name")
	var name: String = _
	
	@BeanProperty
	@TableField(value = "user_age")
	var age: Int = _
	
	@BeanProperty
	@Version
	@TableField(fill = FieldFill.INSERT)
	var version: Int = _
	
	@BeanProperty
	@TableLogic
	var deleted: Int = _
	
	override def toString: String = {
		s"User: [id=$id, name=$name, age=$age]"
	}
}

object User {
	
	def apply(id: String, name: String, age: Int): User = {
		val user: User = new User()
		user.id = id
		user.name = name
		user.age = age
		user
	}
	
}
