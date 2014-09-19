/*******************************************************************************
 *  Copyright 2013 Jason Sipula, Trace Hagan                                   *
 *                                                                             *
 *  Licensed under the Apache License, Version 2.0 (the "License");            *
 *  you may not use this file except in compliance with the License.           *
 *  You may obtain a copy of the License at                                    *
 *                                                                             *
 *      http://www.apache.org/licenses/LICENSE-2.0                             *
 *                                                                             *
 *  Unless required by applicable law or agreed to in writing, software        *
 *  distributed under the License is distributed on an "AS IS" BASIS,          *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 *  See the License for the specific language governing permissions and        *
 *  limitations under the License.                                             *
 *******************************************************************************/

package net.snakedoc.superd;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Deleter {

    public static void buildGUI(final Object[][] duplicates){
        final String[] columnNames = {"File Path", "File Hash"};
        //build button
        JButton jb = new JButton("Delete Selected Files");
        //build list box for duplicates
        final DefaultTableModel model = new DefaultTableModel(duplicates, columnNames){

            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        };
        final JTable filesTable = new JTable(model);
        //add action listener that handles deletion to jbutton jb
        jb.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //get selected files marked for deletion
                int[] listIndicies = filesTable.getSelectedRows();
                //build file array of selected files
                File[] filesSelected = new File[listIndicies.length];
                for (int i = 0; i < listIndicies.length; i++){
                    filesSelected[i]= new File(duplicates[listIndicies[i]][0].toString());
                    model.removeRow(listIndicies[i]);
                }
                //delete files

                for (int j = 0; j < filesSelected.length ; j++){
                    System.out.println(filesSelected[j].toString());

                    /*TODO UNCOMMENT THIS AFTER VERIFIED WORKING
                    filesSelected[j].delete();
                    */
                }
            }
        });
        //build frame for GUI
        JFrame frame = new JFrame("Select Files to Delete");
        //build panel for GUI components
        JPanel panel = new JPanel();
        //set list to allow multiple selection
        filesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(filesTable);
        filesTable.setFillsViewportHeight(true);
        //add a scrollable jlist to panel
        panel.add(scrollPane);
        //add the button
        panel.add(jb);
        //set panel visible
        panel.setVisible(true);
        //set default action of frame
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //add panel to frame
        frame.add(panel);
        //set frame size
        frame.setSize(500,800);
        //show GUI
        frame.setVisible(true);
    }
    public Deleter(){

    }
}
