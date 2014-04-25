{\rtf1\ansi\ansicpg1252\cocoartf1265\cocoasubrtf190
{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural

\f0\fs24 \cf0 package astronomy;\
\
import java.awt.Button;\
import java.awt.event.ActionEvent;\
import java.awt.event.ActionListener;\
\
public class ClickListener implements ActionListener \{\
\
	private Button b;\
	private String input;\
	\
	public ClickListener(Button _b) \{\
		_b.addActionListener(this);\
	\}\
	@Override\
	public void actionPerformed(ActionEvent arg0) \{\
		input = b.getActionCommand();\
		\
	\}\
	\
	public String returnAnswer() \{\
		return input;\
	\}\
\
\}}