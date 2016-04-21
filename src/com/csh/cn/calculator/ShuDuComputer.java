package com.csh.cn.calculator;

import java.util.ArrayList;
import java.util.List;

public class ShuDuComputer {

	//数据
	private int[][] data;
	public int[][] getData() {
		return data;
	}
	
	private List<List<Integer>> list=new ArrayList<List<Integer>>();
	//用来统计有多少种可能性
	private List<Integer> listKeNengXing=new ArrayList<Integer>();
	
	//用来保存假设数据
	private List<BaoCunInfo> listBaoCunInfo=new ArrayList<BaoCunInfo>();
	
	//假设数据的
	private int sunXu=1;
	
	public ShuDuComputer(int[][] data){
		
		this.data=this.arrayFuZhi(data);
	}
	
	//数组赋值
	private int[][] arrayFuZhi(int[][] d){
		int[][] temp=new int[9][9];
		for(int i=0;i<d.length;i++){
			for(int j=0;j<d[i].length;j++){
				temp[i][j]=d[i][j];
			}
		}
		return temp;
	}
	
	//调试时使用
	public void tiaoShi(){
		for(int i=0;i<9;i++){
			System.out.println(data[i][0]+" "+data[i][1]+" "+data[i][2]+" "+
					data[i][3]+" "+data[i][4]+" "+data[i][5]+" "+
					data[i][6]+" "+data[i][7]+" "+data[i][8]+" ");
		}
		System.out.println("---------------------------------------");
		for(int i=0;i<9;i++){
			System.out.println(listKeNengXing.get(i*9+0)+" "+listKeNengXing.get(i*9+1)+" "+listKeNengXing.get(i*9+2)+" "+
					listKeNengXing.get(i*9+3)+" "+listKeNengXing.get(i*9+4)+" "+listKeNengXing.get(i*9+5)+" "+
					listKeNengXing.get(i*9+6)+" "+listKeNengXing.get(i*9+7)+" "+listKeNengXing.get(i*9+8)+" ");
		}
	}
	
	//计算流程		-1---->计算失败		1----->正常
	public int jiSuan(){
		int failed1=this.initFirstdata(this.data);
		if(failed1==-1){
			return -1;
		}
		
		int keNengXing=this.minKeNengXing(this.listKeNengXing);
		while(keNengXing!=-1){
			if(keNengXing==1){
				int singlePostion=this.findSingle(this.listKeNengXing,1);
				List<Integer> t1=this.list.get(singlePostion);
				
				this.data[singlePostion/9][singlePostion%9]=t1.get(0);
				this.listKeNengXing.set(singlePostion, 0);
				//再次进行计算
				int failed2=this.initSeconddata(this.data);
				if(failed2==-1){
					//检查是否有假设值
					if(this.sunXu==1){
						return -1;
					}else{
						//恢复保存点
						this.backBaoCunPoint();
						failed2=this.initSeconddata(this.data);
					}
				}
				
				for(int i=0;i<9;i++){
					System.out.println(data[i][0]+" "+data[i][1]+" "+data[i][2]+" "+
							data[i][3]+" "+data[i][4]+" "+data[i][5]+" "+
							data[i][6]+" "+data[i][7]+" "+data[i][8]+" ");
				}
				keNengXing=this.minKeNengXing(this.listKeNengXing);
			}else{
				int singlePostion=this.findSingle(this.listKeNengXing,keNengXing);
				List<Integer> t1=this.list.get(singlePostion);
				
				List<Integer> listShenYu=this.getNewList(t1);
				int random=(int)(Math.random()*listShenYu.size());
				this.sunXu++;
				int value=listShenYu.get(random);
				this.data[singlePostion/9][singlePostion%9]=value;
				listShenYu.remove(random);
				this.listBaoCunInfo.add(new BaoCunInfo(this.data,
									singlePostion/9,
									singlePostion%9, 
									value, 
									listShenYu.size(),
									this.sunXu,
									listShenYu));
				
				this.listKeNengXing.set(singlePostion, 0);
				//再次进行计算
				int failed2=this.initSeconddata(this.data);
				if(failed2==-1){
					//检查是否有假设值
					if(this.sunXu==1){
						return -1;
					}else{
						//恢复保存点
						this.backBaoCunPoint();
					}
				}
				keNengXing=this.minKeNengXing(this.listKeNengXing);
				System.out.println("keNengXing="+keNengXing);
			}
			
		}
		
		if(keNengXing==-1){
			//检验计算结果的正确性
			for(int i=0;i<this.data.length;i++){
				int sum=0;
				for(int j=0;j<this.data[i].length;j++){
					sum+=this.data[i][j];
				}
				if(sum!=45){
					break;
				}
			}
			
			//检验计算结果的正确性
			for(int i=0;i<this.data.length;i++){
				int sum=0;
				for(int j=0;j<this.data[i].length;j++){
					sum+=this.data[j][i];
				}
				if(sum!=45){
					break;
				}
			}
			
		}
		
		return 1;
	}
	
