import greenfoot.*;

public class BreakProgress extends Actor
{
    private GreenfootImage sheet;

    private int frameWidth = 40;
    private int frameHeight = 13;

    private int totalFrames = 17;

    public BreakProgress()
    {
        sheet = new GreenfootImage("breakbar.png");

        setStage(0);
    }

    public void setStage(int stage)
    {
        // clamp stage
        if(stage < 0)
            stage = 0;

        if(stage >= totalFrames)
            stage = totalFrames - 1;

        // create frame image
        GreenfootImage frame =
            new GreenfootImage(frameWidth, frameHeight);

        // draw correct frame from spritesheet
        frame.drawImage(
            sheet,
            -stage /* * schaden*/ * frameWidth,
            0
        );

        setImage(frame);
    }
}