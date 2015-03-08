package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.locationstorage.LocationController;
import hkust.cse.calendar.tests.GUITest;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;


public class ManageLocationDialog extends JPanel 
								implements ListSelectionListener {

	private static final long serialVersionUID = 1L;
	//add Controller for location
	//private LocationController _controller;
	
    private static final String AddButtonString = "Add";
    private static final String DeleteButtonString = "Delete";
    
    private DefaultListModel<Location> retrievedLocationList;
    private DefaultListModel<String> retrievedLocationStringList;
	private JList displayList;
    private JButton deleteButton;
    private JTextField locationName;

    public ManageLocationDialog() {
        super(new BorderLayout());
        // _controller = controller;

        //implement after locationlist implementation is complete.
        //retrievedlocationList = LocationController.getInstance().getLocationList();
        retrievedLocationList = new DefaultListModel<Location>();
        retrievedLocationList = GUITest.getLocationListTest();
        
        //copy only the name(String) of the locations to another list for display
        retrievedLocationStringList = new DefaultListModel<String>();
        updateRetrievedLocationStringListfromRetrievedLocationList(retrievedLocationList);
        
        
        //Create the list and put it in a scroll pane.
        displayList = new JList<String>(retrievedLocationStringList);
        
        //displayList.setModel(retrievedLocationStringList);
        ////////////////////////////////setmodel********************************
        
        displayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        displayList.setSelectedIndex(0);
        displayList.addListSelectionListener(this);
        displayList.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(displayList);

        JButton addButton = new JButton(AddButtonString);
        AddListener addListener = new AddListener(addButton);
        addButton.setActionCommand(AddButtonString);
        addButton.addActionListener(addListener);
        addButton.setEnabled(false);

        deleteButton = new JButton(DeleteButtonString);
        deleteButton.setActionCommand(DeleteButtonString);
        deleteButton.addActionListener(new DeleteListener());

        locationName = new JTextField(10);
        locationName.addActionListener(addListener);
        locationName.getDocument().addDocumentListener(addListener);

        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        //buttonPane.add(deleteButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(locationName);
        buttonPane.add(addButton);
        buttonPane.add(deleteButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }
    
    class DeleteListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            
        	//This method can be called only if
            //there's a valid selection	
            //so go ahead and remove whatever's selected.
            int index = displayList.getSelectedIndex();
            retrievedLocationList.removeElementAt(index);
         
            
            int size = retrievedLocationList.getSize();
            if (size == 0) { //None's left, disable delete button.
                deleteButton.setEnabled(false);
            } 
            else { //Select an index.
                if (index == retrievedLocationList.getSize()	) {
                    //removed item in last position
                    index--;
                }

                displayList.setSelectedIndex(index);
                displayList.ensureIndexIsVisible(index);
            }
        }
        
    }
    
    //This listener is shared by the text field and the add button.
    class AddListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public AddListener(JButton button) {
            this.button = button;
        }

        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
            String name = locationName.getText();

            //User didn't type in a unique name...
            if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                locationName.requestFocusInWindow();
                locationName.selectAll();
                return;
            }

            int index = displayList.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }
            
            //adds new location at the end of the retrievedLocationList
            // need to change later to be sorted in order
            Location newLocation = new Location(locationName.getText(), 0);
            retrievedLocationList.addElement(newLocation);
            
///////////////////////            
            //retrievedLocationStringList;
            
            //Reset the text field.
            locationName.requestFocusInWindow();
            locationName.setText("");

            //Select the new item and make it visible.
            displayList.setSelectedIndex(index);
            displayList.ensureIndexIsVisible(index);
        }

        //This method tests for string equality. 
        //further implement to test more rigorously (ignore whitespace, capital, etc)
        protected boolean alreadyInList(String name) {
        	for(int i=0; i<retrievedLocationList.getSize();i++)
        	{
        		if(retrievedLocationList.getElementAt(i).getName().equals(name))
        			return true;
        	}
            return false;
        }

        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    //This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (displayList.getSelectedIndex() == -1) {
            //No selection, disable fire button.
                deleteButton.setEnabled(false);

            } else {
            //Selection, enable the fire button.
                deleteButton.setEnabled(true);
            }
        }
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowManageLocationDialogGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Manage Locations");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new ManageLocationDialog();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    public void updateRetrievedLocationStringListfromRetrievedLocationList(DefaultListModel<Location> retrievedList) {
    	for(int i =0; i<retrievedLocationList.getSize();i++) {
        	retrievedLocationStringList.addElement(retrievedLocationList.getElementAt(i).getName());	
        }
    }
	
}
