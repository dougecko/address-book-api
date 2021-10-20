./gradlew assemble
mkdir -p build/dependency
cd build/dependency || exit 1
jar -xf ../libs/aba-0.0.1-SNAPSHOT.jar
cd ../..
docker build --build-arg JAR_FILE=build/libs/\*.jar -t shine/aba .