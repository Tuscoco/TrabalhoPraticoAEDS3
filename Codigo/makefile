run: build
	
	@java -cp bin App

build:

	@javac -d bin -sourcepath src src/App.java

clean:

	@rm -rf bin/*.class

delete:

	@find data/temp -type f ! -name "tem.db" -delete