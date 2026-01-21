import greenfoot.*;
import java.util.List;
import java.util.ArrayList;

/**
 * EndGameWorld - Displays the end game screen with stats for both players.
 * Shows winner, time taken, pieces captured with scrollable content.
 * 
 * @author Saiful Shaik
 * @version 1.1
 */
public class EndGameWorld extends MenuWorld
{
    // Game result data
    private String winner; // "WHITE", "BLACK", or "DRAW"
    private String endReason;
    private int whiteTimeRemaining;
    private int blackTimeRemaining;
    private int totalGameTime;
    private int turnCount;
    
    // Captured pieces
    private List<Piece.PieceType> whiteCapturedPieces;
    private List<Piece.PieceType> blackCapturedPieces;
    
    // Scrolling content
    private ArrayList<Actor> scrollableActors = new ArrayList<>();
    private ArrayList<Integer> originalYPositions = new ArrayList<>();
    private int scrollY = 0;
    private int minScrollY = 0;
    private int maxScrollY = 0;
    
    // Viewport bounds
    private static final int VIEWPORT_TOP = 160;
    private static final int VIEWPORT_BOTTOM = 480;
    private static final int VIEWPORT_HEIGHT = VIEWPORT_BOTTOM - VIEWPORT_TOP;
    
    // Scrollbar
    private int scrollbarX = 575;
    private int scrollbarTrackY = VIEWPORT_TOP;
    private int scrollbarTrackHeight = VIEWPORT_HEIGHT;
    private boolean isDraggingScrollbar = false;
    private boolean isDraggingContent = false;
    private int lastMouseY = 0;
    
    // Navigation buttons (fixed, not scrolling)
    private Button playAgainButton;
    private Button mainMenuButton;
    
    // View captured buttons
    private Button whiteViewCapturedButton;
    private Button blackViewCapturedButton;
    
    // Popup state
    private CapturedPiecesPopup activePopup = null;
    private boolean waitingForRelease = false;
    
    public EndGameWorld(String winner, String endReason, 
                        int whiteTimeRemaining, int blackTimeRemaining,
                        int totalGameTime, int turnCount,
                        List<Piece.PieceType> whiteCapturedPieces,
                        List<Piece.PieceType> blackCapturedPieces)
    {
        super();
        this.winner = winner;
        this.endReason = endReason;
        this.whiteTimeRemaining = whiteTimeRemaining;
        this.blackTimeRemaining = blackTimeRemaining;
        this.totalGameTime = totalGameTime;
        this.turnCount = turnCount;
        this.whiteCapturedPieces = whiteCapturedPieces != null ? whiteCapturedPieces : new ArrayList<>();
        this.blackCapturedPieces = blackCapturedPieces != null ? blackCapturedPieces : new ArrayList<>();
        
        createBackground();
        setupFixedUI();
        setupScrollableContent();
        
        SoundManager.getInstance().playLose();
    }
    
    private void createBackground()
    {
        GreenfootImage bg = new GreenfootImage(600, 600);
        
        // Gradient background
        for (int y = 0; y < 600; y++)
        {
            int r = 15 + (y * 10 / 600);
            int g = 20 + (y * 15 / 600);
            int b = 40 + (y * 30 / 600);
            bg.setColor(new Color(r, g, b));
            bg.drawLine(0, y, 600, y);
        }
        
        // Decorative border
        bg.setColor(new Color(255, 215, 0, 100));
        bg.drawRect(15, 15, 570, 570);
        bg.drawRect(17, 17, 566, 566);
        
        // Viewport area indicator
        bg.setColor(new Color(40, 45, 70));
        bg.fillRect(25, VIEWPORT_TOP, 540, VIEWPORT_HEIGHT);
        bg.setColor(new Color(60, 70, 100));
        bg.drawRect(25, VIEWPORT_TOP, 540, VIEWPORT_HEIGHT);
        
        setBackground(bg);
    }
    
