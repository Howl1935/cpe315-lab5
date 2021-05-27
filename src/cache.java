
public class cache {
    private final int cacheID;
    private final int byteOffset;
    private final int blockOffset;
    private final int index;
    private final int block;
    private final int size;
    private final int[][] addressArr;
    private int hits;
    private int accesses;
    private int mapping;
    private int cols;

    //constructor
    public cache(int casheID, int index, int word, int mapping)
    {
        //cache
        //when cashe is implemented it is passed an id #, the index bit size, and the block size as a word

        this.cacheID = casheID;         //store the id of this cashe
        this.byteOffset = 2;            //all of the offsets will be 2 bits
        //block offset is how many bits we need to access all of the words in the block ex: 2 words = 1 bit, 0 or 1
        //4 words = 2 bits 00, 01, 10, 11
        //words / 2
        //1/2 for ints is 0 because it truncates
        this.block = word;
        this.blockOffset = (word)/2;
        this.index = index;
        int tag = 32 - this.index - this.blockOffset - this.byteOffset;
        int casheIndices = ((int) Math.pow(2.0, (double) index));          //this is the length of our cache

        //multiply by mapping because size is dependent on the associative cache size
        this.size = (casheIndices * (4*word)) * mapping;
        this.cols = 1;
        if(mapping > 1)
            cols = (mapping*cols)*2;
        this.addressArr = new int[casheIndices][cols];
        this.hits = 0;
        this.accesses = 0;
        this.mapping = mapping;
    }


    //this is what method we will call when reading in file
    public boolean computeCache(String address, int lineNum)
    {
        boolean hitMiss = false;
        //turn given address into an integer
        int addVal = Integer.parseInt(address, 16);
        int cacheTag = getTag(addVal);

        //get the index at which this integer would be stored
         int index = getIndex(addVal);

         if(mapping > 1)
        {
            //even numbers 0,2,4,...,n represent arrays
            //odd numbers 1,3,5,...,n represent the lines at which tags were inserted for later comparison

            //first go through the rows and check to see if we have the address

            //get line number for array1 (line #s start at 1 to not interfere with initialized to zero array)
            int oldestLineNum = this.addressArr[index][1];
            int oldestIndex = 0;

            for(int i = 0; i < cols; i+=2)
            {
                //if this element is == to cacheTag
                if(this.addressArr[index][i] == cacheTag )
                {
                    this.addressArr[index][i+1] = lineNum;
                    hitMiss = true;
                    break;
                }
                //otherwise keep track of the smallest line number in this line
                else if(this.addressArr[index][i+1] < oldestLineNum )
                {
                    oldestLineNum = this.addressArr[index][i+1];
                    oldestIndex = i;
                }

            }
            //if hitMiss is true we have found a match and we will return that we got a hit, otherwise we store
            //current tag into oldest space or empty space 
            if(!hitMiss)
            {
                this.addressArr[index][oldestIndex] = cacheTag;
                this.addressArr[index][oldestIndex + 1] = lineNum;
            }

        }
        else{
            //check valueArr to make sure given index value == 0
            //if T put address into our addressArr at the given index
             if(this.addressArr[index][0] == cacheTag)
                hitMiss = true;
            else
                this.addressArr[index][0] = cacheTag;

        }

        return hitMiss;
    }

    public int getIndex(int val)
    {
        int mask = 1;
        int shiftVal = val >>> (byteOffset + blockOffset);
        int result = 0;
        for(int i =0; i < this.index; i++)
        {
            result += shiftVal & mask;
            mask = mask << 1;
        }
        return result % this.addressArr.length;
    }

    public int getTag(int val)
    {
        int tagVal = val >>> (byteOffset + blockOffset + index);

        return tagVal;
    }


    public int getID()
    {
        return this.cacheID;
    }
    public int getCacheSize()
    {
        return this.size;
    }
    public int getAssociativity()
    {
        return this.mapping;
    }
    public int getBlock()
    {
        return this.block;
    }
    public int getHits()
    {
        return this.hits;
    }
    public void setHits()
    {
        this.hits++;
    }
    public void setAccesses()
    {
        this.accesses++;
    }
    public double getHitRate()
    {
        return ((double)this.hits/(double)this.accesses)*100;
    }



}
