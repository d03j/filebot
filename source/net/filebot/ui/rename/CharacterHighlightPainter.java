
package net.filebot.ui.rename;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

import net.filebot.util.ui.GradientStyle;


class CharacterHighlightPainter implements Highlighter.HighlightPainter {

	private Color gradientBeginColor;
	private Color gradientEndColor;


	public CharacterHighlightPainter(Color gradientBeginColor, Color gradientEndColor) {
		this.gradientBeginColor = gradientBeginColor;
		this.gradientEndColor = gradientEndColor;
	}


	@Override
	public void paint(Graphics g, int offset1, int offset2, Shape bounds, JTextComponent c) {
		Graphics2D g2d = (Graphics2D) g;

		try {
			// determine locations
			Rectangle2D p1 = c.modelToView2D(offset1);
			Rectangle2D p2 = c.modelToView2D(offset2);

			Rectangle2D r = p1.createUnion(p2);

			float w = (float) r.getWidth() + 1;
			float h = (float) r.getHeight();

			float x = (float) r.getX() - 1;
			float y = (float) r.getY();

			float arch = 5f;

			RoundRectangle2D shape = new RoundRectangle2D.Float(x, y, w, h, arch, arch);

			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setPaint(GradientStyle.TOP_TO_BOTTOM.getGradientPaint(shape, gradientBeginColor, gradientEndColor));

			g2d.fill(shape);
		} catch (BadLocationException e) {
			//should not happen
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.toString(), e);
		}
	}
}
