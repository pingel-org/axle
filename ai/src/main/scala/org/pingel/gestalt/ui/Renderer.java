package org.pingel.gestalt.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.pingel.gestalt.core.CallGraph;
import org.pingel.gestalt.core.Form;
import org.pingel.gestalt.core.FormFactory;
import org.pingel.gestalt.core.History;
import org.pingel.gestalt.core.Lexicon;
import org.pingel.gestalt.core.Transform;
import org.pingel.gestalt.core.TransformFactory;

public class Renderer
extends JPanel
{
    private Dimension area = new Dimension(0, 0);

    static final Font bigFont = new Font("TimesRoman", Font.BOLD, 24);
    
    JLabel selectionLabel;

    private Widget highlighted = null;
    History history;
    Lexicon factoryLexicon;
    Lexicon lexicon;
    
    public Renderer(Controller controller, History history, Lexicon factoryLexicon, Lexicon lexicon)
    {
        this.history = history;
        this.factoryLexicon = factoryLexicon;
        this.lexicon = lexicon;
        
        setBackground(Color.LIGHT_GRAY);
        addMouseListener(controller);
        addMouseMotionListener(controller);

        selectionLabel = new JLabel();
        selectionLabel.setFont(bigFont);
        JPanel selectionPanel = new JPanel(new GridLayout(0,1));
        selectionPanel.add(selectionLabel);

        JScrollPane scroller = new JScrollPane(this);
        //scroller.setPreferredSize(area);

        controller.add(selectionPanel, BorderLayout.PAGE_START);
        controller.add(scroller, BorderLayout.CENTER);

        Dimension dim = arrangeFactories();
        checkBounds(dim);
    }

    public void checkBounds(Dimension dim)
    {
        area = new Dimension(Math.max(area.width, dim.width), Math.max(area.height, dim.height));
        setPreferredSize(area);
        revalidate();
    }
    
    private Dimension arrangeFactories()
    {
        Point p = new Point();

        p.move(70, 100);

        for( FormFactory factory : factoryLexicon.getFormFactories() ) {
            Form form = factory.getArchetype();
            form.arrange(p);
            p.translate(0, 50);
        }

        p.translate(-50, 100);

        for( TransformFactory tf : factoryLexicon.getTransformFactories() ) {
            Transform t = tf.getTransform();
            t.arrange(p);
            p.translate(0, 50);
        }

        return new Dimension(1000, p.y);
    }
    

    public String getSelectionText()
    {
        if( highlighted == null ) {
            return "no selection";                
        }
        else {
            return highlighted.toString();
        }
    }

    public void setHighlighted(Widget f)
    {
        f.setHighlighted(true);
        this.highlighted = f;
    }
    
    public boolean unHighlight()
    {
        if( highlighted != null ) {
            highlighted.setHighlighted(false);
            highlighted = null;
            return true;
        }
        return false;
    }

    static final long serialVersionUID = 123123;
    
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        
        selectionLabel.setText(getSelectionText());
        
        for( FormFactory ff : factoryLexicon.getFormFactories() ) {
            ff.getArchetype().paint(g);
        }
        
        for( TransformFactory tf : factoryLexicon.getTransformFactories() ) {
            tf.getTransform().paint(g);
        }
        
        for( CallGraph cg : history.getCalls() ) {
            cg.paint(g);
        }
        
        for( Form form : lexicon.getTopForms() ) {
            form.paint(g);
        }
        
    }
    
}
