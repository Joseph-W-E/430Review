package review.problem_11.deadlock;

public interface Lookup
{
    Kind getKind();

    int getKey();

    /**
     * @param kind The kind of lookup to make.
     * @param key  The key to be looked up.
     * @return     A new instance of such a lookup.
     */
    static Lookup make(Kind kind, int key)
    {
        return new Lookup()
        {
            @Override
            public Kind getKind()
            {
                return kind;
            }

            @Override
            public int getKey()
            {
                return key;
            }
        };
    }

    enum Kind
    {
        /**
         * Such a lookup has not yet been made.
         */
        UNREQUESTED,

        /**
         * This lookup has been initiated but not yet handled. This is either because the lookup
         * request has been queued for processing or because the lookup is currently being
         * processed.
         */
        UNHANDLED,

        /**
         * This lookup has been handled and the result has been cached.
         */
        CACHED,

        /**
         * Indicates that there will be no more lookup requests.
         */
        TERMINATE,
    }
}