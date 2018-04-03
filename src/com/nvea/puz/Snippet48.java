
package com.nvea.puz;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Snippet48 {
  static int imgx = 0;
  static int imgy = 0;
  static protected boolean startDrag;
  protected static int startDragX;
  protected static int startDragY;

  public static void main(String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setLayout(new FillLayout());
    final Image originalImage = new Image(display, "b1.png");
    final Image image = originalImage;
    final Canvas canvas = new Canvas(shell, SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE | SWT.V_SCROLL | SWT.H_SCROLL);
    canvas.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseUp(MouseEvent e) {
        startDrag = false;
      }

      @Override
      public void mouseDown(MouseEvent e) {
        int alpha = originalImage.getImageData().getAlpha(e.x - imgx, e.y - imgy);
        if (alpha == 255) {
          startDrag = true;
          startDragX = e.x;
          startDragY = e.y;
        }
      }
    });

    canvas.addMouseMoveListener(new MouseMoveListener() {

      public void mouseMove(MouseEvent e) {
        System.out.println("" + imgx + " " + imgy);
        if (startDrag) {
          imgx = (e.x - startDragX);
          imgy = (e.y - startDragY);
          canvas.redraw();
        }
      }
    });

    canvas.addListener(SWT.Resize, e -> {
      canvas.redraw();
    });
    canvas.addListener(SWT.Paint, e -> {
      GC gc = e.gc;
      gc.fillRectangle(0, 0, 300, 300);
      gc.drawImage(image, imgx, imgy);
      Rectangle rect = image.getBounds();
      Rectangle client = canvas.getClientArea();
      int marginWidth = client.width - rect.width;
      if (marginWidth > 0) {
        gc.fillRectangle(rect.width, 0, marginWidth, client.height);
      }
      int marginHeight = client.height - rect.height;
      if (marginHeight > 0) {
        gc.fillRectangle(0, rect.height, client.width, marginHeight);
      }
    });
    Rectangle rect = image.getBounds();
    shell.setSize(Math.max(200, rect.width - 100), Math.max(150, rect.height - 100));
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    originalImage.dispose();
    display.dispose();
  }

}