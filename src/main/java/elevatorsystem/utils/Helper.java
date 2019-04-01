package elevatorsystem.utils;

/**
 * Class that contains methods that might help user during program usage
 */
public class Helper {
    /**
     * Method that prints the introduction to the program
     */
    public static void printIntroductionToTheProgram() {
        String title = "ELEVATOR SYSTEM";

        String introduction = "\nThis app is destined to observe the behaviour of the elevator system - behaviour based on data provided by YOU :)" +
                "\n\nAt first, you will be asked to enter the number of elevators you want the system to have," +
                "\nas well as their current levels" +
                "\nThen follow other instructions." +
                "\n\nSHOULD YOU NEED ANY HELP - when you are being asked to choose the activity, just type \"help\" to get some" +
                "\nLET'S BEGIN!";

        System.out.println(title);
        System.out.println(introduction);
    }


    /**
     * Method that prints the help when required
     */
    public static void printHelp() {
        String pickupStatement = "\n\npickup - if you type \"pickup\" you will be asked to enter the floor for the pickup," +
                "\nand after that, to enter the direction for the elevator when it reaches destined floor" +
                "\nEXAMPLE:" +
                "\n\"Enter the floor for a pickup\" - if you enter for instance \"15\"," +
                "\nit means that you want any of elevators available in the system to come to the 15th floor" +
                "\nand then if you enter \"-2\" it means you want to travel 2 floors down. " +
                "\nSimilarly with \"3\" - it means you want to get through 3 floors up";

        String stepStatement = "\n\nstep - if you type \"step\" you want the system to take a step (or several steps)" +
                "\nEXAMPLE:" +
                "\n\"Enter the number of steps the system should execute:\" - if you enter for instance 2" +
                "\nthe system will take 2 steps and you will be informed about what is currently happening in the system" +
                "\nYou will receive some information about movement of every elevator if such movement occurs etc.";

        String statusStatement = "\n\nstatus - type \"status\" to get the status of every elevator currently available in the system" +
                "\n- id of the elevator, its current level and closest destination level";

        String helpStatement = "\n\nhelp - type \"help\" to get the information about the usage of the system" +
                "\n- as you did just a moment ago :D";

        String exitStatement = "\n\nexit - when you want to exit the system, just type \"exit\"";

        String helpInfo = "\nWhen you are being asked for the activity," +
                "\nyou can choose from several ones such as:" +
                pickupStatement + stepStatement + statusStatement + helpStatement + exitStatement + "\n\n";


        System.out.println(helpInfo);
    }
}
