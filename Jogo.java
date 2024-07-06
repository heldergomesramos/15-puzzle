import java.util.Scanner;

public class Jogo
{
    public int[][] jogo = new int[4][4];
    public int blankX;
    public int blankY;
    public char prevMove = ' ';
    String pathString = "";
    public int depth = 0;

    //Ler o jogo
    public void readGame(Scanner in)
    {
        for(int i = 0; i < jogo.length; i++)
        {
            for(int j = 0; j < jogo[0].length; j++)
            {
                int temp = in.nextInt();
                jogo[i][j] = temp;
                if(temp == 0)
                {
                    blankY = i; // y
                    blankX = j; // x
                }
            }
        }
    }
    
    //Imprimir o jogo
    public void printGame()
    {
        for(int i = 0; i < jogo.length; i++)
        {
            for(int j = 0; j < jogo[0].length; j++)
                System.out.print(jogo[i][j] + " ");
            System.out.println();
        }
    }

    //Conta o numero de inversoes
    public int inversoes()
    {
        int[] gameArray = new int[16];
        int counter = 0;
        for(int i = 0; i < jogo.length; i++)
            for(int j = 0; j < jogo[0].length; j++)
            {
                gameArray[counter] = jogo[i][j];
                counter++;
            }
        int inversoes = 0;
        for(int i = 0; i < gameArray.length; i++)
            for(int j = i + 1; j < gameArray.length; j++)
                if(gameArray[i] > gameArray[j] && gameArray[j] != 0)
                    inversoes++;
        return inversoes;
    }

    //Devolve True se tiver soluçao
    public boolean solvability()
    {
    	int inversoes = inversoes();
        return ((blankY % 2 == 0 && inversoes % 2 != 0) || (blankY % 2 != 0 && inversoes % 2 == 0));
    }

    //Devolve true se a matriz for igual a do estado final
    public boolean equals(Jogo winner)
    {
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j)
                if (jogo[i][j] != winner.jogo[i][j])
                    return false;
        return true;
    }

    //Conta o numero de pecas fora do lugar
    public int countOutOfPlace(Jogo winner)
    {
        int counter = 0;
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j)
                if (jogo[i][j] != winner.jogo[i][j])
                    ++counter;
        return counter;
    }

    //Heuristica de Manhattan
    public int countMovesOutOfPlace(Jogo winner)
    {
        int counter = 0;
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j)
            {
            	int x;
	            int y;
	            if(jogo[i][j] >= 1 && jogo[i][j] <= 4)
	            {
	                y = 0;
	                x = jogo[i][j] - 1;
	            }
	            else if(jogo[i][j] >= 5 && jogo[i][j] <= 8)
	            {
	                y = 1;
	                x = jogo[i][j] - 5;
	            }
	            else if(jogo[i][j] >= 9 && jogo[i][j] <= 12)
	            {
	                y = 2;
	                x = jogo[i][j] - 9;
	            }
	            else if(jogo[i][j] >= 13 && jogo[i][j] <= 15)
	            {
	                y = 3;
	                x = jogo[i][j] - 13;
	            }
	            else
	            {
	                y = 3;
	                x = 3;
	            }
	
	            counter+= Math.abs(i - y) + Math.abs(j - x);
            }
        return counter;
    }
    
    //Funcao de utilidade para debugging
    public void debug(Jogo winner)
    {
    	printGame();
    	System.out.println("Is it solvable?: " + solvability());
    	System.out.println("Pieces out of place: " + countOutOfPlace(winner));
    	System.out.println("Moves out of place: " + countMovesOutOfPlace(winner));
    	System.out.println("Depth: " + depth);
    	System.out.println("Previous Move: " + prevMove);
    	System.out.println("Path: " + pathString);
    	System.out.println();
    }
    
    //Devolve uma copia do jogo
    public Jogo clone()
    {
        Jogo temp = new Jogo();
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j)
                temp.jogo[i][j] = jogo[i][j];
        temp.blankX = blankX;
        temp.blankY = blankY;
        temp.prevMove = prevMove;
        temp.pathString = pathString; //QUICHE
        temp.depth = depth;
        return temp;
    }

    //Devolve um jogo movido
    public Jogo move(char dir)
    {
        Jogo temp = clone();
        temp.depth++;
        temp.pathString = temp.pathString + dir;
        temp.prevMove = dir;
        if(dir == 'U')
        {
            temp.jogo[temp.blankY][temp.blankX] = temp.jogo[temp.blankY - 1][temp.blankX];
            temp.jogo[temp.blankY - 1][temp.blankX] = 0;
            temp.blankY--;

        }
        if(dir == 'L')
        {
            temp.jogo[temp.blankY][temp.blankX] = temp.jogo[temp.blankY][temp.blankX - 1];
            temp.jogo[temp.blankY][temp.blankX - 1] = 0;
            temp.blankX--;
        }
        if(dir == 'D')
        {
            temp.jogo[temp.blankY][temp.blankX] = temp.jogo[temp.blankY + 1][temp.blankX];
            temp.jogo[temp.blankY + 1][temp.blankX] = 0;
            temp.blankY++;
        }
        if(dir == 'R')
        {
            temp.jogo[temp.blankY][temp.blankX] = temp.jogo[temp.blankY][temp.blankX + 1];
            temp.jogo[temp.blankY][temp.blankX + 1] = 0;
            temp.blankX++;
        }
        return temp;
    }
    
    //Devolve true se for possivel o movimento numa certa direcao
    public boolean canMove(char direction)
    {
    	if(direction == 'U')
    		if(blankY == 0)
    			return false;
    		else
    			return true;
    	if(direction == 'L')
    		if(blankX == 0)
    			return false;
    		else
    			return true;
    	if(direction == 'D')
    		if(blankY == 3)
    			return false;
    		else
    			return true;
    	if(direction == 'R')
    		if(blankX == 3)
    			return false;
    		else
    			return true;
    	return true;
    }
}
