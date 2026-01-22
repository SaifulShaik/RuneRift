import greenfoot.*;
import java.util.ArrayList;

/**
 * Credits world showing game credits and acknowledgments.
 * Supports scrolling to view all content within a clipped viewport.
 * Extends MenuWorld for common menu functionality.
 * 
 * @author Saiful Shaik
 * @version
 */
public class CreditsWorld extends MenuWorld
{
    private Button backButton;
    private int fadeAlpha;
    private int scrollY;
    private ArrayList<Label> creditLabels;
    private ArrayList<Integer> originalYPositions;
    private int minScrollY;
    private int maxScrollY;
    private Label scrollHint;
    private int scrollHintAlpha;
    private boolean scrollHintFadingOut;
    
    // Viewport bounds
    private static final int VIEWPORT_TOP = 110;
    private static final int VIEWPORT_BOTTOM = 500;
    private static final int VIEWPORT_HEIGHT = VIEWPORT_BOTTOM - VIEWPORT_TOP;
    
    // Scrollbar variables
    private int scrollbarX = 565;
    private int scrollbarTrackY = VIEWPORT_TOP;
    private int scrollbarTrackHeight = VIEWPORT_HEIGHT;
    private int scrollbarThumbHeight = 50;
    private boolean isDraggingScrollbar = false;
    private int dragOffsetY = 0;
    
    // Mouse drag scrolling
    private boolean isDraggingContent = false;
    private int lastMouseY = 0;
    
    // Exit animation
    private boolean isExiting = false;
    private int exitAlpha = 0;
    
    public CreditsWorld()
    {
        super(); // Uses MenuWorld's standard dimensions
        fadeAlpha = 0;
        scrollY = 0;
        minScrollY = -200;
        maxScrollY = 0;
        creditLabels = new ArrayList<Label>();
        originalYPositions = new ArrayList<Integer>();
        scrollHintAlpha = 255;
        scrollHintFadingOut = false;
        prepare();
    }
    
    public void act()
    {
        if (isExiting)
        {
            animateExit();
        }
        else
        {
            animateFadeIn();
            handleScrolling();
            handleScrollbarInteraction();
            updateScrollHint();
            updateLabelVisibility();
            checkButtons();
            drawScrollbar();
        }
    }
    
    /**
     * Animate the exit transition
     */
    private void animateExit()
    {
        exitAlpha += 15;
        if (exitAlpha >= 255)
        {
            goToLandingPage();
            return;
        }
        
        // Draw fade overlay
        GreenfootImage overlay = getBackground();
        overlay.setColor(new Color(0, 0, 0, exitAlpha));
        overlay.fillRect(0, 0, getWidth(), getHeight());
    }
    
    private void animateFadeIn()
    {
        if (fadeAlpha < 255)
        {
            fadeAlpha = Math.min(255, fadeAlpha + 10);
            updateBackground();
        }
    }
    
    /**
     * Handle mouse drag and keyboard scrolling
     */
    private void handleScrolling()
    {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        
        // Mouse drag scrolling
        if (mouse != null)
        {
            int mouseY = mouse.getY();
            
            if (Greenfoot.mousePressed(null) && !isDraggingScrollbar)
            {
                int mouseX = mouse.getX();
                if (mouseX < scrollbarX - 20 && mouseY >= VIEWPORT_TOP && mouseY <= VIEWPORT_BOTTOM)
                {
                    isDraggingContent = true;
                    lastMouseY = mouseY;
                }
            }
            
            if (isDraggingContent && Greenfoot.mouseDragged(null))
            {
                int deltaY = mouseY - lastMouseY;
                scroll(deltaY);
                lastMouseY = mouseY;
                scrollHintFadingOut = true;
            }

            if (Greenfoot.mouseClicked(null) || Greenfoot.mouseDragEnded(null))
            {
                isDraggingContent = false;
            }
        }
        
        // Keyboard scrolling
        if (Greenfoot.isKeyDown("down") || Greenfoot.isKeyDown("s"))
        {
            scroll(-8);
            scrollHintFadingOut = true;
        }
        if (Greenfoot.isKeyDown("up") || Greenfoot.isKeyDown("w"))
        {
            scroll(8);
            scrollHintFadingOut = true;
        }
    }
    
