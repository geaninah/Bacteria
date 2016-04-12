import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Bacteria
{
    static List<int[]> getNeighbors(int[] bacteria)
    {
        List<int[]> neighbors = new ArrayList<>();

        // Neighbors previous row
        neighbors.add(new int[] {bacteria[0] - 1, bacteria[1] - 1});
        neighbors.add(new int[] {bacteria[0] - 1, bacteria[1]});
        neighbors.add(new int[] {bacteria[0] - 1, bacteria[1] + 1});

        // Neighbors same row
        neighbors.add(new int[] {bacteria[0], bacteria[1] - 1});
        neighbors.add(new int[] {bacteria[0], bacteria[1] + 1});

        // Neighbors next row
        neighbors.add(new int[] {bacteria[0] + 1, bacteria[1] - 1});
        neighbors.add(new int[] {bacteria[0] + 1, bacteria[1]});
        neighbors.add(new int[] {bacteria[0] + 1, bacteria[1] + 1});

        return neighbors;
    }

    // Verify if bacteria contains a certain position
    static boolean contains(List<int[]> bacteria, int[] position)
    {
        for(int[] currentBacteria : bacteria)
        {
            if(currentBacteria[0] == position[0] && currentBacteria [1] == position[1])
            {
                return true;
            }
        }

        return false;
    }

    // Count the number of alive neighbors
    static int countAliveNeighbors(List<int[]> bacteria, int[] position)
    {
        List<int[]> possibleNeighbors = getNeighbors(position);
        int noOfAliveNeighbors = 0;

        for(int[] neighbor : possibleNeighbors)
        {
            if(contains(bacteria, neighbor))
            {
                noOfAliveNeighbors++;
            }
        }

        return noOfAliveNeighbors;
    }

    static List<int[]> getNextGeneration(List<int[]> bacteria)
    {
        List<int[]> nextGeneration = new ArrayList<>();

        for(int[] currentBacteria : bacteria)
        {
            // Get the number of alive neighbors
            int noOfAliveNeighbors = countAliveNeighbors(bacteria, currentBacteria);
        
            // Add the current bacteria to the next generation if there are
            // 2 or 3 neighbors alive
            if(noOfAliveNeighbors == 2 || noOfAliveNeighbors == 3)
            {
                nextGeneration.add(currentBacteria);
            }

            // Get neighbors of current bacteria
            List<int[]> neighbors = getNeighbors(currentBacteria);

            for(int[] position : neighbors)
            {
                // Skip if this neighbor is an alive bacteria
                if(contains(nextGeneration, position) || contains(bacteria, position))
                {
                    continue;
                }

                noOfAliveNeighbors = countAliveNeighbors(bacteria, position);

                // Checks if this bacteria comes to life
                if(noOfAliveNeighbors == 3)
                {
                    nextGeneration.add(position);
                }
            }
        }

        return nextGeneration;
    }

    public static void main(String args[])
    {
        BufferedReader input = null;
        PrintWriter output = null;

        try
        {
            if(args.length != 2)
            {
                throw new IllegalArgumentException("There must be 2 arguments");
            }

            input = new BufferedReader(new FileReader(args[0]));
            output = new PrintWriter(new FileWriter(args[1]));

            List<int[]> bacteria = new ArrayList<>();
            String currentLine;

            // Read lines from textfile
            while((currentLine = input.readLine()) != null)
            {
                String[] position = currentLine.split(",");

                int x = Integer.parseInt(position[0]);
                int y = Integer.parseInt(position[1]);
                
                // Stop if we have -1 and -1 as input
                if(x == -1 && y == -1)
                {
                    break;
                }

                bacteria.add(new int[] {x, y});
            }
            
            // Replace the current bacteria with new generation
            bacteria = getNextGeneration(bacteria);

            for(int[] currentBacteria : bacteria)
            {
                output.println(currentBacteria[0] + "," + currentBacteria[1]);
            }

            output.println("-1,-1");

        }
        catch(Exception exception)
        {
            System.err.println(exception);
        }
        finally
        {
            try
            {
                if(input != null)
                {
                    input.close();
                }
            }
            catch(IOException exception)
            {
                System.err.println("Could not close the input " + exception);
            }

            if(output != null)
            {
                output.close();

                if(output.checkError())
                {
                    System.err.println("Something went wrong with the output");
                }
            }
        }
    }
}