    private void setupFixedUI()
    {
        // Title - GAME OVER
        Label titleLabel = new Label("GAME OVER", 48);
        titleLabel.setFillColor(new Color(255, 215, 0));
        titleLabel.setLineColor(new Color(180, 140, 0));
        addObject(titleLabel, 300, 45);
        
        // Winner announcement
        String winnerText = winner.equals("DRAW") ? "IT'S A DRAW!" : winner + " WINS!";
        Color winnerColor = winner.equals("DRAW") ? new Color(255, 255, 255) : 
                          (winner.equals("WHITE") ? new Color(255, 255, 255) : new Color(200, 150, 255));
        Label winnerLabel = new Label(winnerText, 32);
        winnerLabel.setFillColor(winnerColor);
        winnerLabel.setLineColor(new Color(100, 80, 150));
        addObject(winnerLabel, 300, 90);
        
        // End reason
        Label reasonLabel = new Label(endReason, 18);
        reasonLabel.setFillColor(new Color(220, 220, 220));
        addObject(reasonLabel, 300, 120);
        
        // Scroll hint
        Label scrollHint = new Label("↓ Scroll to see more ↓", 14);
        scrollHint.setFillColor(new Color(200, 200, 200));
        addObject(scrollHint, 300, VIEWPORT_BOTTOM + 15);
        
        // Navigation buttons at bottom
        playAgainButton = new Button("PLAY AGAIN", 160, 45, 
            new Color(50, 150, 50), new Color(70, 180, 70), Color.WHITE, 20);
        addObject(playAgainButton, 180, 555);
        
        mainMenuButton = new Button("MAIN MENU", 160, 45,
            new Color(150, 50, 50), new Color(180, 70, 70), Color.WHITE, 20);
        addObject(mainMenuButton, 420, 555);
    }
    
    private void setupScrollableContent()
    {
        int currentY = VIEWPORT_TOP + 30;
        int spacing = 35;
        
        // Game stats section
        Label statsTitle = new Label("═══ GAME STATISTICS ═══", 20);
        statsTitle.setFillColor(new Color(255, 215, 0));
        addScrollableActor(statsTitle, 300, currentY);
        currentY += spacing;
        
        Label timeLabel = new Label("Game Duration: " + formatTime(totalGameTime), 18);
        timeLabel.setFillColor(Color.WHITE);
        addScrollableActor(timeLabel, 300, currentY);
        currentY += spacing - 5;
        
        Label turnLabel = new Label("Total Turns: " + turnCount, 18);
        turnLabel.setFillColor(Color.WHITE);
        addScrollableActor(turnLabel, 300, currentY);
        currentY += spacing + 15;
        
        // Player stats based on game outcome
        if (winner.equals("DRAW")) {
            currentY = setupDrawStats(currentY);
        } else {
            currentY = setupWinnerLoserStats(currentY);
        }
        
        // Calculate scroll bounds
        int contentHeight = currentY - VIEWPORT_TOP;
        if (contentHeight > VIEWPORT_HEIGHT) {
            minScrollY = -(contentHeight - VIEWPORT_HEIGHT + 40);
        } else {
            minScrollY = 0;
        }
    }
    
    private int setupDrawStats(int startY)
    {
        int currentY = startY;
        int spacing = 30;
        
        // WHITE player stats
        Label whiteSectionTitle = new Label("═══ WHITE ═══", 22);
        whiteSectionTitle.setFillColor(new Color(240, 240, 240));
        addScrollableActor(whiteSectionTitle, 300, currentY);
        currentY += spacing;
        
        Label whiteTime = new Label("Time Remaining: " + formatTime(whiteTimeRemaining), 18);
        whiteTime.setFillColor(Color.WHITE);
        addScrollableActor(whiteTime, 300, currentY);
        currentY += spacing - 5;
        
        Label whiteCaptured = new Label("Pieces Captured: " + whiteCapturedPieces.size(), 18);
        whiteCaptured.setFillColor(Color.WHITE);
        addScrollableActor(whiteCaptured, 300, currentY);
        currentY += spacing;
        
        whiteViewCapturedButton = new Button("View White's Captures", 200, 40,
            new Color(70, 100, 150), new Color(90, 120, 170), Color.WHITE, 16);
        addScrollableActor(whiteViewCapturedButton, 300, currentY);
        currentY += spacing + 25;
        
        // BLACK player stats
        Label blackSectionTitle = new Label("═══ BLACK ═══", 22);
        blackSectionTitle.setFillColor(new Color(200, 150, 255));
        blackSectionTitle.setLineColor(new Color(120, 80, 180));
        addScrollableActor(blackSectionTitle, 300, currentY);
        currentY += spacing;
        
        Label blackTime = new Label("Time Remaining: " + formatTime(blackTimeRemaining), 18);
        blackTime.setFillColor(new Color(255, 255, 255));
        addScrollableActor(blackTime, 300, currentY);
        currentY += spacing - 5;
        
        Label blackCaptured = new Label("Pieces Captured: " + blackCapturedPieces.size(), 18);
        blackCaptured.setFillColor(new Color(255, 255, 255));
        addScrollableActor(blackCaptured, 300, currentY);
        currentY += spacing;
        
        blackViewCapturedButton = new Button("View Black's Captures", 200, 40,
            new Color(60, 80, 120), new Color(80, 100, 140), Color.WHITE, 16);
        addScrollableActor(blackViewCapturedButton, 300, currentY);
        currentY += spacing + 20;
        
        return currentY;
    }
    
