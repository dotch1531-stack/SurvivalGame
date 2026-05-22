import greenfoot.*;

public class BreakProgress extends WorldObject
{
    private GreenfootImage sheet;

    private int frameWidth = 40;
    private int frameHeight = 13;

    private int totalFrames = 10;

    public BreakProgress()
    {
        sheet = new GreenfootImage("breakbar.png");

        setStage(0);
    }

    public void setStage(int stage)
    {
        if(stage < 0)
            stage = 0;

        if(stage >= totalFrames)
            stage = totalFrames - 1;

        GreenfootImage frame =
            new GreenfootImage(frameWidth, frameHeight);

        frame.drawImage(
            sheet,
            -stage *  frameWidth,
            0
        );

        setImage(frame);
    }
}