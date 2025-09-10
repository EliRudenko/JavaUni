package comparators;

import java.util.Comparator;
import models.Cat;

public class CatNameLengthComparator implements Comparator<Cat>
{
    @Override
    public int compare(Cat c1, Cat c2)
    {
        int diff = c1.name.length() - c2.name.length();
        if (diff == 0) { return c1.name.compareTo(c2.name); }
        return diff;
    }
}
