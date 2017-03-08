package review.problem_2;

/**
 * Created by jellio on 3/8/2017.
 */
public class CustomCallback implements IAsyncCallback {

    private String r;
    private Exception e;

    @Override
    public void setResult(String result) {
        this.r = result;
    }

    @Override
    public void setException(ThingException e) {
        this.e = e;
    }

    public boolean hasData() {
        return r != null || e != null;
    }
}
