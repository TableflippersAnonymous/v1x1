mvn-clean:
	mvn clean ${V1X1_MAKE_TARGET}

mvn-package:
	mvn -T16 package ${V1X1_MAKE_TARGET} -Denvironment=${V1X1_MAKE_ENV}
