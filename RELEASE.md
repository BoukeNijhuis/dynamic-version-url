# Release

Steps:
1. update the version number in the pom.xml
2. mvn clean
3. mvn -Ppublication
4. mvn jreleaser:deploy