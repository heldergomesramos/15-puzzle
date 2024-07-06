import java.util.*;
public class JogoDosQuinzeMain 
{
	static Jogo winner = new Jogo();
	static Stack<Character> pathStack = new Stack<>();
	static LinkedList<Jogo> toExpand = new LinkedList<>();
	static String path = "";
	static long startTime;
	static long timeElapsed = 0;
	static boolean found = false;
	static int depthLimit = 0;
	static String order = "ULDR";
	static int expandedNodes = 0;
	static int greedyLimit = 1000;
	public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        Jogo atual = new Jogo();
        atual.readGame(in);
        winner.readGame(in);
        if(atual.solvability())	
        {
        	deepSearch(atual,false);
			bfs(atual);
        	deepSearch(atual,true);
	        greedy(atual, false);
	        greedy(atual, true);
	        aStar(atual,false);
	    	aStar(atual,true);
        }	
        else
        	System.out.println("There is no solution");
    }
	
	//Print aos dados da solução e dá reset a alguns dados
	public static void printAndReset(boolean deep) 
	{
		Runtime runtime = Runtime.getRuntime();
		System.out.print("Path: ");
		if(deep)
		{
			int cost = pathStack.size();
			printStack();
			System.out.println("Cost: " + cost);
		}
		else
		{
			System.out.println(path);
			System.out.println("Cost: " + path.length());
		}
        System.out.println("Expanded Nodes: " + expandedNodes);
        System.out.println("Time Elapsed: " + timeElapsed + "ms");
        System.out.println("Memory Used: " + (runtime.totalMemory() - runtime.freeMemory())/(1024) + "kb");
        System.out.println();
        System.gc();
        toExpand.clear();
        expandedNodes = 0;
	}
	
	//Reseta alguns dados
	public static void reset()
	{
		toExpand.clear();
		expandedNodes = 0;
		pathStack.clear();
		System.gc();
	}
	
	//Da print e limpa a stack
	public static void printStack()
	{
		if(pathStack.isEmpty())
			return;
		Stack<Character> reverse = new Stack<>();
		while(!pathStack.isEmpty())
			reverse.push(pathStack.pop());
		while(!reverse.isEmpty())
			System.out.print(reverse.pop());
		System.out.println();
	}
	
	//Devolve o caracter da direcao oposta
	public static char opDirection(char a) 
	{
		if(a == 'U')
			return 'D';
		if(a == 'L')
			return 'R';
		if(a == 'D')
			return 'U';
		return 'L';
	}
	
	//Algoritmo Deep Search (F -> Normal, T -> Iterativa)
	public static void deepSearch(Jogo atual, Boolean iterative)
    {
		try 
		{
	        if(iterative)
	        	System.out.println("Iterative Deep Search:...");
	        else 
	        	System.out.println("Deep Search:...");
	        startTime = System.currentTimeMillis();
	        if(!iterative)
	        	depthLimit = Integer.MAX_VALUE;
	        else 
	        	depthLimit = 0;
	        while(!found) 
	        {
	            pathStack.clear();
	            if(iterative)
	            	depthLimit++;
	            deepSearch(atual, ' ',0);
	            if(!iterative) 
	            	break;
	        }
	        timeElapsed = System.currentTimeMillis() - startTime;
	        printAndReset(true);
		}
        catch(StackOverflowError e)
		{
            System.out.println("Error: Stack Overflow");
            System.out.println();
            reset();
		}
		catch(OutOfMemoryError e)
  		{
  			System.out.println("Error: Out of Memory");
            System.out.println();
            reset();
  		}
    }

	//Funcao recursiva da deep search
    public static void deepSearch(Jogo atual, char lock, int count)
    {
    	expandedNodes++;
    	if(found)
        	return;
    	if(atual.equals(winner))
    	{
    		found = true;
    		return;
    	}
    	if(count >= depthLimit)
    	{
    		return;
    	}
        Jogo temp = atual.clone();
        for(int i = 0; i < 4; i++)
        {
        	char dir = order.charAt(i);
        	if(lock != dir && temp.canMove(dir))
            {
            	temp = temp.move(dir);
                pathStack.push(dir);
                deepSearch(temp, opDirection(dir),count + 1);
                temp = atual.clone();
                if(!found)
                	pathStack.pop();
                else
                	return;
            }
        }
    }
    
    //Algoritmo Breadth First Search
  	public static void bfs(Jogo atual)
  	{
  		try 
    	{
  			System.out.println("Breadth First Search:...");
	  		Queue<Jogo> queue = new LinkedList<>();
	  		queue.add(atual);
	  		startTime = System.currentTimeMillis();
	  		Jogo temp = new Jogo();
	  		while(!queue.isEmpty())
	  		{
	  			expandedNodes++;
	  			temp = queue.remove();
	  			if(temp.equals(winner))
	  			{
	  				timeElapsed = System.currentTimeMillis() - startTime;
                	path = temp.pathString;
                	printAndReset(false);
                	return;
	  			}
	  			for(int i = 0; i < 4; i++)
	  				if(temp.canMove(order.charAt(i)))
	  					queue.add(temp.move(order.charAt(i)));
	  		}
	  		
    	}
    	catch(StackOverflowError e)
		{
            System.out.println("Error: Stack Overflow");
            System.out.println();
            reset();
		}
  		catch(OutOfMemoryError e)
  		{
  			System.out.println("Error: Out of Memory");
            System.out.println();
            reset();
  		}
  	}
  	
    //Algoritmo Greedy (F -> Pieces out of place, T -> Manhattan)
    public static void greedy(Jogo atual, boolean heuristic)
    {
    	try 
    	{
    		if(heuristic)
        		System.out.println("Greedy using Manhattan:...");
        	else
        		System.out.println("Greedy using Piece Count:...");
        	startTime = System.currentTimeMillis();
        	toExpand.add(atual);
        	Jogo temp = new Jogo();
        	Jogo selected = new Jogo();
        	while (true)
        	{
        		if(expandedNodes > greedyLimit)
        		{
        			System.out.println("Error: Exceeded node limit");
        			System.out.println();
        			reset();
        			return;
        		}
        		expandedNodes++;
        		int min = Integer.MAX_VALUE;
        		for (int i = 0; i < toExpand.size() ; i++)
                {
                    temp = toExpand.get(i);
                    int g;
                    if(heuristic)
                    	g = temp.countMovesOutOfPlace(winner);
                    else
                    	g = temp.countOutOfPlace(winner);
                    if(g < min)
                    {
                        min = g;
                        selected = temp;
                    }
                }
        		toExpand.remove(selected);
                if(selected.prevMove != ' ')	
                	pathStack.push(selected.prevMove);
                if(selected.equals(winner))
                {
                	timeElapsed = System.currentTimeMillis() - startTime;
                	path = selected.pathString;
                	printAndReset(false);
                	return;
                }
                for(int i = 0; i < 4; i++)
	  				if(selected.canMove(order.charAt(i)))
	  					toExpand.add(selected.move(order.charAt(i)));
        	}
    	}
    	catch(StackOverflowError e)
		{
            System.out.println("Error: Stack Overflow");
            System.out.println();
            reset();
		}
    	catch(OutOfMemoryError e)
  		{
  			System.out.println("Error: Out of Memory");
            System.out.println();
            reset();
  		}
    }
    
    //Algoritmo A* (F -> Pieces out of place, T -> Manhattan)
    public static void aStar(Jogo atual, boolean heuristic)
    {
    	try
    	{
    		if(heuristic)
        		System.out.println("A* using Manhattan:...");
        	else
        		System.out.println("A* using Piece Count:...");
        	startTime = System.currentTimeMillis();
        	toExpand.add(atual);
        	Jogo temp = new Jogo();
        	Jogo selected = new Jogo();
        	while (true)
        	{
        		expandedNodes++;
        		int min = Integer.MAX_VALUE;
        		for (int i = 0; i < toExpand.size() ; i++)
                {
                    temp = toExpand.get(i);
                    int g;
                    if(heuristic)
                    	g = temp.countMovesOutOfPlace(winner);
                    else
                    	g = temp.countOutOfPlace(winner);
                    if(g + temp.depth < min)
                    {
                        min = g + temp.depth;
                        selected = temp;
                    }
                }
        		toExpand.remove(selected);
                if(selected.equals(winner))
                {
                	timeElapsed = System.currentTimeMillis() - startTime;
                	path = selected.pathString;
                	printAndReset(false);
                	return;
                }
                for(int i = 0; i < 4; i++)
	  				if(selected.canMove(order.charAt(i)))
	  					toExpand.add(selected.move(order.charAt(i)));
        	}
    	}
    	catch(StackOverflowError e)
		{
            System.out.println("Error: Stack Overflow");
            System.out.println();
            reset();
		}
    	catch(OutOfMemoryError e)
  		{
  			System.out.println("Error: Out of Memory");
            System.out.println();
            reset();
  		}
    }
}
