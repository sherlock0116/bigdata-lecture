package cgs.bigdata.calcite

import org.apache.calcite.rel.`type`.{RelDataType, RelDataTypeFactory}
import org.apache.calcite.schema.impl.AbstractTable


/**
 *
 */
class AbsTable(tableSchema: TableSchema) extends AbstractTable {
	
	override def getRowType(typeFactory: RelDataTypeFactory): RelDataType = {
		val builder: RelDataTypeFactory.FieldInfoBuilder = typeFactory.builder()
		tableSchema.schemas.foreach {tbSchema =>
			builder.add(tbSchema.name, tbSchema.rowType)
		}
		builder.build()
	}
}

object AbsTable {
	
	def apply(tableSchema: TableSchema): AbsTable = new AbsTable(tableSchema)
}
