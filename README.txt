/*
  README.txt
  Jan. 2007
*/

This program allows the user to calculate (approximately) the range of
possible amounts of donatable wealth that he might be able to accumulate
under various assumptions. This program parallels the analysis found in
"How Much Money Could a Utilitarian Make by Having a Conventional Job?"
(www.utilitarian-essays.com/donatable-money.pdf), though it uses more
complicated assumptions and is therefore more accurate. When this program
is adjusted to have the same assumptions as were used in that piece, it
gives identical results.

To run the program, simply type

   java wealthCalc

If the files have not yet been compiled, type

   make

To remove all extraneous files, type

   make clean

The user can change the assumptions used by going back into the source code.
Most of the assumptions takek the form of UPPER_CASE constants at the 
beginning of the files. Each of these files has assumptions that can be
changed:

wealthCalc.java
Utilitarian.java
World.java
Status.java
Applications.java

Have fun!