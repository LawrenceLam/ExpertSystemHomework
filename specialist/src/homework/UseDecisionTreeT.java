package homework;

import java.util.HashMap;
import java.util.LinkedList;


import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;


class DecisionTree {
	LinkedList<String[]> dataList = new LinkedList<String[]>();//保存训练集的数据，结构为：序号+属性+类别
	LinkedList<String> attribute = new LinkedList<String>();//存放属性个数
	DecisionTree father;
	String attriValue;
	String attriDivide;
	LinkedList<DecisionTree> child;
	HashMap<String,LinkedList<String>> attributeValue = new HashMap<String,LinkedList<String>>();//存放属性及其对应的属性值
	public DecisionTree(String[][] data,HashMap<String,LinkedList<String>> attributeValue,DecisionTree father,String attriDivide,String attriValue) {
		this.father = father;
		this.attriDivide = attriDivide;
		this.attriValue = attriValue;
		this.attributeValue = attributeValue;
		getDataAndAttribute(data,attributeValue);
		if(!detectEnd()) {//当前节点不为终叶节点，可以继续往下分
			String attriRoot = bestAttri();//得到当前的划分属性
			Map<String,LinkedList<String[]>> child = divideByAttribute(attriRoot);//得到划分后的所有东西
			this.child = new LinkedList<DecisionTree>();//后面划分的节点属于当前的儿子
			//获得不同键值下面的数据集，先获取键值集
			Set<String> keySet = child.keySet();
			//遍历键值集
			Iterator<String> keys = keySet.iterator();
			while(keys.hasNext()) {
				String key = keys.next();
				LinkedList<String[]> childData = child.get(key);//获取此键值下面的所有数据集
				HashMap<String,LinkedList<String>> newAttribute = this.attributeValue;
				newAttribute.remove(attriRoot);
				if(childData.size()==0)continue;
				String[][] datas = new String[childData.size()][childData.get(0).length];//将child下面的data改为二维数组的形式
				for(int i=0;i<childData.size();i++) {
					datas[i] = childData.get(i);
				}
				DecisionTree childNode = new DecisionTree(datas,newAttribute,this,attriRoot,key);
				this.child.add(childNode);
			}
		}
	}
	public void getDataAndAttribute(String[][] data,HashMap<String,LinkedList<String>> attribute) {
		for(int i=0;i<data.length;i++) {//将所有数据集压入类的数据集中
			this.dataList.add(data[i]);
		}
		Set<String> keySet = attribute.keySet();
		Iterator<String> it = keySet.iterator();//将map里面的键值写入到本地的attribute表中，作为属性表
		while(it.hasNext()) {
			String s = it.next();
			this.attribute.add(s);
		}
	}
	boolean detectEnd() {//判断当前节点是否为终叶节点
		Set<String> detect = new HashSet<String>();
		for(int i=0;i<dataList.size();i++) {
			String[] temp = dataList.get(i);
			detect.add(temp[temp.length-1]);
		}//当所有分类结果最终只有一种结果，就是终叶节点
		if(detect.size()==1)return true;
		else return false;
	}
	double calEntropy(String attribute) {
		double result = 0;//所有属性值的熵值和
		double totalNum = this.dataList.size();//总数据集的个数
		Map<String,LinkedList<String[]>> divide = divideByAttribute(attribute);//得到按属性attribute值分类的结果
		Set<String> keySet = divide.keySet();//得到所有键值
		Iterator<String> iterator = keySet.iterator();//遍历所有键值
		while(iterator.hasNext()) {
			String key = iterator.next();
			LinkedList<String[]> values = divide.get(key);//获得当前键值下所有的数据集
			int count = values.size();//当前键值下的数据个数
			Set<String> resultSet = new HashSet<String>();//使用Set来判断结果中有多少种
			for(int i=0;i<count;i++) {
				String[] temp = values.get(i);
				resultSet.add(temp[temp.length-1]);
			}
			Iterator<String> iteratorResult = resultSet.iterator();//遍历结果种数
			double resultInAttribute = 0;//当前属性值下的熵值
			int countI;
			while(iteratorResult.hasNext()) {
				countI=0;//计算不同结果各自有多少种
				String resultI = (String)iteratorResult.next();//当前的结果
				for(int i=0;i<count;i++) {
					String[] temp = values.get(i);//与数据集中的结果比较
					if(temp[temp.length-1].equals(resultI))countI++;//如果数据与当前结果相同，计数加一
				}
				//计算得到当前属性值的熵
				resultInAttribute = resultInAttribute - ((double)countI/count)*(Math.log((double)countI/count)/Math.log(2));
			}
			result = result + ((double)count/totalNum)*resultInAttribute;
		}
		return result;
	}
	public String bestAttri() {
		double min = 100;
		String choose = "";
		for(int i=0;i<this.attribute.size();i++) {
			double cal = calEntropy(this.attribute.get(i));
			if(min>cal) {
				min = cal;
				choose = this.attribute.get(i);
			}
		}
		return choose;
	}
	Map<String,LinkedList<String[]>> divideByAttribute(String attribute){
		LinkedList<String> attriValue = this.attributeValue.get(attribute);//获得当前属性下的属性值
		String[] content = this.dataList.get(0);//从本类的数据中拿出第0个，为了判断当前的attribute在哪一列
		int col=0;
		for(int i=1;i<content.length-1;i++) {
			if(attriValue.contains(content[i])) {
				col = i;//找到当前attribute所在的列
				break;
			}
		}
		Map<String,LinkedList<String[]>> result = new HashMap<String,LinkedList<String[]>>();//结果集
		//下面开始按attribute的值对dataList分类
		for(int i=0;i<attriValue.size();i++) {//遍历
			LinkedList<String[]> resultValue = new LinkedList<String[]>();//当前attribute[i]的值
			for(int j=0;j<this.dataList.size();j++) {
				String[] temp = this.dataList.get(j);
				if(temp[col].equals(attriValue.get(i)))resultValue.add(temp);
			}
			if(resultValue.size()!=0)result.put(attriValue.get(i), resultValue);
		}
		return result;
	}
	
}


