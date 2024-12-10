description = "fastexcel spring boot starter"

dependencies {
	api("cn.idev.excel:fastexcel") {
		exclude(group = "com.sun.xml.bind", module = "jaxb-core")
	}
}
