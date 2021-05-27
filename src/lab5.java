import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Lab 5
 * CPE 315 - Spring 2021 - John Seng
 * Authors: Travis Hawley and Aiden Nguyen
 * Program: Implement a cache simulator which models 7 different cache configurations and prints number of hits and hit rate
 */

public class lab5 {
    public static void main(String[] args) {
        try {
            readFile(args[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void readFile(String filename) throws FileNotFoundException {
        Scanner file = new Scanner(new File(filename));
        ArrayList<cache> cacheList = new ArrayList<>();cacheList.add(new cache(1, 9, 1, 1));
        cacheList.add(new cache(2, 8, 2, 1));
        cacheList.add(new cache(3, 7, 4, 1));

        cacheList.add(new cache(4, 8, 1, 2));
        cacheList.add(new cache(5, 7, 1, 4));
        cacheList.add(new cache(6, 5, 4, 4));

        cacheList.add(new cache(7, 10, 1,1));

        int lineNum = 1;
        while(file.hasNext())
        {
            String[] line = file.nextLine().split("\t");
            for(cache c : cacheList)
            {
                if(c.computeCache(line[1], lineNum))
                {
                    c.setHits();
                }
                c.setAccesses();
            }
            lineNum++;
        }
        for(cache c : cacheList)
        {
            printData(c);
        }
    }

    public static void printData(cache c){
        System.out.printf("Cache #%d\n", c.getID());
        System.out.printf("Cache size: %dB\t\tAssociativity: %d\t\tBlock size: %d\n", c.getCacheSize(), c.getAssociativity(), c.getBlock());
        System.out.printf("Hits: %d\tHit Rate: %.2f%%\n", c.getHits(), c.getHitRate());
        System.out.printf("---------------------------\n");
    }


}
