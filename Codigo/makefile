run: build
	
	@clear
	@java -cp bin App

build:

	@javac -d bin -sourcepath src src/*.java

clean:

	@rm -rf bin/*.class

delete:

	@find data/temp -type f ! -name "tem.db" -delete