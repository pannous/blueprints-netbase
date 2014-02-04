mvn install -U -DskipTests -Dmaven.test.skip=true
mvn jar:jar
cp target/blueprints-netbase-2.4.0.jar lib/
echo needs java version "1.7"
cd lib; java -cp lib/*.jar -jar blueprints-netbase-2.4.0.jar 
