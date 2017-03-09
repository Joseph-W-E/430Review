package review.problem_1;

/**
 * Created by jellio on 3/8/2017.
 */
public class CustomCallback implements IAsyncCallback {

    private String result;
    private Exception exception;

    public String getResult() throws Exception {
        if (exception != null) {
            throw exception;
        } else {
            return result;
        }
    }

    public boolean ready() {
        return result != null || exception != null;
    }

    @Override
    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public void setException(ThingException e) {
        this.exception = exception;
    }
}
