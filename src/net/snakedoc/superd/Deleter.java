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

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

public class Deleter {
    private static final Logger log = Logger.getLogger(Deleter.class);
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
        final JTable filesTable = new JTable(model)
        {
            //CODE FROM ANSWER ON STACKOVERFLOW http://stackoverflow.com/questions/7132400/jtable-row-hightlighter-based-on-value-from-tablecell
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                JComponent jc = (JComponent) c;
                if (!isRowSelected(row))
                {
                    c.setBackground(getRowBackground(row));
                }

                return c;
            }

            private Color getRowBackground(int row)
            {
                boolean isDark = true;

                Object previous = getValueAt(0, 1);

                for (int i = 1; i <= row; i++)
                {
                    Object current = getValueAt(i, 1);

                    if (! current.equals(previous))
                    {
                        isDark = !isDark;
                        previous = current;
                    }
                }

                return isDark ? Color.WHITE : Color.LIGHT_GRAY;
            }
        };
        //add action listener that handles deletion to jbutton jb
        jb.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //get selected files marked for deletion
                int[] listIndicies = filesTable.getSelectedRows();
                Arrays.sort(listIndicies);
                //build file array of selected files
                File[] filesSelected = new File[listIndicies.length];
                for (int i = 0; i < listIndicies.length; i++){
                    filesSelected[i]= new File(model.getValueAt(filesTable.convertRowIndexToModel(listIndicies[i]),0).toString());
                }
                //delete files

                for (int j = 0; j < filesSelected.length ; j++){
                    System.out.println(filesSelected[j].toString());
                    model.removeRow(filesTable.convertRowIndexToModel(listIndicies[j]-j));
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
        filesTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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
