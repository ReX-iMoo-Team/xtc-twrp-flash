package com.rex.xtc.recovery;
 
import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import java.net.URL;
import java.io.InputStream;
import java.io.OutputStream;
import android.widget.Toast;
import android.os.Message;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import java.io.IOException;
import com.sun.tools.javac.util.Log;

public class MainActivity extends Activity { 
String Model=Build.MODEL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		AlertDialog dialog=new AlertDialog.Builder(this)
			.setTitle("为您的设备刷入TWRP")
			.setMessage("您的设备是"+Model+"吗\n注意:一旦刷入TWRP后必须借助电脑才能重新引导magiskboot")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dia, int which) {
					Toast.makeText(MainActivity.this, "即将开始刷入", Toast.LENGTH_LONG).show();
					RequestThread RequestThread=new RequestThread();
					RequestThread.start();
					try {
						Runtime.getRuntime().exec("su -c dd if=/data/user/0/com.rex.xtc.recovery/files/TWRP.img of=/dev/blobk/bootdevice/by-name/recovery");
                        Runtime.getRuntime().exec("su -c reboot");
						
					} catch (Exception e) {
						Toast.makeText(MainActivity.this, "刷入失败！！", Toast.LENGTH_LONG).show();
					}}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dia, int which) {
finish();
				}
			})
			.create();
		dialog.show();
    }
	 class RequestThread extends Thread {
		@Override
		public void run() {
			try {
				String path = "https://123-186-146-240.d.cjjd15.com:30443/download-cdn.123pan.cn/123-193/b6d1af74/1814215835-0/b6d1af74c36407a4574bcf3468f7d0c1/c-m2?v=5&t=1682767411&s=1682767411de8c97404324606b1cc61b8a205a23a1&r=RSUHQH&filename="+Model+".img&x-mf-biz-cid=8301df8f-1488-4c7f-999e-0dd78c046e83-584000&auto_redirect=0&xmfcid=dd211455-e183-4acc-9e3c-2b01344a13ee-9eed82220-9728-98";
				downLoad(path,MainActivity.this);
				
			} catch (Exception e) {
				Toast.makeText(MainActivity.this, "下载失败,请检查您的设备是否适配TWRP", Toast.LENGTH_LONG).show();
			}
		
			
		}}
	public static void downLoad(String path,Context context)throws Exception
	{
		URL url = new URL(path);
		InputStream is = url.openStream();
		//截取最后的文件名
		String end = path.substring(path.lastIndexOf("."));
		//打开手机对应的输出流,输出到文件中
		
		OutputStream os = context.openFileOutput("TWRP.img", Context.MODE_PRIVATE);
		byte[] buffer = new byte[1024];
		int len = 0;
		//从输入六中读取数据,读到缓冲区中
		while((len = is.read(buffer)) > 0)
		{
			os.write(buffer,0,len);
		}
		//关闭输入输出流
		is.close();
		os.close();
	}
	
} 
