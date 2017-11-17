package net.querz.googlyeyes;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {

	private static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private static final int WIDTH = gd.getDisplayMode().getWidth();
	private static final int LEFT_EYE_X = WIDTH - 65;
	private static final int RIGHT_EYE_X = WIDTH - 50;
	private static final int EYE_Y = 15;

	private static final int IMAGE_BASE_SIZE = 64;
	private static final int PUPIL_BASE_SIZE = 16;
	private static final double SCALING = 0.5;

	private static final int PUPIL_SIZE = (int) (PUPIL_BASE_SIZE * SCALING);
	private static final int IMAGE_SIZE = (int) ((double) IMAGE_BASE_SIZE * SCALING);
	private static final int EYE_X_RADIUS = (int) (7.0 * SCALING);
	private static final int EYE_Y_RADIUS = (int) (13.0 * SCALING);
	private static final int EYE_WIDTH = (int) (IMAGE_SIZE / 2.0 * 0.9);
	private static final int EYE_HEIGHT = (int) (IMAGE_SIZE * 0.75);
	private static final int GAP = (int) ((IMAGE_SIZE - 2.0 * EYE_WIDTH) / 3.0);
	private static final int Y_GAP = (IMAGE_SIZE - EYE_HEIGHT) / 2;
	private static final int LEFT_EYE_CENTER_X = (GAP + EYE_WIDTH) / 2 + 1;
	private static final int RIGHT_EYE_CENTER_X = IMAGE_SIZE - GAP - EYE_WIDTH / 2 + 1;
	private static final int EYE_CENTER_Y = (IMAGE_SIZE - Y_GAP) / 2 - 2;
	private static final float STROKE = (float) (3.0 * SCALING);

	public static void main(String[] args) throws InterruptedException, AWTException {
		System.setProperty("apple.awt.UIElement", "true");

		BufferedImage dummy = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

		TrayIcon icon = new TrayIcon(dummy);
		PopupMenu p = new PopupMenu();
		MenuItem m = new MenuItem("Exit");
		m.addActionListener(e -> System.exit(0));
		p.add(m);
		icon.setPopupMenu(p);
		SystemTray.getSystemTray().add(icon);
		dummy.flush();

		for (;;) {
			drawEyes(icon);
			Thread.sleep(100);
		}
	}

	private static void drawEyes(TrayIcon icon) {
		BufferedImage img = null;
		Graphics2D g2d = null;

		try {
			img = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
			g2d = img.createGraphics();

			g2d.setStroke(new BasicStroke(STROKE));
			g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
			g2d.setComposite(AlphaComposite.Clear);
			g2d.fillRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);
			g2d.setComposite(AlphaComposite.SrcOver);
			g2d.setColor(Color.WHITE);
			g2d.drawOval(GAP, (IMAGE_SIZE - EYE_HEIGHT) / 2, EYE_WIDTH, EYE_HEIGHT);
			g2d.drawOval(IMAGE_SIZE - GAP - EYE_WIDTH, (IMAGE_SIZE - EYE_HEIGHT) / 2, EYE_WIDTH, EYE_HEIGHT);

			Point point = MouseInfo.getPointerInfo().getLocation();

			double angleLeft = Math.atan2((double) point.x - LEFT_EYE_X, (double) point.y - EYE_Y);
			double xl = Math.sin(angleLeft) * EYE_X_RADIUS;
			double yl = Math.cos(angleLeft) * EYE_Y_RADIUS;

			g2d.fillOval(LEFT_EYE_CENTER_X + (int) xl - PUPIL_SIZE / 2, EYE_CENTER_Y + (int) yl, PUPIL_SIZE, PUPIL_SIZE);

			double angleRight = Math.atan2((double) point.x - RIGHT_EYE_X, (double) point.y - EYE_Y);
			double xr = Math.sin(angleRight) * EYE_X_RADIUS;
			double yr = Math.cos(angleRight) * EYE_Y_RADIUS;

			g2d.fillOval(RIGHT_EYE_CENTER_X + (int) xr - PUPIL_SIZE / 2, EYE_CENTER_Y + (int) yr, PUPIL_SIZE, PUPIL_SIZE);

			icon.setImage(img);
		} catch (Exception ex) {
			//do nothing
		} finally {
			if (g2d != null) {
				g2d.dispose();
			}
			if (img != null) {
				img.flush();
			}
		}
	}
}