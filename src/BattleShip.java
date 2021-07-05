import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

public class BattleShip {
    String [][] playerOneBoard;
    String [][] ComputerBoard;
    String playerOneName;
    ArrayList<String> availablePlayerMoves;
    ArrayList<String> availableCompMoves;
    String [] playerShip;
    String [] computerShip;

    public BattleShip(){
        playerOneBoard=new String [][]{{" "," "," "}, {" "," "," "}, {" "," "," "}};
        ComputerBoard=new String [][]{{" "," "," "}, {" "," "," "}, {" "," "," "}};
        availablePlayerMoves=new ArrayList<String>();
        availableCompMoves=new ArrayList<String>();
        playerShip=new String[2];
        computerShip=new String[2];
    }

    // Prints the game board after every move cycle
    public void printBoard(){
        System.out.println();
        System.out.println( playerOneName + "'s Board: ");
        System.out.println(Arrays.deepToString(playerOneBoard)
        .replace("],","\n").replace(",","|").replaceAll("[\\[\\]]", " "));
        System.out.println();
        System.out.println("Computer Board: ");
        System.out.println(Arrays.deepToString(ComputerBoard)
        .replace("],","\n").replace(",","|").replaceAll("[\\[\\]]", " "));
        System.out.println();
    }

    // Prints the available move for the player
    public void printChoices(int i){
        System.out.println("  A  B  C ");
        System.out.println("1  |  |");
        System.out.println("2  |  |");
        System.out.println("3  |  |");
        if (i==0){
            List<String> choices=Arrays.asList("A1", "A2", "A3", "B1", "B2", "B3","C1", "C2","C3");
            availablePlayerMoves.addAll(choices);
            availableCompMoves.addAll(choices);
        }
            System.out.println(availablePlayerMoves);
    }

    // Retrieves player name to start the game
    public void startgame(){
        Scanner scan=new Scanner(System.in);
        System.out.println("Welcome to a new game of Battleship!");
        System.out.println("Please enter Player One name: ");
        playerOneName=scan.next();
        System.out.println();
        selectShips();
    }

    // Prompts player to select the position of their ship
    public void selectShips(){
        printChoices(0);
        Scanner scan=new Scanner(System.in);
        System.out.println();
        System.out.println(playerOneName + " please select the location for your ship! (2 spots)");
        System.out.println("Spot 1: ");
        String spotOne=scan.next();
        spotOne=spotOne.toUpperCase();
        while (!availablePlayerMoves.contains(spotOne)){
            System.out.println("Error, choose a spot from the choices: ");
            spotOne=scan.next();
            spotOne=spotOne.toUpperCase();
        }
        System.out.println("Spot 2: ");
        String spotTwo=scan.next();
        spotTwo=spotTwo.toUpperCase();
        while (!availablePlayerMoves.contains(spotTwo) || spotTwo.equals(spotOne) || checkPosition(spotOne, spotTwo)){
            if (!availablePlayerMoves.contains(spotTwo)){
                System.out.println("Error, choose a spot from the choices: ");
                spotTwo=scan.next();
                spotTwo=spotTwo.toUpperCase();
            } else if(spotTwo.equals(spotOne)){
                System.out.println("Error, spots can not be on the same position: ");
                spotTwo=scan.next();
                spotTwo=spotTwo.toUpperCase();
            } else if(checkPosition(spotOne, spotTwo)){
                System.out.println("Error, choose a spot adjacent to spot 1: ");
                spotTwo=scan.next();
                spotTwo=spotTwo.toUpperCase();
            }
        }
        playerShip[0]=spotOne;
        playerShip[1]=spotTwo;
        computerShip=generateComputerShip();
        startBattle();
    }

    // Checks whether the player's ship coordinates are positioned correctly
    public boolean checkPosition(String p1, String p2){
        Boolean letterChange=false;
        Boolean numberChange=false;
        int num1=Character.getNumericValue(p1.charAt(1));
        int num2=Character.getNumericValue(p2.charAt(1));
        if (p1.charAt(0)!=p2.charAt(0))
            letterChange=true;
        if (num1!=num2)
            numberChange=true;
        if ((!letterChange && !numberChange) || (letterChange && numberChange))
            return true;     
        if (letterChange)    
            if ((p1.charAt(0)=='A' && p2.charAt(0)!='B') ||  
                (p2.charAt(0)=='A' && p1.charAt(0)!='B') || 
                (p1.charAt(0)!='A' && p2.charAt(0)=='B') || 
                (p2.charAt(0)!='A' && p1.charAt(0)=='B')) 
                   if ((p1.charAt(0)=='C' && p2.charAt(0)!='B') ||
                       (p2.charAt(0)=='C' && p1.charAt(0)!='B') ||
                       (p1.charAt(0)!='C' && p2.charAt(0)=='B') ||
                       (p2.charAt(0)!='C' && p1.charAt(0)=='B'))
                        return true;    
        if (numberChange)       
            if ((num1!=(num2+1)) && (num1!=(num2-1)))
                return true; 
        return false;
    }

