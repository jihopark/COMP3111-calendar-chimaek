package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;
import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.apptstorage.ApptStorage;
import hkust.cse.calendar.locationstorage.LocationController;
import hkust.cse.calendar.locationstorage.LocationStorageMemory;
import hkust.cse.calendar.tests.GUITest;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.*;


public class ManageLocationDialog extends JPanel 
								implements ListSelectionListener {

	private static final long serialVersionUID = 1L;
	
    private static final String AddButtonString = "Add";
    private static final String DeleteButtonString = "Delete";
    
    private DefaultListModel<Location> retrievedLocationList;
	private JList displayList;
    private JButton deleteButton;
    private JLabel capacityLabel;
    private int selectedLocationCapacity = 0;
    private JTextField locationName;
    private JTextField locationCapacity;

    public ManageLocationDialog() {
        super(new BorderLayout());
        /*
        User user = new User( "noname", "nopass");
		LocationController.getInstance().initLocationStorage(new LocationStorageNullImpl(user));
        retrievedLocationList = GUITest.getLocationListTest();
        */
        retrievedLocationList = LocationController.getInstance().getLocationList();
        displayList = new JList <Location>(retrievedLocationList);
        MouseListener mouseListener = new MouseAdapter() {
        	public void mouseClicked(MouseEvent e) {
        		if(e.getClickCount() == 2 ) {
        			Location selectedItem = (Location) displayList.getSelectedValue();
        			if(selectedItem.getCapacity() < 2){
                        capacityLabel.setText("This facility is not a group facility");
        			} else {
        				capacityLabel.setText("The Group Facility's Capacity is: " + selectedItem.getCapacity());
        			}
        		}
        	}
        };
        displayList.addMouseListener(mouseListener);
        //Create the list and put it in a scroll pane//
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
        
        locationCapacity = new JTextField(3);
        locationCapacity.setEnabled(false);
        
        capacityLabel = new JLabel("The Group Facility Capacity is: " + selectedLocationCapacity);
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
        buttonPane.add(locationCapacity);
        
        if(retrievedLocationList.getSize()==0 || 
        		((retrievedLocationList.getSize()==1) && (retrievedLocationList.getElementAt(0).getName().equals("-") ))) {
            deleteButton.setEnabled(false);
        }
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
        add(listScrollPane, BorderLayout.CENTER);
        add(capacityLabel, BorderLayout.LINE_END);
        add(buttonPane, BorderLayout.PAGE_END);
        
        if(!UserController.getInstance().getCurrentUser().isAdmin()){
        	deleteButton.setEnabled(false);
        	addButton.setEnabled(false);
        	locationName.setEnabled(false);
        	locationCapacity.setEnabled(false);
        }
    }
    
    class DeleteListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            
        	//This method can be called only if
            //there's a valid selection	
            //so go ahead and remove whatever's selected.
            int index = displayList.getSelectedIndex();
        	//System.out.println(retrievedLocationList.getElementAt(index).getName()+": "+retrievedLocationList.getElementAt(index).getAppointmentCount());
            
            
            
            if(!retrievedLocationList.getElementAt(index).getName().equals("-")) {
            	if(LocationController.getInstance().canDeleteLocation(retrievedLocationList.getElementAt(index))) {
            		retrievedLocationList.remove(index);
            		LocationController.getInstance().removeLocation(index);
            	}
            	else {
            		JOptionPane.showMessageDialog(locationName, "Appointment exists for this location!",
            				"Error", JOptionPane.ERROR_MESSAGE);
            	}
            }
            else {
            	//Print error message 
    			JOptionPane.showMessageDialog(locationName, "Cannot delete default location!",
    					"Input Error", JOptionPane.ERROR_MESSAGE);
            }
            int size = LocationController.getInstance().getListSize();
     
            if ((size == 0)||
            		( (size==1) && (retrievedLocationList.getElementAt(0).getName().equals("-") ) )) { //None's left, disable delete button.
                deleteButton.setEnabled(false);
            } 
            else { //Select an index.
            	if (index == retrievedLocationList.size()	) {
                    //removed item in last position
                    index--;
                }
                
                //LocationController.getInstance().printList();
                
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
            String capacity = locationCapacity.getText();
            
            //User didn't type in a unique name...or didn't type any capacity
            if (capacity.equals("") || name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
    			JOptionPane.showMessageDialog(locationName, "Please fill all required fields correctly!",
    					"Input Error", JOptionPane.ERROR_MESSAGE);
                locationName.requestFocusInWindow();
                locationName.selectAll();
                return;
            }

            

            // adds new location at the end of the retrievedLocationList
            // need to change later to be sorted in order
            Location newLocation = new Location();
            newLocation.setName(name);
            
            /*
            //single room
            if()
            newLocation.setCapacity(1);
            
            //group room
            else if()
            	newLocation.setCapacity( );
            */
            
            retrievedLocationList.addElement(newLocation);
            LocationController.getInstance().saveNewLocation(newLocation);
            if(LocationController.getInstance().setLocationCapacity(newLocation.getName(), Integer.parseInt(locationCapacity.getText()))){
                LocationController.getInstance().modifyLocation(newLocation);
            	System.out.println("ManageLocation/setLocationCapacity: location capacity saved successful");
                int tempLocationCapacity = LocationController.getInstance().getLocationCapacity(newLocation.getName());
                System.out.println("ManageLocation/setLocationCapacity: new location Capcity is: " + tempLocationCapacity );
            } else {
            	System.out.println("ManageLocation/setLocationCapacity: location capacity saved failed");
            }
            
            //LocationController.getInstance().printList();
           
            
            //Reset the text field.
            locationName.requestFocusInWindow();
            locationName.setText("");
            locationCapacity.setText("");

            int index = LocationController.getInstance().getListSize()-1; //get selected index
            
            deleteButton.setEnabled(true);
            //Select the new item and make it visible.
            displayList.setSelectedIndex(index);
            displayList.ensureIndexIsVisible(index);
        }

        //This method tests for string equality. 
        //further implement to test more rigorously (ignore whitespace, capital, etc)
        protected boolean alreadyInList(String name) {
        	for(int i=0; i<retrievedLocationList.size();i++)
        	{
        		if(retrievedLocationList.get(i).getName().equalsIgnoreCase(name))
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
                locationCapacity.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                locationCapacity.setEnabled(false);
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
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new ManageLocationDialog();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
    }
    
	
}
