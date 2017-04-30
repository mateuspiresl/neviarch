from subprocess import call

try:
	call(["mkdir", "bin/"])
except:
	pass

call(["javac", "-d", "bin", "src/*.java", "src/com/neviarch/*.java", "src/com/neviarch/instruction/*.java", "src/com/neviarch/util/*.java"])

# javac -d bin src/*.java src/com/neviarch/*.java src/com/neviarch/instruction/*.java src/com/neviarch/util/*.java
# java -cp bin Program