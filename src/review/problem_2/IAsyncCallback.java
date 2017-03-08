package review.problem_2;

/**
 * Created by jellio on 3/8/2017.
 */
public interface IAsyncCallback {

    void setResult(String result);
    void setException(ThingException e);

}
