package cgs.bigdata.hadoop.mapreduce.wordcount

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
class WordCountMRTest extends org.scalatest.FunSuite {
	
	
	test("testFormatWord") {
		val str1: String = "(te,xt)."
		val str2: String = ",(text,)"
		val _str1: String = WordCountMR.formatWord(str1)
		val _str2: String = WordCountMR.formatWord(str2)
		assert(_str1 == "text")
		assert(_str1 == _str2)
		println(s"${_str1} = ${_str2}")
	}
	
	test("testIsNumberic") {
		val num1: String = "2341"
		val num2: String = "12.34"
		val num3: String = "12e.34"
		val num4: String = "0123"
		
		assert(WordCountMR.isNumberic(num1))
		assert(WordCountMR.isNumberic(num2))
		assert(!WordCountMR.isNumberic(num3))
		assert(WordCountMR.isNumberic(num4))
	}
	
	/*
		aa == aa1 => true
		aa == bb => false
		aa eq aa1 => false
		aa eq bb => false
		aa equals aa1 => true
		aa equals bb => false
	 */
	test("eq, equals, ==") {
		case class Person(name: String)
		
		val aa: Person = Person("aa")
		val bb: Person = Person("bb")
		val aa1: Person = Person("aa")
		
		println(s"aa == aa1 => ${aa == aa1}")
		println(s"aa == bb => ${aa == bb}")
		
		println(s"aa eq aa1 => ${aa eq aa1}")
		println(s"aa eq bb => ${aa eq bb}")
		
		println(s"aa equals aa1 => ${aa equals aa1}")
		println(s"aa equals bb => ${aa equals bb}")
	}
}
