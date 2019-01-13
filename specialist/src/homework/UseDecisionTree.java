package homework;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class UseDecisionTree {
	public static void main(String[] args) {
		//7.14
		String[][] data1= {
			{"1","a2","b1","c1","d1","N"},
			{"2","a2","b1","c2","d1","N"},
			{"3","a2","b1","c3","d1","N"},
			{"4","a1","b1","c1","d1","Y"},
			{"5","a1","b1","c3","d1","Y"},
			{"6","a3","b2","c1","d1","N"},
			{"7","a3","b2","c3","d1","N"},
			{"8","a3","b1","c1","d2","Y"},
			{"9","a3","b3","c3","d2","N"},
			{"10","a3","b1","c2","d2","N"},
			{"11","a1","b3","c2","d2","Y"},
			{"12","a1","b3","c3","d2","Y"},
			{"13","a2","b2","c1","d1","N"},
			{"14","a2","b2","c3","d1","N"},
			{"15","a2","b3","c1","d2","Y"},
			{"16","a2","b3","c3","d2","Y"},
			{"17","a3","b2","c1","d2","N"},
			{"18","a3","b2","c3","d2","N"},
			{"19","a2","b2","c3","d2","Y"},
			{"20","a2","b2","c2","d2","Y"},
			{"21","a1","b2","c2","d1","Y"},
			{"22","a1","b2","c3","d1","Y"},
			{"23","a1","b1","c1","d2","Y"},
			{"24","a3","b2","c2","d1","N"}
		};
		HashMap<String,LinkedList<String>> attri1AndValue = new HashMap<String,LinkedList<String>>();
		LinkedList<String> value11 = new LinkedList<String>();
		value11.add("a1");value11.add("a2");value11.add("a3");
		LinkedList<String> value12 = new LinkedList<String>();
		value12.add("b1");value12.add("b2");value12.add("b3");
		LinkedList<String> value13 = new LinkedList<String>();
		value13.add("c1");value13.add("c2");value13.add("c3");
		LinkedList<String> value14 = new LinkedList<String>();
		value14.add("d1");value14.add("d2");
		attri1AndValue.put("DQ", value11);attri1AndValue.put("JZZY",value12);
		attri1AndValue.put("YJZJ", value13);attri1AndValue.put("YJHF", value14);
		DecisionTree dt = new DecisionTree(data1,attri1AndValue,null,null,null);
		UseDecisionTree udt = new UseDecisionTree();
		String[] attribute1 = new String[]{"DQ","JZZY","YJZJ","YJHF"};
		udt.decide(new String[] {"a3","b2","c3","d1"},dt,attribute1 );
	}
	public void decide(String[] data,DecisionTree dt,String[] attribute) {
		int j;
		while(dt.child!=null) {
			j=0;
			String s = dt.child.get(0).attriDivide;
			for(int k=0;k<attribute.length;k++) {
				if(s.equals(attribute[k]))j=k;
			}
			for(int i=0;i<dt.child.size();i++) {
				if(data[j].equals(dt.child.get(i).attriValue)) {
					dt = dt.child.get(i);
					break;
				}
			}
		}
		String[] temp = dt.dataList.get(0);
		System.out.println(temp[temp.length-1]);
	}
}