    /**
     * Handle scrollbar mouse interaction
     */
    private void handleScrollbarInteraction()
    {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        
        if (mouse != null)
        {
            int mouseX = mouse.getX();
            int mouseY = mouse.getY();
            
            // Calculate thumb position based on current scroll
            int thumbY = getScrollbarThumbY();
            
            // Check if mouse is over the scrollbar thumb
            boolean overThumb = mouseX >= scrollbarX - 6 && mouseX <= scrollbarX + 6 &&
                               mouseY >= thumbY && mouseY <= thumbY + scrollbarThumbHeight;
            
            // Check if mouse is over the scrollbar track
            boolean overTrack = mouseX >= scrollbarX - 6 && mouseX <= scrollbarX + 6 &&
                               mouseY >= scrollbarTrackY && mouseY <= scrollbarTrackY + scrollbarTrackHeight;
            
            if (Greenfoot.mousePressed(null) && overThumb)
            {
                isDraggingScrollbar = true;
                dragOffsetY = mouseY - thumbY;
                scrollHintFadingOut = true;
            }
            
            if (Greenfoot.mouseClicked(null) && overTrack && !overThumb)
            {
                int targetThumbY = mouseY - scrollbarThumbHeight / 2;
                targetThumbY = Math.max(scrollbarTrackY, Math.min(scrollbarTrackY + scrollbarTrackHeight - scrollbarThumbHeight, targetThumbY));
                
                float ratio = (float)(targetThumbY - scrollbarTrackY) / (scrollbarTrackHeight - scrollbarThumbHeight);
                scrollY = (int)(maxScrollY - ratio * (maxScrollY - minScrollY));
                updateLabelPositions();
                scrollHintFadingOut = true;
            }
            
            if (isDraggingScrollbar)
            {
                int newThumbY = mouseY - dragOffsetY;
                newThumbY = Math.max(scrollbarTrackY, Math.min(scrollbarTrackY + scrollbarTrackHeight - scrollbarThumbHeight, newThumbY));
                
                float ratio = (float)(newThumbY - scrollbarTrackY) / (scrollbarTrackHeight - scrollbarThumbHeight);
                scrollY = (int)(maxScrollY - ratio * (maxScrollY - minScrollY));
                updateLabelPositions();
            }
        }
        
        if (Greenfoot.mouseClicked(null) || Greenfoot.mouseDragEnded(null))
        {
            isDraggingScrollbar = false;
        }
    }
    
    /**
     * Get the Y position of the scrollbar thumb based on current scroll
     */
    private int getScrollbarThumbY()
    {
        if (maxScrollY == minScrollY) return scrollbarTrackY;
        float ratio = (float)(maxScrollY - scrollY) / (maxScrollY - minScrollY);
        return scrollbarTrackY + (int)(ratio * (scrollbarTrackHeight - scrollbarThumbHeight));
    }
    
    /**
     * Update all label positions based on current scrollY
     */
    private void updateLabelPositions()
    {
        for (int i = 0; i < creditLabels.size(); i++)
        {
            Label label = creditLabels.get(i);
            int newY = originalYPositions.get(i) + scrollY;
            label.setLocation(300, newY);
        }
    }
    
    /**
     * Update label visibility - hide labels outside the viewport
     */
    private void updateLabelVisibility()
    {
        for (int i = 0; i < creditLabels.size(); i++)
        {
            Label label = creditLabels.get(i);
            int y = label.getY();
            int labelHeight = label.getImage().getHeight();
            
            boolean isVisible = (y + labelHeight / 2 > VIEWPORT_TOP) && (y - labelHeight / 2 < VIEWPORT_BOTTOM);
            
            if (isVisible)
            {
                label.getImage().setTransparency(255);
            }
            else
            {
                label.getImage().setTransparency(0);
            }
        }
    }
    
    /**
     * Draw the scrollbar on the background
     */
    private void drawScrollbar()
    {
        GreenfootImage bg = getBackground();
        
        // Clear scrollbar area
        bg.setColor(new Color(20, 25, 50));
        bg.fillRect(scrollbarX - 8, scrollbarTrackY - 2, 16, scrollbarTrackHeight + 4);
        
        // Draw track background
        bg.setColor(new Color(50, 55, 80));
        bg.fillRect(scrollbarX - 4, scrollbarTrackY, 8, scrollbarTrackHeight);
        
        // Draw track border
        bg.setColor(new Color(80, 85, 110));
        bg.drawRect(scrollbarX - 4, scrollbarTrackY, 8, scrollbarTrackHeight);
        
        // Calculate thumb position
        int thumbY = getScrollbarThumbY();
        
        // Draw thumb
        Color thumbColor = isDraggingScrollbar ? new Color(150, 180, 255) : new Color(100, 130, 200);
        bg.setColor(thumbColor);
        bg.fillRect(scrollbarX - 4, thumbY, 8, scrollbarThumbHeight);
        
        // Draw thumb border
        bg.setColor(new Color(180, 200, 255));
        bg.drawRect(scrollbarX - 4, thumbY, 8, scrollbarThumbHeight);
        
        // Draw thumb grip lines
        bg.setColor(new Color(70, 100, 170));
        int gripY = thumbY + scrollbarThumbHeight / 2;
        bg.drawLine(scrollbarX - 2, gripY - 4, scrollbarX + 2, gripY - 4);
        bg.drawLine(scrollbarX - 2, gripY, scrollbarX + 2, gripY);
        bg.drawLine(scrollbarX - 2, gripY + 4, scrollbarX + 2, gripY + 4);
    }
    
    /**
     * Scroll the credits by a given amount
     */
    private void scroll(int amount)
    {
        int newScrollY = scrollY + amount;
        newScrollY = Math.max(minScrollY, Math.min(maxScrollY, newScrollY));
        
        if (newScrollY != scrollY)
        {
            scrollY = newScrollY;
            updateLabelPositions();
        }
    }
    