public class UseDecisionTreeT {
	public static void main(String[] args) {
		LinkedList<String> degree = new LinkedList<String>();
		degree.add("master");degree.add("undergraduate");degree.add("junior");
		LinkedList<String> sex = new LinkedList<String>();
		sex.add("male");sex.add("female");
		LinkedList<String> English = new LinkedList<String>();
		English.add("BelowCET4");English.add("CET4");English.add("CET6");
		LinkedList<String> charac = new LinkedList<String>();
		charac.add("a1");charac.add("a2");charac.add("a3");
		LinkedList<String> job = new LinkedList<String>();
		job.add("b1");job.add("b2");job.add("b3");
		HashMap<String,LinkedList<String>> attriAndValue2 = new HashMap<String,LinkedList<String>>();
		attriAndValue2.put("degree",degree);attriAndValue2.put("sex",sex);
		attriAndValue2.put("English",English);attriAndValue2.put("charac", charac);
		attriAndValue2.put("job", job);
		String[][] data2 = {
				{"1","undergraduate","male","CET4","a1","b1","Y"},
				{"2","undergraduate","female","CET6","a3","b1","N"},
				{"3","undergraduate","male","CET6","a2","b2","Y"},
				{"4","undergraduate","male","CET4","a1","b3","Y"},
				{"5","undergraduate","female","CET4","a2","b2","Y"},
				{"6","undergraduate","male","BelowCET4","a3","b3","Y"},
				{"7","undergraduate","female","CET4","a2","b3","Y"},
				{"8","undergraduate","female","CET4","a1","b1","Y"},
				{"9","junior","male","BelowCET4","a1","b1","N"},
				{"10","junior","male","CET4","a2","b2","Y"},
				{"11","junior","female","CET4","a3","b3","Y"},
				{"12","junior","female","BelowCET4","a3","b3","Y"},
				{"13","master","male","CET6","a2","b2","Y"},
				{"14","master","female","CET6","a2","b2","Y"},
				{"15","master","male","CET4","a1","b3","N"},
				{"16","master","female","CET6","a1","b1","Y"}
		};
		String[] attribute2 = {"degree","sex","English","charac","job"};
		DecisionTree dt2 = new DecisionTree(data2,attriAndValue2,null,null,null);
		UseDecisionTreeT udt = new UseDecisionTreeT();
		udt.decide(new String[] {"undergraduate","female","CET6","a3","b1"},dt2, attribute2);
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
