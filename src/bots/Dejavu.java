package bots;

import main.*;

public class Dejavu implements Bot
{
    @Override
    public String getName()
    {
        return "Deja Vu";
    }

@Override
public Action takeTurn(int blue_levers, int red_levers, String history,
                       String memory, int roundNumber) {

    if(roundNumber == 1)
    {
        memory = "-1";
    }
    int[] blevers = getBlueLevers(memory);
    char[] hist = history.toCharArray();
    int ms = 0;
    int ts = 0;
    int rl = -1;
    boolean bl = false;
    boolean url = false;

    for(int i = 0; i < hist.length; i++)
    {
        switch(hist[i])
        {
            case '1':
            ms++;
            break;
            case '0':
            ts++;
            break;
        }
    }

    if(ts - ms >= 10)
    {   
        for(rl = hist.length - 1; ts - ms <= 5 && rl >= 0; rl--)
        {
            switch(hist[rl])
            {
                case '1':
                ms--;
                break;
                case '0':
                ts--;
                break;
            }
        }
        url = true;
    }

    if(ms - ts >= 7)
    {
        bl = true;
        url = false;
        memory += "," + roundNumber;
    }

    for(int i = 0; i < blevers.length; i++)
    {
        if(rl <= blevers[i])
        {
            rl = blevers[i] + 1;
        }
    }

    if(url)
    {
        return new Action(rl, memory);
    }
    else if(bl)
    {
        return new Action(-1, memory);
    }
    else
    {
        return new Action(0, memory);
    }              
}

private int[] getBlueLevers(String s)
{
    String[] b = s.split(",");

    int[] bl = new int[b.length];
    for(int i = 0; i < b.length; i++)
    {
        bl[i] = Integer.parseInt(b[i]);
    }

    return bl;
}

}