    private int setupWinnerLoserStats(int startY)
    {
        int currentY = startY;
        int spacing = 30;
        
        String loser = winner.equals("WHITE") ? "BLACK" : "WHITE";
        int winnerTime = winner.equals("WHITE") ? whiteTimeRemaining : blackTimeRemaining;
        int loserTime = winner.equals("WHITE") ? blackTimeRemaining : whiteTimeRemaining;
        List<Piece.PieceType> winnerCaptures = winner.equals("WHITE") ? whiteCapturedPieces : blackCapturedPieces;
        List<Piece.PieceType> loserCaptures = winner.equals("WHITE") ? blackCapturedPieces : whiteCapturedPieces;
        
        // Winner section
        Label crownLabel = new Label("★ WINNER ★", 24);
        crownLabel.setFillColor(new Color(255, 215, 0));
        addScrollableActor(crownLabel, 300, currentY);
        currentY += spacing;
        
        Color winnerNameColor = winner.equals("WHITE") ? new Color(255, 255, 255) : new Color(200, 150, 255);
        Label winnerName = new Label("═══ " + winner + " ═══", 26);
        winnerName.setFillColor(winnerNameColor);
        winnerName.setLineColor(winner.equals("BLACK") ? new Color(120, 80, 180) : new Color(180, 180, 180));
        addScrollableActor(winnerName, 300, currentY);
        currentY += spacing + 5;
        
        Label winnerTimeLabel = new Label("Time Remaining: " + formatTime(winnerTime), 18);
        winnerTimeLabel.setFillColor(Color.WHITE);
        addScrollableActor(winnerTimeLabel, 300, currentY);
        currentY += spacing - 5;
        
        Label winnerCapturedLabel = new Label("Pieces Captured: " + winnerCaptures.size(), 18);
        winnerCapturedLabel.setFillColor(Color.WHITE);
        addScrollableActor(winnerCapturedLabel, 300, currentY);
        currentY += spacing;
        
        whiteViewCapturedButton = new Button("View " + winner + "'s Captures", 220, 40,
            new Color(70, 100, 150), new Color(90, 120, 170), Color.WHITE, 16);
        addScrollableActor(whiteViewCapturedButton, 300, currentY);
        currentY += spacing + 30;
        
        // Loser section (smaller)
        Label loserTitle = new Label("─── " + loser + " ───", 20);
        loserTitle.setFillColor(new Color(220, 220, 220));
        addScrollableActor(loserTitle, 300, currentY);
        currentY += spacing - 5;
        
        Label loserTimeLabel = new Label("Time Remaining: " + formatTime(loserTime), 16);
        loserTimeLabel.setFillColor(new Color(220, 220, 220));
        addScrollableActor(loserTimeLabel, 300, currentY);
        currentY += spacing - 8;
        
        Label loserCapturedLabel = new Label("Pieces Captured: " + loserCaptures.size(), 16);
        loserCapturedLabel.setFillColor(new Color(220, 220, 220));
        addScrollableActor(loserCapturedLabel, 300, currentY);
        currentY += spacing - 5;
        
        blackViewCapturedButton = new Button("View " + loser + "'s Captures", 200, 35,
            new Color(60, 80, 120), new Color(80, 100, 140), Color.WHITE, 14);
        addScrollableActor(blackViewCapturedButton, 300, currentY);
        currentY += spacing + 20;
        
        return currentY;
    }
    
    private void addScrollableActor(Actor actor, int x, int y)
    {
        addObject(actor, x, y);
        scrollableActors.add(actor);
        originalYPositions.add(y);
    }
    
    public void act()
    {
        // Handle popup
        if (activePopup != null) {
            if (waitingForRelease) {
                if (!Greenfoot.isKeyDown("space") && Greenfoot.getMouseInfo() == null) {
                    waitingForRelease = false;
                }
                if (Greenfoot.mousePressed(null)) {
                    waitingForRelease = false;
                }
                return;
            }
            
            if (Greenfoot.mouseClicked(null) || Greenfoot.isKeyDown("escape")) {
                closePopup();
                return;
            }
            return;
        }
        
        handleScrolling();
        updateActorPositions();
        drawScrollbar();
        handleButtons();
    }
    
