# ElevatorSystem

This app is destined to observe the behaviour of the elevator system - behaviour that is based on the data provided by you!

## Algorithm description:

To create the elevator system that would be efficient I had to consider several different situations that can occur while using the system:

At first, I had to choose the best elevator amongst every elevator available in the system at the moment. 

To accomplish that, I created a copy of list of elevators available in order to not affect original list of elevators.

The pickup method does two things:
- returns minimum number of steps that the elevator needs to take to be able to do pickup operation
- does the pickup operation itself 

In order to find the most suitable elevetor for the pickup operation I called the pickup method for every elevator in created copy.




## Usage 
You can see exemplary usage of the program below:

[![asciicast](https://asciinema.org/a/2aV0VJIeYUfNyT7wCCbQaJXmV.svg)](https://asciinema.org/a/2aV0VJIeYUfNyT7wCCbQaJXmV)
