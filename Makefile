# type "make" to compile all files
# type "make clean" to remove all .class files

all: Applications.class Donation.class Report.class Status.class Utilitarian.class World.class arrayMethods.class wealthCalc.class

Applications.class: Applications.java
	javac Applications.java

Donations.class: Donations.java
	javac Donations.java

Report.class: Report.java
	javac Report.java

Status.class: Status.java
	javac Status.java

Utilitarian.class: Utilitarian.java
	javac Utilitarian.java

World.class: World.java
	javac World.java

arrayMethods.class: arrayMethods.java
	javac arrayMethods.java

wealthCalc.class: wealthCalc.java
	javac wealthCalc.java

clean:
	$(RM) *.class
	$(RM) *.txt
	$(RM) *~