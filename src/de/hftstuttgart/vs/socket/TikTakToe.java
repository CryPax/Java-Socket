package de.hftstuttgart.vs.socket;

import java.util.Arrays;
import java.util.Scanner;

public class TikTakToe {

    public static void main(String[] args) {
        TikTakToe t = new TikTakToe();
        Scanner scan = new Scanner(System.in);


        while(t.isRunning()){
            String[] res = scan.nextLine().split(",");
            t.enterVal(Integer.parseInt(res[0])-1, Integer.parseInt(res[1])-1);
            System.out.println(t.getBoardString());

            t.aiMove();
            System.out.println(t.getBoardString());
        }

    }

    public enum State {
        AI(1),
        User(2);

        public final int value;
        State(int value){
            this.value = value;
        }

    }

    private int[][] board;
    State state;
    boolean running = false;


    public TikTakToe(){
        init();
    }

    private void init() {
        state = State.User;
        board = new int[3][3];
        for (int[] row :
                board) {
            Arrays.fill(row, 0);
        }
        running = true;
    }

    public int aiMove(){
        int bestScore = -1000;
        int[] bestMove = {0,0};

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j] == 0){
                    board[i][j] = State.AI.value;
                    int score = minimax(board, 0, false, j, i);
                    board[i][j] = 0;
                    if(score > bestScore){
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
       return enterVal(bestMove[1], bestMove[0]);
    }

    private int minimax(int[][] board, int depth, boolean isMaximizing, int x, int y) {

        if(whichMarkWon(x,y,State.AI.value))
            return 1;
        else if(whichMarkWon(x,y,State.User.value))
            return -1;
        else if(checkDraw())
            return 0;

        if(isMaximizing){
            int bestScore = -1000;

            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if(board[i][j] == 0){
                        board[i][j] = State.AI.value;
                        int score = minimax(board, 0, false, j, i);
                        board[i][j] = 0;
                        if(score > bestScore){
                            bestScore = score;
                        }
                    }
                }
            }
            return bestScore;
        }else{
            int bestScore = 800;

            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if(board[i][j] == 0){
                        board[i][j] = State.User.value;
                        int score = minimax(board, 0, true, j, i);
                        board[i][j] = 0;
                        if(score < bestScore){
                            bestScore = score;
                        }
                    }
                }
            }
            return bestScore;
        }

    }

    public boolean checkDraw() {
        for (int i = 0; i <board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j] == 0)
                    return false;
            }
        }
        return true;
    }

    public int enterVal(int x, int y){
        board[y][x] = state.value;
        if(checkWin(x,y)){
            running=false;
            return state.value;
        }else if(checkDraw()){
            running = false;
            return 0;
        }
        switchState();
        return -1;
    }

    public boolean checkWin(int x, int y){
        int row= 0 ,col = 0,diag = 0,rdiag = 0;
        int n = 3;
        for (int i = 0; i < n; i++) {
            if(board[i][x] == state.value) col++;
            if(board[y][i] == state.value) row++;
            if(board[i][i] == state.value) diag++;
            if(board[i][n-i-1] == state.value) rdiag++;
        }
        return row == n || col == n || diag == n || rdiag == n;
    }

    public boolean whichMarkWon(int x, int y, int val){
        int row= 0 ,col = 0,diag = 0,rdiag = 0;
        int n = 3;
        for (int i = 0; i < n; i++) {
            if(board[i][x] == val) col++;
            if(board[y][i] == val) row++;
            if(board[i][i] == val) diag++;
            if(board[i][n-i-1] == val) rdiag++;
        }
        return row == n || col == n || diag == n || rdiag == n;
    }
    
    public String getBoardString(){
        String boardString = "";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                boardString += " " + getMarker(board[i][j]) +  ((j<2)?" |": "\n");
            }
            boardString += ((i<2)?"-----------\n":"");
        }
        return boardString;
    }

    private String getMarker(int i) {
        switch (i){
            case 0:
                return " ";
            case 1:
                return "X";
            case 2:
                return "O";
            default:
                return null;
        }
    }

    public boolean isRunning(){
        return running;
    }

    private void switchState(){
        if(state == State.AI){
            state = State.User;
        }else
            state = State.AI;
    }
}
