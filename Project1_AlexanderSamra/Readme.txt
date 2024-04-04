Readme.txt
Alexander Samra 797614601

I set up a configuration that runs the DBMS class. When running this class set the program argument to whatever you
want the size of the BufferPool to be. Then run commands GET, SET, PIN, and UNPIN as you would like.

SET 430 "F05-Rec450, Jane Do, 10 Hill Rd, age020." PASS
GET 430 PASS
GET 20 PASS
SET 430 PASS
PIN 5 PASS
UNPIN 3 PASS
GET 430 PASS
PIN 5 PASS
GET 646 PASS
PIN 3 PASS
SET 10 "F01-Rec010, Tim Boe, 09 Deer Dr, age009." PASS
UNPIN 1 PASS
GET 355 PASS
PIN 2 PASS
GET 156 PASS
SET 10 "F01-Rec010, No Work, 31 Hill St, age100." PASS
PIN 7 PASS
GET 10 PASS
UNPIN 3 PASS
UNPIN 2 PASS
GET 10 PASS
PIN 6 PASS

My project is very close to the one defined in the project specifications
The only major decisions I made was to parse the input and run all the methods from the DBMS class.
I also added a method to the BufferPool class to update the disk