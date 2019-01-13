package homework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

public class SimpleSpecialist {
	static LinkedList<String> rules ;//存放所有的规则
	static {//在类初始化的时候将规则读取到内存里面
		rules = new LinkedList<String>();
		File rule = new File("rules.txt");
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(rule),"gbk");//在Java下文件为gbk编码
			BufferedReader reader = new BufferedReader(isr);
			String line = null;
			while((line = reader.readLine())!=null) {
				rules.add(line);
				//System.out.println(line);
			}
			reader.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	String input() {//用户输入判断条件，不同条件之间用空格隔开
		Scanner input = new Scanner(System.in);
		String line = input.nextLine();
		input.close();
		return line;
	}
	
	String match(String input) {//推到过程
		String[] inputs = input.split(" ");//以空格为分界将输入的条件分离为数组
		LinkedList<String> condition = new LinkedList<String>();//条件
		for(int i=0;i<inputs.length;i++) {
			condition.add(inputs[i]);
		}//将条件存放进list里面方便删除和添加
		LinkedList<String[]> models = new LinkedList<String[]>();//推导时规则的表示形式
		for(int i=0;i<rules.size();i++) {//将rule的形式转变为map形式，第一个数字表示前件条件的个数
			String[] temp = rules.get(i).split("then");
			int count;
			if(temp[0].indexOf("and")!=-1)count = temp[0].split("and").length;
			else count = 1;
			models.add(new String[]{count+"",temp[0],temp[1]});
		}//第一个数字指的是规则前件的条件个数，第二个是前件，第三个是结果
		boolean flag;//flag的用途是来标识条件是否参与规则推导
		for(int i=0;i<condition.size();i++) {//从输入的条件逐个与规则匹配
			flag = false; //对于每个规则，一开始都是没有参与推导
			for(int j=0;j<models.size();j++) {//逐条规则匹配
				String[] temp = models.get(j);
				if(temp[1].indexOf(condition.get(i))!=-1) {//判断规则j的前件中是否含有条件i，若有
					flag=true;
					int tempnum = Integer.parseInt(temp[0])-1;
					temp[0] = tempnum+"";//规则的前件判断一次符合，减少1
					if(temp[0].equals("0")) {//当规则前件为0，说明推到的规则成功，后件加入到条件集中
						if(!condition.contains(temp[2]))condition.add(temp[2]);
						models.remove(j);//规则已经使用完毕，从规则集中删除
						j--;//避免指针移动
					}
					else models.set(j, temp);//前件判断仅减少一次，还不为0，规则j不能删除
				}
			}
			if(flag==true) {//条件i参与推导，已经没有用处了删除
				condition.remove(i);
				i--;//避免指针移动
			}
		}
		String result = null;
		if(condition.size()==1) {//最后留下来的东西里面包含推导得到的结果和没有使用到的条件
			switch(condition.get(0)) {
			case "是虎":result="是虎";break;
			case "是豹":result="是豹";break;
			case "是斑马":result="是斑马";break;
			case "是长颈鹿":result="是长颈鹿";break;
			case "是企鹅":result="是企鹅";break;
			case "是鸵鸟":result="是鸵鸟";break;
			case "是信天翁":result="是信天翁";break;
			default:break;
			}
		}
		else result="输入的条件无法判断属于知识库那种动物";
		return result;
	}
	
	public static void main(String args[]) {
		SimpleSpecialist s = new SimpleSpecialist();
		String condition = s.input();
		System.out.println(s.match(condition));
	}
}
