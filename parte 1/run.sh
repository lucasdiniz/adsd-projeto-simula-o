cd src
javac -cp ../simjava/simjava.jar entities/*.java *.java
java -cp .:../simjava/simjava.jar Main
cd ..