	//恢复保存点
	private void backBaoCunPoint(){
		//1--寻找最后的一个保存点数据
		//冲listBaoCunInfo中取得最后的假设值
		int position=this.listBaoCunInfo.size()-1;
		BaoCunInfo baoCunInfo=this.listBaoCunInfo.get(position);
		//2--判断还有没有剩余的假设值
		int jiaSheZhi=baoCunInfo.getShenYu();
		while(jiaSheZhi==0){
			this.sunXu--;
			position=this.getPositionDateFromLBCI(this.sunXu);
			if(position==-1){
				return ;
			}else{
				jiaSheZhi=this.listBaoCunInfo.get(position).getShenYu();
			}
		}
		//3--恢复现场数据
		//3.1--去除对应的数据，修改后保存
		baoCunInfo=this.listBaoCunInfo.get(position);
		List<Integer> listShenYu=baoCunInfo.getListShenYu();
		int random=(int)(listShenYu.size()*Math.random());
		int value=listShenYu.get(random);
		this.data=this.arrayFuZhi(baoCunInfo.getData());
		this.data[baoCunInfo.getX()][baoCunInfo.getY()]=value;
		baoCunInfo.setShenYu(baoCunInfo.getShenYu()-1);
		listShenYu.remove(random);
		baoCunInfo.setListShenYu(listShenYu);
		//3.1--从listBaoCunInfo中清除现场数据以后的所有的数据
		List<Integer> deletePosition=new ArrayList<Integer>();
		for(int i=position+1;i<this.listBaoCunInfo.size();i++){
			deletePosition.add(position);
		}
		//3.2--删除数据
		for(int i=0;i<deletePosition.size();i++){
			this.listBaoCunInfo.remove(deletePosition.get(i));
		}
	}

	//从listBaoCunInfo中取得指定sunXu的数据---return position没有查询到返回-1
	private int getPositionDateFromLBCI(int sunXu){
		//从后向前查询
		for(int i=this.listBaoCunInfo.size()-1;i>=0;i--){
			if(this.listBaoCunInfo.get(i).getSunXu()==sunXu){
				return i;
			}
		}
		
		return -1;
	}
	
	//查找1个的数据	没有--（-1） 有的话返回（i*9+j）
	private int findSingle(List<Integer> list,int number){
		for(int i=0;i<list.size();i++){
			if(list.get(i)==number){
				return i;
			}
		}
		
		return -1;
	}
	
	//取得可能性中最小的非零的数
	private int minKeNengXing(List<Integer> list){
		int min=0;
		
		int j=0;
		while(list.get(j)==0){
			j++;
			if(j==list.size()){
				return -1;
			}
		}
		min=list.get(j);
		
		for(int i=j;i<list.size();i++){
			if(list.get(i)!=0){
				if(list.get(i)<min){
					min=list.get(i);
					if(min==1){
						return min;
					}
				}
			}
		}
		
		if(min==0){
			//代表计算完毕
			return -1;
		}else{
			return min;
		}
	}
	