    // Randomly places a ship for the computer
    public String [] generateComputerShip(){
        String [] generatedPositions=new String[2];
        generatedPositions[0]=availableCompMoves.get(new Random().nextInt(availableCompMoves.size()));
        generatedPositions[1]=availableCompMoves.get(new Random().nextInt(availableCompMoves.size()));
        while (checkPosition(generatedPositions[0], generatedPositions[1])){
            generatedPositions[1]=availableCompMoves.get(new Random().nextInt(availableCompMoves.size()));
        }
        return generatedPositions;
    }

    // Main game loop
    public void startBattle(){
        Scanner scan=new Scanner(System.in);
        System.out.println();
        while (!checkWinner()){
            printChoices(1);
            System.out.println(playerOneName + ",please enter your attack: ");
            String playerMove=scan.next();
            playerMove=playerMove.toUpperCase();
            while (!availablePlayerMoves.contains(playerMove)){
                System.out.println("Error, choose a move from the move list: ");
                playerMove=scan.next();
                playerMove=playerMove.toUpperCase();
            }
            updateBoards(playerMove);
            generateComputerMove();
            printBoard();
        }
        scan.close();
    }

    // Generates a move for the computer and places it on the board
    public void generateComputerMove(){
        String compMove=availableCompMoves.get(new Random().nextInt(availableCompMoves.size()));
        if (compMove.equals("A1"))
            if (playerShip[0].equals(compMove) || playerShip[1].equals(compMove))
                playerOneBoard[0][0]="x";
        if (compMove.equals("B1"))
             if (playerShip[0].equals(compMove) || playerShip[1].equals(compMove))
                playerOneBoard[0][1]="x";
        if (compMove.equals("C1"))
             if (playerShip[0].equals(compMove) || playerShip[1].equals(compMove))
                playerOneBoard[0][2]="x";  
        if (compMove.equals("A2"))
            if (playerShip[0].equals(compMove) || playerShip[1].equals(compMove))
                playerOneBoard[1][0]="x";    
        if (compMove.equals("B2"))
            if (playerShip[0].equals(compMove) || playerShip[1].equals(compMove))
                playerOneBoard[1][1]="x";
        if (compMove.equals("C2"))
            if (playerShip[0].equals(compMove) || playerShip[1].equals(compMove))
                playerOneBoard[1][2]="x";
        if (compMove.equals("A3"))
            if (playerShip[0].equals(compMove) || playerShip[1].equals(compMove))
                playerOneBoard[2][0]="x";
        if (compMove.equals("B3"))
            if (playerShip[0].equals(compMove) || playerShip[1].equals(compMove))
                playerOneBoard[2][1]="x";
        if (compMove.equals("C3")) 
            if (playerShip[0].equals(compMove) || playerShip[1].equals(compMove))
                playerOneBoard[2][2]="x"; 
        availableCompMoves.remove(compMove);              
        }

    // Updates the computer board after the player has made a move    
    public void updateBoards(String playerMove){
        if (playerMove.equals("A1"))
            if (computerShip[0].equals(playerMove) || computerShip[1].equals(playerMove))
                ComputerBoard[0][0]="x";
        if (playerMove.equals("B1"))
             if (computerShip[0].equals(playerMove) || computerShip[1].equals(playerMove))
                 ComputerBoard[0][1]="x";
        if (playerMove.equals("C1"))
             if (computerShip[0].equals(playerMove) || computerShip[1].equals(playerMove))
                ComputerBoard[0][2]="x";  
        if (playerMove.equals("A2"))
            if (computerShip[0].equals(playerMove) || computerShip[1].equals(playerMove))
                ComputerBoard[1][0]="x";    
        if (playerMove.equals("B2"))
            if (computerShip[0].equals(playerMove) || computerShip[1].equals(playerMove))
                ComputerBoard[1][1]="x";
        if (playerMove.equals("C2"))
            if (computerShip[0].equals(playerMove) || computerShip[1].equals(playerMove))
                ComputerBoard[1][2]="x";
        if (playerMove.equals("A3"))
            if (computerShip[0].equals(playerMove) || computerShip[1].equals(playerMove))
                ComputerBoard[2][0]="x";
        if (playerMove.equals("B3"))
            if (computerShip[0].equals(playerMove) || computerShip[1].equals(playerMove))
                ComputerBoard[2][1]="x";
        if (playerMove.equals("C3")) 
            if (computerShip[0].equals(playerMove) || computerShip[1].equals(playerMove))
                ComputerBoard[2][2]="x";       
        availablePlayerMoves.remove(playerMove);        
        }

    // Checks whether anyone has won the game    
    public boolean checkWinner(){
        int playerCount=0;
        int computerCount=0;
        for (int i=0; i<playerOneBoard.length; i++){
            for (int j=0; j<playerOneBoard[i].length; j++){
                if (playerOneBoard[i][j].equals("x"))
                    playerCount++;
            }
        }
        for (int i=0; i<ComputerBoard.length; i++){
            for (int j=0; j<ComputerBoard[i].length; j++){
                if (ComputerBoard[i][j].equals("x"))
                    computerCount++;
            }
        } 
        if (computerCount==2){
            System.out.println("Congrats you have won");
            return true;
        } else if (playerCount==2){
            System.out.println("Sorry, the computer has won");
            return true;
        }
        return false;
    }
    
    public String toString(){
        return "Thanks for playing!";
    }

    public static void main (String[] args){
        BattleShip s = new BattleShip();
        s.startgame();
        System.out.println(s); 
    }
}