    private void handleScrolling()
    {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        
        if (mouse != null) {
            int mouseY = mouse.getY();
            int mouseX = mouse.getX();
            
            // Mouse drag scrolling
            if (Greenfoot.mousePressed(null) && mouseY >= VIEWPORT_TOP && mouseY <= VIEWPORT_BOTTOM) {
                if (mouseX >= scrollbarX - 15) {
                    isDraggingScrollbar = true;
                } else {
                    isDraggingContent = true;
                }
                lastMouseY = mouseY;
            }
            
            if (Greenfoot.mouseDragged(null)) {
                if (isDraggingContent || isDraggingScrollbar) {
                    int deltaY = mouseY - lastMouseY;
                    if (isDraggingScrollbar) {
                        // Invert for scrollbar
                        scrollY -= (int)(deltaY * 2.5);
                    } else {
                        scrollY += deltaY;
                    }
                    lastMouseY = mouseY;
                    scrollY = Math.max(minScrollY, Math.min(maxScrollY, scrollY));
                }
            }
            
            if (Greenfoot.mouseClicked(null) || Greenfoot.mouseDragEnded(null)) {
                isDraggingContent = false;
                isDraggingScrollbar = false;
            }
        }
        
        // Keyboard scrolling
        if (Greenfoot.isKeyDown("up")) {
            scrollY = Math.min(maxScrollY, scrollY + 8);
        }
        if (Greenfoot.isKeyDown("down")) {
            scrollY = Math.max(minScrollY, scrollY - 8);
        }
    }
    
    private void updateActorPositions()
    {
        for (int i = 0; i < scrollableActors.size(); i++) {
            Actor actor = scrollableActors.get(i);
            int originalY = originalYPositions.get(i);
            int newY = originalY + scrollY;
            
            actor.setLocation(actor.getX(), newY);
            
            // Hide if outside viewport
            if (newY < VIEWPORT_TOP - 30 || newY > VIEWPORT_BOTTOM + 30) {
                actor.getImage().setTransparency(0);
            } else {
                actor.getImage().setTransparency(255);
            }
        }
    }
    
    private void drawScrollbar()
    {
        GreenfootImage bg = getBackground();
        
        // Clear scrollbar area
        bg.setColor(new Color(30, 35, 55));
        bg.fillRect(scrollbarX - 8, scrollbarTrackY, 16, scrollbarTrackHeight);
        
        // Draw track
        bg.setColor(new Color(50, 55, 75));
        bg.fillRect(scrollbarX - 4, scrollbarTrackY, 8, scrollbarTrackHeight);
        
        // Calculate thumb position and size
        if (minScrollY < 0) {
            int totalRange = maxScrollY - minScrollY;
            float viewRatio = (float)VIEWPORT_HEIGHT / (VIEWPORT_HEIGHT + Math.abs(minScrollY));
            int thumbHeight = Math.max(30, (int)(scrollbarTrackHeight * viewRatio));
            
            float scrollRatio = (float)(scrollY - minScrollY) / totalRange;
            int thumbY = scrollbarTrackY + (int)((scrollbarTrackHeight - thumbHeight) * (1 - scrollRatio));
            
            // Draw thumb
            bg.setColor(new Color(100, 120, 160));
            bg.fillRect(scrollbarX - 4, thumbY, 8, thumbHeight);
            bg.setColor(new Color(130, 150, 190));
            bg.fillRect(scrollbarX - 3, thumbY + 1, 6, thumbHeight - 2);
        }
    }
    
    private void handleButtons()
    {
        if (Greenfoot.mouseClicked(playAgainButton)) {
            Greenfoot.setWorld(new EditorWorld());
        }
        else if (Greenfoot.mouseClicked(mainMenuButton)) {
            Greenfoot.setWorld(new LandingPage());
        }
        else if (whiteViewCapturedButton != null && Greenfoot.mouseClicked(whiteViewCapturedButton)) {
            if (winner.equals("DRAW")) {
                showCapturedPiecesPopup("WHITE", whiteCapturedPieces);
            } else {
                showCapturedPiecesPopup(winner, winner.equals("WHITE") ? whiteCapturedPieces : blackCapturedPieces);
            }
        }
        else if (blackViewCapturedButton != null && Greenfoot.mouseClicked(blackViewCapturedButton)) {
            if (winner.equals("DRAW")) {
                showCapturedPiecesPopup("BLACK", blackCapturedPieces);
            } else {
                String loser = winner.equals("WHITE") ? "BLACK" : "WHITE";
                showCapturedPiecesPopup(loser, winner.equals("WHITE") ? blackCapturedPieces : whiteCapturedPieces);
            }
        }
    }
    
    private void showCapturedPiecesPopup(String player, List<Piece.PieceType> capturedPieces)
    {
        activePopup = new CapturedPiecesPopup(player, capturedPieces, this);
        addObject(activePopup, 300, 300);
        waitingForRelease = true;
    }
    
    public void closePopup()
    {
        if (activePopup != null) {
            removeObject(activePopup);
            activePopup = null;
        }
    }
    
    private String formatTime(int seconds)
    {
        if (seconds < 0) seconds = 0;
        int mins = seconds / 60;
        int secs = seconds % 60;
        return String.format("%d:%02d", mins, secs);
    }
}
