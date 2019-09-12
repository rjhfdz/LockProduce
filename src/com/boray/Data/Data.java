package com.boray.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TreeSet;

import javax.comm.SerialPort;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Data {
	//public static String lockCom;
	public static SerialPort lockSerialPort;
	//public static String scanGunCom;
	public static SerialPort scanGunSerialPort;
	public static Timer lockTimer;
	public static TreeSet lockIdTreeSet = new TreeSet();
	public static File projectFile;
	public static String newProjectPath = "";
	public static String newProjectName = "";
	public static String openProjectPath = "";
	public static TreeSet SnTreeSet = new TreeSet();
}