	//第N次初始化数据----判断未知空格的可能数据		-1----代表失败	1----正常
	private int initSeconddata(int[][] data){
		
		for(int i=0;i<data.length;i++){
			for(int j=0;j<data[i].length;j++){
				if(this.data[i][j]==0){
					//list.add(i*9+j, this.getKeNengXingData(i, j));
					List<Integer> t=this.getKeNengXingData(i, j);
					if(t.size()==0){
						return -1;
					}
					this.list.remove(i*9+j);
					this.list.add(i*9+j,t);
					this.listKeNengXing.set(i*9+j, t.size());
				}
			}
		}
		
		return 1;
	}
	
	//第一次初始化数据----判断未知空格的可能数据  -1----代表失败	1----正常
	private int initFirstdata(int[][] data){
		
		for(int i=0;i<data.length;i++){
			for(int j=0;j<data[i].length;j++){
				if(this.data[i][j]==0){
					List<Integer> t=this.getKeNengXingData(i, j);
					if(t.size()==0){
						return -1;
					}
					this.list.add(i*9+j, t);
					this.listKeNengXing.add(i*9+j, t.size());
				}else{
					List<Integer> temp=new ArrayList<Integer>();
					this.list.add(i*9+j,temp);
					this.listKeNengXing.add(i*9+j,0);
				}
			}
		}
		
		return 1;
	}
	
	//取得一个方格的可能行数据
	private List<Integer> getKeNengXingData(int x,int y){
		List<Integer> temp=new ArrayList<Integer>();
		for(int i=1;i<10;i++){
			temp.add(i);
		}
		
		//横向
		for(int i=0;i<9;i++){
			if(i!=x){
				temp=this.dealAllData(temp, this.data[i][y]);
			}
		}
		
		//竖向
		for(int i=0;i<9;i++){
			if(i!=y){
				temp=this.dealAllData(temp, this.data[x][i]);
			}
		}
		
		//所在的9方格
		int startX=x/3*3;
		int startY=y/3*3;
		for(int i=startX;i<startX+3;i++){
			for(int j=startY;j<startY+3;j++){
				if(i!=x&&j!=y){
					temp=this.dealAllData(temp, this.data[i][j]);
				}
			}
		}
		
		return temp;
	}
	
	//复制一个新的List对象
	private List<Integer> getNewList(List<Integer> list){
		List<Integer> data=new ArrayList<Integer>();
		for(int i=0;i<list.size();i++){
			data.add(list.get(i));
		}
		
		return data;
	}
	
	//处理一个List，判断有没有改数据
	private List<Integer> dealAllData(List<Integer> temp,int number){
		for(int i=0;i<temp.size();i++){
			if(temp.get(i)==number){
				temp.remove(i);
				break;
			}
		}
		
		return temp;
	}
	
	public class BaoCunInfo {

		private int[][] data;
		private int x;
		private int y;
		//剩余可能性数据的个数
		private int shenYu;
		//剩余的假设值
		private List<Integer> listShenYu;
		//假设的值
		private int value;
		//假设数据的顺序
		private int sunXu;
		
		public BaoCunInfo(int[][] data,
				int x, int y, 
				int value,
				int shenYu,
				int sunXu,
				List<Integer> listShenYu) {
			super();
			this.data =this.arrayFuZhi(data);
			this.x = x;
			this.y = y;
			this.value = value;
			this.sunXu=sunXu;
			this.shenYu=shenYu;
			this.listShenYu=listShenYu;
		}
		
		//数组赋值
		private int[][] arrayFuZhi(int[][] d){
			int[][] temp=new int[9][9];
			for(int i=0;i<d.length;i++){
				for(int j=0;j<d[i].length;j++){
					temp[i][j]=d[i][j];
				}
			}
			return temp;
		}
		
		
		public List<Integer> getListShenYu() {
			return listShenYu;
		}

		public void setListShenYu(List<Integer> listShenYu) {
			this.listShenYu = listShenYu;
		}
		public void setShenYu(int shenYu) {
			this.shenYu = shenYu;
		}
		public int getShenYu() {
			return shenYu;
		}
		public List<List<Integer>> getList() {
			return list;
		}
		public void setSunXu(int sunXu) {
			this.sunXu = sunXu;
		}
		public int getSunXu() {
			return sunXu;
		}
		public int[][] getData() {
			return data;
		}

		public void setData(int[][] data) {
			this.data = data;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}
}
