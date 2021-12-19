package cgs.bdl.springboot.mybatis.plus

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import org.apache.ibatis.reflection.MetaObject
import org.springframework.stereotype.Component

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */

@Component
class MybatisPlusMetaObjHandler extends MetaObjectHandler {
	
	override def insertFill(metaObject: MetaObject): Unit = {
		
		this.setFieldValByName("version", 1, metaObject)
	}
	
	override def updateFill(metaObject: MetaObject): Unit = {
	
	}
}
