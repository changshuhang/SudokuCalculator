package com.csh.cn.surface;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

@SuppressLint("WrongCall")
public class StartViewDrawThread extends Thread{

	private boolean flag=true;
	private StartView StartView;
	private int span=50;
	private SurfaceHolder surfaceHolder;
	
	public StartViewDrawThread(StartView StartView,
			SurfaceHolder surfaceHolder) {
		this.StartView = StartView;
		this.surfaceHolder = surfaceHolder;
	}
	
	public void setFlag(boolean flag){
		this.flag=flag;
	}
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Canvas canvas;
		while(flag){
			canvas=null;
			try{
				canvas=this.surfaceHolder.lockCanvas(null);
				synchronized(this.surfaceHolder){
					if(this.StartView!=null){
						this.StartView.onDraw(canvas);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(canvas!=null){
					this.surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
			try {
				Thread.sleep(span);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
