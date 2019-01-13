package homework;

import java.util.HashMap;
import java.util.LinkedList;

public class UseDecisionTree3 {
	public static void main(String[] args) {
		String[][] data = {
				{"1","a1","b1","Y"},
				{"2","a2","b1","N"},
				{"3","a1","b2","Y"},
				{"4","a2","b2","N"},
				{"5","a3","b1","N"}
		};
		LinkedList<String> a = new LinkedList<String>();
		LinkedList<String> b = new LinkedList<String>();
		a.add("a1");a.add("a2");a.add("a3");
		b.add("b1");b.add("b2");
		HashMap<String,LinkedList<String>> att = new HashMap<String,LinkedList<String>>();
		att.put("a",a);att.put("b",b);
		String[] attribute = {"a","b"};
		UseDecisionTree3 udt = new UseDecisionTree3();
		DecisionTree dt = new DecisionTree(data,att,null,null,null);
		udt.decide(new String[] {"a3","b2"},dt , attribute);
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
