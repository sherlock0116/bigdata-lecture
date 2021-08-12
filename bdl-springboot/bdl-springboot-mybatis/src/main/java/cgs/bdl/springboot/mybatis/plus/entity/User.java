package cgs.bdl.springboot.mybatis.plus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 *
 */
@Data
@Builder
@TableName("dim_user")
public class User {

	@TableId(value = "user_id", type = IdType.NONE)
	private String id;
	@TableField("user_name")
	private String name;
	@TableField("user_age")
	private int age;
}
