mvn install -U
mvn jar:jar
cp target/blueprints-netbase-2.4.0.jar lib/
cd lib; java -cp lib/*.jar -jar blueprints-netbase-2.4.0.jar 