import java.util.Random;
import java.util.Scanner;

/**
 * This project recreates the game of battleships.
 * A player will place 5 of their ships on a 10 by 10 grid.
 * The computer player will deploy five ships on the same grid.
 * Once the game starts the player and computer take turns,
 * trying to sink each other's ships by guessing the coordinates to "attack".
 * The game ends when either the player or computer has no ships left.
 * @author Marisa Tania
 */
public class BattleShips {
    int totalShips = 5;
    int compShips;
    int userShips;
    int x;
    int y;
    public String[][]ocean = new String[10][10];
    public String[][]compMap = new String[10][10];
    Scanner input = new Scanner(System.in);
    Random rand = new Random();

    public static void main(String[]args){
        BattleShip battleShip = new BattleShip();
        // Display welcome message
        battleShip.welcome();
        // Deploy user's ships
        battleShip.deployUser();
        // Deploy computer's ships
        battleShip.deployComp();
        // Game on
        battleShip.battle();
    }

    /**
     * Game intro
     */
    public void welcome(){
        System.out.println("\n**** Welcome to Battle Ships game ****");
        System.out.println("\nRight now, the sea is empty.");
        displayMap();
        System.out.println("------------------------------------------");
    }

    /**
     * The ocean map is represented by a 10 by 10 grid of two-dimensional array.
     * This method prints out the ocean map by nested looping over the array
     * and adding the indexes and pipe characters before and after each row.
     */
    public void displayMap(){
        System.out.println("\n+-Battle Ship-+\n");
        System.out.println("  0123456789  ");
        for(int i=0;i<ocean.length;i++){
            System.out.print(i+"|");
            for (int j=0;j<ocean[i].length;j++){
                if(ocean[i][j] == null){
                    System.out.print(" ");
                }else {
                    System.out.print(ocean[i][j]);
                }
            }
            System.out.println("|"+i);
        }
        System.out.println("  0123456789  \n");
    }

    /**
     * This method asks the user coordinates to place their 5 ships.
     */
    public void getInput(int i){
        System.out.print("Enter X coordinate for your ship "+i+": ");
        x = input.nextInt();
        System.out.print("Enter Y coordinate for your ship "+i+": ");
        y = input.nextInt();
    }

    /**
     *  User ships will be stored in a single index of the array
     *  and displayed as a "U" in the ocean map.
     */
    public void deployUser(){
        for (int i = 1; i<=totalShips;i++) {
            getInput(i);
            userShips++;

            // Check inputs
            while (x>9||y>9) {//can’t place ships outside the 10 by 10 grid
                System.out.println("The coordinate you entered is out of range, please try again");
                getInput(i);
            }

            while (ocean[x][y]!=null) { //can't place two or more ships on the same location
                System.out.println("The coordinate you entered is already used, please try again");
                getInput(i);
            }
            ocean[x][y]="U";
        }
        displayMap();
        System.out.println("------------------------------------------------");
    }

    /**
     * The computer will deploy 5 ships by randomly picking X and Y coordinates.
     * Computer ships will be stored in a single index of the array
     * and displayed as a "C" in the ocean map.
     */
    public void deployComp(){
        System.out.println("Computer is deploying ships");
        int i = 0;
        while (i != totalShips) {
            x = rand.nextInt(10);
            y = rand.nextInt(10);
            if (ocean[x][y]==null && compMap[x][y]==null) {
                compMap[x][y] = "C";
                System.out.println((i+1) + ". computer ship DEPLOYED");
                i++;
                compShips++;
            }
        }
        System.out.println("------------------------------------------------");
        displayShips();
    }

    public void displayShips(){
        System.out.println("Your ships: "+userShips+" | Computer ships: "+compShips);
    }

    public void playerGuess(){
        System.out.print("Enter X coordinate to attack: ");
        x = input.nextInt();
        System.out.print("Enter Y coordinate to attack: ");
        y = input.nextInt();
    }

    /**
     * This method ask the player to guess comp's X and Y coordinates,
     * check if those coordinates are valid, and evaluate the result of the move.
     */
    public void playersTurn(){
        System.out.println("\nYOUR TURN");
        System.out.println("Guess the computer ship's coordinate!");
        playerGuess();
        // Check inputs
        while (x>9||y>9) {//can’t place ships outside the 10 by 10 grid
            System.out.println("The coordinate you entered is out of range, please try again");
            playerGuess();
        }
        while (ocean[x][y] =="!" || ocean[x][y] =="-" || ocean[x][y]=="x" || compMap[x][y]=="-") {
            System.out.println("The coordinate you entered is already used, please try again");
            playerGuess();
        }
        if (compMap[x][y] == "C") {//User correctly guessed the comp's coordinate
            System.out.println("Boom! You sunk the computer's ship!");
            ocean[x][y] = "!";
            compMap[x][y] = "!";
            compShips--;
        }else if (ocean[x][y]=="U") {
            System.out.println("Oh no, you sunk one of your ships :(");
            ocean[x][y]="x";
            userShips--;
        }else {//User missed
            System.out.println("Sorry, you missed. Try again!");
            ocean[x][y]="-";
        }
    }

    /**
     * This methods keep generating random numbers until you get a valid guess,
     * meaning a location that is within the bounds of the board
     * and the computer hasn't already guessed.
     */
    public void compTurn(){
        System.out.println("\nCOMPUTER'S TURN");
        boolean guess = false;
        while (guess==false){
            x = rand.nextInt(10);
            y = rand.nextInt(10);
            if (ocean[x][y] == "U") {
                ocean[x][y] = "!";
                System.out.println("The Computer sunk one of your ships!");
                userShips--;
                guess=true;
            } else if(compMap[x][y] == "C"){
                System.out.println("The Computer sunk one of its own ships");
                ocean[x][y]="x";
                compMap[x][y]="x";
                compShips--;
                guess=true;
            }else {
                System.out.println("Computer missed");
                compMap[x][y]="-";
                ocean[x][y]="~";
                guess=true;
            }
        }
    }

    public void battle(){
        while (compShips!=0 && userShips!=0){
            playersTurn();
            compTurn();
            //System.out.println(Arrays.deepToString(compMap));
            displayMap();
            displayShips();
            System.out.println("------------------------------------------------");
        }
        displayMap();
        if (userShips==0){
            System.out.println("\n*** GAME OVER ***");
        } else if (compShips==0) {
            System.out.println("Yay! You won the Battle :)");
        }
    }
}

