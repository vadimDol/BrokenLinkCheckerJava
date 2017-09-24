package net.BrokenLinkChecker;

import java.util.ArrayList;

public class Stack
{
    private ArrayList<String> a;

    public Stack()
    {
        a = new ArrayList(10);
    }

    public boolean isEmpty()
    {
        return a.isEmpty();
    }

    public String pop()
    {
        String last;
        last = a.remove((a.size()- 1));
        return(last);
    }

    public void push(String x)
    {
        a.add(x);
    }

    public String arrayTop()
    {
        return(a.get(a.size() -1));
    }

    public boolean isExists(String str) {
        return a.contains(str);
    }
}