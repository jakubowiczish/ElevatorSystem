# ElevatorSystem

This app is destined to observe the behaviour of the elevator system - behaviour that is based on the data provided by you!

## Algorithm description:

To create the elevator system that would be efficient I had to consider several different situations that can occur while using the system:

At first, I had to choose the best elevator amongst every elevator available in the system at the moment. 

To accomplish that, I created a copy of list of elevators available in order to not affect original list of elevators.

The pickup method does two things:
- returns number of steps that the elevator needs to take to be able to do pickup operation
- does the pickup operation itself 

In order to find the most suitable elevetor for the pickup operation I called the pickup method for every elevator in created copy of elevators.

Pickup method behaviour - this method basically has 2 modes - first, when there is need to add destination level to the list, and second, when there is no such need. 
Adding destination level to the list is only required when the best elevator is found, so I will explain it later.

Otherwise, there are pickup method steps:
1. When the elevator is not moving:
  a) when the list of levels to visit is empty, the floor is just being added to the list
  b) depending on the distance between current level of elevator and our floor for the pickup, the floor is being added to        the list accordingly - in order to do the smallest possible number of steps

2. When the elevator is going UP:
   - there is a need to check if there is a sublist of levels that are increasing - for instance:
      - in the list: [2,3,7,8,4] the increasing sublist is: [2,3,7,8]
      
   a) if the floor is within the bounds of the sublist (floor > 2 and floor < 8) and the sublist does not contain the floor         then we look for the place to insert the floor into the levels list - for instance:
      - floor is equal to 5 so it is withing bounds (5 > 2 and 5 < 8) and the sublist does not contain 5 (5 != 2 and 5 != 3         and 5 != 7 and 5 != 8) -
      the floor is being added to the list in a suitable position (for example above levels list after insertion of 5 looks         like: [2,3,5,7,8,4]
   b) otherwise, if the currentLevel < floor, then floor is being added at the beginning of the list
                 if not, floor is being added just after the increasing sub list

3. When the elevator is going DOWN:
   - similarly to the situation when elevator is going UP, there is now need to theck if there is a sublist of levels that      are decreasing e.g. for list [6,5,3,7,8] the decreasing sublist is: [6,5,3]
   
   a) if the floor is between bounds and sublist does not contain the floor - the floor is being added to the list in a             suitable position for instance - when the floor = 4, the list after inserting is: [6,5,4,3,7,8]
   b) otherwise, if the currentLevel > floor, then floor is being added at the beginning of the list
                 if not, floor is being added just after the decreasing sub list


When the most suitable elevator for insterting floor is found, there is time to insert destination level to the list.

The destination floor (destination = floor + offset) is to be added to the list accordingly to its position in relation to floor, as it should always be visited after the floor

#
All important methods are described by means of java docs 

## Usage 
You can see exemplary usage of the program below:

[![asciicast](https://asciinema.org/a/2aV0VJIeYUfNyT7wCCbQaJXmV.svg)](https://asciinema.org/a/2aV0VJIeYUfNyT7wCCbQaJXmV)
