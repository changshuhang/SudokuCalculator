package com.csh.cn.surface;

import java.util.ArrayList;
import java.util.List;

import com.csh.cn.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class StartView extends SurfaceView implements SurfaceHolder.Callback{

	//显示计算时间
	private boolean isCalculator=false;
	private int startClaculatorX;
	private int startClaculatorY;
	private String info="";
	private int infoTextSize;
	public void setInfo(String info) {
		this.info = info;
	}
	
	public void setIsCalculator(boolean isCalculator){
		this.isCalculator=isCalculator;
	}
	
	private StartViewDrawThread startViewDrawThread=null;
	
	//坐标点
	private Point[][] points=new Point[9][9]; 
	
	int startX;
	int startY;
	
	//屏幕的宽高
	private int screenWidth;
	private int screenHeight;
	
	//图片的理论宽高
	private int pictureWidth;
	private int pictureHeight;
	
	private Bitmap[] numberBitmaps=new Bitmap[19];

	//画笔
	private Paint paint;
	Paint p=new Paint();
	
	//清空所有数据
	public void clearData(){
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				this.points[i][j].setValue(0);
				this.points[i][j].setBitmap(this.numberBitmaps[0]);
			}
		}
	}
	
	//设置数据
	public void setData(int[][] data){
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				this.points[i][j].setValue(data[j][i]);
				this.points[i][j].setBitmap(this.numberBitmaps[data[j][i]]);
			}
		}
		
	}
	
	//取得数据
	public int[][] getData(){
		int[][] data=new int[9][9]; 
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				data[j][i]=this.points[i][j].getValue();
			}
		}
		
		return data;
	}
	
	public StartView(Context context) {
		super(context);
	}

	public StartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.getHolder().addCallback(this);
		this.startViewDrawThread=new StartViewDrawThread(this, getHolder());
		
		p.setAntiAlias(true);
		p.setStrokeWidth(2);
		p.setColor(Color.RED);
		this.paint=new Paint();
		this.paint.setAntiAlias(true);
		
		this.initData();
	}
	
	//初始化数据
	private void initData(){
		Resources resources=getResources();
		this.numberBitmaps[0]=BitmapFactory.decodeResource(resources, R.drawable.number_null);
		this.numberBitmaps[1]=BitmapFactory.decodeResource(resources, R.drawable.number_1);
		this.numberBitmaps[2]=BitmapFactory.decodeResource(resources, R.drawable.number_2);
		this.numberBitmaps[3]=BitmapFactory.decodeResource(resources, R.drawable.number_3);
		this.numberBitmaps[4]=BitmapFactory.decodeResource(resources, R.drawable.number_4);
		this.numberBitmaps[5]=BitmapFactory.decodeResource(resources, R.drawable.number_5);
		this.numberBitmaps[6]=BitmapFactory.decodeResource(resources, R.drawable.number_6);
		this.numberBitmaps[7]=BitmapFactory.decodeResource(resources, R.drawable.number_7);
		this.numberBitmaps[8]=BitmapFactory.decodeResource(resources, R.drawable.number_8);
		this.numberBitmaps[9]=BitmapFactory.decodeResource(resources, R.drawable.number_9);
		this.numberBitmaps[10]=BitmapFactory.decodeResource(resources, R.drawable.number_big_1);
		this.numberBitmaps[11]=BitmapFactory.decodeResource(resources, R.drawable.number_big_2);
		this.numberBitmaps[12]=BitmapFactory.decodeResource(resources, R.drawable.number_big_3);
		this.numberBitmaps[13]=BitmapFactory.decodeResource(resources, R.drawable.number_big_4);
		this.numberBitmaps[14]=BitmapFactory.decodeResource(resources, R.drawable.number_big_5);
		this.numberBitmaps[15]=BitmapFactory.decodeResource(resources, R.drawable.number_big_6);
		this.numberBitmaps[16]=BitmapFactory.decodeResource(resources, R.drawable.number_big_7);
		this.numberBitmaps[17]=BitmapFactory.decodeResource(resources, R.drawable.number_big_8);
		this.numberBitmaps[18]=BitmapFactory.decodeResource(resources, R.drawable.number_big_9);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		this.screenHeight=getHeight();
		this.screenWidth=getWidth();
		this.pictureWidth=this.screenWidth/9;
		this.pictureHeight=this.screenHeight/9;
		
		for(int i=0;i<this.numberBitmaps.length;i++){
			this.numberBitmaps[i]=Bitmap.createScaledBitmap
					(this.numberBitmaps[i],
					this.pictureWidth, 
					this.pictureHeight, 
					false);
		}
		
		this.startX=this.screenWidth-9*this.pictureWidth;
		this.startY=this.screenHeight-9*this.pictureHeight;
		
		this.infoTextSize=this.screenWidth/16;
		this.startClaculatorY=(this.screenHeight-this.infoTextSize)/2;
		this.startClaculatorX=(this.screenWidth-8*this.infoTextSize)/2;
		
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				Point point=new Point(startX+i*this.pictureWidth, 
						startY+j*this.pictureHeight, 
						this.numberBitmaps[0]);
				this.points[i][j]=point;
			}
		}
		
		//先判断是否有数据可以读出
		SharedPreferences shared=getContext().getSharedPreferences("sudocalculator", Context.MODE_PRIVATE);
		boolean isdata=shared.getBoolean("isdata", false);
		if(isdata){
			String message=shared.getString("data", "0");
			for(int i=0;i<message.length();i++){
				int value=Integer.parseInt(message.substring(i,i+1));
				this.points[i/9][i%9].setValue(value);
				this.points[i/9][i%9].setBitmap(this.numberBitmaps[value]);
			}
			//用完数据后清空数据
			SharedPreferences.Editor editor=shared.edit();
			editor.remove("data");
			editor.remove("isdata");
			editor.commit();
		}
		
		synchronized (StartView.this.startViewDrawThread) {
			StartView.this.startViewDrawThread.setFlag(true);
			StartView.this.startViewDrawThread.start();
        }
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		synchronized (StartView.this.startViewDrawThread) {
			boolean retry=true;
			StartView.this.startViewDrawThread.setFlag(false);
			while(retry){
				try {
					StartView.this.startViewDrawThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				retry=false;
			}
        }
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//canvas.drawBitmap(background, null, new Rect(0, 0, screenWidth, screenHeight), paint);
		
		canvas.drawColor(Color.WHITE);
		
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				this.points[i][j].drawSelf(canvas, paint);
			}
		}
		
		//划红线
		p.setColor(Color.RED);
		for(int i=0;i<=9;i+=3){
			canvas.drawLine(this.startX+i*this.pictureWidth-1,
					this.startY,
					this.startX+i*this.pictureWidth-1,
					this.startY+9*this.pictureHeight,
					p);
			canvas.drawLine(this.startX,
					this.startY+i*this.pictureHeight-1,
					this.startX+9*this.pictureWidth,
					this.startY+i*this.pictureHeight-1,
					p);
		}
		//划绿线
		p.setColor(Color.GREEN);
		for(int i=1;i<=2;i++){
			canvas.drawLine(this.startX+i*this.pictureWidth-1,
					this.startY,
					this.startX+i*this.pictureWidth-1,
					this.startY+9*this.pictureHeight,
					p);
			canvas.drawLine(this.startX,
					this.startY+i*this.pictureHeight-1,
					this.startX+9*this.pictureWidth,
					this.startY+i*this.pictureHeight-1,
					p);
		}
		for(int i=4;i<=5;i++){
			canvas.drawLine(this.startX+i*this.pictureWidth-1,
					this.startY,
					this.startX+i*this.pictureWidth-1,
					this.startY+9*this.pictureHeight,
					p);
			canvas.drawLine(this.startX,
					this.startY+i*this.pictureHeight-1,
					this.startX+9*this.pictureWidth,
					this.startY+i*this.pictureHeight-1,
					p);
		}
		for(int i=7;i<=8;i++){
			canvas.drawLine(this.startX+i*this.pictureWidth-1,
					this.startY,
					this.startX+i*this.pictureWidth-1,
					this.startY+9*this.pictureHeight,
					p);
			canvas.drawLine(this.startX,
					this.startY+i*this.pictureHeight-1,
					this.startX+9*this.pictureWidth,
					this.startY+i*this.pictureHeight-1,
					p);
		}
		
		p.setColor(Color.RED);
		p.setTextSize(this.infoTextSize);
		if(this.isCalculator){
			canvas.drawText(this.info, this.startClaculatorX, this.startClaculatorY, p);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x=event.getX();
		float y=event.getY();
		if(!this.isCalculator){
			if((x>=this.startX&&x<=this.startX+9*this.pictureWidth)
					&&(y>=this.startY&&y<=this.startY+9*this.pictureHeight)){
					int pointX=(int)(x-this.startX)/this.pictureWidth;
					int pointY=(int)(y-this.startY)/this.pictureHeight;
					if((pointX>=0&&pointX<9)&&(pointY>=0&&pointY<9)){
						if(this.points[pointX][pointY].isClickSelf(x, y)){
							showSelectNumber(this.getShenNumber(pointX, pointY),pointX,pointY);
						}
					}
				}
		}else{
			Toast.makeText(getContext(), R.string.activity_info, Toast.LENGTH_SHORT).show();
		}
		return super.onTouchEvent(event);
	}
	
	//查询该地方剩余的数字
	private int[] getShenNumber(int x,int y){
		List<Integer> list=new ArrayList<Integer>();
		for(int i=1;i<=9;i++){
			list.add(i);
		}
		
		//横向
		for(int i=0;i<9;i++){
			if(i!=x){
				list=this.dealAllData(list, this.points[i][y].getValue());
			}
		}
		
		//竖向
		for(int i=0;i<9;i++){
			if(i!=y){
				list=this.dealAllData(list, this.points[x][i].getValue());
			}
		}
		
		//所在的9方格
		int startX=x/3*3;
		int startY=y/3*3;
		for(int i=startX;i<startX+3;i++){
			for(int j=startY;j<startY+3;j++){
				if(i!=x&&j!=y){
					list=this.dealAllData(list, this.points[i][j].getValue());
				}
			}
		}
		
		int[] data=new int[9];
		for(int i=0;i<9;i++){
			if(list.get(i)!=0){
				data[i]=1;
			}else{
				data[i]=0;
			}
		}
		
		return data;
	}
	
	//处理一个List，判断有没有改数据
	private List<Integer> dealAllData(List<Integer> temp,int number){
		for(int i=0;i<temp.size();i++){
			if(temp.get(i)==number){
				temp.set(i,0);
				break;
			}
		}
		
		return temp;
	}
	
	//弹出数字选择对话框
	private void showSelectNumber(int[] b,final int x,final int y){
		RelativeLayout relativeLayout=(RelativeLayout)View.inflate
				(getContext(), R.layout.selector_number2, null);
		final ImageView[] imageView=new ImageView[9];
		imageView[0]=(ImageView)relativeLayout.findViewById(R.id.imageView1);
		imageView[1]=(ImageView)relativeLayout.findViewById(R.id.imageView2);
		imageView[2]=(ImageView)relativeLayout.findViewById(R.id.imageView3);
		imageView[3]=(ImageView)relativeLayout.findViewById(R.id.imageView4);
		imageView[4]=(ImageView)relativeLayout.findViewById(R.id.imageView5);
		imageView[5]=(ImageView)relativeLayout.findViewById(R.id.imageView6);
		imageView[6]=(ImageView)relativeLayout.findViewById(R.id.imageView7);
		imageView[7]=(ImageView)relativeLayout.findViewById(R.id.imageView8);
		imageView[8]=(ImageView)relativeLayout.findViewById(R.id.imageView9);
		
		for(int i=0;i<b.length;i++){
			if(b[i]==0){
				imageView[i].setVisibility(ImageView.INVISIBLE);
			}else{
				imageView[i].setVisibility(ImageView.VISIBLE);
			}
		}
		final Dialog dialog=new AlertDialog.Builder(getContext())
							.setTitle(R.string.select_number)
							.setView(relativeLayout)
							.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(final DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
								}
							})
							.setPositiveButton(R.string.clear, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									points[x][y].setValue(0);
									points[x][y].setBitmap(numberBitmaps[0]);
								}
							})
							.create();
		
		for(int i=0;i<9;i++){
			imageView[i].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(v.getId()==R.id.imageView1){
						points[x][y].setBitmap(numberBitmaps[1]);
						points[x][y].setValue(1);
						dialog.dismiss();
					}else if(v.getId()==R.id.imageView2){
						points[x][y].setBitmap(numberBitmaps[2]);
						points[x][y].setValue(2);
						dialog.dismiss();
					}else if(v.getId()==R.id.imageView3){
						points[x][y].setBitmap(numberBitmaps[3]);
						points[x][y].setValue(3);
						dialog.dismiss();
					}else if(v.getId()==R.id.imageView4){
						points[x][y].setBitmap(numberBitmaps[4]);
						points[x][y].setValue(4);
						dialog.dismiss();
					}else if(v.getId()==R.id.imageView5){
						points[x][y].setBitmap(numberBitmaps[5]);
						points[x][y].setValue(5);
						dialog.dismiss();
					}else if(v.getId()==R.id.imageView6){
						points[x][y].setBitmap(numberBitmaps[6]);
						points[x][y].setValue(6);
						dialog.dismiss();
					}else if(v.getId()==R.id.imageView7){
						points[x][y].setBitmap(numberBitmaps[7]);
						points[x][y].setValue(7);
						dialog.dismiss();
					}else if(v.getId()==R.id.imageView8){
						points[x][y].setBitmap(numberBitmaps[8]);
						points[x][y].setValue(8);
						dialog.dismiss();
					}else if(v.getId()==R.id.imageView9){
						points[x][y].setBitmap(numberBitmaps[9]);
						points[x][y].setValue(9);
						dialog.dismiss();
					}
				}
			});
		}
		
		dialog.show();
	}
}
