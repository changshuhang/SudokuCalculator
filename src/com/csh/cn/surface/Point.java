package com.csh.cn.surface;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Point {

	//�Ƿ���Ա����
	
	boolean isCilck=true;
	public boolean isCilck() {
		return isCilck;
	}
	public void setCilck(boolean isCilck) {
		this.isCilck = isCilck;
	}

	//���õ�ֵ
	int value=0;
	public void setValue(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
	
	int x;
	int y;
	Bitmap bitmap;
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
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	public Point(int x,int y ,Bitmap bitmap){
		this.x=x;
		this.y=y;
		this.bitmap=bitmap;
	}
	
	public void drawSelf(Canvas canvas,Paint paint){
		canvas.drawBitmap(bitmap, x, y, paint);
	}
	
	//�ж��Ƿ������Լ�
	public boolean isClickSelf(float x,float y){
		if(isCilck){
			if((x>=this.x&&x<=this.x+bitmap.getWidth())&&(y>=this.y&&y<=this.y+bitmap.getHeight())){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
}