    /**
     * Update scroll hint visibility
     */
    private void updateScrollHint()
    {
        if (scrollHintFadingOut && scrollHintAlpha > 0)
        {
            scrollHintAlpha = Math.max(0, scrollHintAlpha - 5);
            if (scrollHint != null)
            {
                scrollHint.setFillColor(new Color(150, 150, 170, scrollHintAlpha));
            }
        }
    }
    
    private void checkButtons()
    {
        if (backButton != null && backButton.wasClicked() && !isExiting)
        {
            isExiting = true;
        }
    }
    
    private void prepare()
    {
        updateBackground();
        
        // Title
        Label titleLabel = new Label("CREDITS", 48);
        titleLabel.setLineColor(new Color(0, 0, 0, 0));
        titleLabel.setFillColor(Color.WHITE);
        addObject(titleLabel, 300, 55);
        
        // Credit lines
        int y = VIEWPORT_TOP + 30;
        
        // Game title
        addScrollableCreditLine("CLASHMATE", 32, new Color(100, 180, 255), y);
        y += 40;
        
        addScrollableCreditLine("A Clash Royale Chess Game", 18, Color.WHITE, y);
        y += 50;
        
        // Created by section
        addScrollableCreditLine("Created by:", 20, new Color(180, 180, 200), y);
        y += 35;
        
        addScrollableCreditLine("Owen Lee", 22, Color.WHITE, y);
        y += 30;
        
        addScrollableCreditLine("Joe Zhou", 22, Color.WHITE, y);
        y += 30;
        
        addScrollableCreditLine("Saiful Shaik", 22, Color.WHITE, y);
        y += 50;
        
        // Developed using section
        addScrollableCreditLine("Developed using:", 20, new Color(180, 180, 200), y);
        y += 35;
        
        addScrollableCreditLine("Greenfoot IDE", 18, Color.WHITE, y);
        y += 50;
        
        // Images section
        addScrollableCreditLine("Images:", 20, new Color(180, 180, 200), y);
        y += 35;
        
        addScrollableCreditLine("Clash Royale by Supercell", 18, Color.WHITE, y);
        y += 50;
        
        // SFX section
        addScrollableCreditLine("Sound Effects:", 20, new Color(180, 180, 200), y);
        y += 35;
        
        addScrollableCreditLine("Pixabay", 18, Color.WHITE, y);
        y += 50;
        
        // Special thanks
        addScrollableCreditLine("Special Thanks:", 20, new Color(180, 180, 200), y);
        y += 35;
        
        addScrollableCreditLine("Our Players & Testers", 18, Color.WHITE, y);
        y += 50;
        
        // Copyright
        addScrollableCreditLine("© 2026", 16, new Color(150, 150, 170), y);
        y += 40;
        
        // Calculate scroll range based on content
        // Content that extends below viewport bottom needs scrolling
        int contentHeight = y - (VIEWPORT_TOP + 30);
        int scrollableAmount = contentHeight - VIEWPORT_HEIGHT + 60;
        minScrollY = -Math.max(0, scrollableAmount);
        maxScrollY = 0;
        
        // Scroll hint at bottom
        scrollHint = new Label("↑ ↓  Scroll to see more", 14);
        scrollHint.setLineColor(new Color(0, 0, 0, 0));
        scrollHint.setFillColor(new Color(150, 150, 170, scrollHintAlpha));
        addObject(scrollHint, 300, 520);
        
        // Back button
        backButton = new Button("← BACK", 150, 50,
                               new Color(70, 70, 90),
                               new Color(100, 100, 130),
                               Color.WHITE, 22);
        addObject(backButton, 300, 560);
    }
    
    /**
     * Add a credit line that can be scrolled
     */
    private void addScrollableCreditLine(String text, int fontSize, Color color, int yPos)
    {
        Label line = new Label(text, fontSize);
        line.setLineColor(new Color(0, 0, 0, 0));
        line.setFillColor(color);
        addObject(line, 300, yPos);
        
        creditLabels.add(line);
        originalYPositions.add(yPos);
    }
    
    private void updateBackground()
    {
        GreenfootImage bg = new GreenfootImage(getWidth(), getHeight());
        
        // Gradient background
        for (int y = 0; y < getHeight(); y++)
        {
            float ratio = (float) y / getHeight();
            int r = (int) (20 + ratio * 10);
            int g = (int) (25 + ratio * 15);
            int b = (int) (45 + ratio * 25);
            int alpha = Math.min(fadeAlpha, 255);
            bg.setColor(new Color(r, g, b, alpha));
            bg.drawLine(0, y, getWidth(), y);
        }
        
        // Draw subtle viewport border to show the scrollable area
        if (fadeAlpha > 100)
        {
            int borderAlpha = Math.min((fadeAlpha - 100), 40);
            bg.setColor(new Color(100, 120, 160, borderAlpha));
            bg.drawLine(30, VIEWPORT_TOP - 2, 540, VIEWPORT_TOP - 2);
            bg.drawLine(30, VIEWPORT_BOTTOM + 2, 540, VIEWPORT_BOTTOM + 2);
        }
        
        setBackground(bg);
    }
}
