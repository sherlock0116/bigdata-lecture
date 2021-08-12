package cgs.bigdata.calcite


case class Row(name: String, rowType: String)
case class TableSchema(name: String, schemas: Seq[Row])
