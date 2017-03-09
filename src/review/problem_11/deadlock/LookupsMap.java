package review.problem_11.deadlock;

import java.util.ArrayList;
import java.util.List;

import review.problem_11.deadlock.Lookup.Kind;

/**
 * Maps from keys to some value of {@link Lookup.Kind} in order to keep track of the status of a set
 * of {@link Lookup} requests.
 */
public class LookupsMap
{
    private List<Lookup> lookups = new ArrayList<>();

    public Lookup get(int key)
    {
        for (int i = 0; i < lookups.size(); i++)
        {
            Lookup lookup = lookups.get(i);
            if (lookup.getKey() == key)
            {
                return lookup;
            }
        }
        return Lookup.make(Kind.UNREQUESTED, key);
    }

    /**
     * @param kind
     * @param key
     */
    public void set(int key, Kind kind)
    {
        set(Lookup.make(kind, key));
    }

    public void set(Lookup lookup)
    {
        for (int i = 0; i < lookups.size(); i++)
        {
            if (lookups.get(i).getKey() == lookup.getKey())
            {
                lookups.set(i, lookup);
                return;
            }
        }
        lookups.add(lookup);
    }
}