from subprocess import call

try:
	call(["mkdir", "bin/"])
except:
	pass

call(["javac", "-d", "bin/", "@sources.txt"])

# javac -d bin/ @sources.txt
# java -cp bin Program