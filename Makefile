compiler:
	ant

clean:
	ant clean

stutest.out:
	cat stutest.in
	java -cp ./bin main/Main stutest.in > stutest.out
	cat stutest.out

proftest.out:
	cat $(PROFTEST)
	java -cp ./bin main/Main $(PROFTEST) > proftest.out
	cat proftest.out
