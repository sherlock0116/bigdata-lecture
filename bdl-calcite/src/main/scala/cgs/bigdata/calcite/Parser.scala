package cgs.bigdata.calcite


import org.apache.calcite.config.{CalciteConnectionConfig, CalciteConnectionConfigImpl}
import org.apache.calcite.jdbc.CalciteSchema
import org.apache.calcite.plan.{Context, ConventionTraitDef}
import org.apache.calcite.prepare.CalciteCatalogReader
import org.apache.calcite.rel.RelDistributionTraitDef
import org.apache.calcite.rel.`type`.{RelDataTypeFactory, RelDataTypeSystem}
import org.apache.calcite.rex.RexBuilder
import org.apache.calcite.schema.SchemaPlus
import org.apache.calcite.sql.SqlNode
import org.apache.calcite.sql.`type`.SqlTypeFactoryImpl
import org.apache.calcite.sql.fun.SqlStdOperatorTable
import org.apache.calcite.sql.parser.SqlParser
import org.apache.calcite.sql.validate.{SqlConformance, SqlConformanceEnum, SqlValidator, SqlValidatorUtil}
import org.apache.calcite.tools.{FrameworkConfig, Frameworks}

import java.util.Properties

/**
 *
 */
class Parser (val sql: String, val schemas: Array[TableSchema]) {
	
	import cgs.bigdata.calcite.Parser._
	
	/*
		将表的 schema 注册进 RootSchema
	 */
	registerTables(schemas)
	
	/*
	 	step1: sql 解析阶段 (sql -> SqlNode)
	 */
	private val parser: SqlParser = createSqlParser(sql)
	private val sqlNode: SqlNode = parser.parseStmt()
	
	/*
	 	step2: SqlNode 验证 (SqlNode -> SqlNode)
	 */
	private val validator: SqlValidator = createSqlValidator()
	private val correctSqlNode: SqlNode = validator.validate(sqlNode)
	
	def getSqlParser: SqlParser = parser
	def getSqlNode: SqlNode = correctSqlNode
}

object Parser {
	
	def apply (sql: String, schemas: Array[TableSchema]): Parser = new Parser(sql, schemas)
	
	private val parser_config: SqlParser.Config = SqlParser.Config.DEFAULT
	private val root_schema: SchemaPlus = Frameworks.createRootSchema(true)
	private val framework_config: FrameworkConfig = Frameworks.newConfigBuilder()
			.parserConfig(parser_config)
			.defaultSchema(root_schema)
			.traitDefs(ConventionTraitDef.INSTANCE, RelDistributionTraitDef.INSTANCE)
			.build()
	private val default_typefactory_impl: SqlTypeFactoryImpl = new SqlTypeFactoryImpl(RelDataTypeSystem.DEFAULT)
	
	private def registerTables(schemas: Array[TableSchema]): Unit = {
		schemas.foreach {tableSchema =>
			root_schema.add(tableSchema.name, AbsTable(tableSchema))
		}
	}
	
	private def createSqlParser(sql: String) = SqlParser.create(sql, parser_config);
	private def createSqlValidator(): SqlValidator = {
		val catalogReader: CalciteCatalogReader = new CalciteCatalogReader(
			CalciteSchema.from(root_schema),
			CalciteSchema.from(root_schema).path(null),
			default_typefactory_impl,
			new CalciteConnectionConfigImpl(new Properties()))
		
		SqlValidatorUtil.newValidator(
			SqlStdOperatorTable.instance(),
			catalogReader,
			default_typefactory_impl,
			conformance(framework_config))
	}
	
	private def conformance(config: FrameworkConfig): SqlConformance = {
		val context: Context = config.getContext
		if (context != null) {
			val connectionConfig: CalciteConnectionConfig = context.unwrap(classOf[CalciteConnectionConfig])
			if (connectionConfig != null) {
				connectionConfig.conformance()
			}
		}
		SqlConformanceEnum.DEFAULT
	}
	
	def createRexBuilder(typeFactory: RelDataTypeFactory): RexBuilder = new RexBuilder(typeFactory)
	
	
}
