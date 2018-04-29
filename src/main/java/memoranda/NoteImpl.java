/**
 * NoteImpl.java
 * Created on 13.02.2003, 15:36:55 Alex
 * Package: net.sf.memoranda
 * 
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package main.java.memoranda;

import java.util.Collection;
import java.util.Vector;

import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.interfaces.INote;
import main.java.memoranda.interfaces.INoteListener;
import main.java.memoranda.interfaces.IProject;
import nu.xom.Attribute;
import nu.xom.Element;

/**
 * 
 */
/*$Id: NoteImpl.java,v 1.6 2004/10/06 19:15:44 ivanrise Exp $*/
public class NoteImpl implements INote, Comparable {
    
    private Element _el = null; 
    private IProject _project;
    
    // TASK 2-2 SMELL BETWEEN CLASSES
    // NoteImpl and CurrentNote had a code smell between the class.
    // Basically, CurrentNote was using NoteImpl and acting as a middle man
    // to carry out its functionality. I then combined CurrentNote to NoteImp and removed
    // CurrentNote class to fix this code smell.
    
    private static INote currentNote = null;
    private static Vector noteListeners = new Vector();
    
    
    /**
     * Constructor for NoteImpl.
     */
    public NoteImpl(Element el, IProject project) {
        _el = el;
        _project = project;
    }

    /**
     * @see main.java.memoranda.interfaces.INote#getDate()
     */
    public CalendarDate getDate() {
		Element day = (Element)_el.getParent();
		Element month = (Element)day.getParent();
		Element year = (Element)month.getParent();

     //   return new CalendarDate(day.getAttribute("date").getValue());
		
		return new CalendarDate(new Integer(day.getAttribute("day").getValue()).intValue(), 
								new Integer(month.getAttribute("month").getValue()).intValue(),
								new Integer(year.getAttribute("year").getValue()).intValue());

    }
    
    public IProject getProject() {
        return _project;
    }
    /**
     * @see main.java.memoranda.interfaces.INote#getTitle()
     */
    public String getTitle() {
        Attribute ta = _el.getAttribute("title");
        if (ta == null) return "";
        return _el.getAttribute("title").getValue();
    }
    /**
     * @see main.java.memoranda.interfaces.INote#setTitle(java.lang.String)
     */
    public void setTitle(String s) {
        Attribute ta = _el.getAttribute("title");
        if (ta == null) _el.addAttribute(new Attribute("title", s));
        else 
            ta.setValue(s);
    }
	
	/**
     * @see main.java.memoranda.interfaces.INote#getId
     */
	
	public String getId() {
		Attribute id = _el.getAttribute("refid");
		if (id==null) return "";
		return _el.getAttribute("refid").getValue();
	}
	
	/**
     * @see main.java.memoranda.interfaces.INote#setId(java.lang.String)
     */
	 
	public void setId(String s) {
		Attribute id = _el.getAttribute("refid");
		if(id==null) _el.addAttribute(new Attribute("refid", s));
	}
    /**
     * @see main.java.memoranda.interfaces.INote#isMarked()
     */
    public boolean isMarked() {
        return _el.getAttribute("bookmark") != null;        
    }
    /**
     * @see main.java.memoranda.interfaces.INote#setMark(boolean)
     */
    public void setMark(boolean mark) {
        Attribute ma = _el.getAttribute("bookmark");        
        if (ma == null) {
            if (mark)
                _el.addAttribute(new Attribute("bookmark", "yes"));
            return;
        }
        else if (!mark)
            _el.removeAttribute(ma);
    }
	
	/*
	 * Comparable interface
	 */
	public int compareTo(Object o) {
		INote note = (INote) o;
		if(getDate().getDate().getTime() > note.getDate().getDate().getTime())
			return 1;
		else if(getDate().getDate().getTime() < note.getDate().getDate().getTime())
			return -1;
		else 
			return 0;
	}
    
	
    // TASK 2-2 SMELL BETWEEN CLASSES
    // NoteImpl and CurrentNote had a code smell between the class.
    // Basically, CurrentNote was using NoteImpl and acting as a middle man
    // to carry out its functionality. Below are the methods related to manage 
	// current note.

    public static INote get() {
        return currentNote;
    }

    public static void set(INote note, boolean toSaveCurrentNote) {
        noteChanged(note, toSaveCurrentNote);
        currentNote = note;
    }

    public static void reset() {
//       set toSave to true to mimic status quo behaviour only. the appropriate setting could be false
        set(null, true);
    }

    public static void addNoteListener(INoteListener nl) {
        noteListeners.add(nl);
    }

    public static Collection getChangeListeners() {
        return noteListeners;
    }

    private static void noteChanged(INote note, boolean toSaveCurrentNote) {
        for (int i = 0; i < noteListeners.size(); i++) {
            ((INoteListener)noteListeners.get(i)).noteChange(note,toSaveCurrentNote);
        }
    }
}
