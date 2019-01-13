package homework;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Scanner;

public class DuplicateSpecialist {

	static LinkedList<String> rules ;//存放所有的规则
	static {//在类初始化的时候将规则读取到内存里面
		rules = new LinkedList<String>();
		//C:\\Users\\lawrence\\Desktop\\
		File rule = new File("rules.txt");
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(rule),"UTF-8");//在Java下文件为gbk编码
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
		return line;
	}
	
	String match(String input) {//推到过程
		String[] inputs = input.split(" ");//以空格为分界将输入的条件分离为数组
		LinkedList<String> condition = new LinkedList<String>();//条件
		for(int i=0;i<inputs.length;i++) {
			condition.add(inputs[i]);
		}//将条件存放进list里面方便删除和添加
		LinkedList<String[]> models = new LinkedList<String[]>();//推导时规则的表示形式
		for(int i=0;i<rules.size();i++) {//将rule，第一个数字表示前件条件的个数
			String[] temp = rules.get(i).split("then");
			models.add(new String[]{temp[0],temp[1]});
		}//第一个数字指的是规则前件的条件个数，第二个是前件，第三个是结果
		boolean flag;//flag的用途是来标识条件是否参与规则推导
		for(int i=0;i<models.size();i++) {//从输入的条件逐个与规则匹配
			flag=true;
			String[] temp = models.get(i);
			String[] condi;
			if(temp[0].contains("and"))condi = temp[0].split("and");
			else condi = new String[]{temp[0]};
			for(int j=0;j<condi.length;j++) {
				if(!condition.contains(condi[j])) {
					flag=false;break;
				}
			}
			if(flag==true) {
				models.remove(i);
				if(!condition.contains(temp[1]))condition.add(temp[1]);
				i--;
				for(int k=0;k<condi.length;k++) {
					condition.remove(condi[k]);
				}
			}
		}
		String result = null;
		if(condition.size()==1) {//最后留下来的东西里面包含推导得到的结果和没有使用到的条件
			result=condition.get(0);
			if(result.equals(input))result="输入的条件无法判断属于知识库那种动物";
		}
		else {
			boolean flag1=true;
			for(int i=0;i<inputs.length;i++) {
				if(!condition.contains(inputs[i])) {
					flag1=false;
					break;
				}
			}
			if(!flag1) {
				result="根据你输入的条件，该动物的推导结果为：";
				for(int i=0;i<condition.size();i++) {
					result+=condition.get(i);
					if(i<condition.size()-1)result+="并且";
				}
			}
			else result="输入的条件无法判断属于知识库那种动物";
		}
		return result;
	}
	
	void addRule() throws IOException {
		Scanner input = new Scanner(System.in);
		System.out.println("请输入前件，如果前件有多个请用and分隔，不要使用空格");
		String front = input.nextLine();
		System.out.println("请输入后件，同样使用and分隔");
		String back = input.nextLine();
		String newRule = front+"then"+back;
		if(rules.contains(newRule))System.out.println("规则已存在");
		else {
			rules.add(newRule);
			File rule = new File("rules.txt");
			FileWriter fw = new FileWriter(rule,true);
			PrintWriter pw = new PrintWriter(fw);
			pw.flush();
			if(rules.size()!=1)pw.print("\n"+newRule);
			else pw.print(newRule);
			fw.flush();
			System.out.println("添加成功");
			fw.close();
			pw.close();
		}
	}
	
	void deleteRule() throws IOException {
		System.out.println("系统包含的规则有：");
		for(int i=0;i<rules.size();i++) {
			System.out.println(i+1+":"+rules.get(i));
		}
		System.out.println("请输入要删除的规则的序号");
		Scanner input = new Scanner(System.in);
		int num = input.nextInt();
		while(num<1||num>rules.size()) {
			System.out.println("输入有误,请重新输入");
			num = input.nextInt();
		}
		rules.remove(num-1);
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(new File("rules.txt")));
		BufferedWriter bw = new BufferedWriter(osw);
		String content="";
		for(int i=0;i<rules.size();i++) {
			content+=rules.get(i);
			if(i!=rules.size()-1)content+="\n";
		}
		bw.append(content);
		bw.close();
		osw.close();
	}
	
	public static void main(String args[]) throws IOException {
		DuplicateSpecialist s = new DuplicateSpecialist();
		while(true) {
			boolean flag=false;
			int choose;
			Scanner input = new Scanner(System.in);
			System.out.print("这是一个推导系统，请输入要选择的操作：\n1代表查询\n2代表添加规则\n3代表删除规则\n4代表退出\n");
			choose = input.nextInt();
			switch(choose) {
			case 1:
				System.out.println("请输入动物的体征，输入格式为：“有XX” “是XX”");
				String condition = s.input();
				System.out.println(s.match(condition));break;
			case 2:
				s.addRule();break;
			case 3:
				s.deleteRule();break;
			case 4:flag=true;break;
			default:System.out.println("输入有误");break;
			}
			if(flag) {
				input.close();
				break;
			}
		}
	}
}
