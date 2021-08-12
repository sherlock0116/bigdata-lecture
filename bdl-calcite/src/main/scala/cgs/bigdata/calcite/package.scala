package cgs.bigdata

import org.apache.calcite.rel.`type`.RelDataTypeSystemImpl
import org.apache.calcite.sql.`type`.{BasicSqlType, SqlTypeName}

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
package object calcite {
	
	implicit def typeConverter(`type`: String): BasicSqlType = {
		`type` match {
			case str if str.toLowerCase == "string" =>
				new BasicSqlType(new RelDataTypeSystemImpl() {}, SqlTypeName.CHAR)
			case str if str.toLowerCase == "int" =>
				new BasicSqlType(new RelDataTypeSystemImpl() {}, SqlTypeName.INTEGER)
			
		}
	}
}
