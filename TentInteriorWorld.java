import greenfoot.*;

public class TentInteriorWorld extends World
{
    public static TentInteriorWorld instance;

    public TentInteriorWorld()
    {
		super(800, 800, 1);
        instance = this;

        GreenfootImage bg = getBackground();

        drawRoom(bg);
    }

    private void drawRoom(GreenfootImage bg)
    {
        int tileSize = 80;

        int gridSize = 3 * tileSize; // 240px

        int centerX = 400;
        int centerY = 400;

        int startX = centerX - gridSize / 2;
        int startY = centerY - gridSize / 2;

        int border = 20;

        // =========================
        // 🟤 ÄUSSERER RAHMEN (HELLBRAUN)
        // =========================
        bg.setColor(new Color(190, 160, 120));
        bg.fill();

        // =========================
        // 🟤 MITTLERER RAHMEN (DUNKLER)
        // =========================
        bg.setColor(new Color(110, 80, 50));
        bg.fillRect(
            startX - border,
            startY - border,
            gridSize + border * 2,
            gridSize + border * 2
        );

        // =========================
        // 🟫 BODEN (3x3 TILE)
        // =========================
        GreenfootImage tile = new GreenfootImage("tentTile.png");

        for(int x = 0; x < 3; x++)
        {
            for(int y = 0; y < 3; y++)
            {
                bg.drawImage(tile,
                    startX + x * tileSize,
                    startY + y * tileSize
                );
            }
        }

        // =========================
        // ⬛ LINIE UM BODEN
        // =========================
        bg.setColor(Color.BLACK);
        bg.drawRect(startX, startY, gridSize, gridSize);

        // =========================
        // 🧍 PLAYER
        // =========================
        addObject(new TentPlayer(), 400, 400);
    }

}