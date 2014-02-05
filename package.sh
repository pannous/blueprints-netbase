mvn jar:jar
cp ./target/blueprints-netbase-2.4.0.jar ./lib/
mvn install -U -DskipTests -Dmaven.test.skip=true