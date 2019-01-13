package homework;

import java.util.Random;
import java.util.Scanner;

public class Chess {
	char[][] chess = new char[4][4];
	char player,computer;
	boolean playerF;
	boolean detectWin(char player) {//判断玩家player是否胜利
		for(int i=1;i<4;i++)
			if(chess[i][1]==player&&chess[i][2]==player&&chess[i][3]==player) {
				return true;
			}else {
				if(chess[1][i]==player&&chess[2][i]==player&&chess[3][i]==player)
					return true;
			}
		if(chess[1][1]==player&&chess[2][2]==player&&chess[3][3]==player)
			return true;
		if(chess[1][3]==player&&chess[2][2]==player&&chess[3][1]==player)
			return true;
		return false;
	}
	boolean isEmpty() {//判断棋盘是否为空
		for(int i=1;i<4;i++) {
			for(int j=1;j<4;j++) {
				if(chess[i][j]=='-')return false;
			}
		}
		return true;
	}
	
	//初始化游戏
	public void startGame() {
		for(int i=1;i<4;i++)
			for(int j=1;j<4;j++)
				chess[i][j]='-';
		Scanner input = new Scanner(System.in);
		player = 'O';
		computer='X';
		System.out.print("请选择玩家符号X/O,(输入1代表选择X，输入2代表选择O,默认为O)：");
		int get = input.nextInt();
		if(get==1) {
			player = 'X';
			computer = 'O';
		}else if(get!=2) {
			System.out.println("输入有误，默认为O");
		}
		playerF = true;
		System.out.println("是否先手1/2(1代表是，2代表否，默认是)：");
		get = input.nextInt();
		if(get==2)playerF=false;
		else if(get!=1) {
			System.out.println("输入有误，默认玩家先手！");
		}
		if(playerF) {
			Print();
		}
		else {
			Random rand = new Random();
			int startPoint = rand.nextInt(9)+1;
			int row;
			if(startPoint%3==0)row=startPoint/3;
			else row = startPoint/3+1;
			int col = startPoint-(row-1)*3;
			chess[row][col]=computer;
			Print();
		}
	}
	void Print() {
		System.out.println("-------------");
		for(int i=1;i<4;i++) {
			for(int j=1;j<4;j++) {
				System.out.print("| ");
				System.out.print(chess[i][j]+" ");
				if(j==3)System.out.println("|");
			}
			if(i==3)System.out.println("-------------");
		}
	}
	void playerInput() {
		System.out.print("轮到你走了，请输入棋的位置：");
		int row,col;
		Scanner input = new Scanner(System.in);
		while(true) {
			int num = input.nextInt();
			if(num>=1&&num<=9) {
				if(num%3==0)row = num/3;
				else row = num/3+1;
				col = num-3*(row-1);
				if(chess[row][col]!='-')System.out.println("该位置已有棋子");
				else break;
			}else {
				System.out.println("输入有误，请重新输入");
				continue;
			}
		}
		chess[row][col]=player;
		Print();
//		input.close();
	}
	//-1,0,1分别代表玩家玩家胜利，平局，电脑胜利时节点的值
	int bestInput(String state,String nextState,int alpha,int beta) {//输入，调用剪枝的过程
		char ch;
		if(state.equals("computer"))ch=computer;
		else ch=player;
		if(detectWin(ch)) {
			if(state.equals("computer"))return 1;
			else  return -1;
		}else if(isEmpty()) {
			return 0;
		}
		else {
			int score;
			for(int i=1;i<4;i++) {
				for(int j=1;j<4;j++) {
					if(chess[i][j]=='-') {
						chess[i][j]=ch;
						score=bestInput(nextState,state,alpha,beta);
						chess[i][j]='-';
						if(state.equals("computer")) {
							if(score>=alpha)alpha=score;
							if(alpha>beta)return beta;
						}else {
							if(score<beta)beta=score;
							if(beta<=alpha)return alpha;
						}
					}
				}
			}
			if(state.equals("computer"))return alpha;
			else return beta;
		}
	}
	
	void computerInput() {
		int best=0;
		int bestScore=-1000; 
		int score;
		for(int i=1;i<=3;i++) {
			for(int j=1;j<=3;j++) {
				if(chess[i][j]=='-') {
					chess[i][j]=computer;
					score = bestInput("player","computer",-1000,1000);//alpha-beta剪枝是一个根据上下界限剪枝的算法，初始的上下界限为无穷
					if(score>bestScore) {//在同一层的节点里面需要不断试探性递归，用回溯法找到最合适的下棋点使自己胜算最大
						bestScore=score;
						best=(i-1)*3+j;
					}
					chess[i][j]='-';
				}
			}
		}
		int row,col;
		if(best%3==0)row = best/3;
		else row = best/3+1;
		col = best-(row-1)*3;
		chess[row][col]=computer;
		Print();
	}
	
	public static void main(String[] args) {
		Chess c = new Chess();
		c.startGame();
		String current = "player";
		while(!c.detectWin(c.computer)&&!c.detectWin(c.player)&&!c.isEmpty()) {//终止条件是当前棋盘为空或者有一方胜利
			switch(current) {
			case "player":
				c.playerInput();current="computer";break;//当玩家下完后轮到电脑下
			case "computer":
				c.computerInput();current="player";break;
			default:break;
			}
		}
		if(c.detectWin(c.computer))System.out.println("电脑胜利！");
		else if(c.detectWin(c.player))System.out.println("玩家胜利");
		else System.out.println("平局！");
	}
}
