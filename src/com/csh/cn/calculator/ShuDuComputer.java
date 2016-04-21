package com.csh.cn.calculator;

import java.util.ArrayList;
import java.util.List;

public class ShuDuComputer {

	//����
	private int[][] data;
	public int[][] getData() {
		return data;
	}
	
	private List<List<Integer>> list=new ArrayList<List<Integer>>();
	//����ͳ���ж����ֿ�����
	private List<Integer> listKeNengXing=new ArrayList<Integer>();
	
	//���������������
	private List<BaoCunInfo> listBaoCunInfo=new ArrayList<BaoCunInfo>();
	
	//�������ݵ�
	private int sunXu=1;
	
	public ShuDuComputer(int[][] data){
		
		this.data=this.arrayFuZhi(data);
	}
	
	//���鸳ֵ
	private int[][] arrayFuZhi(int[][] d){
		int[][] temp=new int[9][9];
		for(int i=0;i<d.length;i++){
			for(int j=0;j<d[i].length;j++){
				temp[i][j]=d[i][j];
			}
		}
		return temp;
	}
	
	//����ʱʹ��
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
	
	//��������		-1---->����ʧ��		1----->����
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
				//�ٴν��м���
				int failed2=this.initSeconddata(this.data);
				if(failed2==-1){
					//����Ƿ��м���ֵ
					if(this.sunXu==1){
						return -1;
					}else{
						//�ָ������
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
				//�ٴν��м���
				int failed2=this.initSeconddata(this.data);
				if(failed2==-1){
					//����Ƿ��м���ֵ
					if(this.sunXu==1){
						return -1;
					}else{
						//�ָ������
						this.backBaoCunPoint();
					}
				}
				keNengXing=this.minKeNengXing(this.listKeNengXing);
				System.out.println("keNengXing="+keNengXing);
			}
			
		}
		
		if(keNengXing==-1){
			//�������������ȷ��
			for(int i=0;i<this.data.length;i++){
				int sum=0;
				for(int j=0;j<this.data[i].length;j++){
					sum+=this.data[i][j];
				}
				if(sum!=45){
					break;
				}
			}
			
			//�������������ȷ��
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
	
	//�ָ������
	private void backBaoCunPoint(){
		//1--Ѱ������һ�����������
		//��listBaoCunInfo��ȡ�����ļ���ֵ
		int position=this.listBaoCunInfo.size()-1;
		BaoCunInfo baoCunInfo=this.listBaoCunInfo.get(position);
		//2--�жϻ���û��ʣ��ļ���ֵ
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
		//3--�ָ��ֳ�����
		//3.1--ȥ����Ӧ�����ݣ��޸ĺ󱣴�
		baoCunInfo=this.listBaoCunInfo.get(position);
		List<Integer> listShenYu=baoCunInfo.getListShenYu();
		int random=(int)(listShenYu.size()*Math.random());
		int value=listShenYu.get(random);
		this.data=this.arrayFuZhi(baoCunInfo.getData());
		this.data[baoCunInfo.getX()][baoCunInfo.getY()]=value;
		baoCunInfo.setShenYu(baoCunInfo.getShenYu()-1);
		listShenYu.remove(random);
		baoCunInfo.setListShenYu(listShenYu);
		//3.1--��listBaoCunInfo������ֳ������Ժ�����е�����
		List<Integer> deletePosition=new ArrayList<Integer>();
		for(int i=position+1;i<this.listBaoCunInfo.size();i++){
			deletePosition.add(position);
		}
		//3.2--ɾ������
		for(int i=0;i<deletePosition.size();i++){
			this.listBaoCunInfo.remove(deletePosition.get(i));
		}
	}

	//��listBaoCunInfo��ȡ��ָ��sunXu������---return positionû�в�ѯ������-1
	private int getPositionDateFromLBCI(int sunXu){
		//�Ӻ���ǰ��ѯ
		for(int i=this.listBaoCunInfo.size()-1;i>=0;i--){
			if(this.listBaoCunInfo.get(i).getSunXu()==sunXu){
				return i;
			}
		}
		
		return -1;
	}
	
	//����1��������	û��--��-1�� �еĻ����أ�i*9+j��
	private int findSingle(List<Integer> list,int number){
		for(int i=0;i<list.size();i++){
			if(list.get(i)==number){
				return i;
			}
		}
		
		return -1;
	}
	
	//ȡ�ÿ���������С�ķ������
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
			//����������
			return -1;
		}else{
			return min;
		}
	}
	
	//��N�γ�ʼ������----�ж�δ֪�ո�Ŀ�������		-1----����ʧ��	1----����
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
	
	//��һ�γ�ʼ������----�ж�δ֪�ո�Ŀ�������  -1----����ʧ��	1----����
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
	
	//ȡ��һ������Ŀ���������
	private List<Integer> getKeNengXingData(int x,int y){
		List<Integer> temp=new ArrayList<Integer>();
		for(int i=1;i<10;i++){
			temp.add(i);
		}
		
		//����
		for(int i=0;i<9;i++){
			if(i!=x){
				temp=this.dealAllData(temp, this.data[i][y]);
			}
		}
		
		//����
		for(int i=0;i<9;i++){
			if(i!=y){
				temp=this.dealAllData(temp, this.data[x][i]);
			}
		}
		
		//���ڵ�9����
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
	
	//����һ���µ�List����
	private List<Integer> getNewList(List<Integer> list){
		List<Integer> data=new ArrayList<Integer>();
		for(int i=0;i<list.size();i++){
			data.add(list.get(i));
		}
		
		return data;
	}
	
	//����һ��List���ж���û�и�����
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
		//ʣ����������ݵĸ���
		private int shenYu;
		//ʣ��ļ���ֵ
		private List<Integer> listShenYu;
		//�����ֵ
		private int value;
		//�������ݵ�˳��
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
		
		//���鸳ֵ
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
