package com.boray.ui;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class NewJTable extends JTable implements Cloneable{
	private int r=0,c=0;
	private int oldR=0,oldC=0;
	private boolean yes = false;
	private int type;
	public NewJTable(Object[][] data, String[] name) {
		super(data,name);
	}
	public NewJTable(DefaultTableModel model,int type){
		super(model);
		this.type = type;
		setUI(BasicTableUI.createUI(this));
		setShowGrid(true);
		((DefaultTableCellRenderer)getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
	}
	public final void setR_C(int r,int c,boolean b){
		this.r = r;
		this.c = c;
		yes = b;
	}
	public boolean isCellEditable(int row, int col) {
		if (col==0 && type == 9) {
			return true;
		}
		return false;
	}
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {

		  Component component = super.prepareRenderer(renderer, row, column);
		  return component;
	}
}
