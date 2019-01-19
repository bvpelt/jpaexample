# JPA

From [Code Geeks](https://www.javacodegeeks.com/tutorials/java-tutorials/enterprise-java-tutorials#JPA)

## Setup project

Create project from commandline with
``` 
mvn archetype:generate -DgroupId=com.javacodegeeks.ultimate -DartifactId=jpa -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false
```

## Review data
Open sql commandline 
``` 
java -cp ~/.m2/repository/com/h2database/h2/1.3.176/h2-1.3.176.jar org.h2.tools.Shell -url jdbc:h2:~/jpa
```