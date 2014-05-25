  
/*
 * Copyright (c) Ian F. Darwin, http://www.darwinsys.com/, 1996-2002.
 * All rights reserved. Software written by Ian F. Darwin and others.
 * $Id: LICENSE,v 1.8 2004/02/09 03:33:38 ian Exp $
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Java, the Duke mascot, and all variants of Sun's Java "steaming coffee
 * cup" logo are trademarks of Sun Microsystems. Sun's, and James Gosling's,
 * pioneering role in inventing and promulgating (and standardizing) the Java 
 * language and environment is gratefully acknowledged.
 * 
 * The pioneering role of Dennis Ritchie and Bjarne Stroustrup, of AT&T, for
 * inventing predecessor languages C and C++ is also gratefully acknowledged.
 */

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;

/*
 * AppletViewer - a simple Applet Viewer program.
 * @author  Ian Darwin, http://www.darwinsys.com/
 */
public class AppletViewer {
  /** The main Frame of this program */
  JFrame f;
  /** The AppletAdapter (gives AppletStub, AppletContext, showStatus) */
  static AppletAdapter aa = null;
  /** The name of the Applet subclass */
  String appName = null;
  /** The Class for the actual applet type */
  Class ac = null;
  /** The Applet instance we are running, or null. Can not be a JApplet
   * until all the entire world is converted to JApplet. */
  Applet ai = null;
  /** The width of the Applet */
  final int WIDTH = 250;
  /** The height of the Applet */
  final int HEIGHT = 200;

  /** Main is where it all starts. 
   * Construct the GUI. Load the Applet. Start it running.
   */
  public static void main(String[] av) {
    new AppletViewer(av.length==0?"HelloApplet":av[0]);
  }

  /** Construct the GUI for an Applet Viewer */
  AppletViewer(String appName) {
    super();

    this.appName = appName;

    f = new JFrame("AppletViewer");
    f.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        f.setVisible(false);
        f.dispose();
        System.exit(0);
      }
    });
    Container cp = f.getContentPane();
    cp.setLayout(new BorderLayout());

    // Instantiate the AppletAdapter which gives us
    // AppletStub and AppletContext.
    if (aa == null)
      aa = new AppletAdapter();

    // The AppletAdapter also gives us showStatus.
    // Therefore, must add() it very early on, since the Applet's
    // Constructor or its init() may use showStatus()
    cp.add(BorderLayout.SOUTH, aa);

    showStatus("Loading Applet " + appName);

    loadApplet(appName , WIDTH, HEIGHT);  // sets ac and ai
    if (ai == null)
      return;

    // Now right away, tell the Applet how to find showStatus et al.
    ai.setStub(aa);

    // Connect the Applet to the Frame.
    cp.add(BorderLayout.CENTER, ai);

    Dimension d = ai.getSize();
    d.height += aa.getSize().height;
    f.setSize(d);
    f.setVisible(true);    // make the Frame and all in it appear

    showStatus("Applet " + appName + " loaded");

    // Here we pretend to be a browser!
    ai.init();
    ai.start();
  }

  /*
   * Load the Applet into memory. Should do caching.
   */
  void loadApplet(String appletName, int w, int h) {
    // appletName = ... extract from the HTML CODE= somehow ...;
    // width =     ditto
    // height =     ditto
    try {
      // get a Class object for the Applet subclass
      ac = Class.forName(appletName);
      // Construct an instance (as if using no-argument constructor)
      ai = (Applet) ac.newInstance();
    } catch(ClassNotFoundException e) {
      showStatus("Applet subclass " + appletName + " did not load");
      return;
    } catch (Exception e ){
      showStatus("Applet " + appletName + " did not instantiate");
      return;
    }
    ai.setSize(w, h);
  }

  public void showStatus(String s) {
    aa.getAppletContext().showStatus(s);
  }
}

/*
 * AppletAdaptor: partial implementation of AppletStub and AppletContext.
 *
 * This code is far from finished, as you will see.
 *
 * @author  Ian Darwin, http://www.darwinsys.com/, for Learning Tree Course 478
 */
class AppletAdapter extends Panel implements AppletStub, AppletContext {
  /** The status window at the bottom */
  Label status = null;

  /** Construct the GUI for an Applet Status window */
  AppletAdapter() {
    super();

    // Must do this very early on, since the Applet's
    // Constructor or its init() may use showStatus()
    add(status = new Label());

    // Give "status" the full width
    status.setSize(getSize().width, status.getSize().height);

    showStatus("AppletAdapter constructed");  // now it can be said
  }

  /****************** AppletStub ***********************/
  /** Called when the applet wants to be resized.  */
  public void appletResize(int w, int h) {
    // applet.setSize(w, h);
  }

  /** Gets a reference to the applet's context.  */
  public AppletContext getAppletContext() {
    return this;
  }

  /** Gets the base URL.  */
  public URL getCodeBase() {
    return getClass().getResource(".");
  }

  /** Gets the document URL.  */
  public URL getDocumentBase() {
    return getClass().getResource(".");
  }

  /** Returns the value of the named parameter in the HTML tag.  */
  public String getParameter(String name) {
    String value = null;
    return value;
  }
  /** Determines if the applet is active.  */
  public boolean isActive() {
    return true;
  }

  /************************ AppletContext ************************/

  /** Finds and returns the applet with the given name. */
  public Applet getApplet(String an) {
    return null;
  }

  /** Finds all the applets in the document */
  public Enumeration getApplets()  {
    class AppletLister implements Enumeration {
      public boolean hasMoreElements() {
        return false;
      }
      public Object nextElement() {
        return null;
      }
    }
    return new AppletLister();
  }

  /** Create an audio clip for the given URL of a .au file */
  public AudioClip getAudioClip(URL u) {
    return null;
  }

  /** Look up and create an Image object that can be paint()ed */
  public Image getImage(URL u)  {
    return null;
  }

  /** Request to overlay the current page with a new one - ignored */
  public void showDocument(URL u) {
  }

  /** as above but with a Frame target */
  public void showDocument(URL u, String frame)  {
  }

  /** Called by the Applet to display a message in the bottom line */
  public void showStatus(String msg) {
    if (msg == null)
      msg = "";
    status.setText(msg);
  }

  /* StreamKey stuff - new in JDK1.4 */
  Map streamMap = new HashMap();

  /** Associate the stream with the key. */
  public void setStream(String key, InputStream stream) throws IOException {
    streamMap.put(key, stream);
  }

  public InputStream getStream(String key) {
    return (InputStream)streamMap.get(key);
  }

  public Iterator getStreamKeys() {
    return streamMap.keySet().iterator();
  }
}

