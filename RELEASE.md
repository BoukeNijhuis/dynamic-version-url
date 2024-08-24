# Release

Steps:
1. mvn clean test
2. update the version number in the pom.xml: mvn build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.nextIncrementalVersion} versions:commit
3. mvn -Ppublication
4. mvn jreleaser:deploy