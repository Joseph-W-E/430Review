package review.problem_12.error;

import java.awt.Point;

public class Trajectory
{
    private Point[] data;

    public Trajectory(Point[] data)
    {
        this.data = data;
    }

    public Point[] getValues()
    {
        synchronized(this)
        {
            return this.data;
        }
    }

    public Point getValue(int index)
    {
        synchronized(this.data[index])
        {
            return this.data[index];
        }
    }

    public void update(int i, Point p)
    {
        synchronized(this.data[i])
        {
            this.data[i] = p;
        }
    }
}